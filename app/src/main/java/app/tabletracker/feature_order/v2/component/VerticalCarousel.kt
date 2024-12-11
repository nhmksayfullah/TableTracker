package app.tabletracker.feature_order.v2.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.tabletracker.theme.TableTrackerTheme


@Immutable
data class VerticalCarouselItem(
    val header: @Composable () -> Unit,
    val content: @Composable () -> Unit
)

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
                    .clickable(!isExpanded) { onToggleExpand() }
            ) {
                header()
            }
            AnimatedVisibility(
                visible = isExpanded,
                modifier = Modifier.animateContentSize(),
                enter = expandVertically(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing))
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
    items: List<VerticalCarouselItem>
) {
    Column {
        items.forEachIndexed { index, item ->
            VerticalCarouselItem(
                isExpanded = state.isExpanded(index),
                modifier = modifier,
                header = item.header,
                content = item.content,
                onToggleExpand = { state.toggleExpand(index) }
            )
        }
    }
}

@Preview
@Composable
private fun VerticalCarouselPreview() {
    LazyColumn {

    }
    val items = listOf(
        VerticalCarouselItem(
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
                        .background(Color.Red)
                )
            }
        ),
        VerticalCarouselItem(
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
                        .background(Color.Blue)
                )
            }
        ),
        VerticalCarouselItem(
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
                        .background(Color.Yellow)
                )
            }
        )
    )
    val state = rememberVerticalCarouselState(items.size)
    LaunchedEffect(Unit) {
        state.toggleExpand(1)
    }
    TableTrackerTheme {
        VerticalCarousel(
            state = state,
            items = items
        )
    }
}