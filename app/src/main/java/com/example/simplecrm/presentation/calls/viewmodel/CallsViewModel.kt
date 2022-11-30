package com.example.simplecrm.presentation.calls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplecrm.common.Resource
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.use_case.AddGetCustomersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor(private val addGetCustomersUseCase: AddGetCustomersUseCase) :
    ViewModel() {

    private val _getCallCustomersResponse =
        MutableStateFlow<Resource<List<Customer>>>(Resource.Loading())
    val getCallCustomersResponse get() = _getCallCustomersResponse


    fun getCallCustomers() = viewModelScope.launch {
        addGetCustomersUseCase.getCanCallCustomers().collect { response ->
            when (response) {
                is Resource.Error -> _getCallCustomersResponse.value =
                    Resource.Error(response.message!!)
                is Resource.Loading -> _getCallCustomersResponse.value = Resource.Loading()
                is Resource.Success -> _getCallCustomersResponse.value =
                    Resource.Success(response.data!!)
            }
        }
    }




}