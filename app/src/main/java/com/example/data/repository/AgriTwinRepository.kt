package com.example.data.repository

import com.example.data.local.AgriTwinDatabase
import com.example.data.local.entities.AlertEntity
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.FarmEntity
import com.example.data.local.entities.FarmZoneEntity
import com.example.data.local.entities.SimulationRecordEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.local.entities.WeatherRecordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID

class AgriTwinRepository(private val database: AgriTwinDatabase) {

    val userProfile: Flow<UserProfileEntity?> = database.userProfileDao().getUserProfile()
    val primaryFarm: Flow<FarmEntity?> = database.farmDao().getPrimaryFarm()
    val allAlerts: Flow<List<AlertEntity>> = database.alertDao().getAllAlerts()
    val chatMessages: Flow<List<ChatMessageEntity>> = database.chatMessageDao().getChatMessages()

    fun getFarmZones(farmId: String): Flow<List<FarmZoneEntity>> =
        database.farmZoneDao().getZonesForFarm(farmId)

    fun getLatestWeather(farmId: String): Flow<WeatherRecordEntity?> =
        database.weatherRecordDao().getLatestWeather(farmId)

    fun getSimulations(farmId: String): Flow<List<SimulationRecordEntity>> =
        database.simulationRecordDao().getSimulations(farmId)

    suspend fun initializeDefaultDemoDataIfNeeded() = withContext(Dispatchers.IO) {
        // Check if profile exists
        val defaultProfile = UserProfileEntity(
            id = "default_user",
            fullName = "Muthu Kumar",
            phone = "+91 98765 43210",
            language = "ta",
            state = "Tamil Nadu",
            district = "Madurai",
            village = "Alanganallur",
            farmerType = "Small & Marginal Farmer (2.0 Acres)"
        )
        database.userProfileDao().insertProfile(defaultProfile)

        val demoFarm = FarmEntity(
            id = "demo_farm_1",
            userId = "default_user",
            name = "Muthu's Green Acres",
            locationName = "Madurai, Tamil Nadu",
            latitude = 9.9252,
            longitude = 78.1198,
            areaAcres = 2.0,
            primaryCrop = "Tomato",
            cropVariety = "Shivam Hybrid (PKM-1)",
            cropStage = "Flowering & Fruit Setting",
            irrigationMethod = "Drip Irrigation",
            soilType = "Red Loamy Clay",
            sowingDate = "15 July 2026",
            healthScore = 86,
            isDemo = true
        )
        database.farmDao().insertFarm(demoFarm)

        val initialZones = listOf(
            FarmZoneEntity(
                id = "zone_a",
                farmId = "demo_farm_1",
                zoneName = "Zone A: East Field",
                cropName = "Tomato (PKM-1)",
                cropStage = "Flowering Stage (Day 42)",
                soilMoisturePercent = 68,
                soilTemperatureC = 27.8,
                ecNutrientLevel = 1.6,
                healthStatus = "GOOD",
                recommendation = "Maintain soil moisture above 60%. Next fertigation scheduled in 3 days.",
                colorHex = "#2E7D32"
            ),
            FarmZoneEntity(
                id = "zone_b",
                farmId = "demo_farm_1",
                zoneName = "Zone B: West Field",
                cropName = "Tomato (PKM-1)",
                cropStage = "Vegetative Stage (Day 25)",
                soilMoisturePercent = 64,
                soilTemperatureC = 28.4,
                ecNutrientLevel = 1.4,
                healthStatus = "GOOD",
                recommendation = "Canopy growth vigorous. Optimal vegetative development.",
                colorHex = "#388E3C"
            ),
            FarmZoneEntity(
                id = "zone_c",
                farmId = "demo_farm_1",
                zoneName = "Zone C: Lowland Block",
                cropName = "Tomato (Intercrop Chilli)",
                cropStage = "Fruit Setting (Day 50)",
                soilMoisturePercent = 75,
                soilTemperatureC = 26.9,
                ecNutrientLevel = 1.8,
                healthStatus = "WATCH",
                recommendation = "High moisture zone. Ensure drainage before tomorrow's rain.",
                colorHex = "#F57F17"
            )
        )
        database.farmZoneDao().insertZones(initialZones)

        val initialWeather = WeatherRecordEntity(
            id = "latest_weather",
            farmId = "demo_farm_1",
            location = "Madurai (Alanganallur)",
            temperatureC = 28.5,
            feelsLikeC = 30.2,
            humidityPercent = 78,
            condition = "Scattered Clouds / Monsoon Influx",
            rainProbabilityPercent = 84,
            rainExpectedTomorrow = true,
            windSpeedKmh = 14.5,
            solarRadiationWpm = 640
        )
        database.weatherRecordDao().insertWeather(initialWeather)

        val initialAlerts = listOf(
            AlertEntity(
                id = "alert_1",
                farmId = "demo_farm_1",
                title = "Rain Alert Expected Tomorrow 🌧️",
                description = "Monsoon shower forecast with 84% probability. Soil moisture currently adequate at 68%.",
                category = "WEATHER",
                severity = "HIGH",
                actionText = "Skip irrigation today to prevent root rot and save pump electricity."
            ),
            AlertEntity(
                id = "alert_2",
                farmId = "demo_farm_1",
                title = "PM-KISAN Next Installment Notification 🌾",
                description = "19th Installment of ₹2,000 scheduled for verified Aadhaar-seeded accounts.",
                category = "SCHEME",
                severity = "LOW",
                actionText = "Check Aadhaar linkage status in the Schemes tab."
            ),
            AlertEntity(
                id = "alert_3",
                farmId = "demo_farm_1",
                title = "Fungal Blight Risk Watch 🦠",
                description = "Warm humidity above 75% creates moderate risk for early blight on lower tomato foliage.",
                category = "DISEASE",
                severity = "MEDIUM",
                actionText = "Inspect underside of lower leaves. Apply neem oil spray if spotting appears."
            )
        )
        database.alertDao().insertAlerts(initialAlerts)

        val defaultSim = SimulationRecordEntity(
            id = "sim_default",
            farmId = "demo_farm_1",
            scenarioName = "Skip Irrigation & Rely on Rain",
            estimatedYieldKg = 18400.0,
            waterSavedLiters = 12000,
            estimatedCostRs = 34500.0,
            estimatedRevenueRs = 220800.0,
            netProfitRs = 186300.0,
            riskLevel = "LOW",
            isRecommended = true
        )
        database.simulationRecordDao().insertSimulation(defaultSim)
    }

    suspend fun saveFarm(farm: FarmEntity) = withContext(Dispatchers.IO) {
        database.farmDao().insertFarm(farm)
    }

    suspend fun updateLanguage(langCode: String) = withContext(Dispatchers.IO) {
        val current = database.userProfileDao().getUserProfile()
        // Simple update or insert
        val updated = UserProfileEntity(
            id = "default_user",
            language = langCode
        )
        database.userProfileDao().insertProfile(updated)
    }

    suspend fun addChatMessage(text: String, isUser: Boolean, toolInvoked: String? = null, language: String = "en") =
        withContext(Dispatchers.IO) {
            val message = ChatMessageEntity(
                id = UUID.randomUUID().toString(),
                sender = if (isUser) "USER" else "AGRITWIN_AI",
                messageText = text,
                language = language,
                toolInvoked = toolInvoked,
                timestamp = System.currentTimeMillis()
            )
            database.chatMessageDao().insertMessage(message)
        }

    suspend fun saveSimulationResult(sim: SimulationRecordEntity) = withContext(Dispatchers.IO) {
        database.simulationRecordDao().insertSimulation(sim)
    }
}
