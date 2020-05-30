package com.paillapp.app.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kronos.cacaomovil.Constants
import kronos.cacaomovil.R
import kronos.cacaomovil.models.NotificacionM

class AdapterNotificacion(
    private val mContext: Activity,
    private val listEP: MutableList<NotificacionM>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txNotificacion:TextView? = null
        var contNotificacion: RelativeLayout? = null

        init {
            txNotificacion = view.findViewById(R.id.txNotificacion) as TextView
            contNotificacion = view.findViewById(R.id.contNotificacion) as RelativeLayout
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

    }

    override fun getItemCount(): Int {
        return listEP.size
    }


}
