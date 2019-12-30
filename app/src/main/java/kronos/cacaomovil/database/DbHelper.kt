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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriasDB.TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + ArticulosDB.TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + GuiasDB.TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + SesionesDB.TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + ArchivosDB.TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + AppsDB.TABLE_NAME)
        onCreate(db)
    }

    companion object {
        private val DB_NAME = "CacaomovilApp"
        private val DB_Version = 3
    }
}
