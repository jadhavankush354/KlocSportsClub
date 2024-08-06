package com.firstapplication.file.Repository

import com.firstapplication.file.SportsEquipmentDao
import com.firstapplication.file.SportsEquipmentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface SportsEquipmetRepository
{
    suspend fun insertSport(sportsEquipmentEntity: SportsEquipmentEntity)

    suspend fun getEquipmentSizeByAge(
        equipment: String,
        equipmentType: String,
        categoriesType: String,
        playerAge: Int): String?

    suspend fun getEquipmentSizeByHeight(
        equipment: String,
        equipmentType: String,
        categoriesType: String,
        playerHeight: Double): String?




}












