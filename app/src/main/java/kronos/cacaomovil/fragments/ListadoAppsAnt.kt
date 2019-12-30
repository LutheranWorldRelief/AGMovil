package kronos.cacaomovil.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kronos.cacaomovil.Constants

import kronos.cacaomovil.R
import kronos.cacaomovil.activities.HomeActivity
import kronos.cacaomovil.adapter.AdapterCategorias
import kronos.cacaomovil.adapter.SectionedGridRecyclerViewAdapter
import kronos.cacaomovil.adapter.SimpleAdapter
import kronos.cacaomovil.database.AppsDB
import kronos.cacaomovil.database.CategoriasDB
import kronos.cacaomovil.models.AppsM
import kronos.cacaomovil.models.BibliotecaM
import kronos.cacaomovil.models.CategoriasM
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList


class ListadoAppsAnt : Fragment(), View.OnClickListener, AdapterCategorias.ItemClickListener {


    internal var context: Activity? = null
    //internal lateinit var lstOptions: GridView
    internal var arraylist: MutableList<AppsM> = ArrayList<AppsM>()
    internal lateinit var adapter_apps: SimpleAdapter
    lateinit var dialog: ACProgressFlower
    lateinit var recyclerApps: RecyclerView
    lateinit var swipeRefreshLayoutApp: SwipeRefreshLayout
    internal var listCategorias: MutableList<CategoriasM> = ArrayList<CategoriasM>()
    lateinit var categories:JSONArray
    var posicionActual = 0
    lateinit var AppsData: AppsDB

    lateinit var adapter: AdapterCategorias


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
        swipeRefreshLayoutApp = context!!.findViewById(R.id.swipeRefreshLayoutApp) as SwipeRefreshLayout
        swipeRefreshLayoutApp.setColorSchemeResources(
                R.color.colorPrimaryDark,
                R.color.colorPrimary)

        swipeRefreshLayoutApp.setOnRefreshListener {
            // Initialize a new Runnable
            loadApps(false)
        }

        AppsData = AppsDB(context!!)


        recyclerApps = context!!.findViewById(R.id.recyclerApps) as RecyclerView
        recyclerApps.setHasFixedSize(true);
        recyclerApps.setLayoutManager(GridLayoutManager(context,3))




