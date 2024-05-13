package app.tabletracker.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.util.TableTrackerDefault

@Composable
fun SplitScreen(
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit,
    drawerContent: @Composable (DrawerState) -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    ratio: SplitRatio = TableTrackerDefault.SplitRatio
) {

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                drawerContent(drawerState)
            }
        }
    ) {
        SplitScreen(
            modifier = modifier,
            leftContent = leftContent,
            rightContent = rightContent,
            floatingActionButton = floatingActionButton,
            ratio = ratio
        )
    }
}

@Composable
fun SplitScreen(
    modifier: Modifier = Modifier,
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    ratio: SplitRatio = TableTrackerDefault.SplitRatio
) {
    if (ratio.leftWeight == 0f || ratio.rightWeight == 0f) {
        Column(
            modifier
                .fillMaxSize()
                .then(modifier)
        ) {
            if (ratio.leftWeight == 0f) rightContent()
            if (ratio.rightWeight == 0f) leftContent()
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(ratio.leftWeight)
            ) {
                leftContent()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(ratio.rightWeight)
            ) {
                rightContent()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        floatingActionButton()
    }
}


data class SplitRatio(
    val leftWeight: Float,
    val rightWeight: Float = 1 - leftWeight
)