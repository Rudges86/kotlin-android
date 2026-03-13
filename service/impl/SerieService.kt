package service.impl

import java.util.Scanner

interface SerieService {
    fun cadastrarSerie(scan: Scanner):Unit;
    fun pesquisarPorId(scan: Scanner): Boolean;
    fun pesquisarPorTitulo(scan: Scanner): Boolean;
    fun pesquisarPorGenero(scan: Scanner): Boolean;
    fun alterarService(scan: Scanner):Unit;
    fun removeService(scan: Scanner):Unit;
    fun listarTodasAsSeries(scan:Scanner):Unit
}