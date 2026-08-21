package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.AlertEntity
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.FarmEntity
import com.example.data.local.entities.FarmZoneEntity
import com.example.data.local.entities.SimulationRecordEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.local.entities.WeatherRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfileEntity)

    @Update
    suspend fun updateProfile(profile: UserProfileEntity)
}

@Dao
interface FarmDao {
    @Query("SELECT * FROM farms LIMIT 1")
    fun getPrimaryFarm(): Flow<FarmEntity?>

    @Query("SELECT * FROM farms")
    fun getAllFarms(): Flow<List<FarmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarm(farm: FarmEntity)

    @Update
    suspend fun updateFarm(farm: FarmEntity)
}

@Dao
interface FarmZoneDao {
    @Query("SELECT * FROM farm_zones WHERE farmId = :farmId")
    fun getZonesForFarm(farmId: String): Flow<List<FarmZoneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZones(zones: List<FarmZoneEntity>)

    @Query("DELETE FROM farm_zones WHERE farmId = :farmId")
    suspend fun deleteZonesForFarm(farmId: String)
}

@Dao
interface WeatherRecordDao {
    @Query("SELECT * FROM weather_records WHERE farmId = :farmId LIMIT 1")
    fun getLatestWeather(farmId: String): Flow<WeatherRecordEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherRecordEntity)
}

@Dao
interface AlertDao {
    @Query("SELECT * FROM farm_alerts ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<AlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerts(alerts: List<AlertEntity>)

    @Query("UPDATE farm_alerts SET isRead = 1 WHERE id = :alertId")
    suspend fun markAsRead(alertId: String)
}

@Dao
interface SimulationRecordDao {
    @Query("SELECT * FROM simulation_records WHERE farmId = :farmId ORDER BY timestamp DESC")
    fun getSimulations(farmId: String): Flow<List<SimulationRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSimulation(simulation: SimulationRecordEntity)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()
}
