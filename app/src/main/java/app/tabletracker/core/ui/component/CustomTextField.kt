package app.tabletracker.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Type here...",
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    val borderColor = if (isError) Color(0xFFE57464) // Soft Red for Error
    else if (value.isNotEmpty()) MaterialTheme.colorScheme.primary // Highlighted when filled
    else MaterialTheme.colorScheme.inversePrimary

    val backgroundColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, borderColor, RoundedCornerShape(12.dp))
                        .background(backgroundColor, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    innerTextField()
                }
            }
        )

        // Show error message if there's an error
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}