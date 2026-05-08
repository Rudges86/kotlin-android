package com.example.sqliteapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.sqliteapp.ui.components.ContatoItem
import com.example.sqliteapp.viewmodel.ContatoViewModel

import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.example.sqliteapp.data.model.Contato


@Composable
fun ListaContatosScreen(
    viewModel: ContatoViewModel,
    onNovoContatoClick: () -> Unit,
    onEditContatoClick: (Contato) -> Unit
) {
    val contatos by viewModel.contatos.observeAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNovoContatoClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }
    ) { paddingValues ->
        if (contatos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nenhum contato cadastrado.")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(contatos) { contato ->
                    ContatoItem(
                        contato = contato,
                        onEditClick = { onEditContatoClick(contato) },
                        onDeleteClick = { viewModel.excluirContato(contato) }
                    )
                }
            }
        }
    }
}