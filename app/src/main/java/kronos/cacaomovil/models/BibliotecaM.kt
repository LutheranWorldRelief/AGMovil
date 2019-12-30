package kronos.cacaomovil.models

class BibliotecaM {
    lateinit var id: String
    lateinit var name: String
    lateinit var description: String
    lateinit var image: String
    lateinit var nameFind: String
    lateinit var descriptionFind: String

    constructor(id: String, name: String, description: String, image: String,nameFind: String,descriptionFind:String) {
        this.id = id
        this.name = name
        this.description = description
        this.image = image
        this.nameFind = nameFind
        this.descriptionFind = descriptionFind
    }

    constructor() {

    }
}