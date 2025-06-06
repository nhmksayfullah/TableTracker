package app.tabletracker.app.domain.repository

import kotlinx.coroutines.flow.Flow

interface ApplicationRepository {
    fun hasInventory(): Flow<Boolean>
    fun isUserRegistered(): Flow<Boolean>
    suspend fun hasCategoryWithDefaultIndex(): Boolean
    suspend fun updateCategoryIndex()
}