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


class ListadoOtro : Fragment(), View.OnClickListener {
    internal var context: Activity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.listado_otro, container, false)
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


    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }
}
