package kronos.cacaomovil

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Environment
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kronos.cacaomovil.common.DescargarImagen
import kronos.cacaomovil.models.NotificacionM
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern
import java.text.ParseException

/**
 * Created by Francisco on 31/5/15.
 */
object Constants {
    //val URL_BASE = "http://agd.codecastle.com.sv/api/v1/"
    val URL_BASE = "https://admin.cacaomovil.com/api/v1/"
    val APPS = URL_BASE+"apps"
    val CATEGORIES = URL_BASE+"categories"
    val GUIDES = URL_BASE+"guides/"
    val SECTIONS = URL_BASE+"sections/"
    val ARTICLES = URL_BASE+"articles/"
    val GUIA_COMPLETA = URL_BASE+"guides/%s/guide"
    val ARCHIVO_GUIA = URL_BASE+"upload_files/"
    val PRICE = URL_BASE+"apps/cocoa_price"
    val SAVE_TOKEN = URL_BASE+"notifications/save_token"
    val NOTIFICATIONS = URL_BASE+"notifications"
    val BORRAR_NOTIFICACION = URL_BASE+"notifications/delete"

    var token = ""
    val TAG = "cacao"
    var days = arrayOf(
            "Donmingo",
            "Lunes",
            "Martes",
            "Miercoles",
            "Jueves",
            "Viernes",
            "Sabado"
    )

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

    fun obtenerNotificacionesDetalle(id:String,activity: Activity){

        var dialogLoad = ACProgressFlower.Builder(activity)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Cargando")
                .fadeColor(Color.DKGRAY).build()

        dialogLoad.setCancelable(false)
        dialogLoad.show()

        var requestQueue = Volley.newRequestQueue(activity)

        val jsonObjRequestHome = object : StringRequest(
                Method.GET,
                Constants.NOTIFICATIONS+"/$id",
                Response.Listener { response ->
                    var res = JSONObject(response)

                    var notifications = res.getJSONArray("notification")
                    dialogLoad.dismiss()
                    for (i in 0..(notifications.length() - 1)) {
                        val item = notifications.getJSONObject(i)
                        var itemN = NotificacionM(item.getString("id"),item.getString("title"),item.getString("message"),item.getString("image"),formatoFechaLarga(item.getString("date")))
                        modalNotificaciones(activity,itemN)
                    }

                }, Response.ErrorListener { error ->
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=UTF-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return params.toString().toByteArray()
            }
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)
    }

    fun formatoFechaLarga(fecha:String): String {
        val format =
                SimpleDateFormat("dd-MM-yyyy")
        try {
            val date = format.parse(fecha)
            var formatoFechaPop = "dd MMMM, yyyy"
            val sdf = SimpleDateFormat(formatoFechaPop)
            var fecha = sdf.format(date).toString()

            val calendar = Calendar.getInstance()
            val dayIndex: Int = calendar.get(Calendar.DAY_OF_WEEK)
            return "${fecha.toLowerCase()}"

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }


    fun modalNotificaciones(activity: Activity,item:NotificacionM){
        val dialogAviso = Dialog(activity)
        dialogAviso.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAviso.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogAviso.setContentView(R.layout.pop_notificaciones)
        //dialogAviso.setCancelable(false)

        val btAceptar: Button
        val txTitle: TextView
        val close: ImageView
        val txDetalle: TextView
        val imgNotificacion: ImageView
        val txFecha:TextView

        btAceptar = dialogAviso.findViewById<View>(R.id.btAceptar) as Button
        close = dialogAviso.findViewById<View>(R.id.close) as ImageView
        txTitle = dialogAviso.findViewById<View>(R.id.txTitle) as TextView
        txFecha = dialogAviso.findViewById<View>(R.id.txFecha) as TextView
        txDetalle = dialogAviso.findViewById<View>(R.id.txDetalle) as TextView
        imgNotificacion = dialogAviso.findViewById<View>(R.id.imgNotificacion) as ImageView

        close.setOnClickListener {
            dialogAviso.dismiss()
        }

        btAceptar.setOnClickListener {

            val perms = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

            if(!EasyPermissions.hasPermissions(activity, *perms)){
                ActivityCompat.requestPermissions(activity, perms, 42);
            }else{

                var splitName = item.image.split("/")
                var fileName = splitName[splitName.size-1]
                fileName = fileName.replace('%','_');

                DescargarImagen(item.image,fileName,activity)

            }

            //dialogAviso.dismiss()
        }


        txTitle!!.setText(item.title)
        txDetalle!!.setText(item.message)
        txFecha!!.setText(item.date)
        if(!item.image.toString().equals("")){
            imgNotificacion.visibility = View.VISIBLE
            btAceptar.visibility = View.VISIBLE
            Picasso.get()
                    .load(item.image)
                    .into(imgNotificacion)

        }

        dialogAviso.show()

    }

}