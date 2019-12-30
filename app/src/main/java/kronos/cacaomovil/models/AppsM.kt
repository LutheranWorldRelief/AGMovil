package kronos.cacaomovil.models

class AppsM {
    lateinit var id: String
    lateinit var nombre: String
    lateinit var image: String
    lateinit var app_type: String
    lateinit var app_url: String



    constructor(id: String, nombre: String, image: String, app_type: String, app_url: String) {
        this.id = id
        this.nombre = nombre
        this.image = image
        this.app_type = app_type
        this.app_url = app_url
    }

    constructor() {

    }
}