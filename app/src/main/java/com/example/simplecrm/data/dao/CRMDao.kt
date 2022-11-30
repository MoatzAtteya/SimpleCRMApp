package com.example.simplecrm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.model.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface CRMDao {

    @Query("SELECT * FROM customers ORDER BY id ASC")
    fun getCustomers() : List<Customer>

    @Query("SELECT * FROM customers WHERE canCall = 1 ORDER BY id ASC")
    fun getCanCallCustomers() : List<Customer>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCustomer(customer: Customer)

    @Delete
    fun deleteCustomer(customer: Customer)

    @Query("DELETE FROM customers")
    fun deleteAllCustomers()

    @Query("SELECT * FROM Records ORDER BY time DESC")
    fun getRecords() : List<Record>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecord(record: Record)

}