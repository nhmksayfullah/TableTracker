package app.tabletracker.app.data.repository

import app.tabletracker.app.data.local.AppDao
import app.tabletracker.app.domain.repository.ApplicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ApplicationRoomRepository(private val appDao: AppDao): ApplicationRepository {
    override fun hasInventory(): Flow<Boolean> {
        return appDao.hasInventory()
    }

    override fun isUserRegistered(): Flow<Boolean> {
        return appDao.isUserRegistered()
    }

    override suspend fun hasCategoryWithDefaultIndex(): Boolean {
        return appDao.hasCategoryWithDefaultIndex()
    }

    override suspend fun updateCategoryIndex() {
        if (appDao.hasCategoryWithDefaultIndex()) {
            val categories = appDao.readAllCategory().firstOrNull() ?: emptyList()
            val categoriesWithDefaultIndex = categories.filter { it.index == -1 }

            if (categoriesWithDefaultIndex.isNotEmpty()) {
                val maxIndex = appDao.getMaxIndex() ?: -1
                var nextIndex = maxIndex + 1
                categoriesWithDefaultIndex.forEach {
                    appDao.updateCategoryIndex(categoryId = it.id, newIndex = nextIndex)
                    nextIndex++
                }
            }

        }
    }
}