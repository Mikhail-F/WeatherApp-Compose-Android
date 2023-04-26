package com.example.weatherappcompose.data

import org.json.JSONArray
import org.json.JSONObject

data class WeatherModel(
    val city: String,
    val time: String,
    val currentTemp: String,
    val condition: String,
    val icon: String,
    val maxTemp: String,
    val minTemp: String,
    val hours: List<WeatherModel> = emptyList()
) {
    companion object {
        fun getWeatherByDays(response: String): List<WeatherModel>{
            if (response.isEmpty()) return listOf()
            val list = ArrayList<WeatherModel>()
            val mainObject = JSONObject(response)
            val city = mainObject.getJSONObject("location").getString("name")
            val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")

            for (i in 0 until days.length()){
                val item = days[i] as JSONObject
                list.add(
                    WeatherModel(
                        city,
                        item.getString("date"),
                        "",
                        item.getJSONObject("day").getJSONObject("condition")
                            .getString("text"),
                        item.getJSONObject("day").getJSONObject("condition")
                            .getString("icon"),
                        item.getJSONObject("day").getString("maxtemp_c").toFloat().toInt().toString(),
                        item.getJSONObject("day").getString("mintemp_c").toFloat().toInt().toString(),
                       getWeatherByDays( item.getJSONArray("hour"))

                    )
                )
            }
            list[0] = list[0].copy(
                time = mainObject.getJSONObject("current").getString("last_updated"),
                currentTemp = mainObject.getJSONObject("current").getString("temp_c").toFloat().toInt().toString(),
            )
            return list
        }

        fun getWeatherByDays(response: JSONArray): List<WeatherModel>{
            if (response.length() == 0) return listOf()
            val list = ArrayList<WeatherModel>()
            val mainObject = response

            for (i in 0 until mainObject.length()){
                val item = mainObject[i] as JSONObject
                list.add(
                    WeatherModel(
                        "",
                        item.getString("time"),
                        item.getString("temp_c").toFloat().toInt().toString(),
                        item.getJSONObject("condition").getString("text"),
                        item.getJSONObject("condition").getString("icon"),
                        "",
                        "",
                        listOf(),
                    )
                )
            }
            return list
        }
    }
}
