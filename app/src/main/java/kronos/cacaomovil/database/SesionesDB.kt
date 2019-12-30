package kronos.cacaomovil.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


class SesionesDB(context: Context) {

    companion object {
        val TABLE_NAME = "Secciones"

        val id = "_id"
        val idWeb = "idWeb"
        val name = "name"
        val idGuia = "idGuia"


        val crearTablaSecciones= ("create table " + TABLE_NAME + " ("
                + id + " integer primary key AUTOINCREMENT, "
                + idWeb + " text not null,"
                + name + " text not null,"
                + idGuia + " text ); ")

        val columnas = arrayOf(idWeb, name,idGuia)
    }

    private val helper: DbHelper
    private val db: SQLiteDatabase

    init {
        helper = DbHelper(context)
        db = helper.writableDatabase
    }

    private fun generarContentValues(id: String, nameInfo: String, idGuiaInfo: String): ContentValues {
        val valores = ContentValues()
        valores.put(idWeb, id)
        valores.put(name, nameInfo)
        valores.put(idGuia, idGuiaInfo)
        return valores
    }

    fun insertar(idWeb: String, name: String, idGuia: String) {
        db.insert(TABLE_NAME, null, generarContentValues(idWeb, name,idGuia))
    }


    fun eliminar(id: String) {
        db.delete(TABLE_NAME, "$idWeb=?", arrayOf(id))
    }

    fun eliminarTodo() {
        db.delete(TABLE_NAME, null, null)
    }


    fun getSecciones(idGuiaInfo:String): Cursor {
        val param = arrayOf<String>(idGuiaInfo)
        return db.rawQuery("SELECT * FROM $TABLE_NAME where $idGuia=?", param)
    }



}
