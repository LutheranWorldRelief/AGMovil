package kronos.cacaomovil.adapter

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kronos.cacaomovil.R
import kronos.cacaomovil.activities.ListadoGuias
import kronos.cacaomovil.activities.ListadoSesiones
import kronos.cacaomovil.models.GuiasM
import com.squareup.picasso.Picasso
import android.content.DialogInterface
import android.os.Build
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.net.Uri
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kronos.cacaomovil.Constants
import android.os.AsyncTask
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.widget.Toast
import co.metalab.asyncawait.async
import kronos.cacaomovil.common.DescargarArchivos
import kronos.cacaomovil.database.*
import kronos.cacaomovil.models.SesionesM
import org.json.JSONArray
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.hasPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class AdapterGuias(private val mContext: Activity, private val listDishes: List<GuiasM>) : RecyclerView.Adapter<AdapterGuias.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txName: TextView
        var txDescripcion: TextView? = null
        var imageGuia: ImageView? = null
        var archivo: ImageView? = null
        var tipo: ImageView?=null
        var rlCont: RelativeLayout?=null

        init {
            imageGuia = view.findViewById(R.id.imageGuia) as ImageView
            archivo = view.findViewById(R.id.archivo) as ImageView
            txName = view.findViewById(R.id.txName) as TextView
            txDescripcion = view.findViewById(R.id.txDescripcion) as TextView
            tipo = view.findViewById(R.id.tipo) as ImageView
            rlCont = view.findViewById(R.id.rlCont) as RelativeLayout

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.items_biblioteca, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemsGuias = listDishes[position]

        Picasso.get()
                .load(itemsGuias.image)
                .resize(120, 165)
                .into(holder.imageGuia);

        if(itemsGuias.descargado==true){
            Picasso.get()
                    .load(R.drawable.reload)
                    .into(holder.tipo)

            holder.archivo!!.visibility = View.VISIBLE

            if(itemsGuias.formato.toLowerCase().toString().equals("pdf")){
                Picasso.get()
                        .load(R.drawable.pdf)
                        .into(holder.archivo)
            }else if(itemsGuias.formato.toLowerCase().toString().equals("")){
                Picasso.get()
                        .load(R.drawable.epub)
                        .into(holder.archivo)
            }
        }else{
            Picasso.get()
                    .load(R.drawable.cloud_computing)
                    .into(holder.tipo)

            holder.archivo!!.visibility = View.GONE
        }

        holder.txName!!.setText(itemsGuias.name);

        holder.rlCont!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val i = Intent(mContext, ListadoSesiones::class.java)
                i.putExtra("id", itemsGuias.id)
                i.putExtra("name", itemsGuias.name)
                i.putExtra("image", itemsGuias.image)
                i.putExtra("categoria", itemsGuias.idCategoria)

                mContext.startActivity(i)
            }
        })

        holder.imageGuia!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val i = Intent(mContext, ListadoSesiones::class.java)
                i.putExtra("id", itemsGuias.id)
                i.putExtra("name", itemsGuias.name)
                i.putExtra("image", itemsGuias.image)
                i.putExtra("categoria", itemsGuias.idCategoria)

                mContext.startActivity(i)
            }
        })

        holder.archivo!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                val obtenerRegistro = ListadoGuias.ArchivosData.getArchivoByGuia(itemsGuias.id)
                var nombreArchivo = ""

                if(obtenerRegistro.count>0){
                    if (obtenerRegistro.moveToFirst())
                        do {
                            nombreArchivo = obtenerRegistro.getString(obtenerRegistro.getColumnIndex(ArchivosDB.name))
                        } while (obtenerRegistro.moveToNext())
                }

                if (!nombreArchivo.toString().equals("")){
                    var nuevaCarpeta = File(Environment.getExternalStorageDirectory(), "filesGuias");
                    nuevaCarpeta.mkdirs();

                    var file = File(nuevaCarpeta, nombreArchivo)
                    if (file.exists()) {
                        var path = Uri.fromFile(file)
                        //var path = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".kronos.cacaomovil.provider", file)

                        try {
                            var intent = Intent(Intent.ACTION_VIEW)

                            if(itemsGuias.formato.toLowerCase().toString().equals("pdf")){
                                intent.setDataAndType(path, "application/pdf")
                            }else if(itemsGuias.formato.toLowerCase().toString().equals("")){
                                intent.setDataAndType(path, "application/epub+zip")
                            }
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            ListadoGuias.context.startActivity(intent)
                            //finish();
                        } catch (ee: ActivityNotFoundException) {
                            if(itemsGuias.formato.toLowerCase().toString().equals("pdf")){
                                var toast1 = Toast.makeText(ListadoGuias.context,"La aplicación PDF Reader no está instalada en su dispositivo", Toast.LENGTH_SHORT);
                                toast1.show()
                            }else if(itemsGuias.formato.toLowerCase().toString().equals("")){
                                var toast1 = Toast.makeText(ListadoGuias.context,"Necesitas instalar una aplicacion para leer archivo Epub", Toast.LENGTH_SHORT);
                                toast1.show()
                            }

                        }
                    } else {
                        var toast1 = Toast.makeText(ListadoGuias.context,"Lo sentimos, el archivo no existe. Inténtelo descargándolo de nuevo", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }



            }
        })


        holder.tipo!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                var mensaje = "Estas a punto de descargar esta guía en ePub o PDF para poder compartirla con tus amigos, esto puede ocupar espacio en tu celular\n" +
                        "\n" +
                        "¿Deseas continuar?"
                if(itemsGuias.descargado==true){
                    mensaje = "Estas a punto de actualizar esta guía en ePub o PDF para poder compartirla con tus amigos, esto puede ocupar espacio en tu celular\n" +
                            "\n" +
                            "¿Deseas continuar?"
                }

                //val perms = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

                val perms = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

                if(!hasPermissions(mContext, *perms)){
                    ActivityCompat.requestPermissions(mContext, perms, 42);
                }else{
                    val builder: AlertDialog.Builder
                    builder = AlertDialog.Builder(mContext)
                    builder.setTitle("AG Movil")
                            .setMessage(mensaje)
                            .setPositiveButton("Si", DialogInterface.OnClickListener { dialog, which ->
                                val dialog = ProgressDialog.show(mContext, "","Cargando. Por favor espera...", true)

                                val nuevaCarpeta = File(Environment.getExternalStorageDirectory(), "filesGuias")
                                nuevaCarpeta.mkdirs()


                                if(itemsGuias.descargado==true){
                                    var urlArchivo = ListadoGuias.ArchivosData.getArchivoByGuia(ListadoGuias.listGuias[position].id)
                                    var nombreArchivo = ""
                                    if (urlArchivo.moveToFirst())
                                        do {
                                            nombreArchivo = urlArchivo.getString(urlArchivo.getColumnIndex(ArchivosDB.name))
                                        } while (urlArchivo.moveToNext())

                                    var file = File("/storage/emulated/0/filesGuias/"+nombreArchivo)
                                    if(file.exists()){
                                        file.delete()
                                    }
                                }

                                System.out.println(Constants.ARCHIVO_GUIA+itemsGuias.id)

                                var requestQueue = Volley.newRequestQueue(mContext)

                                val jsonObjRequestHome = object : StringRequest(
                                        Request.Method.GET,
                                        Constants.ARCHIVO_GUIA+itemsGuias.id,
                                        Response.Listener { response ->
                                            val res = JSONObject(response)

                                            val files= res.getJSONArray("files")


                                            for(i in 0..files.length()-1) {
                                                val filesItem = files.getJSONObject(i)
                                                var urlArchivo = filesItem.getString("url")
                                                var file_name = filesItem.getString("file_name")
                                                var file_type= filesItem.getString("file_type")
                                                DescargarArchivos(urlArchivo,file_name,file_type,itemsGuias.id,position,mContext)

                                                System.out.println(filesItem)
                                            }

                                            dialog.dismiss()


                                        }, Response.ErrorListener { error ->

                                    VolleyLog.d(Constants.TAG, "Error: " + error.message)

                                    dialog.dismiss()
                                }
                                ) {
                                }

                                // Añadir petición a la cola
                                requestQueue!!.add(jsonObjRequestHome)

                            })
                            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                                // do nothing
                            })
                            .setIcon(R.mipmap.ic_launcher)
                            .show()

                }


            }
        })




    }

    override fun getItemCount(): Int {
        return listDishes.size
    }

}
