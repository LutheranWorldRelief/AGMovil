package kronos.cacaomovil.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kronos.cacaomovil.Constants

import kronos.cacaomovil.R
import kronos.cacaomovil.adapter.AdapterBiblioteca
import kronos.cacaomovil.database.CategoriasDB
import kronos.cacaomovil.models.BibliotecaM
import org.json.JSONObject
import java.util.ArrayList
import android.widget.Toast
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.Normalizer
import android.os.Handler
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class ListadoBiblioteca : Fragment(), View.OnClickListener {
    internal var context: Activity? = null
    internal var listBiblioteca: MutableList<BibliotecaM> = ArrayList<BibliotecaM>()
    private var adapter: AdapterBiblioteca? = null
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerBiblioteca: RecyclerView
    lateinit var dialogBiblioteca: ACProgressFlower
    lateinit var CategoriasData: CategoriasDB
    var palabraBuscar:String = ""
    private lateinit var Session: SharedPreferences
    internal var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.listado_biblioteca, container, false)
    }

    override fun onActivityCreated(state: Bundle?) {
        super.onActivityCreated(state)
        context = getActivity()
        inits()
    }

    fun finishListadoRutas() {
        context!!.finish()
    }

    private fun inits() {
        listBiblioteca.clear()
        CategoriasData = CategoriasDB(context!!)

        adapter = AdapterBiblioteca(this!!.context!!, listBiblioteca as ArrayList<BibliotecaM>)
        layoutManager = LinearLayoutManager(context)
        swipeRefreshLayout  = context!!.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener {
            loadCategorias(false)

        }

        recyclerBiblioteca = context!!.findViewById(R.id.recyclerBiblioteca) as RecyclerView

        recyclerBiblioteca.layoutManager = layoutManager
        recyclerBiblioteca.itemAnimator = DefaultItemAnimator()
        recyclerBiblioteca.adapter = adapter


        var dividerItemDecoration = DividerItemDecoration(recyclerBiblioteca.getContext(), layoutManager.getOrientation())
        recyclerBiblioteca.addItemDecoration(dividerItemDecoration)


        loadCategorias(true)

        val searchView = context!!.findViewById(R.id.etBuscarBiblioteca) as SearchView
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                //se oculta el EditText
                palabraBuscar = query
                //searchView.setQuery("hola", false)
                searchView.setIconified(true)

                adapter!!.filter.filter(query)
                Handler().postDelayed(Runnable {
                    //----------------------------
                    searchView.setQuery(palabraBuscar, false)
                    palabraBuscar = ""
                    //----------------------------
                }, 200) //5000 millisegundos = 5 segundos.
                //adapter!!.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                System.out.println("cambiar onQueryTextChange "+query)
                adapter!!.filter.filter(query)
                //adapter!!.notifyDataSetChanged()
                return false
            }
        })

        Session = context!!.getSharedPreferences("datosAG", Context.MODE_PRIVATE)
        if(Session!!.getBoolean("mostrarShow",true)){
            ShowDialogB()
        }

    }

    fun loadCategorias(load: Boolean) {
        if(load==true){
            dialogBiblioteca = ACProgressFlower.Builder(context)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("Loading")
                    .fadeColor(Color.DKGRAY).build()

            dialogBiblioteca.setCancelable(false)
            dialogBiblioteca.show()
        }


        var requestQueue = Volley.newRequestQueue(context)

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.CATEGORIES,
                Response.Listener { response ->

                    CategoriasData.eliminarTodo()

                    cargarDataCategorias(response)

                    if(load==true){
                        dialogBiblioteca.dismiss()
                    }else{
                        swipeRefreshLayout!!.isRefreshing = false
                    }
                    System.out.println("response "+response)

                }, Response.ErrorListener { error ->
                    cargarDataCategorias("")

                    VolleyLog.d(Constants.TAG, "Error: " + error.message)

                if(load==true){
                    dialogBiblioteca.dismiss()
                }else{
                    swipeRefreshLayout!!.isRefreshing = false
                }
        }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)


    }


    fun cargarDataCategorias(response: String) {

        listBiblioteca.clear()

        if(response.toString().equals("")){
            val cProd = CategoriasData.getCategorias()
            if (cProd.moveToFirst())
                do {


                    val idCategoria = cProd.getString(cProd.getColumnIndex(CategoriasDB.idWeb))
                    val name = cProd.getString(cProd.getColumnIndex(CategoriasDB.name))
                    val descripcion = cProd.getString(cProd.getColumnIndex(CategoriasDB.descripcion))
                    val image = cProd.getString(cProd.getColumnIndex(CategoriasDB.image))

                    listBiblioteca.add(BibliotecaM(idCategoria, name,descripcion,image,Constants.remove(name),Constants.remove(descripcion)))


                } while (cProd.moveToNext())
        }else{
            val res = JSONObject(response)
            var categories = res.getJSONArray("categories")

            for(i in 0..categories.length()-1) {
                val itemsCategoria = categories.getJSONObject(i)
                listBiblioteca.add(BibliotecaM(itemsCategoria.getString("id"), itemsCategoria.getString("name"), itemsCategoria.getString("description"),itemsCategoria.getString("image"),Constants.remove(itemsCategoria.getString("name")),Constants.remove(itemsCategoria.getString("description"))))
                CategoriasData.insertar(itemsCategoria.getString("id"), itemsCategoria.getString("name"), itemsCategoria.getString("image"),itemsCategoria.getString("description"))

            }
        }

        adapter!!.notifyDataSetChanged()
    }

    fun mostrarBusqueda(word:String){
        var listBibliotecaClone: MutableList<BibliotecaM> = ArrayList<BibliotecaM>()
        listBibliotecaClone.clear()
        listBibliotecaClone = listBiblioteca


    }



    override fun onClick(v: View) {
        when (v.id) {

        }
    }

    fun ShowDialogB(){
        var myDialog = Dialog(context!!)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setContentView(R.layout.activity_biblioteca_dialog)

        var textView = myDialog.findViewById<View>(R.id.textView) as TextView
        textView.text = "Para tener acceso al contenido de la biblioteca que te interesa leer cuando no tengas conexión a internet, debes abrir todos los temas que deseas guardar. Cuando recuperes conexión, podrás volver a ver todo el contenido."
        var check = myDialog.findViewById<View>(R.id.checkBox) as CheckBox

        var abrirMoodleDialog= myDialog.findViewById<View>(R.id.abrirMoodleDialog) as Button
        abrirMoodleDialog.setText("Aceptar")
        abrirMoodleDialog.setOnClickListener{
            if(check.isChecked){
                Session!!.edit().putBoolean("mostrarShow",false).commit()
            }
            myDialog.cancel()
        }

        myDialog.show()
    }
}
