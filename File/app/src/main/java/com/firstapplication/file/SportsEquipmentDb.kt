package com.firstapplication.file

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.firstapplication.file.SportsEquipmentEntity

@Database(entities = [SportsEquipmentEntity::class], version = 2,exportSchema = false)
abstract class SportsEquipmentDb : RoomDatabase()
{
    abstract val sportsEquipmentDao: SportsEquipmentDao

    companion object {
        @Volatile
        private var INSTANCE: SportsEquipmentDb? = null

        fun getInstance(context: Context): SportsEquipmentDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SportsEquipmentDb::class.java,
                    "Sports_equipment"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
