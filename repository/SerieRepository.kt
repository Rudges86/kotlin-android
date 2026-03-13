package repository

import model.Serie

class SerieRepository {
   private val series = mutableListOf<Serie>(
       Serie(1, "Breaking Bad", "Drama", 5),
       Serie(2, "Game of Thrones", "Fantasia", 8),
       Serie(3, "Stranger Things", "Ficção Científica", 4),
       Serie(4, "The Witcher", "Fantasia", 3),
       Serie(5, "Dark", "Ficção Científica", 3),
       Serie(6, "The Office", "Comédia", 9),
       Serie(7, "Friends", "Comédia", 10),
       Serie(8, "House of the Dragon", "Fantasia", 2),
       Serie(9, "Narcos", "Crime", 3),
       Serie(10, "The Mandalorian", "Ficção Científica", 3),
       Serie(11, "Sherlock", "Suspense", 4),
       Serie(12, "Peaky Blinders", "Drama", 6),
       Serie(13, "The Last of Us", "Drama", 1),
       Serie(14, "Loki", "Ficção Científica", 2),
       Serie(15, "The Boys", "Ação", 4)
   );

    fun cadastrarSerie(serie: Serie): String {
        series.add(serie);
        return "Serie cadastrada com sucesso.";
    }

    fun getAllSeries():List<Serie> {
        return series;
    }

    fun deletarSerie(id:Int):String {
         val removido = series.removeIf{it.id  == id};
        return if(removido) {
            "Removido com sucesso";
        } else {
            "Não encontrado!";
        }
    }

    fun alterarSerie(serie: Serie,serieAtualizada: Serie):String {
        serie.apply {
            titulo = serieAtualizada.titulo
            genero = serieAtualizada.genero
            temporadas = serieAtualizada.temporadas
        }

        return "Série atualizada com sucesso";
    }


    fun buscarPorId(id: Int): Serie? {
        return series.find { it.id == id}
    }

    fun buscarPorTitulo(titulo:String):Serie? {
        return series.find { it.titulo.equals(titulo,true)}
    }

    fun buscarPorGenero(genero:String): List<Serie> {
        return series.filter { it.genero.equals(genero,true)}
    }
}

