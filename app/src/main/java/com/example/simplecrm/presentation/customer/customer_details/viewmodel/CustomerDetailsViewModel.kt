package com.example.simplecrm.presentation.customer.customer_details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplecrm.common.Resource
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.model.Record
import com.example.simplecrm.domain.use_case.AddGetRecordsUseCase
import com.example.simplecrm.domain.use_case.DeleteCustomerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerDetailsViewModel @Inject constructor(private val deleteCustomerUseCase: DeleteCustomerUseCase , private val addGetRecordsUseCase: AddGetRecordsUseCase) :
    ViewModel() {

    private val _deleteCustomerResponse =
        MutableStateFlow<Resource<String>>(Resource.Loading())
    val deleteCustomerResponse get() = _deleteCustomerResponse


    fun deleteCustomer(customer: Customer) = viewModelScope.launch {
        deleteCustomerUseCase.deleteCustomer(customer).collect { response ->
            when (response) {
                is Resource.Error -> _deleteCustomerResponse.value =
                    Resource.Error(response.message!!)
                is Resource.Loading -> _deleteCustomerResponse.value = Resource.Loading()
                is Resource.Success -> _deleteCustomerResponse.value =
                    Resource.Success(response.data!!)
            }
        }
    }

    private val _insertRecordResponse =
        MutableStateFlow<Resource<String>>(Resource.Loading())
    val insertRecordResponse get() = _insertRecordResponse


    fun insertRecords(record: Record) = viewModelScope.launch {
        addGetRecordsUseCase.insertRecord(record).collect { response ->
            when (response) {
                is Resource.Error -> _insertRecordResponse.value =
                    Resource.Error(response.message!!)
                is Resource.Loading -> _insertRecordResponse.value = Resource.Loading()
                is Resource.Success -> _insertRecordResponse.value =
                    Resource.Success(response.data!!)
            }
        }
    }

}