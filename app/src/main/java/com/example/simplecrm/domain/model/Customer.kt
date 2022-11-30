package com.example.simplecrm.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var firstName:  String,
    var LastName:  String,
    var Email:  String,
    var mobileNumber:  String,
    var profileImg : String,
    var information: String = "",
    var companyName:  String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var companyPhoneNumber : String ="",
    var canCall: Boolean = false,
    var canVisit: Boolean = false,
    var canFollowUp: Boolean = false
) : java.io.Serializable
