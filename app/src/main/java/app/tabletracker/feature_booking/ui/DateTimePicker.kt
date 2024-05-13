package app.tabletracker.feature_booking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.tabletracker.core.ui.SplitScreen

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DateTimePicker() {
    val datePickerState = rememberDatePickerState(1714153648473)
    val timePickerState = rememberTimePickerState(
        is24Hour = false,
        initialHour = 13,
        initialMinute = 33
    )
    SplitScreen(
        leftContent = {
            Column {
                DatePicker(
                    state = datePickerState
                )
                Text(text = datePickerState.selectedDateMillis.toString())

            }
        },
        rightContent = {
            Column {
                TimePicker(state = timePickerState)
                Text(text = "${timePickerState.hour}:${timePickerState.minute}")
            }
        }
    )
}