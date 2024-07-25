package app.tabletracker.feature_customer.data.repository

import app.tabletracker.feature_customer.data.local.CustomerDao
import app.tabletracker.feature_customer.data.model.Customer
import app.tabletracker.feature_customer.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow

class CustomerRoomRepository(private val customerDao: CustomerDao): CustomerRepository {
    override suspend fun insertCustomer(customer: Customer) {
        customerDao.insertCustomer(customer)
    }

    override suspend fun deleteCustomer(customer: Customer) {
        customerDao.deleteCustomer(customer)
    }

    override fun readCustomerWithId(id: String): Flow<Customer> {
        return customerDao.readCustomerWithId(id)
    }

    override fun readCustomerWithName(name: String): Flow<List<Customer>> {
        return customerDao.readCustomerWithName(name)
    }

    override fun readCustomerWithPostCode(postCode: String): Flow<List<Customer>> {
        return readCustomerWithPostCode(postCode)
    }

    override fun readCustomerWithHouseNumber(houseNumber: String): Flow<List<Customer>> {
        return customerDao.readCustomerWithHouseNumber(houseNumber)
    }

    override fun readCustomerWithStreet(street: String): Flow<List<Customer>> {
        return readCustomerWithStreet(street)
    }

    override fun readCustomerWithContact(contact: String): Flow<List<Customer>> {
        return customerDao.readCustomerWithContact(contact)
    }
}