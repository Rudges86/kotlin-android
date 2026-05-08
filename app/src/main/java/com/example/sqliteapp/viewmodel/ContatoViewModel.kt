package com.example.sqliteapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.sqliteapp.data.model.Contato
import com.example.sqliteapp.data.repository.ContatoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import kotlinx.coroutines.withContext

class ContatoViewModel(private val repository: ContatoRepository): ViewModel() {
    val contatos = repository.todosOsContatos.asLiveData()
    var contatoSelecionado by mutableStateOf<Contato?>(null)
    var logradouro by  mutableStateOf("")
    var bairro by mutableStateOf("")
    var cidade by mutableStateOf("")
    var estado by mutableStateOf("")
    var estaCarregando by mutableStateOf(false)
    var mensagemErro by mutableStateOf<String?>(null)

    // --- Operações do banco ---

    fun salvarContato(contato: Contato, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (contato.id == 0) {
                    repository.inserir(contato)
                } else {
                    repository.atualizar(contato)
                }

                withContext(Dispatchers.Main) {
                    contatoSelecionado = null
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Erro ao salvar: ${e.message}")
            }
        }
    }

    fun excluirContato(contato: Contato) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deletar(contato)
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Erro ao excluir: ${e.message}")
            }
        }
    }

// --- INTEGRAÇÃO VIACEP ---

    fun pesquisarCEP(cepLimpo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                estaCarregando = true
                mensagemErro = null
            }

            try {
                val endereco = repository.buscarEndereco(cepLimpo)

                withContext(Dispatchers.Main) {
                    logradouro = endereco.logradouro
                    bairro = endereco.bairro
                    cidade = endereco.localidade
                    estado = endereco.uf
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    exibirErroTemporario("CEP inválido")
                }
                Log.e("CEP_ERROR", "Erro: ${e.message}")
            } finally {
                withContext(Dispatchers.Main) {
                    estaCarregando = false
                }
            }
        }
    }

    private fun exibirErroTemporario(mensagem: String) {
        mensagemErro = mensagem
        viewModelScope.launch {
            delay(3000)
            mensagemErro = null
        }
    }
    fun resetarCamposCEP() {
        logradouro = ""
        bairro = ""
        cidade = ""
        estado = ""
        mensagemErro = null
    }
}

