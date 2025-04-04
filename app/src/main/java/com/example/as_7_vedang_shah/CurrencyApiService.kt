package com.example.as_7_vedang_shah

import com.example.as_7_vedang_shah.models.ExchangeRatesResponse
import retrofit2.http.GET

interface CurrencyApiService {

    // Suspend function to fetch supported currency codes (e.g., CAD, USD, ISK)
    @GET("currencies.json")
    suspend fun getCurrencies(): Map<String, String>

    // Suspend function to fetch exchange rates relative to CAD
    @GET("currencies/cad.json")
    suspend fun getExchangeRates(): ExchangeRatesResponse
}

