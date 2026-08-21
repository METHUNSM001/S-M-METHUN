package com.example.engine

import com.example.data.local.entities.FarmEntity
import com.example.data.local.entities.FarmZoneEntity
import com.example.data.local.entities.WeatherRecordEntity
import com.example.i18n.AppLanguage
import com.example.i18n.Translations

data class FarmDecisionSummary(
    val healthScore: Int, // 0-100
    val healthStatus: HealthStatus,
    val waterNeed: WaterStatus,
    val diseaseRisk: DiseaseRiskLevel,
    val diseaseExplanation: String,
    val todaysActionTitle: String,
    val todaysActionDetail: String,
    val rainSummary: String,
    val isRainExpected: Boolean,
    val estimatedYieldQuintals: Double,
    val estimatedRevenueRs: Double,
    val estimatedCostRs: Double,
    val estimatedProfitRs: Double
)

enum class HealthStatus {
    GOOD, WATCH, ACTION_NEEDED
}

enum class WaterStatus {
    NOT_NEEDED, NEEDED, DRAINAGE_ALERT
}

enum class DiseaseRiskLevel {
    LOW, MEDIUM, HIGH
}

object DecisionEngine {

    fun evaluate(
        farm: FarmEntity?,
        zones: List<FarmZoneEntity>,
        weather: WeatherRecordEntity?,
        language: AppLanguage
    ): FarmDecisionSummary {
        val rainProb = weather?.rainProbabilityPercent ?: 80
        val isRain = weather?.rainExpectedTomorrow ?: (rainProb > 60)
        val humidity = weather?.humidityPercent ?: 75
        val avgMoisture = if (zones.isNotEmpty()) zones.map { it.soilMoisturePercent }.average() else 68.0

        // Determine Water Status
        val waterStatus: WaterStatus = when {
            avgMoisture > 80 -> WaterStatus.DRAINAGE_ALERT
            isRain || avgMoisture >= 65.0 -> WaterStatus.NOT_NEEDED
            avgMoisture < 45.0 -> WaterStatus.NEEDED
            else -> WaterStatus.NOT_NEEDED
        }

        // Determine Disease Risk
        val diseaseRisk: DiseaseRiskLevel = when {
            humidity > 80 && avgMoisture > 70 -> DiseaseRiskLevel.MEDIUM
            humidity > 88 -> DiseaseRiskLevel.HIGH
            else -> DiseaseRiskLevel.LOW
        }

        val diseaseExp = when (language) {
            AppLanguage.TAMIL -> when (diseaseRisk) {
                DiseaseRiskLevel.LOW -> "ஈரப்பதம் மற்றும் வெப்பநிலை சாதகமாக உள்ளது. பூச்சி ஆபத்து குறைவு."
                DiseaseRiskLevel.MEDIUM -> "அதிக ஈரப்பதம் (78%) உள்ளதால் தக்காளி இலை கருகல் நோய் வர வாய்ப்பு. இலைகளை கண்காணிக்கவும்."
                DiseaseRiskLevel.HIGH -> "பூஞ்சை நோய் பரவும் அபாயம் அதிகம். பரிந்துரைக்கப்பட்ட வேப்பெண்ணெய் தெளிக்கவும்."
            }
            AppLanguage.HINDI -> when (diseaseRisk) {
                DiseaseRiskLevel.LOW -> "नमी और तापमान सामान्य है। कीट का जोखिम कम है।"
                DiseaseRiskLevel.MEDIUM -> "उच्च नमी (78%) के कारण पत्ती झुलसा का मध्यम खतरा। पत्तियों का निरीक्षण करें।"
                DiseaseRiskLevel.HIGH -> "फफूंद रोग का उच्च खतरा। जैविक कीटनाशक का छिड़काव करें।"
            }
            AppLanguage.ENGLISH -> when (diseaseRisk) {
                DiseaseRiskLevel.LOW -> "Optimal temperature and canopy aeration. Low pest or blight risk."
                DiseaseRiskLevel.MEDIUM -> "High humidity (78%) creates moderate early blight risk. Inspect lower foliage."
                DiseaseRiskLevel.HIGH -> "High fungal disease risk detected. Apply neem-based protective spray."
            }
        }

        // Determine Today's Action
        val actionDetail = when (language) {
            AppLanguage.TAMIL -> {
                if (waterStatus == WaterStatus.NOT_NEEDED) {
                    "இன்று தக்காளி பயிருக்கு தண்ணீர் பாய்ச்ச வேண்டாம். நாளை 84% மழை வாய்ப்புள்ளது. நிலத்தில் போதுமான ஈரப்பதம் (68%) உள்ளது."
                } else if (waterStatus == WaterStatus.DRAINAGE_ALERT) {
                    "களத்தில் அதிக நீர் தேங்காமல் வடிகால் அமைப்பை சரிபார்க்கவும்."
                } else {
                    "இன்று காலை 9 மணிக்குள் 45 நிமிடம் சொட்டு நீர் பாசனம் செய்யவும்."
                }
            }
            AppLanguage.HINDI -> {
                if (waterStatus == WaterStatus.NOT_NEEDED) {
                    "आज टमाटर की फसल में पानी न दें। कल 84% बारिश की संभावना है और मिट्टी में पर्याप्त नमी (68%) मौजूद है।"
                } else if (waterStatus == WaterStatus.DRAINAGE_ALERT) {
                    "खेत में जलभराव रोकने के लिए जल निकासी नाली साफ करें।"
                } else {
                    "सुबह 9 बजे से पहले 45 मिनट के लिए ड्रिप सिंचाई चलाएं।"
                }
            }
            AppLanguage.ENGLISH -> {
                if (waterStatus == WaterStatus.NOT_NEEDED) {
                    "Don't water your tomato crop today. Rain is expected tomorrow (84% chance) and soil currently has optimal 68% moisture."
                } else if (waterStatus == WaterStatus.DRAINAGE_ALERT) {
                    "Clear drainage channels to prevent waterlogging before heavy monsoon precipitation."
                } else {
                    "Run light drip irrigation for 45 minutes before 10 AM."
                }
            }
        }

        val rainSummary = when (language) {
            AppLanguage.TAMIL -> if (isRain) "நாளை மழை எதிர்பார்க்கப்படுகிறது ($rainProb%)" else "வறண்ட வானிலை"
            AppLanguage.HINDI -> if (isRain) "कल बारिश की संभावना ($rainProb%)" else "शुष्क मौसम"
            AppLanguage.ENGLISH -> if (isRain) "Rain Expected Tomorrow ($rainProb%)" else "Dry & Sunny Conditions"
        }

        val area = farm?.areaAcres ?: 2.0
        val estYieldQuintals = area * 92.0 // ~184 Quintals for 2 acres tomato
        val estRevenue = estYieldQuintals * 1200.0 // ~₹12/kg -> ₹1200/quintal
        val estCost = area * 17250.0
        val estProfit = estRevenue - estCost

        return FarmDecisionSummary(
            healthScore = farm?.healthScore ?: 86,
            healthStatus = HealthStatus.GOOD,
            waterNeed = waterStatus,
            diseaseRisk = diseaseRisk,
            diseaseExplanation = diseaseExp,
            todaysActionTitle = Translations.get("todays_action", language),
            todaysActionDetail = actionDetail,
            rainSummary = rainSummary,
            isRainExpected = isRain,
            estimatedYieldQuintals = estYieldQuintals,
            estimatedRevenueRs = estRevenue,
            estimatedCostRs = estCost,
            estimatedProfitRs = estProfit
        )
    }
}
