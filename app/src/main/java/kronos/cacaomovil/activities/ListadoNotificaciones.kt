package kronos.cacaomovil.activities

import android.Manifest
import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.gesture.GestureLibraries.fromFile
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.StrictMode
import android.view.View
import android.view.Window
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.paillapp.app.adapter.AdapterNotificacion
import kronos.cacaomovil.Constants
import kronos.cacaomovil.R
import kronos.cacaomovil.adapter.AdapterBiblioteca
import kronos.cacaomovil.adapter.AdapterGuias
import kronos.cacaomovil.adapter.SectionedGridRecyclerViewAdapter
import kronos.cacaomovil.adapter.SimpleAdapter
import kronos.cacaomovil.common.DescargarArchivos
import kronos.cacaomovil.database.ArchivosDB
import kronos.cacaomovil.database.GuiasDB
import kronos.cacaomovil.database.SesionesDB
import kronos.cacaomovil.fragments.HomeOpciones
import kronos.cacaomovil.models.*
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.File.separator
import java.util.ArrayList


class ListadoNotificaciones : AppCompatActivity(), View.OnClickListener{
    //Defining Variables
    private var toolbar: Toolbar? = null
    lateinit var dialog: ACProgressFlower

    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerNotificaciones: RecyclerView
    internal var listNotificacion: MutableList<NotificacionM> = ArrayList<NotificacionM>()
    private var adapter: AdapterNotificacion? = null
    internal lateinit var context: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listado_notificaciones)

        context = this@ListadoNotificaciones

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        listNotificacion.clear()
        adapter = AdapterNotificacion(context!!, listNotificacion)
        layoutManager = LinearLayoutManager(context)
        recyclerNotificaciones = context!!.findViewById(R.id.recyclerNotificaciones) as RecyclerView
        recyclerNotificaciones.addItemDecoration(DividerItemDecoration(recyclerNotificaciones.getContext(), DividerItemDecoration.VERTICAL));

        recyclerNotificaciones.layoutManager = layoutManager
        recyclerNotificaciones.itemAnimator = DefaultItemAnimator()
        recyclerNotificaciones.adapter = adapter

        loadGuias()



    }


    fun loadGuias() {
        dialog = ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Cargando")
                .fadeColor(Color.DKGRAY).build()

        dialog.setCancelable(false)
        dialog.show()


        var requestQueue = Volley.newRequestQueue(context)

        val jsonObjRequestHome = object : StringRequest(
                Method.GET,
                Constants.NOTIFICATIONS,
                Response.Listener { response ->
                    listNotificacion.clear()
                    var res = JSONObject(response)
                    var notifications = res.getJSONArray("notifications")
                    for (i in 0..(notifications.length() - 1)) {
                        if(i<10){
                            val item = notifications.getJSONObject(i)
                            listNotificacion.add(NotificacionM(item.getString("id"),item.getString("title"),item.getString("message")))
                        }

                    }

                    adapter!!.notifyDataSetChanged()
                    dialog.dismiss()

                }, Response.ErrorListener { error ->
            dialog.dismiss()
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=UTF-8"
            }
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)


    }


    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
    }

    public override fun onResume() {
        super.onResume()
    }


    override fun onClick(v: View) {

    }

}
