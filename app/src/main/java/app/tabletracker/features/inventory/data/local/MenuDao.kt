package app.tabletracker.features.inventory.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import app.tabletracker.features.inventory.data.entity.Category
import app.tabletracker.features.inventory.data.entity.CategoryWithMenuItems
import app.tabletracker.features.inventory.data.entity.MenuItem
import kotlinx.coroutines.flow.Flow


@Dao
interface MenuDao {
    @Query("SELECT * FROM menuitem")
    fun readAllMenuItems(): Flow<List<MenuItem>>

    @Transaction
    @Query("SELECT * FROM category WHERE id = :categoryId")
    fun readAllMenuItemsOnCategory(categoryId: Int): Flow<CategoryWithMenuItems>


    @Query("UPDATE Category SET `index` = :newIndex WHERE id = :categoryId")
    suspend fun updateCategoryIndex(categoryId: Int, newIndex: Int)

    @Query("UPDATE MenuItem SET `index` = :newIndex WHERE id = :menuItemId")
    suspend fun updateMenuItemIndex(menuItemId: String, newIndex: Int)

    @Query("SELECT * FROM category")
    fun readAllCategory(): Flow<List<Category>>

    @Transaction
    @Query("SELECT * FROM category")
    fun readAllCategoriesWithMenuItems(): Flow<List<CategoryWithMenuItems>>

    @Upsert
    suspend fun writeMenuItem(menuItem: MenuItem)

    @Upsert
    suspend fun writeCategory(category: Category)

    @Delete
    suspend fun deleteMenuItem(menuItem: MenuItem)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Update
    suspend fun updateCategories(categories: List<Category>)
}