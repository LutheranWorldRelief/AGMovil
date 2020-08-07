package kronos.cacaomovil.activities

import android.app.Activity
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kronos.cacaomovil.Constants
import kronos.cacaomovil.R
import kronos.cacaomovil.adapter.*
import kronos.cacaomovil.database.ArticulosDB
import kronos.cacaomovil.database.GuiasDB
import kronos.cacaomovil.database.SesionesDB
import kronos.cacaomovil.fragments.HomeOpciones
import kronos.cacaomovil.models.AppsM
import kronos.cacaomovil.models.BibliotecaM
import kronos.cacaomovil.models.GuiasM
import kronos.cacaomovil.models.SesionesM
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.util.ArrayList


class ListadoSesiones : AppCompatActivity(), View.OnClickListener {

    //Defining Variables
    private var toolbar: Toolbar? = null
    internal lateinit var context: Activity
    internal var id:String = ""
    internal var name:String = ""
    internal var image:String = ""
    internal var categoria:String = ""

    lateinit var dialog: ACProgressFlower
    lateinit var txGuias: TextView
    lateinit var portada:ImageView

    private var adapter: AdapterSesiones? = null
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerGuias: RecyclerView
    internal var listSesiones: MutableList<SesionesM> = ArrayList<SesionesM>()
    lateinit var SesionesData: SesionesDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listado_sesiones)

        context = this@ListadoSesiones

        val extras = intent.extras
        if (extras != null) {
            id = extras.getString("id").toString()
            name = extras.getString("name").toString()
            image = extras.getString("image").toString()
            categoria = extras.getString("categoria").toString()
        }

        SesionesData = SesionesDB(context!!)



        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        portada = findViewById<View>(R.id.portada) as ImageView

        setSupportActionBar(toolbar)

        txGuias = findViewById<View>(R.id.txGuias) as TextView


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        listSesiones.clear()
        adapter = AdapterSesiones(this!!.context!!, listSesiones)
        layoutManager = LinearLayoutManager(context)
        recyclerGuias = context!!.findViewById(R.id.recyclerGuias) as RecyclerView

        recyclerGuias.layoutManager = layoutManager
        recyclerGuias.itemAnimator = DefaultItemAnimator()
        recyclerGuias.adapter = adapter

        var dividerItemDecoration = DividerItemDecoration(recyclerGuias.getContext(), layoutManager.getOrientation())
        recyclerGuias.addItemDecoration(dividerItemDecoration)

        loadGuias()
        guardarGuia()
    }

    private fun guardarGuia() {
        val urlCall = String.format(Constants.GUIA_COMPLETA, id)

        var requestQueue = Volley.newRequestQueue(context)

        System.out.println("urlCall $urlCall")

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                urlCall,
                Response.Listener { response ->

                    val res = JSONObject(response)
                    //System.out.println("terminooo " + res)
                    var GuiasData = GuiasDB(context)
                    var SesionesData = SesionesDB(context)
                    var ArticulosData = ArticulosDB(context)


                    if (res.has("guide")) {
                        System.out.println("entro a guias")
                        var itemsG = res.getJSONObject("guide")
                        //for(i in 0..guides.length()-1) {
                            //val itemsG = guides.getJSONObject(i)

                            var archive = "false"
                            if(itemsG.has("archive")){
                                archive = itemsG.getString("archive")
                            }
                            GuiasData.eliminar(itemsG.getString("id"))
                            GuiasData.insertar(itemsG.getString("id"),itemsG.getString("name"),itemsG.getString("image"),categoria,itemsG.getString("order"),archive)


                            val sections = itemsG.getJSONArray("sections")
                            for(j in 0..sections.length()-1) {
                                val itemsS = sections.getJSONObject(j)
                                SesionesData.eliminar(itemsS.getString("id"))
                                SesionesData.insertar(itemsS.getString("id"),itemsS.getString("name"),id)

                                if (itemsS.has("articles")) {
                                    val articles = itemsS.getJSONArray("articles")
                                    for(k in 0..articles.length()-1) {
                                        val itemsA = articles.getJSONObject(k)
                                        //System.out.println("itemsA $itemsA")
                                        ArticulosData.eliminar(itemsA.getString("id"))
                                        ArticulosData.insertar(itemsA.getString("id"),itemsA.getString("name"),itemsA.getString("title"),itemsA.getString("description"),itemsA.getString("content"),itemsS.getString("id"))
                                    }
                                }

                            }

                       // }
                    }


                }, Response.ErrorListener { error ->

                    //VolleyLog.d("aqui es el error", "Error: " + error)
                    //System.out.println("aqui es el errro ${error.message}")

                }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)
    }


    fun loadGuias() {
        dialog = ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading")
                .fadeColor(Color.DKGRAY).build()

        dialog.setCancelable(false)
        dialog.show()


        var requestQueue = Volley.newRequestQueue(context)

        System.out.println("Constants "+Constants.SECTIONS+id)

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.SECTIONS+id,
                Response.Listener { response ->

                    cargarSesiones(response)

                    dialog.dismiss()


                }, Response.ErrorListener { error ->
                    cargarSesiones("")
                    dialog.dismiss()
        }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)


    }

    private fun cargarSesiones(response: String?) {


        txGuias.setText(name)

        Picasso.get()
                .load(image)
                .into(portada)

        if(!response.toString().equals("")){
            val res = JSONObject(response)

            val sections= res.getJSONArray("sections")

            for(i in 0..sections.length()-1) {
                val itemsSesiones = sections.getJSONObject(i)
                listSesiones.add(SesionesM(itemsSesiones.getString("id"), itemsSesiones.getString("name")))
            }
        }else{
            val cSesiones = SesionesData.getSecciones(id)
            if (cSesiones.moveToFirst())
                do {

                    //no mostrar acceso agil
                    val idSesion = cSesiones.getString(cSesiones.getColumnIndex(SesionesDB.idWeb))
                    val nameSesion = cSesiones.getString(cSesiones.getColumnIndex(SesionesDB.name))

                    listSesiones.add(SesionesM(idSesion,nameSesion))


                } while (cSesiones.moveToNext())
        }


        //listSesiones.sortedBy { myObject -> myObject.orden }

        adapter!!.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
    }

    public override fun onResume() {
        super.onResume()
    }


    override fun onClick(v: View) {

    }
}
