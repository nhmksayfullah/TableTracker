package app.tabletracker.app.config

import app.tabletracker.app.data.local.TableTrackerDatabase
import app.tabletracker.app.data.repository.ApplicationRoomRepository
import app.tabletracker.app.domain.repository.ApplicationRepository
import app.tabletracker.auth.data.repository.AuthRoomRepository
import app.tabletracker.auth.domain.repository.AuthRepository
import app.tabletracker.feature_menu.data.repository.EditMenuRoomRepository
import app.tabletracker.feature_menu.domain.repository.EditMenuRepository
import app.tabletracker.feature_order.data.repository.OrderRoomRepository
import app.tabletracker.feature_order.domain.repository.OrderRepository
import app.tabletracker.settings.data.repository.SettingsRoomRepository
import app.tabletracker.settings.domain.SettingsRepository

interface TableTrackerContainer {
    val applicationRepository: ApplicationRepository
    val editMenuRepository: EditMenuRepository
    val orderRepository: OrderRepository
    val authRepository: AuthRepository
    val settingsRepository: SettingsRepository
}

class TableTrackerDataContainer(database: TableTrackerDatabase): TableTrackerContainer {
    override val applicationRepository: ApplicationRepository by lazy {
        ApplicationRoomRepository(database.appDao)
    }
    override val editMenuRepository: EditMenuRepository by lazy {
        EditMenuRoomRepository(database.menuDao)
    }
    override val orderRepository: OrderRepository by lazy {
        OrderRoomRepository(database.orderDao)
    }
    override val authRepository: AuthRepository by lazy {
        AuthRoomRepository(database.authDao)
    }
    override val settingsRepository: SettingsRepository by lazy {
        SettingsRoomRepository(database.settingsDao)
    }
}