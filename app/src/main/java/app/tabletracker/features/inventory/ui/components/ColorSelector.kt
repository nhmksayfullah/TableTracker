package app.tabletracker.features.inventory.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import app.tabletracker.theme.MaterialColor

@Composable
fun ColorSelector(
    selectedColorArgb: Int,
    onColorSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Select Color",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.height(200.dp)
        ) {
            items(MaterialColor.entries) { materialColor ->
                val isSelected = selectedColorArgb == materialColor.color.toArgb()
                ColorItem(
                    color = materialColor.color,
                    isSelected = isSelected,
                    onClick = { onColorSelected(materialColor.color.toArgb()) }
                )
            }
        }
    }
}

@Composable
private fun ColorItem(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(4.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface) else BorderStroke(0.dp, Color.Transparent),
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    ) {
        // Empty content
    }
}
