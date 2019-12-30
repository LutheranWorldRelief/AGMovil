package kronos.cacaomovil

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Vibrator
import android.view.View
import android.view.inputmethod.InputMethodManager
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower

import org.json.JSONArray
import org.json.JSONException

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Francisco on 31/5/15.
 */
object Constants {
    val URL_BASE = "http://agd.codecastle.com.sv/api/v1/"
    val APPS = URL_BASE+"apps"
    val CATEGORIES = URL_BASE+"categories"
    val GUIDES = URL_BASE+"guides/"
    val SECTIONS = URL_BASE+"sections/"
    val ARTICLES = URL_BASE+"articles/"
    val GUIA_COMPLETA = URL_BASE+"guides/%s/guide"
    val ARCHIVO_GUIA = URL_BASE+"upload_files/"
    val TAG = "cacao"


    fun verificaConexion(ctx: Activity): Boolean {
        var bConectado = false
        val connec = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val redes = connec.allNetworkInfo
        for (i in 0..1) {
            if (redes[i].state == NetworkInfo.State.CONNECTED) {
                bConectado = true
            }
        }
        return bConectado
    }


    fun isEmailValid(email: String): Boolean {
        var isValid = false

        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }


    fun cerrarTeclado(context: Activity) {
        val view = context.currentFocus
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun remove(input: String): String {
        // Cadena de caracteres original a sustituir.
        val original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ"
        // Cadena de caracteres ASCII que reemplazarán los originales.
        val ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC"
        var output = input
        for (i in 0 until original.length) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original[i], ascii[i])
        }//for i
        return output
    }

}