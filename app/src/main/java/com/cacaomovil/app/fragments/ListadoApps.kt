package com.cacaomovil.app.fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView

import com.cacaomovil.app.R
import com.cacaomovil.app.adapter.AdapterApps
import com.cacaomovil.app.models.AppsM
import java.util.ArrayList


class ListadoApps : Fragment(), View.OnClickListener {
    internal var context: Activity? = null
    internal lateinit var lstOptions: GridView
    internal var arraylist: MutableList<AppsM> = ArrayList<AppsM>()
    internal lateinit var adapter_apps: AdapterApps

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.listado_apps, container, false)
    }

    override fun onActivityCreated(state: Bundle?) {
        super.onActivityCreated(state)
        context = super.getActivity()
        inits()
    }

    fun finishListadoRutas() {
        context!!.finish()
    }

    private fun inits() {
        lstOptions = context!!.findViewById(R.id.app_gridview) as GridView


        adapter_apps = AdapterApps(context, arraylist)
        lstOptions.adapter = adapter_apps
        lstOptions.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->

        }
        arraylist.clear()
        arraylist.add(AppsM("1", "Cacao SMS", R.drawable.sms))
        arraylist.add(AppsM("2", "Cacao Móvil", R.drawable.movil))
        arraylist.add(AppsM("3", "Cacao Respuestas", R.drawable.respuestas))
        arraylist.add(AppsM("4", "Financiera", R.drawable.financiera))
        arraylist.add(AppsM("5", "Mapa de sabores", R.drawable.mapa))
        arraylist.add(AppsM("6", "Edgar", R.drawable.edgar))

        adapter_apps.notifyDataSetChanged()

    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }
}
