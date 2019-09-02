package com.cacaomovil.app.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.cacaomovil.app.R
import com.cacaomovil.app.models.BibliotecaM
import com.squareup.picasso.Picasso


class AdapterBiblioteca(private val mContext: Activity, private val listDishes: List<BibliotecaM>) : RecyclerView.Adapter<AdapterBiblioteca.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txName: TextView
        var txDescripcion: TextView? = null
        var imageGuia: ImageView? = null
        var tipo: ImageView?=null

        init {
            imageGuia = view.findViewById(R.id.imageGuia) as ImageView
            txName = view.findViewById(R.id.txName) as TextView
            txDescripcion = view.findViewById(R.id.txDescripcion) as TextView
            tipo = view.findViewById(R.id.tipo) as ImageView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.items_biblioteca, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val Dishes = listDishes[position]

        Picasso.get()
                .load(Dishes.image)
                .resize(93, 114)
                .into(holder.imageGuia);

        holder.txName!!.setText(Dishes.nombre);
        holder.txDescripcion!!.setText(Dishes.descripcion);

        if (Dishes.tipo == 0) {
            Picasso.get()
                    .load(R.drawable.reload)
                    .into(holder.tipo);
        }else {
            Picasso.get()
                    .load(R.drawable.cloud_computing)
                    .into(holder.tipo);
        }



    }

    override fun getItemCount(): Int {
        return listDishes.size
    }
}
