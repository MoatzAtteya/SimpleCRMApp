package com.example.simplecrm.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Records")
data class Record(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var actionType : Int,
    var customerName : String,
    var customerCompany  : String,
    var customerProfileImg : String,
    var time : Long
) : java.io.Serializable
