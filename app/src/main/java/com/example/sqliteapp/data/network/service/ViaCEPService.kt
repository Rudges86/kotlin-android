package com.example.sqliteapp.data.network.service

import com.example.sqliteapp.data.network.model.ViaCepResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCEPService {
    @GET("{cep}/json/")
    suspend fun buscarCEP(@Path("cep") cep: String): ViaCepResponse
}