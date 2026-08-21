package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.entities.FarmEntity
import com.example.data.local.entities.WeatherRecordEntity
import com.example.engine.DecisionEngine
import com.example.engine.VoiceAssistantEngine
import com.example.engine.WaterStatus
import com.example.i18n.AppLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("AgriTwin AI", appName)
    }

    @Test
    fun `decision engine accurately recommends saving water when rain expected`() {
        val farm = FarmEntity(
            id = "test_farm",
            name = "Test Farm",
            areaAcres = 2.0,
            primaryCrop = "Tomato",
            cropVariety = "Shivam Hybrid",
            cropStage = "Flowering",
            irrigationMethod = "Drip",
            soilType = "Red Loamy"
        )
        val weather = WeatherRecordEntity(
            id = "w1",
            farmId = "test_farm",
            temperatureC = 28.0,
            humidityPercent = 75,
            rainProbabilityPercent = 85,
            forecastSummary = "Rain expected tomorrow"
        )

        val decision = DecisionEngine.evaluate(farm, emptyList(), weather, AppLanguage.ENGLISH)
        assertEquals(WaterStatus.NOT_NEEDED, decision.waterNeed)
        assertEquals(true, decision.isRainExpected)
    }

    @Test
    fun `voice assistant matches Tanglish water query`() {
        val farm = FarmEntity(
            id = "test_farm",
            name = "Test Farm",
            areaAcres = 2.0,
            primaryCrop = "Tomato",
            cropVariety = "Shivam Hybrid",
            cropStage = "Flowering",
            irrigationMethod = "Drip",
            soilType = "Red Loamy"
        )
        val weather = WeatherRecordEntity(
            id = "w1",
            farmId = "test_farm",
            temperatureC = 28.0,
            humidityPercent = 75,
            rainProbabilityPercent = 85,
            forecastSummary = "Rain expected tomorrow"
        )

        val result = VoiceAssistantEngine.processQuery("Innaikku thanni vidalama?", AppLanguage.TAMIL, farm, weather)
        assertEquals("WATER_NEED_QUERY", result.matchedIntent)
        assertEquals("get_water_need()", result.toolInvoked)
        assertNotNull(result.spokenResponse)
    }
}

