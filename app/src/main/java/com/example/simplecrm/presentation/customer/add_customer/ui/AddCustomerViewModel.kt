package com.example.simplecrm.presentation.customer.add_customer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.use_case.AddGetCustomersUseCase
import com.example.simplecrm.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCustomerViewModel @Inject constructor(private val addGetCustomersUseCase: AddGetCustomersUseCase) : ViewModel() {

    private val _insertCustomerResponse =
        MutableStateFlow<Resource<String>>(Resource.Loading())
    val insertCustomerResponse get() = _insertCustomerResponse

    fun addCustomer(customer: Customer) = viewModelScope.launch {
        addGetCustomersUseCase.insertCustomer(customer).collect { response ->
            when (response) {
                is Resource.Error -> _insertCustomerResponse.value =
                    Resource.Error(response.message!!)
                is Resource.Loading -> _insertCustomerResponse.value = Resource.Loading()
                is Resource.Success -> _insertCustomerResponse.value =
                    Resource.Success(response.data!!)
            }
        }
    }
}