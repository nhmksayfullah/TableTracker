package app.tabletracker.app.domain.repository

import kotlinx.coroutines.flow.Flow

interface ApplicationRepository {
    fun isTableNotEmpty(): Flow<Boolean>
    fun isUserRegistered(): Flow<Boolean>
}