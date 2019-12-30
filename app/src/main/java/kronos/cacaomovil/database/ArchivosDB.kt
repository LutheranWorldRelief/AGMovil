package kronos.cacaomovil.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


class ArchivosDB(context: Context) {

    companion object {
        val TABLE_NAME = "Archivos"

        val id = "_id"
        val name = "name"
        val guia = "guia"


        val crearTablaArchivos= ("create table " + TABLE_NAME + " ("
                + id + " integer primary key AUTOINCREMENT, "
                + name + " text, "
                + guia + " text ); ")

        val columnas = arrayOf(name, guia)
    }

    private val helper: DbHelper
    private val db: SQLiteDatabase

    init {
        helper = DbHelper(context)
        db = helper.writableDatabase
    }

    private fun generarContentValues(nameInfo: String, guiaInfo: String): ContentValues {
        val valores = ContentValues()
        valores.put(name, nameInfo)
        valores.put(guia, guiaInfo)
        return valores
    }

    fun insertar(name: String, guia: String) {
        db.insert(TABLE_NAME, null, generarContentValues(name, guia))
    }


    fun eliminar(id: String) {
        db.delete(TABLE_NAME, "$guia=?", arrayOf(id))
    }

    fun eliminarTodo() {
        db.delete(TABLE_NAME, null, null)
    }


    fun getArchivoByGuia(idGuia:String): Cursor {
        val param = arrayOf<String>(idGuia)
        return db.rawQuery("SELECT * FROM $TABLE_NAME where $guia=?", param)
    }

}
