package app.tabletracker.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.tabletracker.core.database.TableTrackerDatabase
import app.tabletracker.app.data.repository.ApplicationRoomRepository
import app.tabletracker.app.domain.repository.ApplicationRepository
import app.tabletracker.features.auth.data.repository.AuthRoomRepository
import app.tabletracker.features.auth.data.repository.DevicePreferencesRepository
import app.tabletracker.features.auth.domain.repository.AuthRepository
import app.tabletracker.features.customer.data.repository.CustomerRoomRepository
import app.tabletracker.features.customer.domain.repository.CustomerRepository
import app.tabletracker.features.inventory.data.repository.EditMenuRoomRepository
import app.tabletracker.features.inventory.domain.repository.EditMenuRepository
import app.tabletracker.features.order.data.repository.OrderRoomRepository
import app.tabletracker.features.order.domain.repository.OrderRepository
import app.tabletracker.features.settings.data.repository.SettingsRoomRepository
import app.tabletracker.features.settings.domain.SettingsRepository

interface TableTrackerContainer {
    val applicationRepository: ApplicationRepository
    val editMenuRepository: EditMenuRepository
    val orderRepository: OrderRepository
    val authRepository: AuthRepository
    val settingsRepository: SettingsRepository
    val customerRepository: CustomerRepository
    val deviceTypeRepository: DevicePreferencesRepository
}

class TableTrackerDataContainer(
    private val database: TableTrackerDatabase,
    private val dataStore: DataStore<Preferences>
) : TableTrackerContainer {
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
    override val customerRepository: CustomerRepository by lazy {
        CustomerRoomRepository(database.customerDao)
    }
    override val deviceTypeRepository: DevicePreferencesRepository by lazy {
        DevicePreferencesRepository(dataStore)
    }
}