package com.example.simplecrm.presentation.records.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplecrm.common.Resource
import com.example.simplecrm.domain.model.Record
import com.example.simplecrm.domain.use_case.AddGetRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(private val addGetRecordsUseCase: AddGetRecordsUseCase) :
    ViewModel() {

    private val _getRecordsResponse =
        MutableStateFlow<Resource<List<Record>>>(Resource.Loading())
    val getRecordsResponse get() = _getRecordsResponse


    fun getRecords() = viewModelScope.launch {
        addGetRecordsUseCase.getRecords().collect { response ->
            when (response) {
                is Resource.Error -> _getRecordsResponse.value =
                    Resource.Error(response.message!!)
                is Resource.Loading -> _getRecordsResponse.value = Resource.Loading()
                is Resource.Success -> _getRecordsResponse.value =
                    Resource.Success(response.data!!)
            }
        }
    }
}