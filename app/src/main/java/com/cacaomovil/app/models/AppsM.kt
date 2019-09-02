package com.cacaomovil.app.models

class AppsM {
    lateinit var id: String
    lateinit var nombre: String
    var image: Int = 0

    constructor(id: String, nombre: String, image: Int) {
        this.id = id
        this.nombre = nombre
        this.image = image
    }

    constructor() {

    }
}