package com.example.simplecrm.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.model.Record

@Database(entities = [Customer::class,Record::class], version = 1, exportSchema = false)

abstract class CRMDatabase : RoomDatabase() {
    abstract fun crmDao(): CRMDao
    companion object {
        @Volatile
        private var INSTANCE: CRMDatabase? = null

        fun getDatabase(context: Context): CRMDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CRMDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}