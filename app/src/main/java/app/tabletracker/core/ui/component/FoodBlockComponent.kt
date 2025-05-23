package app.tabletracker.core.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodBlockComponent(
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    verticalLineColor: Color? = null,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .then(modifier),
        colors = CardDefaults
            .cardColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .combinedClickable(
                    onClick = onClick,
                    onDoubleClick = onDoubleClick
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            verticalLineColor?.let {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(4.dp)
                        .background(it, RoundedCornerShape(8.dp))
                )
            }

            Text(
                text = text,
                modifier = textModifier.padding(8.dp),
                maxLines = 2,
                style = textStyle,
            )
        }
    }
}

