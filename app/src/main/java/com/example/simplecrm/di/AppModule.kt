package com.example.simplecrm.di

import android.content.Context
import androidx.room.Room
import com.example.simplecrm.data.dao.CRMDao
import com.example.simplecrm.data.dao.CRMDatabase
import com.example.simplecrm.data.repository.CRMRepositoryImpl
import com.example.simplecrm.domain.repository.CRMRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext appContext: Context): CRMDatabase {
        return Room.databaseBuilder(
            appContext,
            CRMDatabase::class.java,
            "asd"
        ).allowMainThreadQueries().build()
    }


    @Provides
    @Singleton
    fun providesCRMDao(appDatabase: CRMDatabase): CRMDao {
        return appDatabase.crmDao()
    }

    @Provides
    @Singleton
    fun providesCustomerRepository(crmDao: CRMDao) :CRMRepository{
        return CRMRepositoryImpl(crmDao)
    }


}