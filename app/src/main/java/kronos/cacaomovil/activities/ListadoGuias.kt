package kronos.cacaomovil.activities

import android.Manifest
import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.gesture.GestureLibraries.fromFile
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.StrictMode
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
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
import kronos.cacaomovil.adapter.AdapterBiblioteca
import kronos.cacaomovil.adapter.AdapterGuias
import kronos.cacaomovil.adapter.SectionedGridRecyclerViewAdapter
import kronos.cacaomovil.adapter.SimpleAdapter
import kronos.cacaomovil.common.DescargarArchivos
import kronos.cacaomovil.database.ArchivosDB
import kronos.cacaomovil.database.GuiasDB
import kronos.cacaomovil.database.SesionesDB
import kronos.cacaomovil.fragments.HomeOpciones
import kronos.cacaomovil.models.AppsM
import kronos.cacaomovil.models.BibliotecaM
import kronos.cacaomovil.models.GuiasM
import kronos.cacaomovil.models.SesionesM
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.File.separator
import java.util.ArrayList


class ListadoGuias : AppCompatActivity(), View.OnClickListener{



    //Defining Variables
    private var toolbar: Toolbar? = null
    internal var id:String = ""
    internal var name:String = ""
    lateinit var dialog: ACProgressFlower
    lateinit var txGuias: TextView

    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerGuias: RecyclerView
    companion object {
        internal var listGuias: MutableList<GuiasM> = ArrayList<GuiasM>()
        private var adapter: AdapterGuias? = null
        internal lateinit var context: Activity
        lateinit var ArchivosData: ArchivosDB
    }

