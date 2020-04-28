package kronos.cacaomovil.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kronos.cacaomovil.R
import kronos.cacaomovil.adapter.AdapterBiblioteca
import kronos.cacaomovil.models.BibliotecaM



class ListadoBibliotecaAnt : Fragment(), View.OnClickListener {
    internal var context: Activity? = null
    internal var listBiblioteca: MutableList<BibliotecaM> = ArrayList<BibliotecaM>()
    private var adapter: AdapterBiblioteca? = null
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerBiblioteca: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.listado_biblioteca, container, false)
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
        listBiblioteca.clear()
        adapter = AdapterBiblioteca(this!!.context!!, listBiblioteca as ArrayList<BibliotecaM>)
        layoutManager = LinearLayoutManager(context)
        recyclerBiblioteca = context!!.findViewById(R.id.recyclerBiblioteca) as RecyclerView

        recyclerBiblioteca.layoutManager = layoutManager
        recyclerBiblioteca.itemAnimator = DefaultItemAnimator()
        recyclerBiblioteca.adapter = adapter


        //listBiblioteca.add(BibliotecaM("1", "Guía de género", "Some details. Nulla vitae elit libero, a pharetra augue. Aenean lacinia bibendum nu", R.drawable.guia1, 0))
        //listBiblioteca.add(BibliotecaM("1", "Guía de género 2", "Some details. Nulla vitae elit libero, a pharetra augue. Aenean lacinia bibendum nu", R.drawable.guia2, 1))

        adapter!!.notifyDataSetChanged()

    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }
}
