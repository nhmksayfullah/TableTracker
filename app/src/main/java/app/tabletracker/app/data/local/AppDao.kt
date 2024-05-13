package app.tabletracker.app.data.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT EXISTS(SELECT 1 FROM menuitem)")
    fun isTableNotEmpty(): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM restaurant)")
    fun isUserRegistered(): Flow<Boolean>
}