package com.example.simplecrm.domain.use_case

import com.example.simplecrm.common.Resource
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.repository.CRMRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCustomerUseCase @Inject constructor(private val repo : CRMRepository) {
    fun deleteCustomer(customer: Customer) : Flow<Resource<String>> = flow {
        try {
            repo.deleteCustomer(customer)
            emit(Resource.Success("Customer deleted successfully."))
        }catch (ex : Exception){
            ex.printStackTrace()
            emit(Resource.Error(ex.message.toString()))
        }
    }
}