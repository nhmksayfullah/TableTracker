package app.tabletracker.features.inventory.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tabletracker.features.inventory.domain.repository.EditMenuRepository
import app.tabletracker.features.inventory.data.entity.Category
import app.tabletracker.features.inventory.data.entity.MenuItem
import app.tabletracker.features.order.data.entity.OrderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditMenuViewModel(private val repository: EditMenuRepository) : ViewModel() {
    var uiState = MutableStateFlow(EditMenuUiState())
        private set
    private var job: Job? = null

    init {
        populateCategoriesWithMenuItems()
    }

    fun onEvent(event: EditMenuUiEvent) {
        when (event) {
            is EditMenuUiEvent.SetSelectedCategory -> {
                uiState.update {
                    it.copy(
                        selectedCategory = event.category
                    )
                }
            }

            is EditMenuUiEvent.SetSelectedMenuItem -> {
                uiState.update {
                    it.copy(
                        selectedMenuItem = event.menuItem
                    )
                }
            }


            is EditMenuUiEvent.UpsertCategory -> {
                viewModelScope.launch {
                    repository.writeCategory(event.category)
                    updateKitchenCopyStatusOfMenuItemsOnCategory(event.category)
                }

            }

            is EditMenuUiEvent.UpsertMenuItem -> {
                viewModelScope.launch {
                    val newMenuItem = updateMenuItemPrice(event.newPrices)
                    newMenuItem?.let {
                        repository.writeMenuItem(it)
                        uiState.update { currentState ->
                            currentState.copy(
                                selectedMenuItem = MenuItem.empty(it.categoryId)
                            )
                        }
                    }

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


            is EditMenuUiEvent.UpdateSelectedMenuItem -> {
                uiState.update {
                    it.copy(
                        selectedMenuItem = event.menuItem
                    )
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


    private fun updateKitchenCopyStatusOfMenuItemsOnCategory(category: Category) {
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
            uiState.update { currentState ->
                currentState.copy(
                    categories = it
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun updateMenuItemPrice(newPrices: Map<OrderType, Float>): MenuItem? {
        var menuItem = uiState.value.selectedMenuItem
        menuItem?.let {
            uiState.update { currentState ->
                currentState.copy(
                    selectedMenuItem = it.copy(prices = newPrices)
                )
            }
            val newMenuItem = it.copy(prices = newPrices)
            return newMenuItem
        }
        return null
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