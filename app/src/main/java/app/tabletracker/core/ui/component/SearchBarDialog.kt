package app.tabletracker.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarDialog(
    modifier: Modifier = Modifier
) {
    var isActive by rememberSaveable {
        mutableStateOf(false)
    }
    Dialog(
        onDismissRequest = {

        }) {
        Column {
            DockedSearchBar(
                query = "",
                onQueryChange = {},
                onSearch = {},
                active = isActive,
                onActiveChange = {

                },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            ) {

            }
            CoreAlphabeticKeyboard(
                onKeyPressed = {
                    isActive = true
                },
                onBackspacePressed = { /*TODO*/ },
                onDonePressed = {
                    isActive = false
                }
            ) {

            }
        }
    }
}

@Preview
@Composable
private fun SearchBarDialogPreview() {
    SearchBarDialog()
}