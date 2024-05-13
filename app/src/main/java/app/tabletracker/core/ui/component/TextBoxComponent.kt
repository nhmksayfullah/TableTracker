package app.tabletracker.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

// This component is made with card and text.
// It should be used on the app whenever we need a block of text.
@Composable
fun TextBoxComponent(
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .then(modifier),
        colors = CardDefaults
            .cardColors(
                containerColor = containerColor,
                contentColor = contentColor
            )
    ) {
        Column(
            modifier = Modifier.wrapContentSize().clickable { onClick() }
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(16.dp)
                    .then(textModifier),
                style = textStyle

            )
        }
    }
}