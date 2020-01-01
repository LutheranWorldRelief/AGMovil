package kronos.cacaomovil.models

import java.util.ArrayList

class CategoriasM {
    lateinit var id: String
    lateinit var nombre: String
    lateinit var icon: String
    var seleccionado: Boolean = false
    internal var apps: MutableList<AppsM> = ArrayList<AppsM>()
    internal var appsDestacadas: MutableList<AppsM> = ArrayList<AppsM>()
    var visible: Boolean = false

    constructor(id: String, nombre: String, seleccionado: Boolean,icon:String,apps: MutableList<AppsM>,appsDestacadas: MutableList<AppsM>) {
        this.id = id
        this.nombre = nombre
        this.seleccionado = seleccionado
        this.icon = icon
        this.apps = apps
        this.appsDestacadas = appsDestacadas
    }

    constructor(id: String, nombre: String, seleccionado: Boolean,icon:String,apps: MutableList<AppsM>,appsDestacadas: MutableList<AppsM>,visible:Boolean) {
        this.id = id
        this.nombre = nombre
        this.seleccionado = seleccionado
        this.icon = icon
        this.apps = apps
        this.appsDestacadas = appsDestacadas
        this.visible = visible
    }

    constructor(id: String, nombre: String, seleccionado: Boolean,icon:String) {
        this.id = id
        this.nombre = nombre
        this.seleccionado = seleccionado
        this.icon = icon
    }

    constructor() {

    }
}