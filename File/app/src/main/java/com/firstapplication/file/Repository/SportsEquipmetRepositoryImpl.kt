package com.firstapplication.file.Repository


import com.firstapplication.file.SportsEquipmentDao
import com.firstapplication.file.SportsEquipmentEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SportsEquipmetRepositoryImpl @Inject constructor(
    private val sportsEquipmentDao: SportsEquipmentDao
) : SportsEquipmetRepository {
    override suspend fun insertSport(sportsEquipmentEntity: SportsEquipmentEntity) {
        withContext(IO) {
            sportsEquipmentDao.insertSport(sportsEquipmentEntity)
        }
    }

    override suspend fun getEquipmentSizeByAge(
        equipment: String,
        equipmentType: String,
        categoriesType: String,
        playerAge: Int
    ):String? {
        return withContext(IO) {
            sportsEquipmentDao.getEquipmentSizebyAge(
                equipment,
                equipmentType,
                categoriesType,
                playerAge
            )
        }
    }

    override suspend fun getEquipmentSizeByHeight(
        equipment: String,
        equipmentType: String,
        categoriesType: String,
        playerHeight: Double,
    ): String? {
        return withContext(IO) {
            sportsEquipmentDao.getEquipmentSizebyheight(
                equipment,
                equipmentType,
                categoriesType,
                playerHeight)
        }
    }
}
