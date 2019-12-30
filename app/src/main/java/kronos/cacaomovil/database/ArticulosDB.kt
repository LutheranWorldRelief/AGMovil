package kronos.cacaomovil.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


class ArticulosDB(context: Context) {

    companion object {
        val TABLE_NAME = "Articulos"

        val id = "_id"
        val idWeb = "idWeb"
        val name = "name"
        val title = "title"
        val descripcion = "descripcion"
        val content = "content"
        val idSeccion = "idSeccion"


        val crearTablaArticulos= ("create table " + TABLE_NAME + " ("
                + id + " integer primary key AUTOINCREMENT, "
                + idWeb + " text not null,"
                + name + " text not null,"
                + title + " text not null,"
                + descripcion + " text not null,"
                + content + " text not null,"
                + idSeccion + " text ); ")

        val columnas = arrayOf(idWeb, name, title,descripcion, content,idSeccion)
    }

    private val helper: DbHelper
    private val db: SQLiteDatabase

    init {
        helper = DbHelper(context)
        db = helper.writableDatabase
    }

    private fun generarContentValues(id: String, nameInfo: String,titleInfo: String, descripcionInfo: String, contentInfo: String, idSeccionInfo: String): ContentValues {
        val valores = ContentValues()
        valores.put(idWeb, id)
        valores.put(name, nameInfo)
        valores.put(title, titleInfo)
        valores.put(descripcion, descripcionInfo)
        valores.put(content, contentInfo)
        valores.put(idSeccion, idSeccionInfo)
        return valores
    }

    fun insertar(idWeb: String, name: String, title: String, descripcion: String, content: String, idSeccion: String) {
        db.insert(TABLE_NAME, null, generarContentValues(idWeb, name, title,descripcion,content,idSeccion))
    }


    fun eliminar(id: String) {
        db.delete(TABLE_NAME, "$idWeb=?", arrayOf(id))
    }

    fun eliminarTodo() {
        db.delete(TABLE_NAME, null, null)
    }


    fun getArticulos(idSeccion:String): Cursor {
        val param = arrayOf<String>(idSeccion)
        return db.rawQuery("SELECT * FROM $TABLE_NAME where idSeccion=?", param)
    }



}
