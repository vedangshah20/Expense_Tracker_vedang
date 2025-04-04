package com.example.as_7_vedang_shah.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val Conv_URL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/"

    val api:  CurrencyServAPI by lazy {
        Retrofit.Builder()
            .baseUrl(Conv_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyServAPI::class.java)
    }
}