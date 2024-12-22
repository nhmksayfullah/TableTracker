package app.tabletracker.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun KeyboardDialog(
    modifier: Modifier = Modifier,
    dialogState: Boolean = false,
    keyboardType: DialogKeyboardType = DialogKeyboardType.Alphabetic,
    value: String = "",
    placeholder: String = "Enter value",
    label: String = "Enter value",
    onDismissRequest: () -> Unit,
    onCompleted: (String) -> Unit
) {
    var _value by rememberSaveable {
        mutableStateOf(value)
    }

    Dialog(
        onDismissRequest = {
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 200.dp, vertical = 60.dp)
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {

            TextField(
                value = _value,
                onValueChange = { _value = it },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(text = placeholder)
                },
                label = {
                    Text(text = label)
                },
                maxLines = 3,
                minLines = 1

            )
            Spacer(modifier = Modifier.height(20.dp))
            when(keyboardType) {
                DialogKeyboardType.Alphabetic -> {
                    CoreAlphabeticKeyboard(
                        onKeyPressed = {
                            _value += it
                        },
                        onBackspacePressed = {
                            _value = _value.dropLast(1)
                        },
                        onDonePressed = {
                            onCompleted(_value.trimEnd())
                        },
                        onSpacePressed = {
                            _value += " "
                        },
                        onLinePressed = {
                            _value += "\n"
                        }
                    )
                }
                DialogKeyboardType.Numeric -> {
                    CoreNumericKeyboard(
                        onKeyPressed = {
                            if (it == '.') {
                                if (_value.contains('.')) return@CoreNumericKeyboard else _value += it
                            } else {
                                _value += it
                            }
                        },
                        onBackspacePressed = {
                            _value = _value.dropLast(1)
                        },
                        onDonePressed = {
                            when {
                                _value.isEmpty() -> {
                                    onCompleted("0.0")
                                }
                                _value.startsWith('.') -> {
                                    onCompleted("0$_value")
                                }
                                _value.endsWith('.') -> {
                                    onCompleted("${_value}0")
                                }
                                else -> {
                                    onCompleted(_value)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CoreNumericKeyboard(
    modifier: Modifier = Modifier,
    onKeyPressed: (Char) -> Unit,
    onBackspacePressed: () -> Unit,
    onDonePressed: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf('1', '2', '3').forEach {
                CoreKey(key = it) {
                    onKeyPressed(it)
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf('4', '5', '6').forEach {
                CoreKey(key = it) {
                    onKeyPressed(it)
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf('7', '8', '9').forEach {
                CoreKey(key = it) {
                    onKeyPressed(it)
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CoreKey(key = '0') {
                onKeyPressed(it)
            }
            CoreKey(
                key = '.',
                weight = .5f
            ) {
                onKeyPressed(it)
            }
            CoreKey(
                key = '⌫',
                weight = .5f
            ) {
                onBackspacePressed()
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onDonePressed()
                    }
            ) {
                Text(
                    text = "Done",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun CoreAlphabeticKeyboard(
    modifier: Modifier = Modifier,
    onKeyPressed: (Char) -> Unit,
    onBackspacePressed: () -> Unit,
    onDonePressed: () -> Unit,
    onSpacePressed: () -> Unit,
    onLinePressed: () -> Unit = {}
) {
    var isCaps by rememberSaveable {
        mutableStateOf(true)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Keys.line1.forEach {
                CoreKey(key = if (isCaps) it.uppercaseChar() else it) {
                    onKeyPressed(it)
                    isCaps = false
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.weight(.5f))
            Keys.line2.forEach {
                CoreKey(key = if (isCaps) it.uppercaseChar() else it) {
                    onKeyPressed(it)
                    isCaps = false
                }
            }
            Spacer(modifier = Modifier.weight(.5f))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Keys.line3.forEach {
                CoreKey(key = if (isCaps) it.uppercaseChar() else it) {
                    onKeyPressed(it)
                    isCaps = false
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Keys.line4.forEach {
                CoreKey(key = if (isCaps) it.uppercaseChar() else it) {
                    onKeyPressed(it)
                    isCaps = false
                }
            }
            CoreKey(key = '⌫') {
                onBackspacePressed()
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1.5f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        isCaps = !isCaps
                    }
            ) {
                Text(
                    text = "Shift",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Card(
                modifier = Modifier
                    .weight(5.5f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onSpacePressed()
                    }
            ) {
                Text(
                    text = " ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Card(
                modifier = Modifier
                    .weight(1.5f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onLinePressed()
                    }
            ) {
                Text(
                    text = "Line",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Card(
                modifier = Modifier
                    .weight(1.5f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onDonePressed()
                    }
            ) {
                Text(
                    text = "Done",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun RowScope.CoreKey(
    key: Char,
    modifier: Modifier = Modifier,
    weight: Float = 1f,
    onKeyPressed: (Char) -> Unit
) {
    Card(
        modifier = Modifier
            .weight(weight)
            .align(Alignment.CenterVertically)
            .clickable {
                onKeyPressed(key)
            }
            .then(modifier)
    ) {
        Text(
            text = key.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
    }
}


object Keys {
    val line1 = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val line2 = listOf('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p')
    val line3 = listOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l')
    val line4 = listOf('z', 'x', 'c', 'v', 'b', 'n', 'm')

}

enum class DialogKeyboardType {
    Alphabetic, Numeric
}