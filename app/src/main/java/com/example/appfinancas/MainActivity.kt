package com.example.appfinancas

import ads_mobile_sdk.h5
import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appfinancas.ui.theme.AppFinancasTheme
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.appfinancas.model.Sonho
import com.example.appfinancas.model.TipoTransacao
import com.example.appfinancas.model.Transacao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppFinancasTheme {
                MainScreen()
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var telaAtual by remember { mutableStateOf("home") }
    val listaTransacoes = remember { mutableStateListOf<Transacao>() }
    val saldo = listaTransacoes.filter { it.tipo == TipoTransacao.GANHO }.sumOf { it.valor } -
            listaTransacoes.filter { it.tipo == TipoTransacao.GASTO }.sumOf { it.valor }

    val listaSonhos = remember { mutableStateListOf<Sonho>() }

    val listaGastos = listaTransacoes.filter { it.tipo == TipoTransacao.GASTO }
    val gastosPorMes = listaGastos.groupBy { it.mes }
        .mapValues { entry -> entry.value.sumOf { it.valor } }

    val mesMaiorGasto = gastosPorMes.maxByOrNull { it.value }
    val mesMenorGasto = gastosPorMes.minByOrNull { it.value }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = telaAtual == "home",
                    onClick = { telaAtual = "home" },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = telaAtual == "ganhos",
                    onClick = { telaAtual = "ganhos" },
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = "Ganhos") },
                    label = { Text("Ganhos") }
                )
                NavigationBarItem(
                    selected = telaAtual == "gastos",
                    onClick = { telaAtual = "gastos" },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Gastos") },
                    label = { Text("Gastos") }
                )
                NavigationBarItem(
                    selected = telaAtual == "sonhos",
                    onClick = { telaAtual = "sonhos" },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Sonhos") },
                    label = { Text("Sonhos") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (telaAtual) {
                "home" -> HomeScreen(saldo, mesMaiorGasto, mesMenorGasto, sonhoAtivo = listaSonhos.sortedWith(
                    compareByDescending<Sonho> { saldo >= it.valorTotal }
                        .thenBy { it.valorTotal - saldo }
                ).firstOrNull()

                )
                "ganhos" -> GanhosScreen(onSalvar = {desc,valor,mes ->
                    listaTransacoes.add(Transacao(desc,valor,mes,TipoTransacao.GANHO))
                })
                "gastos" -> GastosScreen (onSalvar = {desc,valor,mes ->
                    listaTransacoes.add(Transacao(desc,valor,mes,TipoTransacao.GASTO))
                })
                "sonhos" -> SonhosScreen(
                    saldoAtual = saldo,
                    listaSonhos = listaSonhos,
                    onAdicionarSonho = { novo -> listaSonhos.add(novo)})
            }
        }
    }
}


