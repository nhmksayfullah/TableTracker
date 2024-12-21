package app.tabletracker.app.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import app.tabletracker.auth.data.local.AuthDao
import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra
import app.tabletracker.feature_customer.data.local.CustomerDao
import app.tabletracker.feature_customer.data.model.Customer
import app.tabletracker.feature_customer.data.model.CustomerTypeConverter
import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.ItemPriceTypeConverter
import app.tabletracker.feature_menu.data.entity.MealTypeConverter
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.data.local.MenuDao
import app.tabletracker.feature_order.data.entity.Discount
import app.tabletracker.feature_order.data.entity.DiscountTypeConverter
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
        Customer::class,
        Discount::class
    ],
    version = 11,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = Migration4To5::class),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11)
    ]
)
@TypeConverters(
    ItemPriceTypeConverter::class,
    MenuItemTypeConverter::class,
    CustomerTypeConverter::class,
    MealTypeConverter::class,
    DiscountTypeConverter::class
)
abstract class TableTrackerDatabase : RoomDatabase() {

    abstract val appDao: AppDao
    abstract val menuDao: MenuDao
    abstract val orderDao: OrderDao
    abstract val authDao: AuthDao
    abstract val settingsDao: SettingsDao
    abstract val customerDao: CustomerDao

    companion object {
        @Volatile
        private var instance: TableTrackerDatabase? = null
        fun createDatabase(context: Context): TableTrackerDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    TableTrackerDatabase::class.java,
                    "table_tracker_database"
                ).addMigrations(migration6To7)
                    .build().also {
                    instance = it
                }
            }
        }
    }

}

@DeleteColumn(tableName = "MenuItem", columnName = "meal")
class Migration4To5: AutoMigrationSpec

val migration6To7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create the new table
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `Discount` (" +
                    "`id` TEXT NOT NULL, " +
                    "`title` TEXT NOT NULL, " +
                    "`value` TEXT NOT NULL, " +
                    "PRIMARY KEY(`id`))"
        )
    }
}