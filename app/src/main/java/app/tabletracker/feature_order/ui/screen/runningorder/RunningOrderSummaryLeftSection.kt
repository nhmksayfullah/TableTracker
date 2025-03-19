package app.tabletracker.feature_order.ui.screen.runningorder

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_order.data.entity.Order
import app.tabletracker.R


@Composable
fun RunningOrderSummaryLeftSection(
    order: Order,
    modifier: Modifier = Modifier,
    onCustomizeOrder: () -> Unit,
    onPrintReceipt: () -> Unit,
    onPrintKitchenCopy: () -> Unit
) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceDim
        ),
        shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp, topStart = 8.dp, topEnd = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sub Total:")
                Spacer(modifier = Modifier.weight(1f))
                Text("£%.2f".format(order.totalPrice))
            }

            if(order.discount != null) {
                val discount = order.discount.value.let {
                    try {
                        it.toFloat() * order.totalPrice / 100
                    } catch (e: Exception) {
                        0.0f
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Discount:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text("-£%.2f".format(discount))
                }
                Row {
                    Text(text = "Total:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text("£%.2f".format(order.totalPrice - discount))
                }
            }

            Spacer(modifier = Modifier
                .height(8.dp))
            Button(
                onClick = onCustomizeOrder,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Customize Order",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Row {

                Button(
                    onClick = onPrintReceipt,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_print_24),
                        contentDescription = "Print Receipt"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Receipt",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))

                OutlinedButton(
                    onClick = onPrintKitchenCopy,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_print_24),
                        contentDescription = "Print Receipt"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Kitchen",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

    }
}