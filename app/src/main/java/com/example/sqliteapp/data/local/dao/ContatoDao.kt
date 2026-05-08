package com.example.sqliteapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sqliteapp.data.model.Contato
import kotlinx.coroutines.flow.Flow

@Dao
interface ContatoDao {
    @Insert
    fun inserir(contato: Contato)

    @Query("SELECT * FROM contatos")
    fun buscarTodos(): Flow<List<Contato>>

    @Update
    fun atualizar(contato: Contato)

    @Delete
    fun deletar(contato: Contato)

}