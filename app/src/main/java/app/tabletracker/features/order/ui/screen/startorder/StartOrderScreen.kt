package app.tabletracker.features.order.ui.screen.startorder

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.core.ui.component.TextBoxComponent
import app.tabletracker.features.order.data.entity.OrderStatus
import app.tabletracker.features.order.data.entity.OrderType
import app.tabletracker.features.order.data.entity.OrderWithOrderItems
import app.tabletracker.features.order.ui.state.OrderUiEvent
import app.tabletracker.features.order.ui.state.OrderViewModel
import app.tabletracker.theme.MaterialColor

@Composable
fun StartOrderScreen(
    orderViewModel: OrderViewModel,
    modifier: Modifier = Modifier,
    onConnectWithCompanion: () -> Unit,
    onCreateNewOrder: () -> Unit,
) {


    BackHandler(true) {}
    val orderUiState by orderViewModel.uiState.collectAsStateWithLifecycle()
    val deviceType by orderViewModel.deviceType.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            BrandingSection()
            Spacer(Modifier.weight(1f))
            FilterChip(
                selected = true,
                onClick = {},
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = if (deviceType.name == "Main") 
                        MaterialColor.Blue.color 
                    else MaterialColor.Red.color,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                label = {
                    Text(
                        text = "${deviceType.name} Device"
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OverViewSection(
            totalOrder = orderUiState.todayOrders.filter {
                it.order.orderStatus == OrderStatus.Completed
            }.size,
            pendingOrder = orderUiState.todayOrders.filter {
                it.order.orderStatus == OrderStatus.Running
            }.size
        )

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.Center,

            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextBoxComponent(
                text = OrderType.DineIn.label,
                modifier = Modifier.padding(4.dp),
                textModifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                textStyle = MaterialTheme.typography.headlineMedium
            ) {
                orderViewModel.onEvent(OrderUiEvent.CreateNewOrder(OrderType.DineIn))
                onCreateNewOrder()
            }
            TextBoxComponent(
                text = OrderType.TakeOut.label,
                modifier = Modifier.padding(4.dp),
                textModifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                textStyle = MaterialTheme.typography.headlineMedium
            ) {
                orderViewModel.onEvent(OrderUiEvent.CreateNewOrder(OrderType.TakeOut))
                onCreateNewOrder()
            }
            TextBoxComponent(
                text = OrderType.Delivery.label,
                modifier = Modifier.padding(4.dp),
                textModifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                textStyle = MaterialTheme.typography.headlineMedium
            ) {
                orderViewModel.onEvent(OrderUiEvent.CreateNewOrder(OrderType.Delivery))
                onCreateNewOrder()
            }
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.End
        ) {
            // Only show CompanionSection on Main devices
            if (deviceType.name == "Main") {
                CompanionSection()
            }
        }
    }


}
