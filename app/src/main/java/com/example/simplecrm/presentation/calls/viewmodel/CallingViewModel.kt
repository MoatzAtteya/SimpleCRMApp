package com.example.simplecrm.presentation.calls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplecrm.common.Resource
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.model.Record
import com.example.simplecrm.domain.use_case.AddGetRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallingViewModel @Inject constructor(private val addGetRecordsUseCase: AddGetRecordsUseCase) :
    ViewModel() {

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