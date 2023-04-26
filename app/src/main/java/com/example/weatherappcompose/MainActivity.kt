package com.example.weatherappcompose

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherappcompose.data.WeatherModel
import com.example.weatherappcompose.screens.DialogSearch
import com.example.weatherappcompose.screens.MainCard
import com.example.weatherappcompose.screens.TabLayout

const val API_KEY = "1dec95905c934ced8c762111230504"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val daysList = remember {
                mutableStateOf(listOf<WeatherModel>())
            }
            val dialogState = remember {
                mutableStateOf(false)
            }
            val currentDay = remember {
                mutableStateOf(WeatherModel("", "", "", "", "", "", "", listOf()))
            }

            if(dialogState.value) DialogSearch(dialogState = dialogState, getCity = { getResult(city = it,  daysList, currentDay, this@MainActivity) })
            getResult("London", daysList, currentDay, this@MainActivity)
            Image(
                painter = painterResource(id = R.drawable.wather),
                contentDescription = "im1",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.5f),
                contentScale = ContentScale.FillBounds
            )
            Column {
                MainCard(currentDay, onClickSync = {
                    getResult("London", daysList, currentDay, this@MainActivity)
                },
                    onClickSearch = { dialogState.value = true}
                )
                TabLayout(daysList, currentDay)
            }
        }
    }
}

private fun getResult(
    city: String,
    state: MutableState<List<WeatherModel>>,
    currentDay: MutableState<WeatherModel>,
    context: Context
) {
    var url = "https://api.weatherapi.com/v1/forecast.json?key=$API_KEY&q=$city&days=3&aqi=no"
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(Request.Method.GET, url, { response ->
        val list = getWeatherByDays(response)
        currentDay.value = list[0]
        state.value = list
    },
        { error -> Log.d("Log", error.toString()) }
    )
    queue.add(stringRequest)
}

private fun getWeatherByDays(response: String): List<WeatherModel> {
    if (response.isEmpty()) return listOf()
    return WeatherModel.getWeatherByDays(response)
}