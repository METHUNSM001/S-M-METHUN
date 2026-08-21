package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AlertDao
import com.example.data.local.dao.ChatMessageDao
import com.example.data.local.dao.FarmDao
import com.example.data.local.dao.FarmZoneDao
import com.example.data.local.dao.SimulationRecordDao
import com.example.data.local.dao.UserProfileDao
import com.example.data.local.dao.WeatherRecordDao
import com.example.data.local.entities.AlertEntity
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.FarmEntity
import com.example.data.local.entities.FarmZoneEntity
import com.example.data.local.entities.SimulationRecordEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.local.entities.WeatherRecordEntity

@Database(
    entities = [
        UserProfileEntity::class,
        FarmEntity::class,
        FarmZoneEntity::class,
        WeatherRecordEntity::class,
        AlertEntity::class,
        SimulationRecordEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AgriTwinDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun farmDao(): FarmDao
    abstract fun farmZoneDao(): FarmZoneDao
    abstract fun weatherRecordDao(): WeatherRecordDao
    abstract fun alertDao(): AlertDao
    abstract fun simulationRecordDao(): SimulationRecordDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AgriTwinDatabase? = null

        fun getDatabase(context: Context): AgriTwinDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgriTwinDatabase::class.java,
                    "agritwin_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
