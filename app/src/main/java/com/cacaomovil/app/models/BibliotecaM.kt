package com.cacaomovil.app.models

class BibliotecaM {
    lateinit var id: String
    lateinit var nombre: String
    lateinit var descripcion: String
    var image: Int = 0
    var tipo: Int = 0

    constructor(id: String, nombre: String, descripcion: String, image: Int, tipo: Int) {
        this.id = id
        this.nombre = nombre
        this.descripcion = descripcion
        this.image = image
        this.tipo = tipo
    }

    constructor() {

    }
}