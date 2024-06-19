package app.tabletracker.feature_menu.data.repository

import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.data.local.MenuDao
import app.tabletracker.feature_menu.domain.repository.EditMenuRepository
import kotlinx.coroutines.flow.Flow

class EditMenuRoomRepository(private val menuDao: MenuDao): EditMenuRepository {
    override fun readAllMenuItems(): Flow<List<MenuItem>> {
        return menuDao.readAllMenuItems()
    }

    override fun readAllMenuItemsOnCategory(categoryId: Int): Flow<CategoryWithMenuItems> {
        return menuDao.readAllMenuItemsOnCategory(categoryId)
    }

    override fun readAllCategory(): Flow<List<Category>> {
        return menuDao.readAllCategory()
    }

    override fun readAllCategoriesWithMenuItems(): Flow<List<CategoryWithMenuItems>> {
        return menuDao.readAllCategoriesWithMenuItems()
    }

    override suspend fun writeMenuItem(menuItem: MenuItem) {
        menuDao.writeMenuItem(menuItem)
    }

    override suspend fun writeCategory(category: Category) {
        menuDao.writeCategory(category)
    }

    override suspend fun deleteMenuItem(menuItem: MenuItem) {
        menuDao.deleteMenuItem(menuItem)
    }

    override suspend fun deleteCategory(category: Category) {
        menuDao.deleteCategory(category)
    }

    override suspend fun updateCategories(categories: List<Category>) {
        menuDao.updateCategories(categories)
    }
}