package com.example.simplecrm.domain.repository

import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.model.Record
import kotlinx.coroutines.flow.Flow

interface CRMRepository {

    suspend fun getAllCustomers() : List<Customer>

    suspend fun insertCustomer(customer: Customer)

    suspend fun deleteCustomer(customer: Customer)

    suspend fun getCallCustomers() : List<Customer>

    suspend fun getRecords() : List<Record>

    suspend fun insertRecord(record: Record)
}