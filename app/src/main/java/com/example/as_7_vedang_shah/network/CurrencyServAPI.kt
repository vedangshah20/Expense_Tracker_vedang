package com.example.as_7_vedang_shah.network


import com.example.as_7_vedang_shah.models.ExchangeRatesResponse
import retrofit2.http.GET

interface CurrencyServAPI {

    @GET("currencies.json")
    suspend fun getCurrencies(): Map<String, String>


    @GET("currencies/cad.json")
    suspend fun getExchangeRates(): ExchangeRatesResponse
}

