package app.tabletracker.auth.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import app.tabletracker.auth.data.model.DeviceType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DevicePreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val DEVICE_TYPE_KEY = stringPreferencesKey("device_type")
    }

    suspend fun saveDeviceType(deviceType: DeviceType) {
        dataStore.edit {
            it[DEVICE_TYPE_KEY] = deviceType.name
        }
    }

    val deviceType: Flow<DeviceType> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[DEVICE_TYPE_KEY]?.let { name ->
                DeviceType.valueOf(name)
            } ?: DeviceType.Main
        }

}