    lateinit var GuiasData: GuiasDB
    private val WRITE_REQUEST_CODE = 300
    var simpleProgressBar:ProgressBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listado_guias)

        context = this@ListadoGuias

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val extras = intent.extras
        if (extras != null) {
            id = extras.getString("id")
            name = extras.getString("name")
        }

        System.out.println("id "+id)


        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        txGuias = findViewById<View>(R.id.txGuias) as TextView


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        listGuias.clear()
        adapter = AdapterGuias(context!!, listGuias)
        layoutManager = LinearLayoutManager(context)
        recyclerGuias = context!!.findViewById(R.id.recyclerGuias) as RecyclerView

        recyclerGuias.layoutManager = layoutManager
        recyclerGuias.itemAnimator = DefaultItemAnimator()
        recyclerGuias.adapter = adapter

        loadGuias()



    }



    object recargar {
        fun recargaFila(posicion: Int,file_type:String) {
            listGuias[posicion].descargado = true
            listGuias[posicion].formato = file_type
            adapter!!.notifyDataSetChanged()


            val builder: AlertDialog.Builder
            builder = AlertDialog.Builder(context)
            builder.setTitle("AG Movil")
                    .setMessage("¿Desea abrir el archivo?")
                    .setPositiveButton("Si", DialogInterface.OnClickListener { dialog, which ->

                            var urlArchivo = ArchivosData.getArchivoByGuia(listGuias[posicion].id)
                            var nombreArchivo = ""
                            if (urlArchivo.moveToFirst())
                            do {
                                nombreArchivo = urlArchivo.getString(urlArchivo.getColumnIndex(ArchivosDB.name))
                            } while (urlArchivo.moveToNext())



                            val separated = nombreArchivo.split(".")
                            var formato = ""
                            if (separated.size > 0) {
                                formato = separated[separated.size-1];
                            }

                            listGuias[posicion].formato = formato

                            var nuevaCarpeta = File(Environment.getExternalStorageDirectory(), "filesGuias");
                            nuevaCarpeta.mkdirs();

                            var file = File(nuevaCarpeta, nombreArchivo)
                            if (file.exists()) {
                                var path = Uri.fromFile(file)
                                try {
                                    var intent = Intent(Intent.ACTION_VIEW)
                                    if(formato.toLowerCase().toString().equals("pdf")){
                                        intent.setDataAndType(path, "application/pdf")
                                    }else if(formato.toLowerCase().toString().equals("")){
                                        intent.setDataAndType(path, "application/epub+zip")
                                    }
                                    //intent.setDataAndType(path, "application/pdf");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context!!.startActivity(intent);
                                    //finish();
                                } catch (e: ActivityNotFoundException) {

                                    if(formato.toLowerCase().toString().equals("pdf")){
                                        var toast1 = Toast.makeText(ListadoGuias.context,"La aplicación PDF Reader no está instalada en su dispositivo", Toast.LENGTH_SHORT);
                                        toast1.show()
                                    }else if(formato.toLowerCase().toString().equals("")){
                                        var toast1 = Toast.makeText(ListadoGuias.context,"Necesitas instalar una aplicacion para leer archivo Epub", Toast.LENGTH_SHORT);
                                        toast1.show()
                                    }


                                }
                            } else {
                                var toast1 = Toast.makeText(context,"Lo sentimos, el archivo no existe. Inténtelo descargándolo de nuevo", Toast.LENGTH_SHORT);
                                toast1.show();
                            }


                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                        // do nothing
                    })
                    .setIcon(R.mipmap.ic_launcher)
                    .show()
        }
    }

    fun loadGuias() {
        dialog = ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading")
                .fadeColor(Color.DKGRAY).build()

        dialog.setCancelable(false)
        dialog.show()

        GuiasData = GuiasDB(context!!)
        ArchivosData = ArchivosDB(context!!)
        var requestQueue = Volley.newRequestQueue(context)

        System.out.println("url seleccionar "+Constants.GUIDES+id)

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.GUIDES+id,
                Response.Listener { response ->

                    cargarGuias(response)

                    dialog.dismiss()

                }, Response.ErrorListener { error ->
                    cargarGuias("")

            VolleyLog.d(Constants.TAG, "Error: " + error.message)
            dialog.dismiss()
        }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)


    }




    private fun cargarGuias(response: String?) {

        txGuias.setText(name)

        if(!response.toString().equals("")){

            val res = JSONObject(response)
            //val category = res.getJSONObject("category")
            val guides= res.getJSONArray("guides")


            for(i in 0..guides.length()-1) {
                val itemsGuias = guides.getJSONObject(i)

                val obtenerRegistro = ArchivosData.getArchivoByGuia(itemsGuias.getString("id"))
                var descargado = false
                var nombreArchivo = ""


                if(obtenerRegistro.count>0){
                    descargado = true
                    if (obtenerRegistro.moveToFirst())
                        do {
                            nombreArchivo = obtenerRegistro.getString(obtenerRegistro.getColumnIndex(ArchivosDB.name))
                        } while (obtenerRegistro.moveToNext())
                }

                val separated = nombreArchivo.split(".")
                var formato = ""
                if (separated.size > 0) {
                    formato = separated[separated.size-1];
                }

                listGuias.add(GuiasM(itemsGuias.getString("id"), itemsGuias.getString("name"), itemsGuias.getString("image"),descargado,id,formato))
            }
        }else{
            val cGuias = GuiasData.getGuiasByCategoria(id)
            if (cGuias.moveToFirst())
                do {

                    //no mostrar acceso agil
                    val idGuia = cGuias.getString(cGuias.getColumnIndex(GuiasDB.idWeb))
                    val name = cGuias.getString(cGuias.getColumnIndex(GuiasDB.name))
                    val image = cGuias.getString(cGuias.getColumnIndex(GuiasDB.image))

                    val obtenerRegistro = ArchivosData.getArchivoByGuia(idGuia)
                    var descargado = false
                    var nombreArchivo = ""

                    if(obtenerRegistro.count>0){
                        descargado = true
                        if (obtenerRegistro.moveToFirst())
                            do {
                                nombreArchivo = obtenerRegistro.getString(obtenerRegistro.getColumnIndex(ArchivosDB.name))
                            } while (obtenerRegistro.moveToNext())
                    }

                    val separated = nombreArchivo.split(".")
                    var formato = ""
                    if (separated.size > 0) {
                        formato = separated[separated.size-1];
                    }

                    listGuias.add(GuiasM(idGuia, name, image,descargado,id,formato))


                } while (cGuias.moveToNext())
        }

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