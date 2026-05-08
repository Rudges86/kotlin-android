package com.example.sqliteapp.data.repository

import androidx.room.Update
import com.example.sqliteapp.data.local.dao.ContatoDao
import com.example.sqliteapp.data.model.Contato
import com.example.sqliteapp.data.network.model.ViaCepResponse
import com.example.sqliteapp.data.network.retrofit.RetrofitClient
import kotlinx.coroutines.flow.Flow

class ContatoRepository(private val contatoDao: ContatoDao) {

    val todosOsContatos: Flow<List<Contato>> = contatoDao.buscarTodos()

    // O mensageiro leva um novo contato para guardar
    fun inserir(contato: Contato) {
        contatoDao.inserir(contato)
    }


    fun deletar(contato: Contato) {
        contatoDao.deletar(contato)
    }

    suspend fun buscarEndereco(cep: String): ViaCepResponse {
        return RetrofitClient.instance.buscarCEP(cep)
    }

    fun atualizar(contato: Contato) {
        contatoDao.atualizar(contato)
    }
}