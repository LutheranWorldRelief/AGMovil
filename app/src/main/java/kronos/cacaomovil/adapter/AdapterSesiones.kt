package kronos.cacaomovil.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kronos.cacaomovil.R
import kronos.cacaomovil.activities.Articulos
import kronos.cacaomovil.models.SesionesM
import com.squareup.picasso.Picasso


class AdapterSesiones(private val mContext: Activity, private val listSesiones: List<SesionesM>) : RecyclerView.Adapter<AdapterSesiones.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txNumero: TextView? = null
        var txName: TextView? = null
        var rlCont: RelativeLayout?=null

        init {
            txName = view.findViewById(R.id.txName) as TextView
            txNumero = view.findViewById(R.id.txNumero) as TextView
            rlCont = view.findViewById(R.id.rlCont) as RelativeLayout

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.items_sesiones, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemsSesiones = listSesiones[position]


        holder.txName!!.setText(itemsSesiones.name)

        if(position<9){
            holder.txNumero!!.setText("0"+(position+1))
        }else{
            holder.txNumero!!.setText(position.toString())
        }

        holder.rlCont!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val i = Intent(mContext, Articulos::class.java)
                i.putExtra("id", itemsSesiones.id)
                mContext.startActivity(i)
            }
        })

    }

    override fun getItemCount(): Int {
        return listSesiones.size
    }
}
