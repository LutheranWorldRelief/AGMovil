package kronos.cacaomovil.models

class NotificacionM {
    lateinit var id: String
    lateinit var nombre: String



    constructor(id: String, nombre: String) {
        this.id = id
        this.nombre = nombre
    }

    constructor() {

    }
}