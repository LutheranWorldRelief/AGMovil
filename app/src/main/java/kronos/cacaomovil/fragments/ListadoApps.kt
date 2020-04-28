package kronos.cacaomovil.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kronos.cacaomovil.Constants
import kronos.cacaomovil.R
import kronos.cacaomovil.adapter.AdapterAppsList
import kronos.cacaomovil.adapter.AdapterCategorias
import kronos.cacaomovil.adapter.SimpleAdapter
import kronos.cacaomovil.database.AppsDB
import kronos.cacaomovil.models.AppsM
import kronos.cacaomovil.models.CategoriasM
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList


class ListadoApps : Fragment(), View.OnClickListener, AdapterCategorias.ItemClickListener {


    internal var context: Activity? = null
    //internal lateinit var lstOptions: GridView
    internal var arraylist: MutableList<AppsM> = ArrayList<AppsM>()
    internal lateinit var adapter_apps: SimpleAdapter
    lateinit var dialog: ACProgressFlower

    companion object {
        lateinit var layoutManager: LinearLayoutManager
        lateinit var recyclerApps: RecyclerView

    }


    lateinit var swipeRefreshLayoutApp: SwipeRefreshLayout
    internal var listCategorias: MutableList<CategoriasM> = ArrayList<CategoriasM>()
    internal var listCategoriasTop: MutableList<CategoriasM> = ArrayList<CategoriasM>()

    var categories:JSONArray = JSONArray()
    var posicionActual = 0
    lateinit var AppsData: AppsDB

    lateinit var adapter: AdapterCategorias
    lateinit var adapterApps: AdapterAppsList




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

        layoutManager = LinearLayoutManager(context)
        recyclerApps = context!!.findViewById(R.id.recyclerApps) as RecyclerView
        recyclerApps.layoutManager = layoutManager
        recyclerApps.itemAnimator = DefaultItemAnimator()

        //recyclerApps.setHasFixedSize(true);
        //recyclerApps.setLayoutManager(GridLayoutManager(context,3))




