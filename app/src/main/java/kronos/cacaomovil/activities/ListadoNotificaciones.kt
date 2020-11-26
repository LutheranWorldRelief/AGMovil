package kronos.cacaomovil.activities

import android.app.*
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.paillapp.app.adapter.AdapterNotificacion
import kronos.cacaomovil.Constants
import kronos.cacaomovil.R
import kronos.cacaomovil.models.*
import org.json.JSONObject
import java.util.ArrayList
import org.json.JSONArray as JSONArray1


class ListadoNotificaciones : AppCompatActivity(), View.OnClickListener{
    //Defining Variables
    private var toolbar: Toolbar? = null
    lateinit var dialog: ACProgressFlower

    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerNotificaciones: RecyclerView
    private var adapter: AdapterNotificacion? = null
    internal lateinit var context: Activity

    companion object {
        internal var listNotificacion: MutableList<NotificacionM> = ArrayList<NotificacionM>()
    }

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

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        //Log.w("TAG", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    Constants.token = task.result?.token.toString()
                })

        listNotificacion.clear()
        adapter = AdapterNotificacion(context!!, listNotificacion,"lista")
        layoutManager = LinearLayoutManager(context)
        recyclerNotificaciones = context!!.findViewById(R.id.recyclerNotificaciones) as RecyclerView
        recyclerNotificaciones.addItemDecoration(DividerItemDecoration(recyclerNotificaciones.getContext(), DividerItemDecoration.VERTICAL));

        recyclerNotificaciones.layoutManager = layoutManager
        recyclerNotificaciones.itemAnimator = DefaultItemAnimator()
        recyclerNotificaciones.adapter = adapter

        loadNotificacion()



    }


    fun loadNotificacion() {
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
                Constants.NOTIFICATIONS+"?token="+Constants.token,
                Response.Listener { response ->
                    listNotificacion.clear()
                    var res = JSONObject(response)
                    if(res.has("notifications")){
                        val jsonNotifications = res["notifications"]
                        if (jsonNotifications is JSONArray1){
                            var notifications = res.getJSONArray("notifications")
                            for (i in 0 until notifications.length()) {
                                val item = notifications.getJSONObject(i)
                                listNotificacion.add(NotificacionM(item.getString("id"),item.getString("title"),item.getString("message")))
                            }
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
