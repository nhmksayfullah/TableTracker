package app.tabletracker.features.customer.domain.repository

import app.tabletracker.features.customer.data.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    suspend fun insertCustomer(customer: Customer)
    suspend fun deleteCustomer(customer: Customer)
    fun readCustomerWithId(id: String): Flow<Customer>
    fun readCustomerWithName(name: String): Flow<List<Customer>>
    fun readCustomerWithPostCode(postCode: String): Flow<List<Customer>>
    fun readCustomerWithHouseNumber(houseNumber: String): Flow<List<Customer>>
    fun readCustomerWithStreet(street: String): Flow<List<Customer>>
    fun readCustomerWithContact(contact: String): Flow<List<Customer>>
}