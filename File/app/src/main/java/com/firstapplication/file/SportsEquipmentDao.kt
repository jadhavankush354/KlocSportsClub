package com.firstapplication.file

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firstapplication.file.SportsEquipmentEntity

@Dao
interface SportsEquipmentDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSport(sportsequipment: SportsEquipmentEntity)

    @Query("SELECT EquipmentSize FROM sports_equipment " +
            "WHERE Equipment = :equipment " +
            "AND TypeOfEquipment = :equipmentType " +
            "AND Categories = :categories " +
            "AND :playerAge BETWEEN MinPlayerAge AND MaxPlayerAge ")
    suspend fun getEquipmentSizebyAge(
        equipment: String,
        equipmentType: String,
        categories: String,
        playerAge: Int,
    ): String?


    @Query("SELECT EquipmentSize FROM sports_equipment " +
            "WHERE Equipment = :equipment " +
            "AND TypeOfEquipment = :equipmentType " +
            "AND Categories = :categories " +
            "AND :playerHeight BETWEEN MinPlayerHeight AND MaxPlayerHeight")
    suspend fun getEquipmentSizebyheight(
        equipment: String,
        equipmentType: String,
        categories: String,
        playerHeight: Double
    ): String?
}
