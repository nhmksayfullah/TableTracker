package app.tabletracker.feature_order.v2.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tabletracker.feature_customer.data.model.Customer
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_order.data.entity.Order
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderItemStatus
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.data.entity.toOrderItem
import app.tabletracker.feature_order.domain.repository.OrderRepository
import app.tabletracker.util.generateUniqueId
import app.tabletracker.util.getEndOfDay
import app.tabletracker.util.getStartOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel2(
    private val repository: OrderRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow(OrderUiState2())
    val uiState: StateFlow<OrderUiState2>
        get() = _uiState

    init {
        populateMenus()
        populateTodayOrders()
        populateRestaurantInfo()
    }
    init {
        viewModelScope.launch {
            uiState.collect { state ->
                state.restaurantInfo?.let {
                    repository.readRestaurantExtra(it.id).onEach {
                        _uiState.update { currentState ->
                            currentState.copy(
                                restaurantExtra = it
                            )
                        }
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            uiState.collect { state ->
                calculateTotalPrice(state)
            }
        }
    }

    fun onEvent(event: OrderUiEvent2) {
        when (event) {
            is OrderUiEvent2.CreateNewOrder -> createNewOrder(event.orderType)
            is OrderUiEvent2.UpdateCurrentOrder -> updateCurrentOrder(event.order)
            is OrderUiEvent2.AddMenuItemToOrder -> addOrUpdateOrderItem(event.menuItem)
            is OrderUiEvent2.RemoveItemFromOrder -> removeItemFromOrder(event.orderItem)
            is OrderUiEvent2.UpdateOrderItem -> updateOrderItem(event.orderItem)
            is OrderUiEvent2.SetCurrentOrderWithOrderItems -> setCurrentOrderWithOrderItems(event.orderWithOrderItems)
            is OrderUiEvent2.UpdateCustomer -> updateCustomer(event.customer)
        }
    }

    private fun populateRestaurantInfo() {
        repository.readRestaurantInfo().onEach {
            _uiState.update { currentState ->
                currentState.copy(
                    restaurantInfo = it
                )
            }
        }.launchIn(viewModelScope)

    }

    private fun updateCustomer(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value.currentOrder?.order?.let {
                repository.writeOrder(it.copy(customer = customer))
            }
        }
    }

    private fun setCurrentOrderWithOrderItems(orderWithOrderItems: OrderWithOrderItems?) {
        _uiState.update { currentState ->
            currentState.copy(
                currentOrder = orderWithOrderItems
            )
        }
    }

    private fun updateOrderItem(orderItem: OrderItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.writeOrderItem(orderItem)
        }
    }

    private fun updateCurrentOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.writeOrder(order)
        }
    }

    private fun removeItemFromOrder(orderItem: OrderItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteOrderItem(orderItem)
        }
    }

    private suspend fun calculateTotalPrice(state: OrderUiState2) {
        val currentOrder = state.currentOrder ?: return
        val totalPrice = currentOrder.orderItems.sumOf {
            ((it.menuItem.prices[currentOrder.order.orderType] ?: 0.0f) * it.quantity).toDouble()
        }.toFloat() // Convert the result back to Float

        if (totalPrice != currentOrder.order.totalPrice) {
            val updatedOrder = currentOrder.order.copy(totalPrice = totalPrice)
            repository.writeOrder(updatedOrder) // Save the updated total price
            _uiState.update { it.copy(currentOrder = currentOrder.copy(order = updatedOrder)) }
        }
    }

    private fun addOrUpdateOrderItem(menuItem: MenuItem) {
        val existingOrderItem = uiState.value.currentOrder?.orderItems?.find {
            it.menuItem == menuItem && it.orderItemStatus == OrderItemStatus.Added
        }
        if (existingOrderItem != null) {
            writeOrderItem(existingOrderItem.copy(quantity = existingOrderItem.quantity + 1))
        } else {
            uiState.value.currentOrder?.order?.let {
                val orderItem = menuItem.toOrderItem(it.id)
                writeOrderItem(orderItem)
            }
        }
    }

    private fun writeOrderItem(orderItem: OrderItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.writeOrderItem(orderItem)
        }
    }

    private fun createNewOrder(orderType: OrderType) {
        val newOrder = Order(
            id = generateUniqueId(),
            orderNumber = uiState.value.todayOrders.size + 1,
            orderType = orderType
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.writeOrder(newOrder)
            populateLatestOrder()
        }
    }


    private fun populateLatestOrder() {
        repository.readLastAddedOrder().onEach {
            _uiState.update { currentState ->
                currentState.copy(
                    currentOrder = it
                )
            }
        }.launchIn(viewModelScope)
    }


    private fun populateMenus() {
        repository.readAllCategoriesWithMenuItems().onEach {
            _uiState.update { currentState ->
                currentState.copy(
                    menus = it
                )
            }
        }.launchIn(viewModelScope)
    }


    private fun populateTodayOrders() {
        repository.readOrdersCreatedToday(getStartOfDay(), getEndOfDay()).onEach {
            _uiState.update { currentState ->
                currentState.copy(
                    todayOrders = it,
                    runningOrders = it.filter { orderWithOrderItems ->
                        orderWithOrderItems.order.orderStatus == OrderStatus.Running
                    },
                    completedOrders = it.filter { orderWithOrderItems ->
                        orderWithOrderItems.order.orderStatus == OrderStatus.Completed
                    },
                    cancelledOrders = it.filter { orderWithOrderItems ->
                        orderWithOrderItems.order.orderStatus == OrderStatus.Cancelled
                    }
                )
            }
        }.launchIn(viewModelScope)
    }

}