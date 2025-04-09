package app.tabletracker.feature_order.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tabletracker.auth.data.model.DeviceType
import app.tabletracker.auth.data.repository.DevicePreferencesRepository
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
import app.tabletracker.util.getEndOfDay
import app.tabletracker.util.getStartOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class OrderViewModel(
    private val orderRepo: OrderRepository,
    private val deviceTypeRepo: DevicePreferencesRepository
) : ViewModel() {

    val deviceType: StateFlow<DeviceType> = deviceTypeRepo.deviceType.map {
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DeviceType.Companion
    )

    // The UI collects from this StateFlow to get its state updates
    private var _uiState: MutableStateFlow<OrderUiState> = MutableStateFlow(OrderUiState())
    val uiState = _uiState.asStateFlow()
    var currentOrderJob: Job? = null
    var calculateTotalPriceJob: Job? = null

    init {
        populateMenus()
        populateTodayOrders()
        populateRestaurantInfo()
    }


    fun onEvent(event: OrderUiEvent) {
        if (uiState.value.currentOrderLocked && event is OrderUiEvent.SetCurrentOrderWithOrderItems) {
            // Skip this event if order is locked
            return
        }

        when (event) {
            is OrderUiEvent.CreateNewOrder -> createNewOrder(event.orderType)
            is OrderUiEvent.SetCurrentOrderWithOrderItems -> setCurrentOrder(event.orderWithOrderItems)
            is OrderUiEvent.UpdateCurrentOrder -> updateCurrentOrder(event.order)
            is OrderUiEvent.AddMenuItemToOrder -> addOrUpdateOrderItem(event.menuItem)
            is OrderUiEvent.RemoveItemFromOrder -> removeItemFromOrder(event.orderItem)
            is OrderUiEvent.UpdateOrderItem -> updateOrderItem(event.orderItem)
            is OrderUiEvent.UpdateCustomer -> updateCustomer(event.customer)
        }
    }

    private fun updateCustomer(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value.currentOrder?.order?.let {
                orderRepo.writeOrder(it.copy(customer = customer))
            }
        }
    }

    private fun createNewOrder(orderType: OrderType) {
        viewModelScope.launch {
            calculateTotalPriceJob?.cancel()
            currentOrderJob?.cancel()
            // Clear current order first
            // Generate new order number
            val orderNumber = generateOrderNumber()

            // Create new order
            val newOrder = Order(
                orderNumber = orderNumber,
                orderType = orderType
            )

            // Save to database
            withContext(Dispatchers.IO) {

                orderRepo.writeOrder(newOrder)
                // Wait until the order is fully saved and get the complete order with its ID
                currentOrderJob = orderRepo.readOrderWithOrderItems(newOrder.id)
                    .onEach {
                        val createdOrderWithItems = it
                        _uiState.update { currentState ->
                            currentState.copy(
                                currentOrder = createdOrderWithItems
                            )
                        }
                    }.launchIn(viewModelScope)
                calculateTotalPriceJob = viewModelScope.launch(Dispatchers.IO) {
                    uiState.collectLatest {
                        calculateTotalPrice(it)
                    }
                }
            }
        }
    }


    private fun updateCurrentOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepo.writeOrder(order)
        }
    }


    private fun addOrUpdateOrderItem(menuItem: MenuItem) {
        uiState.value.currentOrder?.let {
            viewModelScope.launch {
                // Lock the current order temporarily to prevent changes during this operation
                val orderId = it.order.id
                try {
                    // Check if item already exists in order
                    val existingItem = it.orderItems.find {
                        it.menuItem == menuItem && it.orderItemStatus == OrderItemStatus.Added
                    }

                    if (existingItem != null) {
                        // Update quantity
                        withContext(Dispatchers.IO) {
                            // Double-check that current order hasn't changed
                            if (uiState.value.currentOrder?.order?.id == orderId) {
                                orderRepo.writeOrderItem(existingItem.copy(quantity = existingItem.quantity + 1))
                            }
                        }
                    } else {
                        // Add new item
                        withContext(Dispatchers.IO) {
                            // Double-check that current order hasn't changed
                            if (uiState.value.currentOrder?.order?.id == orderId) {
                                orderRepo.writeOrderItem(menuItem.toOrderItem(orderId = orderId))
                            }
                        }
                    }
                } finally {
                    // Unlock the current order
                }
            }
        }


    }

    private fun removeItemFromOrder(orderItem: OrderItem) {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepo.deleteOrderItem(orderItem)

        }
    }

    private fun updateOrderItem(orderItem: OrderItem) {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepo.writeOrderItem(orderItem)
        }
    }


    private fun generateOrderNumber(): Int {

        val nextOrderNumber = if (uiState.value.todayOrders.isNotEmpty()) {
            uiState.value.todayOrders.last().order.orderNumber + 1
        } else {
            1
        }
        return nextOrderNumber
    }


    private fun populateTodayOrders() {
        orderRepo.readOrdersCreatedToday(getStartOfDay(), getEndOfDay()).onEach {
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

    private fun populateMenus() {
        orderRepo.readAllCategoriesWithMenuItems().onEach {
            _uiState.update { state ->
                state.copy(
                    menus = it
                )
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun calculateTotalPrice(state: OrderUiState) {
        val currentOrder = state.currentOrder ?: return
        val totalPrice = currentOrder.orderItems.sumOf {
            ((it.menuItem.prices[currentOrder.order.orderType] ?: 0.0f) * it.quantity).toDouble()
        }.toFloat() // Convert the result back to Float

        if (totalPrice != currentOrder.order.totalPrice) {
            val updatedOrder = currentOrder.order.copy(totalPrice = totalPrice)
            orderRepo.writeOrder(updatedOrder) // Save the updated total price
            _uiState.update { it.copy(currentOrder = currentOrder.copy(order = updatedOrder)) }
        }
    }

    private fun populateRestaurantInfo() {
        orderRepo.readRestaurantInfo().onEach {
            _uiState.update { currentState ->
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
                orderRepo.readRestaurantExtra(restaurantId).onEach {
                    _uiState.update { state ->
                        state.copy(
                            restaurantExtra = it
                        )
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun setCurrentOrder(orderWithOrderItems: OrderWithOrderItems?) {
        if (orderWithOrderItems == null) {
            _uiState.update {
                it.copy(
                    currentOrder = null
                )
            }
        } else {
            val orderId = orderWithOrderItems.order.id
            orderId.let {
                viewModelScope.launch(Dispatchers.IO) {
                    calculateTotalPriceJob?.cancel()
                    currentOrderJob?.cancel()
                    currentOrderJob = orderRepo.readOrderWithOrderItems(it).onEach {
                        _uiState.update { state ->
                            state.copy(
                                currentOrder = it
                            )
                        }
                    }.launchIn(viewModelScope)

                    calculateTotalPriceJob = viewModelScope.launch(Dispatchers.IO) {
                        uiState.collectLatest {
                            calculateTotalPrice(it)
                        }
                    }
                }
            }
        }

    }

}