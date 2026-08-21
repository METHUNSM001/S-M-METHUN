package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class OpenWeatherResponse(
    val coord: CoordDto? = null,
    val weather: List<WeatherDescriptionDto>? = null,
    val main: MainDto? = null,
    val wind: WindDto? = null,
    val clouds: CloudsDto? = null,
    val name: String? = null
)

@JsonClass(generateAdapter = true)
data class CoordDto(
    val lat: Double,
    val lon: Double
)

@JsonClass(generateAdapter = true)
data class WeatherDescriptionDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@JsonClass(generateAdapter = true)
data class MainDto(
    val temp: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "temp_min") val tempMin: Double,
    @Json(name = "temp_max") val tempMax: Double,
    val pressure: Int,
    val humidity: Int
)

@JsonClass(generateAdapter = true)
data class WindDto(
    val speed: Double,
    val deg: Int? = null
)

@JsonClass(generateAdapter = true)
data class CloudsDto(
    val all: Int
)

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): OpenWeatherResponse
}
