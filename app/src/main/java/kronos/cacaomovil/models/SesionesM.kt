package kronos.cacaomovil.models

class SesionesM {
    lateinit var id: String
    lateinit var name: String

    constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }

    constructor() {

    }
}