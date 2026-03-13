package model

class Serie {
    var id:Int = 0;
    var titulo: String = "";
    var genero: String = "";
    var temporadas: Int = 0;

    constructor(id:Int ,titulo:String, genero: String, temporadas: Int) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.temporadas = temporadas;
    }
}