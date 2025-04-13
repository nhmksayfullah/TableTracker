package app.tabletracker.features.customer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import app.tabletracker.features.customer.data.model.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Upsert
    suspend fun insertCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM Customer WHERE id = :id")
    fun readCustomerWithId(id: String): Flow<Customer>

    @Query("SELECT * FROM Customer WHERE name = :name")
    fun readCustomerWithName(name: String): Flow<List<Customer>>

    @Query("SELECT * FROM Customer WHERE postCode = :postCode")
    fun readCustomerWithPostCode(postCode: String): Flow<List<Customer>>

    @Query("SELECT * FROM Customer WHERE houseNumber = :houseNumber")
    fun readCustomerWithHouseNumber(houseNumber: String): Flow<List<Customer>>

    @Query("SELECT * FROM Customer WHERE street = :street")
    fun readCustomerWithStreet(street: String): Flow<List<Customer>>

    @Query("SELECT * FROM Customer WHERE contact = :contact")
    fun readCustomerWithContact(contact: String): Flow<List<Customer>>

}