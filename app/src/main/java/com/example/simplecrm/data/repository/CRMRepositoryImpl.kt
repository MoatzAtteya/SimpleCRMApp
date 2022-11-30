package com.example.simplecrm.data.repository

import com.example.simplecrm.data.dao.CRMDao
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.model.Record
import com.example.simplecrm.domain.repository.CRMRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CRMRepositoryImpl @Inject constructor(private val dao: CRMDao) : CRMRepository {
    override suspend fun getAllCustomers(): List<Customer> {
        return dao.getCustomers()
    }

    override suspend fun insertCustomer(customer: Customer) {
        dao.insertCustomer(customer)
    }

    override suspend fun deleteCustomer(customer: Customer) {
        dao.deleteCustomer(customer)
    }

    override suspend fun getCallCustomers(): List<Customer> {
        return dao.getCanCallCustomers()
    }

    override suspend fun getRecords(): List<Record> {
        return dao.getRecords()
    }

    override suspend fun insertRecord(record: Record) {
        dao.insertRecord(record)
    }
}