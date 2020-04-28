package kronos.cacaomovil.models

class GuiasM {
    lateinit var id: String
    lateinit var name: String
    lateinit var image: String
    var descargado:Boolean = false
    lateinit var idCategoria: String
    lateinit var formato: String
    var order = 0
    var archive = false

    constructor(id: String, name: String, image: String,descargado:Boolean,idCategoria:String,formato:String,order:Int,archive:Boolean) {
        this.id = id
        this.name = name
        this.image = image
        this.descargado = descargado
        this.idCategoria = idCategoria
        this.formato = formato
        this.order = order
        this.archive = archive
    }

    constructor() {

    }
}