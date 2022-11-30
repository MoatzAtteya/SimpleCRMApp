package com.example.simplecrm.presentation.home.viewmodel

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
class HomeViewModel @Inject constructor(
    private val addGetCustomersUseCase: AddGetCustomersUseCase
) : ViewModel() {

    private val _getAllCustomersResponse =
        MutableStateFlow<Resource<List<Customer>>>(Resource.Loading())
    val getAllCustomersResponse get() = _getAllCustomersResponse


    fun getAllCustomers() = viewModelScope.launch {
        addGetCustomersUseCase.getAllCustomers().collect { response ->
            when (response) {
                is Resource.Error -> _getAllCustomersResponse.value =
                    Resource.Error(response.message!!)
                is Resource.Loading -> _getAllCustomersResponse.value = Resource.Loading()
                is Resource.Success -> _getAllCustomersResponse.value =
                    Resource.Success(response.data!!)
            }
        }
    }



}