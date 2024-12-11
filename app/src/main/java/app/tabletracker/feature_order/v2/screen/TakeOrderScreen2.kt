package app.tabletracker.feature_order.v2.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.feature_order.ui.OrderViewModel

@Composable
fun TakeOrderScreen2(
    orderViewModel: OrderViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = orderViewModel.uiState.collectAsStateWithLifecycle()
    Text("${uiState.value.currentOrder}")
}