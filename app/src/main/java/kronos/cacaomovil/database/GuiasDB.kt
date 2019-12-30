package kronos.cacaomovil.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


class GuiasDB(context: Context) {

    companion object {
        val TABLE_NAME = "Guias"

        val id = "_id"
        val idWeb = "idWeb"
        val name = "name"
        val image = "image"
        val categoria = "idCategoria"


        val crearTablaGuias= ("create table " + TABLE_NAME + " ("
                + id + " integer primary key AUTOINCREMENT, "
                + idWeb + " text not null,"
                + name + " text, "
                + image + " text, "
                + categoria + " text ); ")

        val columnas = arrayOf(idWeb, name, image, categoria)
    }

    private val helper: DbHelper
    private val db: SQLiteDatabase

    init {
        helper = DbHelper(context)
        db = helper.writableDatabase
    }

    private fun generarContentValues(id: String, nameInfo: String, imageInfo: String, categoriaInfo: String): ContentValues {
        val valores = ContentValues()
        valores.put(idWeb, id)
        valores.put(name, nameInfo)
        valores.put(image, imageInfo)
        valores.put(categoria, categoriaInfo)
        return valores
    }

    fun insertar(idWeb: String, name: String, image: String, categoriaInfo: String) {
        db.insert(TABLE_NAME, null, generarContentValues(idWeb, name, image, categoriaInfo))
    }


    fun eliminar(id: String) {
        db.delete(TABLE_NAME, "$idWeb=?", arrayOf(id))
    }

    fun eliminarTodo() {
        db.delete(TABLE_NAME, null, null)
    }


    fun getGuiasByCategoria(idCategoria:String): Cursor {
        val param = arrayOf<String>(idCategoria)
        return db.rawQuery("SELECT * FROM $TABLE_NAME where $categoria=?", param)
    }


    fun getGuiaID(id:String): Cursor {
        val param = arrayOf<String>(id)
        return db.rawQuery("SELECT * FROM $TABLE_NAME where $idWeb=?", param)
    }



}