@Composable
fun HomeScreen(saldo: Double,
               maiorGasto: Map.Entry<String, Double>?,
               menorGasto: Map.Entry<String, Double>?,
               sonhoAtivo: Sonho?) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Saldo Atual", style = MaterialTheme.typography.labelLarge)
        Text(text = "Saldo: $saldo",
            color = if(saldo >=0 ) Color.Green else Color.Red,
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            EstisticaCard(
                titulo = "Maior Gasto",
                mes = maiorGasto?.key ?: "---",
                valor = maiorGasto?.value ?: 0.0,
                cor = Color(0xFFFFCDD2),
                modifier = Modifier.weight(1f)
            )


            EstisticaCard(
                titulo = "Menor Gasto",
                mes = menorGasto?.key ?: "---",
                valor = menorGasto?.value ?: 0.0,
                cor = Color(0xFFC8E6C9),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        if(sonhoAtivo != null) {
            val porcentagem = (saldo / sonhoAtivo.valorTotal * 100).coerceIn(0.0, 100.0)
            Card(modifier = Modifier.fillMaxWidth(0.8f),
                colors = CardDefaults
                    .cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Text(
                    text = "Dica: Você tem saldo para cobrir ${porcentagem.toInt()}% do seu sonho '${sonhoAtivo.objetivo}'!",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }
}


@Composable
fun EstisticaCard(titulo: String, mes: String, valor: Double, cor: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = cor)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = titulo,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black.copy(alpha = 0.6f)
            )
            Text(text = mes,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
                , color = Color.Black
            )
            Text(text = "R$ ${String.format("%.2f", valor)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun GanhosScreen(onSalvar:(String, Double, String) -> Unit) {
    var descricao by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var mes by remember {mutableStateOf("")}

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Cadastrar Ganho",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.Green
            )

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição (ex: Salário)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                label = { Text("Valor (R$)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = mes,
                onValueChange = { mes = it },
                label = { Text("Informe o mês/Ano (ex: Agosto/Ano)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Button(
                onClick = {
                    val valorTratado = valor.replace(",", ".").trim()
                    val v = valorTratado.toDoubleOrNull() ?: 0.0
                    if(descricao.isNotEmpty() && v > 0 && mes.isNotEmpty()) {
                        onSalvar(descricao,v, mes)
                        descricao = ""
                        valor = ""
                        mes = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Salvar Ganho")
            }
        }
    }

}

@Composable
fun GastosScreen(onSalvar:(String, Double, String) -> Unit) {
    var descricao by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var mes by remember {mutableStateOf("")}

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Cadastrar Gasto",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.Red
            )

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição (ex: Conta de luz)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                label = { Text("Valor (R$)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = mes,
                onValueChange = { mes = it },
                label = { Text("Informe o mês/Ano (ex: Agosto/Ano)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Button(
                onClick = {
                    val valorTratado = valor.replace(",", ".").trim()
                    val v = valorTratado.toDoubleOrNull() ?: 0.0
                    if(descricao.isNotEmpty() && v > 0 && mes.isNotEmpty()) {
                        onSalvar(descricao,v, mes)
                        descricao = ""
                        valor = ""
                        mes = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Salvar Gasto")
            }
        }
    }

}

@Composable
fun SonhosScreen(saldoAtual: Double, listaSonhos: List<Sonho>, onAdicionarSonho: (Sonho) -> Unit) {
    var nome by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Meus Objetivos", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Sonho") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = valor,
            onValueChange = { valor = it },
            label = { Text("Valor (R$)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Button(
            onClick = {
                val v = valor.replace(",", ".").toDoubleOrNull() ?: 0.0
                if (nome.isNotEmpty() && v > 0) {
                    onAdicionarSonho(Sonho(nome, v, saldoAtual))
                    nome = ""; valor = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Sonho")
        }

        Spacer(modifier = Modifier.height(8.dp))

        listaSonhos.forEach { sonho ->
            val atingiuObjetivo = saldoAtual >= sonho.valorTotal
            val porcentagem = (saldoAtual / sonho.valorTotal).coerceIn(0.0, 1.0).toFloat()

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (atingiuObjetivo) Color(0xFFC8E6C9) else Color(0xFFFFF9C4)
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = sonho.objetivo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )

                    Text(
                        text = "Meta: R$ ${String.format("%.2f", sonho.valorTotal)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.8f)
                    )

                    LinearProgressIndicator(
                        progress = { porcentagem },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(8.dp),
                        color = if (atingiuObjetivo) Color(0xFF2E7D32) else Color(0xFFFBC02D)
                    )

                    if (atingiuObjetivo) {
                        Text(
                            "Meta Atingida!",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    } else {
                        val faltante = sonho.valorTotal - saldoAtual
                        Text(
                            "Faltam: R$ ${String.format("%.2f", faltante)}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD32F2F)
                        )
                    }

                    Text(
                        text = "${(porcentagem * 100).toInt()}% alcançado",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun Telaplaceholder(nome: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = nome)
    }
}