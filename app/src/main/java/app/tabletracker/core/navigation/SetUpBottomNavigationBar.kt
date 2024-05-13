package app.tabletracker.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.app.ui.AppUiState

@Composable
fun SetupBottomNavigationBar(
    modifier: Modifier = Modifier,
    navOptions: List<BottomNavigationOption>,
    extraNavOptions: List<ExtraNavOption> ,
    onNavigationItemClick: (BottomNavigationOption) -> Unit,
    onExtraNavOptionClick: (ExtraNavOption) -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .then(modifier)
    ) {
        navOptions.forEach { bottomNavigationOption ->
            BottomNavItem(bottomNavOption = bottomNavigationOption) {
                onNavigationItemClick(bottomNavigationOption)
            }
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            extraNavOptions.forEach {
                IconButton(
                    onClick = {
                        onExtraNavOptionClick(it)
                    }
                ) {
                    Icon(imageVector = it.navOption.icon, contentDescription = it.navOption.title)
                }
            }
        }

    }
}

@Composable
private fun BottomNavItem(
    bottomNavOption: BottomNavigationOption,
    onClick: (BottomNavigationOption) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable {
                onClick(bottomNavOption)
            }
            .wrapContentSize()
            .padding(horizontal = 36.dp, vertical = 16.dp)

    ) {
        Icon(imageVector = bottomNavOption.navOption.icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = bottomNavOption.navOption.title)
    }
}