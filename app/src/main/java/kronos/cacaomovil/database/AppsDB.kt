package kronos.cacaomovil.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


class AppsDB(context: Context) {

    companion object {
        val TABLE_NAME = "Apps"

        val id = "_id"
        val idWeb = "idWeb"
        val name = "name"
        val icon = "icon"
        val descripcion = "descripcion"
        val apps = "apps"


        val crearTablaApps= ("create table " + TABLE_NAME + " ("
                + id + " integer primary key AUTOINCREMENT, "
                + idWeb + " text not null,"
                + name + " text not null,"
                + icon + " text not null,"
                + descripcion + " text not null,"
                + apps + " text ); ")

        val columnas = arrayOf(idWeb, name, icon,descripcion, apps)
    }

    private val helper: DbHelper
    private val db: SQLiteDatabase

    init {
        helper = DbHelper(context)
        db = helper.writableDatabase
    }

    private fun generarContentValues(id: String, nameInfo: String,iconInfo: String, descripcionInfo: String, appsInfo: String): ContentValues {
        val valores = ContentValues()
        valores.put(idWeb, id)
        valores.put(name, nameInfo)
        valores.put(icon, iconInfo)
        valores.put(descripcion, descripcionInfo)
        valores.put(apps, appsInfo)
        return valores
    }

    fun insertar(idWeb: String, name: String, icon: String, descripcion: String, apps: String) {
        db.insert(TABLE_NAME, null, generarContentValues(idWeb, name, icon,descripcion,apps))
    }


    fun eliminar(id: String) {
        db.delete(TABLE_NAME, "$idWeb=?", arrayOf(id))
    }

    fun eliminarTodo() {
        db.delete(TABLE_NAME, null, null)
    }


    fun getApps(): Cursor {
        //val param = arrayOf<String>(idSeccion)
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }



}
