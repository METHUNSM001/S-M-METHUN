package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val id: String = "default_user",
    val fullName: String = "Muthu Kumar",
    val phone: String = "+91 98765 43210",
    val language: String = "ta",
    val state: String = "Tamil Nadu",
    val district: String = "Madurai",
    val village: String = "Alanganallur",
    val farmerType: String = "Small & Marginal",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "farms")
data class FarmEntity(
    @PrimaryKey val id: String = "demo_farm_1",
    val userId: String = "default_user",
    val name: String = "Muthu's Green Acres",
    val locationName: String = "Madurai, Tamil Nadu",
    val latitude: Double = 9.9252,
    val longitude: Double = 78.1198,
    val areaAcres: Double = 2.0,
    val primaryCrop: String = "Tomato",
    val cropVariety: String = "Shivam Hybrid (PKM-1)",
    val cropStage: String = "Flowering & Fruit Setting",
    val irrigationMethod: String = "Drip Irrigation",
    val soilType: String = "Red Loamy Clay",
    val sowingDate: String = "15 July 2026",
    val healthScore: Int = 88,
    val isDemo: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "farm_zones")
data class FarmZoneEntity(
    @PrimaryKey val id: String,
    val farmId: String,
    val zoneName: String,
    val cropName: String,
    val cropStage: String,
    val soilMoisturePercent: Int,
    val soilTemperatureC: Double,
    val ecNutrientLevel: Double,
    val healthStatus: String, // GOOD, WATCH, ACTION
    val recommendation: String,
    val colorHex: String
)

@Entity(tableName = "weather_records")
data class WeatherRecordEntity(
    @PrimaryKey val id: String = "latest_weather",
    val farmId: String = "demo_farm_1",
    val location: String = "Madurai",
    val temperatureC: Double = 29.5,
    val feelsLikeC: Double = 31.0,
    val humidityPercent: Int = 78,
    val condition: String = "Scattered Clouds",
    val rainProbabilityPercent: Int = 82,
    val rainExpectedTomorrow: Boolean = true,
    val windSpeedKmh: Double = 14.2,
    val solarRadiationWpm: Int = 620,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "farm_alerts")
data class AlertEntity(
    @PrimaryKey val id: String,
    val farmId: String,
    val title: String,
    val description: String,
    val category: String, // WEATHER, WATER, DISEASE, SCHEME, MARKET
    val severity: String, // HIGH, MEDIUM, LOW
    val actionText: String,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "simulation_records")
data class SimulationRecordEntity(
    @PrimaryKey val id: String,
    val farmId: String,
    val scenarioName: String,
    val estimatedYieldKg: Double,
    val waterSavedLiters: Int,
    val estimatedCostRs: Double,
    val estimatedRevenueRs: Double,
    val netProfitRs: Double,
    val riskLevel: String,
    val isRecommended: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val sender: String, // USER, AGRITWIN_AI
    val messageText: String,
    val language: String,
    val toolInvoked: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
