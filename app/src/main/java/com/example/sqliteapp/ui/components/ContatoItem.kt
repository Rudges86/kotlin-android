package com.example.sqliteapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sqliteapp.data.model.Contato

@Composable
fun ContatoItem(contato: Contato,
                onEditClick: () -> Unit,
                onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Nome:", style = MaterialTheme.typography.titleLarge)
                Text(text = contato.nome, style = MaterialTheme.typography.titleLarge)
                Text(text = contato.nascimento, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Contato:", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Telefone: ${contato.telefone}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Contato: ${contato.email}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Endereço", style = MaterialTheme.typography.bodyMedium)
                Text(text = "${contato.estado} - ${contato.cidade}" , style = MaterialTheme.typography.bodyMedium)
                Text(text = "CEP: ${contato.cep}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Bairro: ${contato.bairro}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Logradouro: ${contato.logradouro}", style = MaterialTheme.typography.bodyMedium)
                Text(text ="Número: ${contato.numero}", style = MaterialTheme.typography.bodyMedium)
            }

            // Botão de Editar
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Blue)
            }

            // Botão de Excluir
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color.Red)
            }
        }
    }
}