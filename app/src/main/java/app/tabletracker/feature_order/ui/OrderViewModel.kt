package app.tabletracker.feature_order.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tabletracker.feature_customer.domain.repository.CustomerRepository
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_order.data.entity.Order
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderItemStatus
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.data.entity.toOrderItem
import app.tabletracker.feature_order.domain.repository.OrderRepository
import app.tabletracker.util.TableTrackerDefault
import app.tabletracker.util.generateUniqueId
import app.tabletracker.util.getEndOfDay
import app.tabletracker.util.getStartOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepo: OrderRepository,
    private val customerRepo: CustomerRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow(OrderUiState())
    val uiState = _uiState.asStateFlow()


    private var populateMenusJob: Job? = null
    private var populateTodayOrdersJob: Job? = null
    private var populateRestaurantInfoJob: Job? = null
    private var populateRestaurantExtraJob: Job? = null


    init {
        populateMenus()
        populateTodayOrders()
        populateRestaurantInfo()
    }

    fun onEvent(uiEvent: OrderUiEvent) {
        when (uiEvent) {
            is OrderUiEvent.CreateNewOrder -> createNewOrder(orderType = uiEvent.orderType)
            is OrderUiEvent.UpdateCurrentOrder -> updateCurrentOrder(order = uiEvent.order)

            is OrderUiEvent.AddItemToOrder -> addOrUpdateOrderItem(menuItem = uiEvent.menuItem, orderId = uiEvent.orderId)
            is OrderUiEvent.RemoveItemFromOrder -> removeItemFromOrder(orderItem = uiEvent.orderItem)
            is OrderUiEvent.UpdateOrderItem -> updateOrderItem(orderItem = uiEvent.orderItem)

            is OrderUiEvent.UpdateCurrentOrderWithOrderItems -> {
                if (uiEvent.orderId == TableTrackerDefault.noOrderId) {
                    populateLatestOrder()
                } else {
                    populateCurrentOrder(orderId = uiEvent.orderId)
                }
            }

            is OrderUiEvent.UpdateCurrentOrderItemsStatus -> updateCurrentOrderItemsStatus(uiEvent.orderItemStatus)
        }
    }


    private fun createNewOrder(orderType: OrderType) {
        Log.d("current order: creating", uiState.value.currentOrder.toString())
        val newOrder = Order(
            id = generateUniqueId(),
            orderNumber = generateOrderNumber(),
            orderType = orderType
        )
        viewModelScope.launch(Dispatchers.IO) {
            orderRepo.writeOrder(
                newOrder
            )
            populateCurrentOrder(newOrder.id)
        }
    }

    private fun populateCurrentOrder(orderId: String?) {
        if (orderId != null) {
            orderRepo.readOrderWithOrderItems(orderId).onEach {
                _uiState.update { state ->
                    state.copy(
                        currentOrder = it
                    )
                }
                if (it.orderItems.isNotEmpty()) {
                    val totalPrice = calculateTotalPrice(it)
                    if (totalPrice != uiState.value.currentOrder?.order?.totalPrice) {
                        it.order.copy(totalPrice = totalPrice)
                            .let { order ->
                                updateCurrentOrder(order)
                            }
                    }

                }
            }.launchIn(viewModelScope)
        } else {
            populateLatestOrder()
        }
    }

    private fun updateCurrentOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepo.writeOrder(order)
        }
//        if (order.orderStatus == OrderStatus.Cancelled) updateCurrentOrderWithOrderItems(null)
    }

    private fun updateCurrentOrderItemsStatus(orderItemStatus: OrderItemStatus) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value.currentOrder?.orderItems?.forEach {
                val orderItem = it.copy(orderItemStatus = orderItemStatus)
                orderRepo.writeOrderItem(orderItem)

            }
        }
    }


    private fun addOrUpdateOrderItem(menuItem: MenuItem, orderId: String) {
        val foundedOrderItem = uiState.value.currentOrder?.orderItems?.find {
            it.menuItem == menuItem && it.orderItemStatus == OrderItemStatus.Added
        }
        if (foundedOrderItem == null) {
            addItemToOrder(menuItem, orderId)
        } else {
            updateOrderItem(
                foundedOrderItem.copy(quantity = foundedOrderItem.quantity + 1)
            )
        }
    }

    private fun addItemToOrder(menuItem: MenuItem, orderId: String) {
        val orderItem = menuItem.toOrderItem(orderId = orderId)
        viewModelScope.launch(Dispatchers.IO) {
            orderRepo.writeOrderItem(orderItem)

        }
    }

    private fun removeItemFromOrder(orderItem: OrderItem) {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepo.deleteOrderItem(orderItem)


        }
    }

    private fun updateOrderItem(orderItem: OrderItem) {
        viewModelScope.launch(Dispatchers.IO){
            orderRepo.writeOrderItem(orderItem)
        }
    }

//    private fun updateCurrentOrderWithOrderItems(orderId: String?) {
//
//    }



    private fun generateOrderNumber(): Int {

        val nextOrderNumber = if (uiState.value.todayOrders.isNotEmpty()) {
            uiState.value.todayOrders.last().order.orderNumber + 1
        } else {
            1
        }
        return nextOrderNumber
    }


    private fun populateLatestOrder() {
        orderRepo.readLastAddedOrder().onEach {
            _uiState.update {state ->
                state.copy(
                    currentOrder = it
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun populateTodayOrders() {
        populateTodayOrdersJob?.cancel()
        populateTodayOrdersJob = orderRepo.readOrdersCreatedToday(getStartOfDay(), getEndOfDay()).onEach {
            _uiState.update {currentState ->
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

    private fun populateMenus() {
        populateMenusJob?.cancel()
        populateMenusJob = orderRepo.readAllCategoriesWithMenuItems().onEach {
            _uiState.update {state ->
                state.copy(
                    menus = it
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun calculateTotalPrice(orderWithOrderItems: OrderWithOrderItems): Float {
        var totalPrice = 0.0f

        val orderItems = orderWithOrderItems.orderItems
        if (orderItems.isNotEmpty()) {
            orderItems.forEach {
                totalPrice += it.menuItem.prices[orderWithOrderItems.order.orderType]?.times(it.quantity) ?: 0.0f
            }
        }
        return totalPrice
    }

    private fun populateRestaurantInfo() {
        populateRestaurantInfoJob?.cancel()
        populateRestaurantInfoJob = orderRepo.readRestaurantInfo().onEach {
            _uiState.update {currentState ->
                currentState.copy(
                    restaurantInfo = it
                )
            }
            populateRestaurantExtra(it.id)
        }.launchIn(viewModelScope)
    }

    private fun populateRestaurantExtra(restaurantId: String) {
        if (uiState.value.restaurantInfo != null) {
            if (uiState.value.restaurantInfo!!.id.isNotEmpty()) {
                populateRestaurantExtraJob?.cancel()
                populateRestaurantExtraJob = orderRepo.readRestaurantExtra(restaurantId).onEach {
                    _uiState.update {state ->
                        state.copy(
                            restaurantExtra = it
                        )
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

}