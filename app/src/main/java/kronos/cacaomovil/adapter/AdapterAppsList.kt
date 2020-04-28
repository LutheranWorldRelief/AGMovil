package kronos.cacaomovil.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kronos.cacaomovil.R
import kronos.cacaomovil.fragments.ListadoApps
import kronos.cacaomovil.models.AppsM
import kronos.cacaomovil.models.CategoriasM
import java.util.*


class AdapterAppsList(private val mContext: Activity, private val listCategories: MutableList<CategoriasM>) : RecyclerView.Adapter<AdapterAppsList.MyViewHolder>() {


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var section_text: TextView? = null
        var linea: TextView? = null

        lateinit var recyclerListApps: RecyclerView
        lateinit var recyclerListAppsDestacadas: RecyclerView
        lateinit var contDestacadas: LinearLayout
        lateinit var contApps: LinearLayout
        lateinit var arrow: ImageView
        lateinit var contEncabezado: RelativeLayout


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
            arrow = view.findViewById(R.id.arrow) as ImageView
            contEncabezado = view.findViewById(R.id.contEncabezado) as RelativeLayout

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
            holder.contEncabezado!!.visibility = View.GONE
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



        if(categories.visible){
            Picasso.get()
                    .load(R.drawable.arrow_up)
                    .resize(100, 100)
                    .into(holder.arrow)

            holder.recyclerListApps.setVisibility(View.VISIBLE)

        }else{
            Picasso.get()
                    .load(R.drawable.arrow_bottom)
                    .resize(100, 100)
                    .into(holder.arrow)

            holder.recyclerListApps.setVisibility(View.GONE)
        }

        holder.contEncabezado!!.setOnClickListener {
            var posicionActual = position
            if(categories.visible){
                Picasso.get()
                        .load(R.drawable.arrow_bottom)
                        .resize(100, 100)
                        .into(holder.arrow)


                categories.visible = false
                holder.recyclerListApps.animate()
                        .translationY(0F)
                        .alpha(0.0f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                holder.recyclerListApps.setVisibility(View.GONE)
                            }
                        })
            }else{
                categories.visible = true
                Picasso.get()
                        .load(R.drawable.arrow_up)
                        .resize(100, 100)
                        .into(holder.arrow)
                holder.recyclerListApps.setVisibility(View.VISIBLE)
                holder.recyclerListApps.animate()
                        .translationY(0F)
                        .alpha(1.0f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                //ListadoApps.layoutManager.scrollToPositionWithOffset(1000, 0)
                                System.out.println("terminooo")
                                ListadoApps.recyclerApps.smoothScrollToPosition(posicionActual)
                            }
                        })
            }

        }
    }

    override fun getItemCount(): Int {
        return listCategories.size
    }
}
