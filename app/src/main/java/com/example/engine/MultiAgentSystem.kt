package com.example.engine

import com.example.data.local.entities.FarmEntity
import com.example.data.local.entities.WeatherRecordEntity
import com.example.i18n.AppLanguage

data class AgentResponse(
    val agentName: String,
    val icon: String,
    val summary: String,
    val details: String,
    val confidencePercent: Int,
    val actionableStep: String
)

object MultiAgentSystem {

    fun runAllAgents(
        farm: FarmEntity?,
        weather: WeatherRecordEntity?,
        language: AppLanguage
    ): List<AgentResponse> {
        return listOf(
            runWeatherAgent(weather, language),
            runWaterAgent(farm, weather, language),
            runCropAgent(farm, language),
            runDiseaseAgent(weather, language),
            runMarketAgent(farm, language),
            runSchemeAgent(farm, language),
            runFinanceAgent(farm, language)
        )
    }

    fun runWeatherAgent(weather: WeatherRecordEntity?, language: AppLanguage): AgentResponse {
        val rainProb = weather?.rainProbabilityPercent ?: 84
        val temp = weather?.temperatureC ?: 28.5
        val summary = when (language) {
            AppLanguage.TAMIL -> "நாளை மழை வாய்ப்பு $rainProb% | வெப்பநிலை ${temp}°C"
            AppLanguage.HINDI -> "कल बारिश की संभावना $rainProb% | तापमान ${temp}°C"
            AppLanguage.ENGLISH -> "Tomorrow Rain Chance: $rainProb% | Temp: ${temp}°C"
        }
        val detail = when (language) {
            AppLanguage.TAMIL -> "மேற்கு பருவமழை காரணமாக அடுத்த 24 மணி நேரத்தில் 18 மி.மீ வரை மழை பெய்ய வாய்ப்புள்ளது."
            AppLanguage.HINDI -> "मानसून के प्रभाव से अगले 24 घंटों में 18 मिमी तक वर्षा हो सकती है।"
            AppLanguage.ENGLISH -> "Monsoon influx will trigger scattered showers up to 18mm in the next 24 hours."
        }
        val action = when (language) {
            AppLanguage.TAMIL -> "வடிகால் அடைப்புகளை நீக்கி மழைநீரை சேமிக்க தயார் செய்யுங்கள்."
            AppLanguage.HINDI -> "खेत की नालियों को साफ रखें ताकि पानी न भरे।"
            AppLanguage.ENGLISH -> "Clear drainage outlets and prepare farm pond rainwater harvesting."
        }
        return AgentResponse("Weather Agent", "🌦️", summary, detail, 92, action)
    }

    fun runWaterAgent(farm: FarmEntity?, weather: WeatherRecordEntity?, language: AppLanguage): AgentResponse {
        val summary = when (language) {
            AppLanguage.TAMIL -> "தண்ணீர் பாய்ச்ச தேவை இல்லை (சேமிப்பு: 12,000 லிட்டர்)"
            AppLanguage.HINDI -> "पानी देने की आवश्यकता नहीं (बचत: 12,000 लीटर)"
            AppLanguage.ENGLISH -> "Irrigation Not Needed Today (Saves 12,000 L)"
        }
        val detail = when (language) {
            AppLanguage.TAMIL -> "மண் ஈரப்பதம் 68% போதுமானதாக உள்ளது. நாளை மழை பெய்வதால் பாசனத்தை தவிர்க்கவும்."
            AppLanguage.HINDI -> "मिट्टी की नमी 68% पर्याप्त है। कल बारिश होने से सिंचाई की जरूरत नहीं।"
            AppLanguage.ENGLISH -> "Root zone soil moisture is optimal at 68%. Imminent precipitation eliminates water deficit."
        }
        val action = when (language) {
            AppLanguage.TAMIL -> "சொட்டு நீர் வால்வை மூடி மின்சார செலவை மிச்சப்படுத்துங்கள்."
            AppLanguage.HINDI -> "ड्रिप सिंचाई बंद रखें और बिजली बचाएं।"
            AppLanguage.ENGLISH -> "Keep drip valve off to conserve electricity and avoid root hypoxia."
        }
        return AgentResponse("Water Agent", "💧", summary, detail, 95, action)
    }

