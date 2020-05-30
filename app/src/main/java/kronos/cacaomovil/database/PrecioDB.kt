package kronos.cacaomovil.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


class PrecioDB(context: Context) {

    companion object {
        val TABLE_NAME = "PrecioCacao"

        val id = "_id"
        val cocoa_price = "cocoa_price"
        val pc = "pc"
        val pcp = "pcp"


        val crearTablaPrecio= ("create table IF NOT EXISTS " + TABLE_NAME + " ("
                + id + " integer primary key AUTOINCREMENT, "
                + cocoa_price + " text, "
                + pc + " text, "
                + pcp + " text ); ")

        val columnas = arrayOf(cocoa_price, pc,pcp)
    }

    private val helper: DbHelper
    private val db: SQLiteDatabase

    init {
        helper = DbHelper(context)
        db = helper.writableDatabase
    }

    private fun generarContentValues(cocoa_priceInfo: String, pcInfo: String, pcpInfo: String): ContentValues {
        val valores = ContentValues()
        valores.put(cocoa_price, cocoa_priceInfo)
        valores.put(pc, pcInfo)
        valores.put(pcp, pcpInfo)
        return valores
    }

    fun insertar(cocoa_price: String, pc: String, pcp: String) {
        db.insert(TABLE_NAME, null, generarContentValues(cocoa_price, pc,pcp))
    }


    fun eliminarTodo() {
        db.delete(TABLE_NAME, null, null)
    }


    fun getPrecioCacao(): Cursor {
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

}
