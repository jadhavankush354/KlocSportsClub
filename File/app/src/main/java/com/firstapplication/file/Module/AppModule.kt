package com.firstapplication.file.Module

import android.app.Application
import androidx.room.Room
import com.firstapplication.file.Repository.SportsEquipmetRepository
import com.firstapplication.file.SportsEquipmentDb
import com.firstapplication.file.Repository.SportsEquipmetRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule
{

    @Provides
    @Singleton
    fun provideMyDataBase(app:Application): SportsEquipmentDb
    {
        return Room.databaseBuilder(
            app,
            SportsEquipmentDb::class.java,
            "sports_equipment"
        ).build()
    }


    @Provides
    @Singleton
    fun provideMyRepository(myDb:SportsEquipmentDb): SportsEquipmetRepository
    {
        return SportsEquipmetRepositoryImpl(myDb.sportsEquipmentDao)
    }
}