    fun runCropAgent(farm: FarmEntity?, language: AppLanguage): AgentResponse {
        val crop = farm?.primaryCrop ?: "Tomato"
        val stage = farm?.cropStage ?: "Flowering"
        val summary = when (language) {
            AppLanguage.TAMIL -> "$crop ($stage) - சிறந்த நலம் (88%)"
            AppLanguage.HINDI -> "$crop ($stage) - उत्तम स्थिति (88%)"
            AppLanguage.ENGLISH -> "$crop ($stage) - Healthy Vigor (88%)"
        }
        val detail = when (language) {
            AppLanguage.TAMIL -> "தக்காளி பூக்கும் பருவம் மற்றும் காய் பிடிக்கும் பருவம் சிறப்பாக உள்ளது."
            AppLanguage.HINDI -> "टमाटर में फूल और फल लगने की अवस्था बहुत अच्छी चल रही है।"
            AppLanguage.ENGLISH -> "Canopy density, flowering ratio, and fruit set indices are well within ideal ICAR benchmarks."
        }
        val action = when (language) {
            AppLanguage.TAMIL -> "பூ உதிர்வை தடுக்க போரான் (Boron 0.2%) தெளிக்க திட்டமிடுங்கள்."
            AppLanguage.HINDI -> "फूल झड़ने से रोकने के लिए बोरान 0.2% का हल्का छिड़काव करें।"
            AppLanguage.ENGLISH -> "Plan micronutrient boron foliar spray post-rain to maximize fruit set."
        }
        return AgentResponse("Crop Agent", "🌾", summary, detail, 89, action)
    }

    fun runDiseaseAgent(weather: WeatherRecordEntity?, language: AppLanguage): AgentResponse {
        val summary = when (language) {
            AppLanguage.TAMIL -> "இலை கருகல் நோய்: நடுத்தர எச்சரிக்கை (ஈரப்பதம் 78%)"
            AppLanguage.HINDI -> "झुलसा रोग: मध्यम चेतावनी (नमी 78%)"
            AppLanguage.ENGLISH -> "Early Blight: Moderate Watch (78% Humidity)"
        }
        val detail = when (language) {
            AppLanguage.TAMIL -> "அதிக ஈரப்பதம் மற்றும் தொடர் மேகமூட்டம் பூஞ்சை வளர்ச்சியை தூண்டலாம்."
            AppLanguage.HINDI -> "अधिक नमी और बादलों के मौसम से फफूंद पनपने की संभावना बढ़ जाती है।"
            AppLanguage.ENGLISH -> "Elevated microclimate humidity combined with overcast skies favors fungal spore proliferation."
        }
        val action = when (language) {
            AppLanguage.TAMIL -> "கீழ் இலைகளில் மஞ்சள் புள்ளிகள் உள்ளதா என பார்வையிடவும்."
            AppLanguage.HINDI -> "निचली पत्तियों पर धब्बों की जांच करें।"
            AppLanguage.ENGLISH -> "Inspect bottom foliage for concentric brown lesions. Keep biopesticide ready."
        }
        return AgentResponse("Disease Agent", "🦠", summary, detail, 86, action)
    }

