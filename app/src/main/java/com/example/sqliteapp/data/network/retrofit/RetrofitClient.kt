package com.example.sqliteapp.data.network.retrofit

import com.example.sqliteapp.data.network.service.ViaCEPService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://viacep.com.br/ws/"

    val instance: ViaCEPService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ViaCEPService::class.java)
    }
}