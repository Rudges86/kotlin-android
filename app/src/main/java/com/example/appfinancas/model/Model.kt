package com.example.appfinancas.model

enum class TipoTransacao {
    GANHO, GASTO
}
data class Transacao(
    val descricao: String,
    val valor: Double,
    val mes: String,
    val tipo: TipoTransacao
)


data class Sonho(
    val objetivo: String,
    val valorTotal: Double,
    val valorPoupado: Double
)