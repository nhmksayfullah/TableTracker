package app.tabletracker.feature_order.v2.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tabletracker.feature_order.data.entity.Order
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderType
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
): ViewModel() {
    private var _uiState = MutableStateFlow(OrderUiState2())
    val uiState: StateFlow<OrderUiState2>
        get() = _uiState

    init {
        populateMenus()
        populateTodayOrders()
    }

    fun onEvent(event: OrderUiEvent2) {
        when(event) {
            is OrderUiEvent2.CreateNewOrder -> {

            }
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
            _uiState.update {currentState ->
                currentState.copy(
                    currentOrder = it
                )
            }
        }
    }


    private fun populateMenus() {
        repository.readAllCategoriesWithMenuItems().onEach {
            _uiState.update {currentState ->
                currentState.copy(
                    menus = it
                )
            }
        }.launchIn(viewModelScope)
    }


    private fun populateTodayOrders() {
        repository.readOrdersCreatedToday(getStartOfDay(), getEndOfDay()).onEach {
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

}