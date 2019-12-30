package kronos.cacaomovil.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


class CategoriasDB(context: Context) {

    companion object {
        val TABLE_NAME = "Categorias"

        val id = "_id"
        val idWeb = "idWeb"
        val name = "name"
        val image = "image"
        val descripcion = "descripcion"


        val crearTablaCategorias = ("create table " + TABLE_NAME + " ("
                + id + " integer primary key AUTOINCREMENT, "
                + idWeb + " text not null,"
                + name + " text, "
                + image + " text, "
                + descripcion + " text ); ")

        val columnas = arrayOf(idWeb, name, image, descripcion)
    }

    private val helper: DbHelper
    private val db: SQLiteDatabase

    init {
        helper = DbHelper(context)
        db = helper.writableDatabase
    }

    private fun generarContentValues(id: String, nameInfo: String, imageInfo: String, descripcionInfo: String): ContentValues {
        val valores = ContentValues()
        valores.put(idWeb, id)
        valores.put(name, nameInfo)
        valores.put(image, imageInfo)
        valores.put(descripcion, descripcionInfo)
        return valores
    }

    fun insertar(idWeb: String, name: String, image: String, descripcion: String) {
        db.insert(TABLE_NAME, null, generarContentValues(idWeb, name, image, descripcion))
    }


    fun eliminar(id: Int) {
        db.delete(TABLE_NAME, "$idWeb=?", arrayOf(Integer.toString(id)))
    }

    fun eliminarTodo() {
        db.delete(TABLE_NAME, null, null)
    }


    fun getCategorias(): Cursor {
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }



}