    fun runMarketAgent(farm: FarmEntity?, language: AppLanguage): AgentResponse {
        val summary = when (language) {
            AppLanguage.TAMIL -> "தக்காளி சந்தை விலை: ₹24 - ₹28 / கிலோ (ஏறுமுகம் 📈)"
            AppLanguage.HINDI -> "टमाटर मंडी भाव: ₹24 - ₹28 / किग्रा (तेजी 📈)"
            AppLanguage.ENGLISH -> "Tomato Mandi Price: ₹24 - ₹28 / kg (Bullish 📈)"
        }
        val detail = when (language) {
            AppLanguage.TAMIL -> "மதுரை மற்றும் திண்டுக்கல் சந்தைகளில் வரத்து குறைவு காரணமாக விலை உயர்ந்துள்ளது."
            AppLanguage.HINDI -> "मदुरै और आसपास की मंडियों में कम आवक के कारण कीमतों में तेजी है।"
            AppLanguage.ENGLISH -> "Regional wholesale supply constraints in South Mandis are sustaining firm farmgate pricing."
        }
        val action = when (language) {
            AppLanguage.TAMIL -> "அடுத்த வார அறுவடைக்கு உள்ளூர் ஒழுங்குமுறை விற்பனைக்கூடத்தை பதிவு செய்யவும்."
            AppLanguage.HINDI -> "अगले सप्ताह की तुड़ाई के लिए स्थानीय मंडी से संपर्क करें।"
            AppLanguage.ENGLISH -> "Schedule graded sorting before dispatch to secure top grade-A pricing."
        }
        return AgentResponse("Market Agent", "📊", summary, detail, 91, action)
    }

    fun runSchemeAgent(farm: FarmEntity?, language: AppLanguage): AgentResponse {
        val summary = when (language) {
            AppLanguage.TAMIL -> "3 அரசு திட்டங்கள் தகுதியுடையவை (PM-KISAN, சொட்டு நீர் மானியம்)"
            AppLanguage.HINDI -> "3 सरकारी योजनाएं पात्र (पीएम किसान, ड्रिप सब्सिडी)"
            AppLanguage.ENGLISH -> "3 Matched Government Schemes (PM-KISAN, PMKSY Subsidy)"
        }
        val detail = when (language) {
            AppLanguage.TAMIL -> "உங்கள் 2 ஏக்கர் தக்காளி நிலத்திற்கு 100% வரை சொட்டு நீர் உபகரண மானியம் பெறலாம்."
            AppLanguage.HINDI -> "आपके 2 एकड़ खेत के लिए सूक्ष्म सिंचाई पर 100% तक सब्सिडी उपलब्ध है।"
            AppLanguage.ENGLISH -> "Small farmer categorization qualifies you for 100% micro-irrigation component subsidy in TN."
        }
        val action = when (language) {
            AppLanguage.TAMIL -> "திட்டங்கள் பகுதியில் சிட்டா & அடங்கல் ஆவணங்களை பதிவேற்றவும்."
            AppLanguage.HINDI -> "योजनाएं टैब में खसरा-खतौनी के साथ आवेदन करें।"
            AppLanguage.ENGLISH -> "View verified application forms in the Schemes & Loans section."
        }
        return AgentResponse("Scheme Agent", "🏛️", summary, detail, 97, action)
    }

    fun runFinanceAgent(farm: FarmEntity?, language: AppLanguage): AgentResponse {
        val summary = when (language) {
            AppLanguage.TAMIL -> "கிசான் கிரெடிட் கார்டு கடன் தகுதி: ₹1,60,000 (4% வட்டி)"
            AppLanguage.HINDI -> "किसान क्रेडिट कार्ड सीमा: ₹1,60,000 (4% ब्याज)"
            AppLanguage.ENGLISH -> "KCC Collateral-Free Credit Limit: ₹1,60,000 @ 4% p.a."
        }
        val detail = when (language) {
            AppLanguage.TAMIL -> "2 ஏக்கர் தோட்டக்கலை பயிருக்கு அடமானம் இல்லா குறுகிய கால கடன் வங்கியில் பெறலாம்."
            AppLanguage.HINDI -> "2 एकड़ बागवानी फसल के लिए बिना गारंटी के रियायती कृषि ऋण उपलब्ध।"
            AppLanguage.ENGLISH -> "Collateral-free crop loan threshold up to ₹1.60 Lakh under Interest Subvention Scheme."
        }
        val action = when (language) {
            AppLanguage.TAMIL -> "தேசிய மயமாக்கப்பட்ட வங்கி கிளையை அனுக எளிய வழிகாட்டியை காணுங்கள்."
            AppLanguage.HINDI -> "बैंक शाखा में आवेदन के चरण देखें।"
            AppLanguage.ENGLISH -> "Check required document checklist in the Finance tab."
        }
        return AgentResponse("Finance Agent", "💰", summary, detail, 94, action)
    }
}
