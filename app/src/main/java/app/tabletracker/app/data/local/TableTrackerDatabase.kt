package app.tabletracker.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.tabletracker.auth.data.local.AuthDao
import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra
import app.tabletracker.feature_customer.data.model.Customer
import app.tabletracker.feature_customer.data.model.CustomerTypeConverter
import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.ItemPriceTypeConverter
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.data.local.MenuDao
import app.tabletracker.feature_order.data.entity.MenuItemTypeConverter
import app.tabletracker.feature_order.data.entity.Order
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.local.OrderDao
import app.tabletracker.settings.data.local.SettingsDao

@Database(
    entities = [
        MenuItem::class,
        Category::class,
        OrderItem::class,
        Order::class,
        Restaurant::class,
        RestaurantExtra::class,
        Customer::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ItemPriceTypeConverter::class,
    MenuItemTypeConverter::class,
    CustomerTypeConverter::class
)
abstract class TableTrackerDatabase : RoomDatabase() {

    abstract val appDao: AppDao
    abstract val menuDao: MenuDao
    abstract val orderDao: OrderDao
    abstract val authDao: AuthDao
    abstract val settingsDao: SettingsDao

    companion object {
        @Volatile
        private var instance: TableTrackerDatabase? = null
        fun createDatabase(context: Context): TableTrackerDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    TableTrackerDatabase::class.java,
                    "table_tracker_database"
                ).build().also {
                    instance = it
                }
            }
        }
    }

}