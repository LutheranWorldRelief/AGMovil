package kronos.cacaomovil.models

class ArticlesM {
    lateinit var id: String
    lateinit var name: String
    lateinit var title: String
    lateinit var description: String
    lateinit var content: String
    lateinit var link: String

    constructor(id: String, name: String, title: String, description: String, content: String,link:String) {
        this.id = id
        this.name = name
        this.title = title
        this.description = description
        this.content = content
        this.link = link
    }

    constructor() {

    }
}