        // set up the RecyclerView
        var recyclerView:RecyclerView  = context!!.findViewById(R.id.rvCategorias) as RecyclerView
        var horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setLayoutManager(horizontalLayoutManager)
        adapter = AdapterCategorias(context!!, listCategorias)
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        loadApps(true)

    }

    override fun onItemClick(view: View, position: Int) {
        if(position==0){
            rellenarDatos("upload", "",false)
        }else{
            rellenarDatos("upload",listCategorias.get(position).nombre,false)
        }

        listCategorias.get(posicionActual).seleccionado = false
        listCategorias.get(position).seleccionado = true
        posicionActual = position
        adapter.notifyDataSetChanged()


        //Toast.makeText(context, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
    }

    fun loadApps(load: Boolean) {
        if(load==true){
            dialog = ACProgressFlower.Builder(context)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("Loading")
                    .fadeColor(Color.DKGRAY).build()

            dialog.setCancelable(false)
            dialog.show()
        }


        var requestQueue = Volley.newRequestQueue(context)

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.APPS,
                Response.Listener { response ->
                    val res = JSONObject(response)
                    categories = res.getJSONArray("categories")

                    rellenarDatos("reset", "",false)

                    if(load==true){
                        dialog.dismiss()
                    }else{
                        swipeRefreshLayoutApp.isRefreshing = false
                    }

                }, Response.ErrorListener { error ->

                    rellenarDatos("reset", "",true)

            VolleyLog.d(Constants.TAG, "Error: " + error.message)
            if(load==true){
                dialog.dismiss()
            }else{
                swipeRefreshLayoutApp.isRefreshing = false
            }
        }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)


    }

    private fun rellenarDatos(action: String, value: String,data: Boolean) {

        if(action.toString().equals("reset")){
            listCategorias.clear()
            //listCategorias.add(CategoriasM("1","Todas",true,""))
        }


        var sections =  ArrayList<SectionedGridRecyclerViewAdapter.Section>()
        var posicion = 0
        arraylist.clear()

        if (data == false){
            AppsData.eliminarTodo()
        }else{
            /*val cApps = AppsData.getApps()
            if (cApps.moveToFirst())
                do {


                    val idCategoria = cProd.getString(cProd.getColumnIndex(CategoriasDB.idWeb))
                    val name = cProd.getString(cProd.getColumnIndex(CategoriasDB.name))
                    val descripcion = cProd.getString(cProd.getColumnIndex(CategoriasDB.descripcion))
                    val image = cProd.getString(cProd.getColumnIndex(CategoriasDB.image))

                    listBiblioteca.add(BibliotecaM(idCategoria, name,descripcion,image,Constants.remove(name)))


                } while (cApps.moveToNext())*/
        }


        for(i in 0..categories.length()-1) {
            val itemsCategoria = categories.getJSONObject(i)
            val apps = itemsCategoria.getJSONArray("apps")

            if(apps.length()!=0){
                sections.add(SectionedGridRecyclerViewAdapter.Section(posicion,itemsCategoria.getString("name")))

                if(action.toString().equals("reset")){
                   // listCategorias.add(CategoriasM(itemsCategoria.getString("id"),itemsCategoria.getString("name"),false,itemsCategoria.getString("icon")))
                }

            }


            /*if(itemsCategoria.getString("id").toString().equals("1")){
                sections.add(SectionedGridRecyclerViewAdapter.Section(posicion,itemsCategoria.getString("name")))
            }else{
                if(action.toString().equals("reset")){
                    listCategorias.add(CategoriasM(itemsCategoria.getString("id"),itemsCategoria.getString("name"),false,itemsCategoria.getString("icon")))
                }
                if(sections.size==1){
                    //agregar sesion vacia donde iran las demas apps
                    sections.add(SectionedGridRecyclerViewAdapter.Section(posicion,""))
                }
            }*/

            //agregar siempre la categoria de destacados
            if(itemsCategoria.getString("id").toString().equals("1")){
                val apps = itemsCategoria.getJSONArray("apps")

                for(j in 0..apps.length()-1) {
                    val itemsApps = apps.getJSONObject(j)
                    arraylist.add(AppsM(itemsApps.getString("id"), itemsApps.getString("name"), itemsApps.getString("image"), itemsApps.getString("app_type"), itemsApps.getString("app_url")))
                }
                posicion = posicion + apps.length()
            }else if(action.toString().equals("reset")){
                //agregar todas las categorias

                val apps = itemsCategoria.getJSONArray("apps")

                for(j in 0..apps.length()-1) {
                    val itemsApps = apps.getJSONObject(j)
                    arraylist.add(AppsM(itemsApps.getString("id"), itemsApps.getString("name"), itemsApps.getString("image"), itemsApps.getString("app_type"), itemsApps.getString("app_url")))
                }
                posicion = posicion + apps.length()
            }else if(action.toString().equals("upload")){
                //agregar solo la categoria seleccionada
                if(value.toString().equals("") || itemsCategoria.getString("name").toString().equals(value)){
                    val apps = itemsCategoria.getJSONArray("apps")

                    for(j in 0..apps.length()-1) {
                        val itemsApps = apps.getJSONObject(j)
                        arraylist.add(AppsM(itemsApps.getString("id"), itemsApps.getString("name"), itemsApps.getString("image"), itemsApps.getString("app_type"), itemsApps.getString("app_url")))
                    }
                    posicion = posicion + apps.length()
                }
            }


        }

        if(action.toString().equals("reset")){
            adapter.notifyDataSetChanged()
        }

        adapter_apps = SimpleAdapter(getActivity(),arraylist)

        System.out.println("arraylist "+arraylist.size)

        //Add your adapter to the sectionAdapter
        var dummy = arrayOfNulls<SectionedGridRecyclerViewAdapter.Section>(sections.size)


        try {
            var mSectionedAdapter = SectionedGridRecyclerViewAdapter(getActivity(),R.layout.section,R.id.section_text,recyclerApps,adapter_apps)
            mSectionedAdapter.setSections(sections.toArray(dummy))

            //Apply this adapter to the RecyclerView
            recyclerApps.setAdapter(mSectionedAdapter)
        } catch (e: Exception ) {
            context!!.finishAffinity()
            val selectStation = Intent(context!!, HomeActivity::class.java)
            context!!.startActivity(selectStation)
        }


    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }
}
