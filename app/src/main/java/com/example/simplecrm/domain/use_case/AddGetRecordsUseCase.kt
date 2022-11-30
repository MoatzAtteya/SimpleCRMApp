package com.example.simplecrm.domain.use_case

import com.example.simplecrm.common.Resource
import com.example.simplecrm.domain.model.Record
import com.example.simplecrm.domain.repository.CRMRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddGetRecordsUseCase @Inject constructor(private val repo: CRMRepository) {
    fun getRecords(): Flow<Resource<List<Record>>> = flow {
        try {
            emit(Resource.Success(repo.getRecords()))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message.toString()))
        }
    }

    fun insertRecord(record: Record) : Flow<Resource<String>> = flow {
        try {
            repo.insertRecord(record)
            emit(Resource.Success("Record added successfully."))
        }catch (ex : Exception){
            ex.printStackTrace()
            emit(Resource.Error(ex.message.toString()))
        }
    }
}