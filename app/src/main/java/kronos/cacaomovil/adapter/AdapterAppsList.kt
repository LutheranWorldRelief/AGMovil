package kronos.cacaomovil.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kronos.cacaomovil.R
import kronos.cacaomovil.models.BibliotecaM
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import android.R.attr.button
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.*
import kronos.cacaomovil.activities.ListadoGuias
import kronos.cacaomovil.models.AppsM
import kronos.cacaomovil.models.CategoriasM
import java.util.ArrayList


class AdapterAppsList(private val mContext: Activity, private val listCategories: MutableList<CategoriasM>) : RecyclerView.Adapter<AdapterAppsList.MyViewHolder>() {


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var section_text: TextView? = null
        var linea: TextView? = null

        lateinit var recyclerListApps: RecyclerView
        lateinit var recyclerListAppsDestacadas: RecyclerView
        lateinit var contDestacadas: LinearLayout
        lateinit var contApps: LinearLayout


        lateinit var adapterApps: SimpleAdapter
        internal var arraylistApps: MutableList<AppsM> = ArrayList<AppsM>()

        lateinit var adapterAppsDestacadas: SimpleAdapter
        internal var arraylistAppsDestacadas: MutableList<AppsM> = ArrayList<AppsM>()

        init {
            section_text = view.findViewById(R.id.section_text) as TextView
            linea = view.findViewById(R.id.linea) as TextView
            contDestacadas = view.findViewById(R.id.contDestacadas) as LinearLayout
            contApps= view.findViewById(R.id.contApps) as LinearLayout
            recyclerListApps = view.findViewById(R.id.recyclerListApps) as RecyclerView

            recyclerListApps.setNestedScrollingEnabled(false)
            recyclerListApps.setHasFixedSize(true);
            recyclerListApps.setLayoutManager(GridLayoutManager(mContext!!,3))


            recyclerListAppsDestacadas = view.findViewById(R.id.recyclerListAppsDestacadas) as RecyclerView

            recyclerListAppsDestacadas.setNestedScrollingEnabled(false)
            recyclerListAppsDestacadas.setHasFixedSize(true);
            recyclerListAppsDestacadas.setLayoutManager(GridLayoutManager(mContext!!,3))


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.section, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val categories = listCategories[position]

        if(categories.nombre.toString().equals("Destacadas")){
            holder.section_text!!.setText("")
            holder.linea!!.visibility = View.GONE
            holder.section_text!!.visibility = View.GONE
        }else{
            holder.section_text!!.setText(categories.nombre)
            holder.linea!!.visibility = View.VISIBLE
            holder.section_text!!.visibility = View.VISIBLE
        }




        holder.arraylistApps.clear()
        holder.arraylistAppsDestacadas.clear()

        //apss normales
        for(j in 0..categories.apps.size-1) {
            val itemsApps = categories.apps[j]
            holder.arraylistApps.add(AppsM(itemsApps.id, itemsApps.nombre, itemsApps.image, itemsApps.app_type, itemsApps.app_url))
        }

        holder.adapterApps = SimpleAdapter(mContext!!,holder.arraylistApps)

        holder.recyclerListApps.setAdapter(holder.adapterApps)
        holder.adapterApps.notifyDataSetChanged()

        //apps destacadas
        for(k in 0..categories.appsDestacadas.size-1) {
            val itemsApps = categories.appsDestacadas[k]
            holder.arraylistAppsDestacadas.add(AppsM(itemsApps.id, itemsApps.nombre, itemsApps.image, itemsApps.app_type, itemsApps.app_url))
        }

        if(categories.appsDestacadas.size==0){
            holder.contDestacadas.visibility = View.GONE
            holder.contApps.visibility = View.VISIBLE
        }else{
            holder.contDestacadas.visibility = View.VISIBLE
            holder.contApps.visibility = View.GONE
        }

        holder.adapterAppsDestacadas = SimpleAdapter(mContext!!,holder.arraylistAppsDestacadas)

        holder.recyclerListAppsDestacadas.setAdapter(holder.adapterAppsDestacadas)
        holder.adapterAppsDestacadas.notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return listCategories.size
    }
}
