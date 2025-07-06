package app.tabletracker.features.inventory.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import app.tabletracker.features.inventory.data.entity.Category
import app.tabletracker.features.inventory.data.entity.CategoryWithMenuItems
import app.tabletracker.features.inventory.data.entity.CategoryWithSubcategories
import app.tabletracker.features.inventory.data.entity.CategoryWithSubcategoriesAndMenuItems
import app.tabletracker.features.inventory.data.entity.MenuItem
import kotlinx.coroutines.flow.Flow


@Dao
interface MenuDao {
    @Query("SELECT * FROM menuitem")
    fun readAllMenuItems(): Flow<List<MenuItem>>

    @Transaction
    @Query("SELECT * FROM category WHERE id = :categoryId")
    fun readAllMenuItemsOnCategory(categoryId: Int): Flow<CategoryWithMenuItems>

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Read all subcategories under a category from the database as a flow.
     *
     * @param categoryId the id of the category to read subcategories from
     * @return a flow of [CategoryWithSubcategories] containing the subcategories
     */
/* <<<<<<<<<<  16dd87c9-5b8a-44da-85cf-e9edf426e41f  >>>>>>>>>>> */
    @Transaction
    @Query("SELECT * FROM category WHERE id = :categoryId")
    fun readSubcategoriesOnCategory(categoryId: Int): Flow<CategoryWithSubcategories>

    @Transaction
    @Query("SELECT * FROM category WHERE id = :categoryId")
    fun readSubcategoriesAndMenuItemsOnCategory(categoryId: Int): Flow<CategoryWithSubcategoriesAndMenuItems>

    @Query("SELECT * FROM category WHERE parentCategoryId IS NULL")
    fun readTopLevelCategories(): Flow<List<Category>>

    @Transaction
    @Query("SELECT * FROM category WHERE parentCategoryId IS NULL")
    fun readTopLevelCategoriesWithMenuItems(): Flow<List<CategoryWithMenuItems>>

    @Transaction
    @Query("SELECT * FROM category WHERE parentCategoryId IS NULL")
    fun readTopLevelCategoriesWithSubcategories(): Flow<List<CategoryWithSubcategories>>

    @Transaction
    @Query("SELECT * FROM category WHERE parentCategoryId IS NULL")
    fun readTopLevelCategoriesWithSubcategoriesAndMenuItems(): Flow<List<CategoryWithSubcategoriesAndMenuItems>>

    @Query("UPDATE Category SET `index` = :newIndex WHERE id = :categoryId")
    suspend fun updateCategoryIndex(categoryId: Int, newIndex: Int)

    @Query("UPDATE MenuItem SET `index` = :newIndex WHERE id = :menuItemId")
    suspend fun updateMenuItemIndex(menuItemId: String, newIndex: Int)

    @Query("SELECT * FROM category")
    fun readAllCategory(): Flow<List<Category>>

    @Transaction
    @Query("SELECT * FROM category")
    fun readAllCategoriesWithMenuItems(): Flow<List<CategoryWithMenuItems>>

    @Transaction
    @Query("SELECT * FROM category")
    fun readAllCategoriesWithSubcategories(): Flow<List<CategoryWithSubcategories>>

    @Transaction
    @Query("SELECT * FROM category")
    fun readAllCategoriesWithSubcategoriesAndMenuItems(): Flow<List<CategoryWithSubcategoriesAndMenuItems>>

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
