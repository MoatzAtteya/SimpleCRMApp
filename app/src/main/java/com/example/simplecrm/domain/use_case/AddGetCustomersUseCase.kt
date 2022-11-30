package com.example.simplecrm.domain.use_case

import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.repository.CRMRepository
import com.example.simplecrm.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddGetCustomersUseCase @Inject constructor(private val repo: CRMRepository) {

    fun getAllCustomers(): Flow<Resource<List<Customer>>> = flow {
        try {
            emit(Resource.Success(repo.getAllCustomers()))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message.toString()))
        }
    }

     fun insertCustomer(customer: Customer) : Flow<Resource<String>> = flow {
        try {
            repo.insertCustomer(customer)
            emit(Resource.Success("Customer added successfully."))
        }catch (ex : Exception){
            ex.printStackTrace()
            emit(Resource.Error(ex.message.toString()))
        }
    }

    fun getCanCallCustomers() : Flow<Resource<List<Customer>>> = flow {
        try {
            emit(Resource.Success(repo.getCallCustomers()))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message.toString()))
        }
    }

}
