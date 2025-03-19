package app.tabletracker.feature_order.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.tabletracker.theme.TableTrackerTheme


class VerticalCarouselState(
    initialIndex: Int = 0
) {
    private var expandedIndex by mutableIntStateOf(initialIndex)
    fun isExpanded(index: Int) = expandedIndex == index
    fun toggleExpand(index: Int) {
        if (expandedIndex != index) {
            expandedIndex = index
        }
    }
}

@Composable
fun rememberVerticalCarouselState(itemCount: Int): VerticalCarouselState {
    return remember { VerticalCarouselState(itemCount) }
}

@Composable
fun ColumnScope.VerticalCarouselItem(
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onToggleExpand: () -> Unit,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    val animatedHeight by animateDpAsState(
        targetValue = 0.dp,
        animationSpec = tween(durationMillis = 0, easing = LinearOutSlowInEasing)
    )

    Card(
        modifier = modifier
            .padding(vertical = 2.dp)
            .then(if (isExpanded) Modifier.weight(1f) else Modifier)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = !isExpanded,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {

                        onToggleExpand()
                    }
            ) {
                header()
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .then(
                        if (isExpanded) Modifier.weight(1f)
                        else Modifier.height(animatedHeight)
                    )
            ) {
                content()
            }
        }
    }
}

@Composable
fun VerticalCarousel(
    state: VerticalCarouselState,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        content = content
    )
}

@Preview
@Composable
private fun VerticalCarouselPreview() {

    val state = rememberVerticalCarouselState(3)
    LaunchedEffect(Unit) {
        state.toggleExpand(1)
    }
    TableTrackerTheme {
        VerticalCarousel(
            state = state
        ) {
            VerticalCarouselItem(
                isExpanded = state.isExpanded(0),
                onToggleExpand = { state.toggleExpand(0) },
                header = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Customer")
                    }
                },
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            )
            VerticalCarouselItem(
                isExpanded = state.isExpanded(1),
                onToggleExpand = { state.toggleExpand(1) },
                header = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Order")
                    }
                },
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            )
            VerticalCarouselItem(
                isExpanded = state.isExpanded(2),
                onToggleExpand = { state.toggleExpand(2) },
                header = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Pay")
                    }
                },
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            )
        }
    }
}