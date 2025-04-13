package app.tabletracker.features.inventory.data.repository

import app.tabletracker.features.inventory.data.local.MenuDao
import app.tabletracker.features.inventory.domain.repository.EditMenuRepository
import app.tabletracker.features.inventory.data.entity.Category
import app.tabletracker.features.inventory.data.entity.CategoryWithMenuItems
import app.tabletracker.features.inventory.data.entity.MenuItem
import kotlinx.coroutines.flow.Flow

class EditMenuRoomRepository(private val menuDao: MenuDao) : EditMenuRepository {
    override fun readAllMenuItems(): Flow<List<MenuItem>> {
        return menuDao.readAllMenuItems()
    }

    override fun readAllMenuItemsOnCategory(categoryId: Int): Flow<CategoryWithMenuItems> {
        return menuDao.readAllMenuItemsOnCategory(categoryId)
    }

    override suspend fun updateCategoryIndex(categoryId: Int, newIndex: Int) {
        menuDao.updateCategoryIndex(categoryId, newIndex)
    }

    override suspend fun updateMenuItemIndex(menuItemId: String, newIndex: Int) {
        menuDao.updateMenuItemIndex(menuItemId, newIndex)
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