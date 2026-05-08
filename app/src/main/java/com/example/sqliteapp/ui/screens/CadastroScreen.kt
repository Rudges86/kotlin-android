package com.example.sqliteapp.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sqliteapp.data.model.Contato
import com.example.sqliteapp.ui.components.CampoPadrao
import com.example.sqliteapp.viewmodel.ContatoViewModel

@Composable
fun CadastroScreen (viewModel: ContatoViewModel, function: () -> Boolean) {
    val context = LocalContext.current
    // 1. Variáveis locais (Estado apenas desta tela)
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var nascimento by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }

    // 2. Sincronização para Edição (Quando clicar em um contato da lista)
    LaunchedEffect(viewModel.contatoSelecionado) {
        viewModel.contatoSelecionado?.let { contato ->
            nome = contato.nome
            email = contato.email
            telefone = contato.telefone
            nascimento = contato.nascimento

            cep = if (contato.cep.length == 8) {
                "${contato.cep.substring(0, 5)}-${contato.cep.substring(5)}"
            } else {
                contato.cep
            }
            numero = contato.numero

            // Alimenta a ViewModel com os dados vindos do banco
            viewModel.bairro = contato.bairro
            viewModel.logradouro = contato.logradouro
            viewModel.cidade = contato.cidade
            viewModel.estado = contato.estado
        }
    }

    val scroll = rememberScrollState();
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
                .safeDrawingPadding()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .verticalScroll(scroll)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = if (viewModel.contatoSelecionado == null) "Novo contato" else "Atualizar contato", style = MaterialTheme.typography.headlineMedium)

            CampoPadrao("Nome", nome, { nome = it }, KeyboardType.Text)
            CampoPadrao("E-mail", email, { email = it }, KeyboardType.Email)
            CampoPadrao("Telefone (ex: 79 99999-9999)", telefone, { input ->
                val numeros = input.filter { it.isDigit() }.take(11)
                telefone = when {
                    numeros.length > 7 -> "(${numeros.substring(0, 2)}) ${numeros.substring(2, 7)}-${numeros.substring(7)}"
                    numeros.length > 2 -> "(${numeros.substring(0, 2)}) ${numeros.substring(2)}"
                    numeros.isNotEmpty() -> "(${numeros}"
                    else -> ""

                } }, KeyboardType.Phone)

            CampoPadrao("Nascimento (ex: dd/mm/aaaa)", nascimento, { input ->
                val numeros = input.filter { it.isDigit() }.take(8)

                nascimento = when {
                    numeros.length > 4 -> "${numeros.substring(0, 2)}/${numeros.substring(2, 4)}/${numeros.substring(4)}"
                    numeros.length > 2 -> "${numeros.substring(0, 2)}/${numeros.substring(2)}"
                    else -> numeros
                }
            }, KeyboardType.Number)
            CampoPadrao("CEP", cep, { input ->

                val apenasNumeros = input.filter { it.isDigit() }


                val digitosLimitados = apenasNumeros.take(8)


                cep = if (digitosLimitados.length > 5) {
                    "${digitosLimitados.substring(0, 5)}-${digitosLimitados.substring(5)}"
                } else {
                    digitosLimitados
                }
                if (digitosLimitados.length == 8) {
                    viewModel.pesquisarCEP(digitosLimitados)
                }
            }, KeyboardType.Number)
            CampoPadrao("Bairro", viewModel.bairro, { viewModel.bairro = it }, KeyboardType.Text)
            CampoPadrao("Logradouro", viewModel.logradouro, { viewModel.logradouro = it }, KeyboardType.Text)
            CampoPadrao("Número", numero, { numero = it }, KeyboardType.Number)
            CampoPadrao("Estado", viewModel.estado, { viewModel.estado = it }, KeyboardType.Text)
            CampoPadrao("Cidade", viewModel.cidade, { viewModel.cidade = it }, KeyboardType.Text)

            Button(
                onClick = {
                    val novo = Contato(
                        id = viewModel.contatoSelecionado?.id ?: 0,
                        nome = nome,
                        email = email,
                        telefone = telefone,
                        nascimento = nascimento,
                        cep = cep.filter { it.isDigit() },
                        bairro = viewModel.bairro,
                        logradouro = viewModel.logradouro,
                        numero = numero,
                        estado = viewModel.estado,
                        cidade = viewModel.cidade
                    )
                    viewModel.salvarContato(novo) {
                        Toast.makeText(context, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
                        function()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                enabled = !viewModel.estaCarregando
            ) {
                Text(text = if (viewModel.contatoSelecionado == null) "Salvar" else "Atualizar")
            }
        }

        AnimatedVisibility(
            visible = viewModel.mensagemErro != null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = viewModel.mensagemErro ?: "",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        if (viewModel.estaCarregando) {
            Box(modifier = Modifier.fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Blue)
            }
        }
    }

}

