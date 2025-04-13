package app.tabletracker.features.order.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OverViewComponent(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: Int? = null,
    iconTint: Color = MaterialTheme.colorScheme.onPrimary
) {
    Card(
        modifier = modifier
            .wrapContentSize(),

        ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                icon?.let {
                    Spacer(
                        modifier = Modifier
                            .width(64.dp)
                    )
                    Icon(
                        painter = painterResource(it),
                        contentDescription = null,
                        tint = iconTint
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}