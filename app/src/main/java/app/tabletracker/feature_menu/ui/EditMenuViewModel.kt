package app.tabletracker.feature_menu.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.domain.repository.EditMenuRepository
import app.tabletracker.feature_order.data.entity.OrderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditMenuViewModel(private val repository: EditMenuRepository): ViewModel() {
    var uiState = MutableStateFlow(EditMenuUiState())
        private set
    private var job: Job? = null

    init {
//        populateCategory()
        populateCategoriesWithMenuItems()
    }

    fun onEvent(event: EditMenuUiEvent) {
        when(event) {
            is EditMenuUiEvent.ChangeCategory -> {
                uiState.update {
                    it.copy(
                        selectedCategory = event.category
                    )
                }
            }

            is EditMenuUiEvent.ChangeDetailsOfMenuItem -> {
                uiState.update {
                    it.copy(
                        selectedMenuItem = event.menuItem
                    )
                }
            }

            is EditMenuUiEvent.ChangePricesOfMenuItem -> {
                updateMenuItemPrice(event.pair.first, event.pair.second)
            }

            EditMenuUiEvent.AddNewCategory -> {
                uiState.update {
                    it.copy(
                        selectedCategory = Category(name = "")
                    )
                }
            }
            EditMenuUiEvent.AddNewMenuItem -> {
                uiState.update {
                    it.copy(
                        selectedMenuItem = MenuItem(
                            name = "",
                            abbreviation = "",
                            description = "",
                            prices = mapOf(),
                            isKitchenCategory = uiState.value.selectedCategory.isKitchenCategory,
                            categoryId = uiState.value.selectedCategory.id
                        )
                    )
                }
            }

            is EditMenuUiEvent.UpsertCategory -> {
                viewModelScope.launch {
                    repository.writeCategory(uiState.value.selectedCategory)
                    updateKitchenCopyStatusOfMenuItemsOnCategory(uiState.value.selectedCategory)
                }

            }
            is EditMenuUiEvent.UpsertMenuItem -> {
                viewModelScope.launch {
                    repository.writeMenuItem(uiState.value.selectedMenuItem)

                }
            }

            is EditMenuUiEvent.DeleteCategory -> {
                viewModelScope.launch {
                    repository.deleteCategory(event.category)
                }
            }
            is EditMenuUiEvent.DeleteMenuItem -> {
                viewModelScope.launch {
                    repository.deleteMenuItem(event.menuItem)
                }
            }

            is EditMenuUiEvent.UpsertCategories -> {
                viewModelScope.launch {
                    repository.updateCategories(event.categories)
                }
            }

            is EditMenuUiEvent.ReorderCategories -> {
                updateCategoryIndex()
                event.categories.forEachIndexed { index, category ->
                    updateCategoryIndex(category.id, index)
                }
            }

            is EditMenuUiEvent.ReorderMenuItems -> {
                event.menuItems.forEachIndexed { index, menuItem ->
                    updateMenuItemIndex(menuItem.id, index)
                }
            }
        }
    }

    private fun updateCategoryIndex(categoryId: Int, newIndex: Int) {
        viewModelScope.launch {
            repository.updateCategoryIndex(categoryId, newIndex)
        }
    }

    private fun updateMenuItemIndex(menuItemId: String, newIndex: Int) {
        viewModelScope.launch {
            repository.updateMenuItemIndex(menuItemId, newIndex)
        }
    }


    private fun updateKitchenCopyStatusOfMenuItemsOnCategory(category: Category){
        Log.d("status: ", "category: ${category.isKitchenCategory}")
        uiState.value.menus.forEach {
            if (it.category.id == category.id) {
                Log.d("status: ", "database category: ${it.category.isKitchenCategory}")
                val menuItems = it.menuItems
                menuItems.forEach { menuItem ->
                    Log.d("status: ", "menuItem: ${menuItem.isKitchenCategory}")
                    if (menuItem.isKitchenCategory != it.category.isKitchenCategory || menuItem.isKitchenCategory != category.isKitchenCategory) {
                        Log.d("status: ", "gets inside")
                        viewModelScope.launch {
                            repository.writeMenuItem(menuItem.copy(isKitchenCategory = category.isKitchenCategory))
                        }
                    }
                }
            }
        }
    }


    private fun populateCategoriesWithMenuItems() {
        job?.cancel()
        job = repository.readAllCategoriesWithMenuItems().onEach {
            uiState.update { currentState ->
                currentState.copy(
                    menus = it
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun populateCategory() {
        job?.cancel()
        job = repository.readAllCategory().onEach {
            uiState.update {currentState ->
                currentState.copy(
                    categories = it
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun updateMenuItemPrice(orderType: OrderType, newPrice: Float) {
        var menuItem = uiState.value.selectedMenuItem
        val updatedPrices = menuItem.prices.toMutableMap()
        updatedPrices[orderType] = newPrice
        uiState.update {
            it.copy(
                selectedMenuItem = it.selectedMenuItem.copy(
                    prices = updatedPrices
                )
            )
        }
    }

    private fun updateCategoryIndex() {
        if (uiState.value.categories.any { it.index == -1 }) {
            uiState.value.categories.forEachIndexed { index, category ->
                viewModelScope.launch {
                    repository.writeCategory(category.copy(index = index))
                }
            }
        }
    }
}