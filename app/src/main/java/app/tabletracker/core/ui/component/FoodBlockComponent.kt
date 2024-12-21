package app.tabletracker.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun FoodBlockComponent(
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: () -> Unit = {}
) {
    var resizedTextStyle by remember {
        mutableStateOf(textStyle)
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }
    val defaultFontSize = MaterialTheme.typography.bodyLarge.fontSize
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable { onClick() }
            .then(modifier),
        colors = CardDefaults
            .cardColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(4.dp),
                maxLines = 2,
                style = resizedTextStyle,
//                onTextLayout = {textLayoutResult ->
//                    if (textLayoutResult.didOverflowHeight) {
//                        resizedTextStyle = resizedTextStyle.copy(
//                            fontSize = defaultFontSize * 0.8f,
//                            textMotion = TextMotion.Animated
//                        )
//                    } else {
//                        shouldDraw = true
//                    }
//                },
            )
        }
    }
}

