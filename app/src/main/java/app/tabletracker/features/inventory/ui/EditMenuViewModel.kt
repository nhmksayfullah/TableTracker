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
    private var menusJob: Job? = null
    private var explorerJob: Job? = null
    private var allCategoriesJob: Job? = null
    private var categoriesJob: Job? = null

    init {
        populateCategoriesWithMenuItems()
        populateTopLevelCategoriesWithSubCategoriesAndMenuItems()
        populateAllCategoriesWithSubCategoriesAndMenuItems()
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
                    val newMenuItem = updateMenuItemPrice(event.menuItem, event.newPrices)
                    repository.writeMenuItem(newMenuItem)
                    uiState.update { currentState ->
                        currentState.copy(
                            selectedMenuItem = MenuItem.empty(newMenuItem.categoryId)
                        )
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
        menusJob?.cancel()
        menusJob = repository.readAllCategoriesWithMenuItems().onEach {
            uiState.update { currentState ->
                currentState.copy(
                    menus = it
                )
            }
        }.launchIn(viewModelScope)
    }

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Populates the [EditMenuUiState.explorer] with top-level categories with their subcategories and menu items.
     *
     * The [EditMenuUiState.explorer] is a list of [CategoryWithSubcategoriesAndMenuItems] which represents top-level categories
     * (categories with no parent category) with their subcategories and menu items. The [repository.readTopLevelCategoriesWithSubcategoriesAndMenuItems] is used to
     * retrieve the data from the database and populate the [EditMenuUiState.explorer].
     *
     * The [explorerJob] is cancelled and relaunched every time this function is called to ensure that the data is always up to date.
     * The [uiState] is updated with the new data when it is available.
     */
/* <<<<<<<<<<  4b88039d-23da-4c49-a883-09d01bb25660  >>>>>>>>>>> */
    private fun populateTopLevelCategoriesWithSubCategoriesAndMenuItems() {
        explorerJob?.cancel()
        explorerJob = repository.readTopLevelCategoriesWithSubcategoriesAndMenuItems().onEach {
            uiState.update { currentState ->
                currentState.copy(
                    explorer = it
                )
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Populates the [EditMenuUiState.allCategories] with all categories (including subcategories) with their subcategories and menu items.
     *
     * This is used by the ChildInventoryExplorerScreen to find categories by ID, including non-top-level categories.
     * The [repository.readAllCategoriesWithSubcategoriesAndMenuItems] is used to retrieve the data from the database.
     *
     * The [allCategoriesJob] is cancelled and relaunched every time this function is called to ensure that the data is always up to date.
     * The [uiState] is updated with the new data when it is available.
     */
    private fun populateAllCategoriesWithSubCategoriesAndMenuItems() {
        allCategoriesJob?.cancel()
        allCategoriesJob = repository.readAllCategoriesWithSubcategoriesAndMenuItems().onEach {
            uiState.update { currentState ->
                currentState.copy(
                    allCategories = it
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun populateCategory() {
        categoriesJob?.cancel()
        categoriesJob = repository.readAllCategory().onEach {
            uiState.update { currentState ->
                currentState.copy(
                    categories = it
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun updateMenuItemPrice(menuItem: MenuItem, newPrices: Map<OrderType, Float>): MenuItem {
            uiState.update { currentState ->
                currentState.copy(
                    selectedMenuItem = menuItem.copy(prices = newPrices)
                )
            }
            return menuItem.copy(prices = newPrices)
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
