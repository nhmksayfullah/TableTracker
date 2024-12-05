package app.tabletracker.app.data.local

import androidx.room.Dao
import androidx.room.Query
import app.tabletracker.feature_menu.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT EXISTS(SELECT 1 FROM menuitem)")
    fun isTableNotEmpty(): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM restaurant)")
    fun isUserRegistered(): Flow<Boolean>

    // Function to check if there are any categories with a default index of -1
    @Query("SELECT EXISTS(SELECT 1 FROM Category WHERE `index` = -1 LIMIT 1)")
    suspend fun hasCategoryWithDefaultIndex(): Boolean

    // Function to update the index of a specific category
    @Query("UPDATE Category SET `index` = :newIndex WHERE id = :categoryId")
    suspend fun updateCategoryIndex(categoryId: Int, newIndex: Int)

    @Query("SELECT * FROM category")
    fun readAllCategory(): Flow<List<Category>>

    // Get the maximum index currently in the table
    @Query("SELECT MAX(`index`) FROM Category")
    suspend fun getMaxIndex(): Int?
}