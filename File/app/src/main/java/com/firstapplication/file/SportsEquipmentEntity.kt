package com.firstapplication.file

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sports_equipment")
data class SportsEquipmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val equipment: String,
    val typeOfEquipment: String,
    val categories: String,
    val EquipmentSize: String,
    val minPlayerAge: Int,
    val maxPlayerAge: Int,
    val minPlayerHeight: Double,
    val maxPlayerHeight: Double
)
