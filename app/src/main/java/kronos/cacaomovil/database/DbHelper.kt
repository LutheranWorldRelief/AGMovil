package kronos.cacaomovil.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_Version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CategoriasDB.crearTablaCategorias)
        db.execSQL(ArticulosDB.crearTablaArticulos)
        db.execSQL(GuiasDB.crearTablaGuias)
        db.execSQL(SesionesDB.crearTablaSecciones)
        db.execSQL(ArchivosDB.crearTablaArchivos)
        db.execSQL(AppsDB.crearTablaApps)
        db.execSQL(PrecioDB.crearTablaPrecio)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        when (newVersion) {
            4 ->{
                db.execSQL(PrecioDB.crearTablaPrecio)
            }
        }
    }

    companion object {
        private val DB_NAME = "CacaomovilApp"
        private val DB_Version = 4
    }
}
