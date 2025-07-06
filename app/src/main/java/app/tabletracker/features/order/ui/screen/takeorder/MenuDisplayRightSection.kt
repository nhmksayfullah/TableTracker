package app.tabletracker.features.order.ui.screen.takeorder

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.tabletracker.R
import app.tabletracker.features.inventory.data.entity.Category
import app.tabletracker.features.inventory.data.entity.CategoryWithMenuItems
import app.tabletracker.features.inventory.data.entity.MenuItem

/**
 * Breadcrumb trail component for order taking screen
 */
@Composable
fun OrderBreadcrumbTrail(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Always show "Menu" as the root
        Text(
            text = "Menu",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (categories.isEmpty()) FontWeight.Bold else FontWeight.Normal,
            color = if (categories.isEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.clickable { 
                // Go back to root menu
                onCategoryClick(Category(id = -1, name = "Menu")) 
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Show selected category if any
        if (categories.isNotEmpty()) {
            Text(
                text = " > ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = categories[0].name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * @deprecated Use [OrderExplorerScreen] instead for dynamic hierarchical navigation
 */
@Deprecated("Use OrderExplorerScreen instead for dynamic hierarchical navigation")
@Composable
fun MenuDisplayRightSection(
    menus: List<CategoryWithMenuItems>,
    modifier: Modifier = Modifier,
    onMenuItemClicked: (MenuItem) -> Unit
) {
    var menuDisplaySection by rememberSaveable {
        mutableStateOf(MenuDisplaySection.Category)
    }

    var selectedCategoryId by remember {
        mutableIntStateOf(-1)
    }
    var selectedCategory by remember {
        mutableStateOf<Category?>(null)
    }

    BackHandler(true) {
        when (menuDisplaySection) {
            MenuDisplaySection.Category -> {}
            MenuDisplaySection.MenuItem -> {
                menuDisplaySection = MenuDisplaySection.Category
                selectedCategoryId = -1
                selectedCategory = null
            }
        }
    }

    Column(
        modifier = modifier
            .padding(8.dp)
    ) {
        // Breadcrumb navigation
        OrderBreadcrumbTrail(
            categories = if (selectedCategory != null) listOf(selectedCategory!!) else emptyList(),
            onCategoryClick = { category ->
                // If clicking on the root, go back to category view
                if (category.id == -1) {
                    menuDisplaySection = MenuDisplaySection.Category
                    selectedCategoryId = -1
                    selectedCategory = null
                }
            }
        )

        Spacer(Modifier.height(4.dp))

        AnimatedContent(
            targetState = menuDisplaySection,
            transitionSpec = {
                when (targetState) {
                    MenuDisplaySection.Category -> {
                        // Coming back to category → slide from left
                        (slideInHorizontally { -it } + fadeIn() togetherWith
                                slideOutHorizontally { it } + fadeOut()).using(
                            SizeTransform(clip = false)
                        )
                    }
                    MenuDisplaySection.MenuItem -> {
                        // Going to menu items → slide from right
                        (slideInHorizontally { it } + fadeIn() togetherWith
                                slideOutHorizontally { -it } + fadeOut()).using(
                            SizeTransform(clip = false)
                        )
                    }
                }
            }
        ) {
            when(it) {
                MenuDisplaySection.Category -> {
                    SelectCategoryRightSection(
                        menus = menus.sortedBy { it.category.index },
                        onCategoryClicked = { category ->
                            menuDisplaySection = MenuDisplaySection.MenuItem
                            selectedCategoryId = category.id
                            selectedCategory = category
                        }
                    )
                }
                MenuDisplaySection.MenuItem -> {
                    menus.find {
                        it.category.id == selectedCategoryId
                    }?.let {
                        if (it.menuItems.isNotEmpty()) {
                            SelectMenuItemRightSection(
                                menus = it.menuItems.sortedBy { it.index },
                                onMenuItemClicked = onMenuItemClicked
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No Menu Items Available")
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class MenuDisplaySection {
    Category, MenuItem
}
