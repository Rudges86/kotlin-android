package service

import loading
import model.Serie
import repository.SerieRepository
import service.impl.SerieService
import java.util.InputMismatchException
import java.util.Scanner

class SerieServiceImpl : SerieService {
    private val repository = SerieRepository();

    override fun cadastrarSerie(scan: Scanner): Unit {
        loading()
        if (cabecalho(
                "Cadastrar", listOf(
                    "Prosseguir (1)",
                    "Tecle qualquer tecla para voltar ao menu anterior"
                ), scan
            ) != 1
        ) {
            return
        }

        while (true) {
            var titulo = scan.lerString("Informe o título: ");
            var genero = scan.lerString("Informe o gênero: ");
            var temporada = scan.lerInt("Inforne a quantiade de temporadas (ex: 1): ")
            loading()
            val lista = repository.getAllSeries();
            if (lista.any { it.titulo.equals(titulo, ignoreCase = true) }) {
                println("Série já cadastrada!")
            } else {
                var novoId = 0
                novoId = if (lista.isEmpty()) {
                    1;
                } else {
                    lista.maxOf { it.id } + 1
                }
                println(repository.cadastrarSerie(Serie(novoId, titulo, genero, temporada)));
            }
            if (!scan.lerString("Deseja cadastrar mais uma? S/N").equals("s", true)) {
                break
            }
        }
    }

    override fun pesquisarPorId(scan: Scanner): Boolean {
        loading()
        if (cabecalho("Buscar Série por ID", listOf("Prosseguir", "Voltar ao menu anterior"), scan) != 1) {
            return true
        }
        while (true) {
            try {
                val id = scan.lerInt("Informe o número da série:");
                val serie = repository.buscarPorId(id)
                loading()
                imprimiSerie(serie)
                println("1 - Pesquisar mais uma? Qualquer tecla para voltar ao menu anterior.")
                if (scan.nextLine().trim() != "1") {
                    loading()
                    return true
                }
            } catch (e: InputMismatchException) {
                scan.lerString("Informe apenas o número da série.")
            }
        }
    }

    override fun pesquisarPorTitulo(scan: Scanner): Boolean {
        loading()
        if (cabecalho(
                "Buscar Série por Título", listOf("Prosseguir", "Voltar ao menu anterior"),
                scan
            ) != 1
        ) {
            return true
        }
        while (true) {
            val titulo = scan.lerString("Informe o Título da série:")
            val serie = repository.buscarPorTitulo(titulo)
            loading()
            imprimiSerie(serie);
            println("1 - Pesquisar mais uma? Qualquer tecla para voltar ao menu anterior.")
            if (scan.nextLine().trim() != "1") {
                loading()
                return true
            }
        }
    }

    override fun pesquisarPorGenero(scan: Scanner): Boolean {
        loading()
        if (cabecalho(
                "Buscar Série por Genero", listOf("Prosseguir", "Voltar ao menu anterior"),
                scan
            ) != 1
        ) {
            loading()
            return true
        }

        while (true) {
            val genero = scan.lerString("Informe o Gênero da série:")
            val serie = repository.buscarPorGenero(genero)
            loading()
            if (serie.isEmpty()) {
                println("Gênero não encontrado")
            } else {
                serie.forEach {
                    imprimiSerie(it)
                }
            }
            println("1 - Pesquisar mais uma? Qualquer tecla para voltar ao menu anterior.")
            if (scan.nextLine().trim() != "1") {
                loading()
                return true
            }
        }
    }

    override fun removeService(scan: Scanner): Unit {
        loading()
        println("**** Deletar ***")
        var id = scan.lerInt("Informe o número da série: ")
        println(repository.deletarSerie(id));
        loading()
    }

    override fun listarTodasAsSeries(scan: Scanner): Unit {
        loading()
        println("**** Listar ***")
        (repository.getAllSeries() ?: emptyList())
            .ifEmpty {
                println("Não tem nenhuma série cadastrada.")
                loading()
                return
            }.forEach {
                imprimiSerie(it);
            }
        scan.lerString("Tecle ENTER para voltar para o menu anterior.")
        loading()
    }

    override fun alterarService(scan: Scanner): Unit {
        loading()
        when (cabecalho(
            "Editar série",
            listOf("Buscar por id", "Buscar por nome", "Qualquer tecla para voltar ao menu anterior"),
            scan
        )) {
            1 -> {
                val id = scan.lerInt("Informe o número da série:")
                loading()
                val serie = repository.buscarPorId(id)
                alterarSerie(serie, scan)
                loading()
            }
            2 -> {
                val titulo = scan.lerString("Informe o título da série: ")
                loading()
                val serie = repository.buscarPorTitulo(titulo)
                alterarSerie(serie, scan)
                loading()
            }

            else -> {
                loading()
                return
            }

        }
    }

    private fun alterarSerie(serie: Serie?, scan: Scanner) {
        imprimiSerie(serie)
        if (serie != null) {
            if (scan.lerString("Deseja alterar ? S/N").equals("s", true)) {
                val titulo = scan.lerString("Título: ");
                val genero = scan.lerString("Gênero: ");
                val temporada = scan.lerInt("Temporada: ");
                val serieAtualizada: Serie = Serie(serie.id, titulo, genero, temporada);
                if (scan.lerString("Confirma a alteração ? s/n ").equals("s", true)) {
                    println(repository.alterarSerie(serie, serieAtualizada));
                } else {
                    println("Alteração cancelada")
                }
            }
        } else {
            if(scan.lerString("Deseja fazer uma nova busca? S/N").equals("s",true)) {
                alterarService(scan)
            }
        }
    }

    private fun cabecalho(menu: String, opcoes: List<String>, scan: Scanner): Int {
        println("****${menu}***")
        opcoes.forEachIndexed { index, opcao -> println("${index + 1} - $opcao") }
        while (true) {
            println("Digite o número da opção desejada: ")
            try {
                val opcao = scan.nextInt();
                scan.nextLine()
                return opcao
            } catch (e: InputMismatchException) {
                println("Digite apenas números!")
                scan.nextLine();
            }
        }
    }

    private fun imprimiSerie(serie: Serie?): Unit {
        println(
            serie?.let {
                """
                Série encontrada:
                Id: ${serie.id}
                Título: ${serie.titulo}
                Gênero: ${serie.genero}
                Temporadas: ${serie.temporadas}
                """.trimIndent()
            } ?: "Série não encontrada."
        )
        loading()
    }

    private fun Scanner.lerInt(mensagem: String): Int {
        while (true) {
            println(mensagem)
            try {
                val valor = this.nextInt()
                nextLine()
                return valor;
            } catch (e: InputMismatchException) {
                nextLine()
                println("Digite apenas números!")
            }
        }
    }

    private fun Scanner.lerString(mensagem: String): String {
        println(mensagem)
        return this.nextLine()
    }


}