        // set up the RecyclerView
        var recyclerView:RecyclerView  = context!!.findViewById(R.id.rvCategorias) as RecyclerView
        var horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setLayoutManager(horizontalLayoutManager)
        adapter = AdapterCategorias(context!!, listCategoriasTop)
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        loadApps(true)

    }

    override fun onItemClick(view: View, position: Int) {
        if(position==0){
            rellenarDatos("upload", "",false)
        }else{
            rellenarDatos("upload",listCategoriasTop.get(position).nombre,false)
        }

        //System.out.println(posicionActual)

        listCategoriasTop.get(posicionActual).seleccionado = false
        listCategoriasTop.get(position).seleccionado = true
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
        var dataDB = data

        if(action.toString().equals("reset")){
            listCategorias.clear()
            listCategoriasTop.clear()
            listCategoriasTop.add(CategoriasM("1","Todas",true,""))
        }else if(action.toString().equals("upload")){
            listCategorias.clear()
        }

        arraylist.clear()


        if(categories.length()==0) dataDB = true

        if (dataDB == false){
            AppsData.eliminarTodo()

            var appsDestacadas: MutableList<AppsM> = ArrayList<AppsM>()

            for(i in 0..categories.length()-1) {
                val itemsCategoria = categories.getJSONObject(i)
                val appsJson = itemsCategoria.getJSONArray("apps")


                AppsData.insertar(itemsCategoria.getString("id"),itemsCategoria.getString("name"),itemsCategoria.getString("icon"),"",appsJson.toString())


                if(appsJson.length()!=0){

                    if(action.toString().equals("reset")){
                        //agregar todas las categorias
                        posicionActual = 0
                        var apps: MutableList<AppsM> = ArrayList<AppsM>()
                        var appsDestacadasRelleno: MutableList<AppsM> = ArrayList<AppsM>()

                        apps.clear()
                        appsDestacadasRelleno.clear()

                        for(j in 0..appsJson.length()-1) {
                            val itemsApps = appsJson.getJSONObject(j)
                            if(itemsApps.getBoolean("outstanding")==true){
                                appsDestacadas.add(AppsM(itemsApps.getString("id"), itemsApps.getString("name"), itemsApps.getString("image"), itemsApps.getString("app_type"), itemsApps.getString("app_url")))
                            }

                            apps.add(AppsM(itemsApps.getString("id"), itemsApps.getString("name"), itemsApps.getString("image"), itemsApps.getString("app_type"), itemsApps.getString("app_url")))

                        }


                        listCategorias.add(CategoriasM(itemsCategoria.getString("id"),itemsCategoria.getString("name"),false,itemsCategoria.getString("icon"),apps,appsDestacadasRelleno))
                        listCategoriasTop.add(CategoriasM(itemsCategoria.getString("id"),itemsCategoria.getString("name"),false,itemsCategoria.getString("icon"),apps,appsDestacadasRelleno))

                    }else if(action.toString().equals("upload")){
                        //agregar solo la categoria seleccionada
                        if(value.toString().equals("") || itemsCategoria.getString("name").toString().equals(value)){
                            var apps: MutableList<AppsM> = ArrayList<AppsM>()
                            var appsDestacadasRelleno: MutableList<AppsM> = ArrayList<AppsM>()

                            apps.clear()
                            appsDestacadasRelleno.clear()

                            for(j in 0..appsJson.length()-1) {
                                val itemsApps = appsJson.getJSONObject(j)
                                if(itemsApps.getBoolean("outstanding")==true){
                                    appsDestacadas.add(AppsM(itemsApps.getString("id"), itemsApps.getString("name"), itemsApps.getString("image"), itemsApps.getString("app_type"), itemsApps.getString("app_url")))
                                }

                                apps.add(AppsM(itemsApps.getString("id"), itemsApps.getString("name"), itemsApps.getString("image"), itemsApps.getString("app_type"), itemsApps.getString("app_url")))

                            }

                            var open = true
                            if(value.toString().equals("")){
                                open = false
                            }

                            listCategorias.add(CategoriasM(itemsCategoria.getString("id"),itemsCategoria.getString("name"),false,itemsCategoria.getString("icon"),apps,appsDestacadasRelleno,open))

                        }
                    }


                }



            }

            //agregar bloque de destacadas
            if(appsDestacadas.size!=0 && value.toString().equals("")){
                var appsRelleno: MutableList<AppsM> = ArrayList<AppsM>()
                listCategorias.add(0,CategoriasM("","Destacadas",false,"",appsRelleno,appsDestacadas))
            }

        }else{
            val cApps = AppsData.getApps()
            var appsDestacadas: MutableList<AppsM> = ArrayList<AppsM>()

            if (cApps.moveToFirst())
                do {
                    val appsDB = cApps.getString(cApps.getColumnIndex(AppsDB.apps))
                    val appsJson = JSONArray(appsDB)

                    val idCategoria = cApps.getString(cApps.getColumnIndex(AppsDB.idWeb))
                    val name = cApps.getString(cApps.getColumnIndex(AppsDB.name))
                    val icon = cApps.getString(cApps.getColumnIndex(AppsDB.icon))

                 if(appsJson.length()!=0){

                     if(action.toString().equals("reset")){
                         //agregar todas las categorias
                         posicionActual = 0
                         var apps: MutableList<AppsM> = ArrayList<AppsM>()
                         var appsDestacadasRelleno: MutableList<AppsM> = ArrayList<AppsM>()

                         apps.clear()
                         appsDestacadasRelleno.clear()

                         for(j in 0..appsJson.length()-1) {
                             val itemsApps = appsJson.getJSONObject(j)
                             if(itemsApps.getBoolean("outstanding")==true){
                                 appsDestacadas.add(AppsM(itemsApps.getString("id"), itemsApps.getString("name"), itemsApps.getString("image"), itemsApps.getString("app_type"), itemsApps.getString("app_url")))
                             }

                             apps.add(AppsM(itemsApps.getString("id"), itemsApps.getString("name"), itemsApps.getString("image"), itemsApps.getString("app_type"), itemsApps.getString("app_url")))

                         }


                         listCategorias.add(CategoriasM(idCategoria,name,false,icon,apps,appsDestacadasRelleno))
                         listCategoriasTop.add(CategoriasM(idCategoria,name,false,icon,apps,appsDestacadasRelleno))

                     }else if(action.toString().equals("upload")){
                         //agregar solo la categoria seleccionada
                         if(value.toString().equals("") || name.toString().equals(value)){
                             var apps: MutableList<AppsM> = ArrayList<AppsM>()
                             var appsDestacadasRelleno: MutableList<AppsM> = ArrayList<AppsM>()

                             apps.clear()
                             appsDestacadasRelleno.clear()

                             for(j in 0..appsJson.length()-1) {
                                 val itemsApps = appsJson.getJSONObject(j)
                                 if(itemsApps.getBoolean("outstanding")==true){
                                     appsDestacadas.add(AppsM(itemsApps.getString("id"), itemsApps.getString("name"), itemsApps.getString("image"), itemsApps.getString("app_type"), itemsApps.getString("app_url")))
                                 }
                                 apps.add(AppsM(itemsApps.getString("id"), itemsApps.getString("name"), itemsApps.getString("image"), itemsApps.getString("app_type"), itemsApps.getString("app_url")))

                             }


                             listCategorias.add(CategoriasM(idCategoria,name,false,icon,apps,appsDestacadasRelleno))

                         }
                     }


                 }

             } while (cApps.moveToNext())

            //agregar bloque de destacadas
            if(appsDestacadas.size!=0 && value.toString().equals("")){
                var appsRelleno: MutableList<AppsM> = ArrayList<AppsM>()
                listCategorias.add(0,CategoriasM("","Destacadas",false,"",appsRelleno,appsDestacadas))
            }
        }




        adapterApps = AdapterAppsList(context!!, listCategorias)
        recyclerApps.setAdapter(adapterApps)
        adapterApps.notifyDataSetChanged()
        adapter.notifyDataSetChanged()

    }

 override fun onClick(v: View) {
     when (v.id) {

     }
 }
}
