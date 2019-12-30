package kronos.cacaomovil.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import kronos.cacaomovil.R
import kronos.cacaomovil.models.AppsM
import kronos.cacaomovil.models.CategoriasM
import com.squareup.picasso.Picasso

import java.util.ArrayList

class AdapterCategorias// data is passed into the constructor
internal constructor(context: Activity, private val mCategoria: MutableList<CategoriasM> ) : RecyclerView.Adapter<AdapterCategorias.ViewHolder>() {
    private val mInflater: LayoutInflater
    private var mClickListener: ItemClickListener? = null

    init {
        this.mInflater = LayoutInflater.from(context)
    }

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.categoria_top, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the view and textview in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoria = mCategoria[position]
        holder.txNombre.text = categoria.nombre
        if(categoria.seleccionado==true){
            holder.txNombre.setTextColor(Color.parseColor("#ea7a48"))
            holder.imgCategoria.setColorFilter(Color.parseColor("#ea7a48"))
        }else{
            holder.txNombre.setTextColor(Color.parseColor("#666666"))
            holder.imgCategoria.setColorFilter(Color.parseColor("#666666"))
        }

        if(categoria.icon.toString().equals("")){

            Picasso.get()
                    .load(R.drawable.category)
                    .error(R.drawable.category)
                    .into(holder.imgCategoria)

        }else{
            Picasso.get()
                    .load(categoria.icon)
                    .error(R.drawable.category)
                    .into(holder.imgCategoria)
        }

    }

    // total number of rows
    override fun getItemCount(): Int {
        return mCategoria.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var imgCategoria: ImageView
        internal var txNombre: TextView

        init {
            imgCategoria = itemView.findViewById(R.id.imgCategoria)
            txNombre = itemView.findViewById(R.id.txNombre)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    // convenience method for getting data at click position
    fun getItem(id: Int): String {
        return mCategoria[id].id
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}