package app.tabletracker.feature_menu.domain.repository

import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_menu.data.entity.MenuItem
import kotlinx.coroutines.flow.Flow

interface EditMenuRepository {
    fun readAllMenuItems(): Flow<List<MenuItem>>
    fun readAllMenuItemsOnCategory(categoryId: Int): Flow<CategoryWithMenuItems>
    suspend fun updateCategoryIndex(categoryId: Int, newIndex: Int)

    fun readAllCategory(): Flow<List<Category>>
    fun readAllCategoriesWithMenuItems(): Flow<List<CategoryWithMenuItems>>

    suspend fun writeMenuItem(menuItem: MenuItem)
    suspend fun writeCategory(category: Category)
    suspend fun deleteMenuItem(menuItem: MenuItem)
    suspend fun deleteCategory(category: Category)
    suspend fun updateCategories(categories: List<Category>)
}