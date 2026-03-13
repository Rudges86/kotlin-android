import service.SerieServiceImpl
import service.impl.SerieService
import java.util.*
import javax.swing.text.TabStop

fun main() {
    val service: SerieService = SerieServiceImpl();
    val scan = Scanner(System.`in`);

    println("Olá seja bem-vindo ao sistema de controle de séries.")
    println("Para começar por favor digite o seu nome: ")
    val nome: String = scan.nextLine();
    loading();
    println("Seja bem-vindo ${nome}, o que deseja fazer:")
    var stop:Boolean = false;
    while(!stop) {
        when(cabecalho("Menu principal", listOf(
                "Cadastrar série",
                "Pesquisar uma série",
                "Listar todas as séries",
                "Editar uma série",
                "Remover uma série",
                "Para encerrar a aplicação."), scan)) {

            1 -> service.cadastrarSerie(scan)
            2 -> pesquisar(service, scan)
            3 -> service.listarTodasAsSeries(scan)
            4 -> service.alterarService(scan)
            5 -> service.removeService(scan)
            6 -> {
                println("encerrando o programa.");
                loading()
                println("--------FINALIZADO--------")
                stop = true;
            }
            else -> println("Opção inválida.")
        }

    }
}

fun loading() {
   repeat(8) {
        Thread.sleep(200);
        print("**")
    }
    println("")
}

fun cabecalho(menu:String, opcoes: List<String>, scan: Scanner ): Int {
    println("****${menu}***")
    opcoes.forEachIndexed{index, opcoes -> println("${index + 1} - $opcoes") }
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

fun pesquisar(service:SerieService, scan: Scanner){
    while(true) {
        when(cabecalho("Pesquisar", listOf("Pesquisar por id",
            "Pesquisar por título",
            "Pesquisar por gênero",
            "Voltar para o menu anterior")
            ,scan)) {
            1-> service.pesquisarPorId(scan)
            2-> service.pesquisarPorTitulo(scan)
            3-> service.pesquisarPorGenero(scan)
            4-> return
            else -> println("Opção inválida")
        }
    }
}
