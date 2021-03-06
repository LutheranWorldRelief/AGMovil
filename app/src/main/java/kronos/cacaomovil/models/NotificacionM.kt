package kronos.cacaomovil.models

class NotificacionM {
    lateinit var id: String
    lateinit var title: String
    lateinit var message: String
    lateinit var date: String
    lateinit var image: String

    constructor(id: String, title: String, message: String) {
        this.id = id
        this.title = title
        this.message = message
    }

    constructor(id: String, title: String, message: String, image: String, date: String) {
        this.id = id
        this.title = title
        this.message = message
        this.image = image
        this.date = date
    }

    constructor() {

    }
}