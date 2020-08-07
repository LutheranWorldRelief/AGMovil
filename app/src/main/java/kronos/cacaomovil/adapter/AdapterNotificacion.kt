package com.paillapp.app.adapter

import android.app.Activity
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kronos.cacaomovil.Constants
import kronos.cacaomovil.R
import kronos.cacaomovil.activities.ListadoNotificaciones.Companion.listNotificacion
import kronos.cacaomovil.models.NotificacionM
import org.json.JSONObject

class AdapterNotificacion(
    private val mContext: Activity,
    private val listEP: MutableList<NotificacionM>,
    private val tipo: String
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txNotificacion:TextView? = null
        var contNotificacion: RelativeLayout? = null
        var delete_icon:ImageView? = null

        init {
            txNotificacion = view.findViewById(R.id.txNotificacion) as TextView
            contNotificacion = view.findViewById(R.id.contNotificacion) as RelativeLayout
            delete_icon = view.findViewById(R.id.delete_icon) as ImageView
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notificacion, parent, false)

        vh = MyViewHolder(itemView)

        return vh

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = listEP[position]
        val paletteViewHolder = holder as MyViewHolder


        paletteViewHolder.txNotificacion!!.setText(item.title)


        paletteViewHolder.contNotificacion!!.setOnClickListener(View.OnClickListener {

            Constants.obtenerNotificacionesDetalle(item.id,mContext)

        })

        if(tipo.toString().equals("lista")){
            paletteViewHolder.delete_icon!!.visibility = View.VISIBLE
        }else{
            paletteViewHolder.delete_icon!!.visibility = View.GONE
        }

        paletteViewHolder.delete_icon!!.setOnClickListener(View.OnClickListener {


            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Seguro desea eliminar esta notificación?")
            builder.setIcon(R.mipmap.ic_launcher)
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                listNotificacion.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,listNotificacion.count())

                val params = JSONObject()
                params.put("notification_id",item.id)
                params.put("token",Constants.token)

                System.out.println("params "+params)
                System.out.println("params "+ Constants.BORRAR_NOTIFICACION)

                var requestQueue = Volley.newRequestQueue(mContext)
                val jsonObjRequestHome = object : StringRequest(
                        Method.POST,
                        Constants.BORRAR_NOTIFICACION,
                        Response.Listener { response ->
                        }, Response.ErrorListener { error ->
                    dialog.dismiss()
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
            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()

        })

    }

    override fun getItemCount(): Int {
        return listEP.size
    }


}
