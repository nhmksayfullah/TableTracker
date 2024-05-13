package app.tabletracker.app.data.repository

import app.tabletracker.app.data.local.AppDao
import app.tabletracker.app.domain.repository.ApplicationRepository
import kotlinx.coroutines.flow.Flow

class ApplicationRoomRepository(private val appDao: AppDao): ApplicationRepository {
    override fun isTableNotEmpty(): Flow<Boolean> {
        return appDao.isTableNotEmpty()
    }

    override fun isUserRegistered(): Flow<Boolean> {
        return appDao.isUserRegistered()
    }
}