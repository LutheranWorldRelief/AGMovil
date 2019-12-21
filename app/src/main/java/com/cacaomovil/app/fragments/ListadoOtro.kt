package com.cacaomovil.app.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.cacaomovil.app.R
import com.cacaomovil.app.activities.WebView
import kotlinx.android.synthetic.main.listado_otro.*
import android.text.style.UnderlineSpan
import android.text.SpannableString
import android.view.Window
import android.widget.TextView
import android.widget.Toast

class ListadoOtro : Fragment(), View.OnClickListener {

    internal var context: Activity? = null
    internal lateinit var myDialog : Dialog
    internal lateinit var txt : TextView

    /**
     * URL Moodle
     */
    private val urlMoodle = "https://developer.brianpalma.com/";

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

    fun ShowDialog(){
        myDialog = Dialog(context)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setContentView(R.layout.activity_dialog)
        myDialog.setTitle("Aula virtual")

        txt = myDialog.findViewById<View>(R.id.abrirMoodleDialog) as TextView
        txt.isEnabled = true

        txt.setOnClickListener{
            //Toast.makeText(context, "Abriendo la aplicación de Moodle", Toast.LENGTH_SHORT).show()
            launchApp("com.moodle.moodlemobile")
            myDialog.cancel()
        }
        myDialog.show()
    }

    private fun inits() {

        //tabLayout = context!!.findViewById(R.id.tab_layout) as TabLayout
        val btnAbrirModalMoodle = abrirModalMoodle.findViewById<TextView>(R.id.abrirModalMoodle)
        btnAbrirModalMoodle.setOnClickListener(View.OnClickListener {

            if(verificaAppInstalada("com.moodle.moodlemobile")){
                //Si esta instalada la abro de una vez
                launchApp("com.moodle.moodlemobile")
            }else{
                //Muestro el modal
                ShowDialog()
            }

        })

        /**
         * Cuando doy clic en el titulo de Aula Virtual Cacaomovil, ingreso al enlace de la plataforma
         */
        val btnEnlaceAulaVirtual = enlaceAulaVirtual.findViewById<TextView>(R.id.enlaceAulaVirtual)
        btnEnlaceAulaVirtual.setOnClickListener(View.OnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("moodlemobile://" + urlMoodle)
            startActivity(i)
        })

        /**
         * Abro el webview cuando preciono el botón de Acceso Web
         */
        val btnAbriWebView = abrirWebView.findViewById<Button>(R.id.abrirWebView)
        btnAbriWebView.setOnClickListener(View.OnClickListener {
            val intent = Intent(this.context, WebView::class.java)
            startActivity(intent)
        })

        /**
         * Cuando preciono el boton de Acceso APP, verifico si la aplicación de Moodle se encuentra instalada
         * Si no se encuentra instalada lo llevo al Google Play a buscar la aplicación
         */
        /*
        val btnAbrirMoodle = abrirMoodle.findViewById<Button>(R.id.abrirMoodle)
        btnAbrirMoodle.setOnClickListener(View.OnClickListener {
            launchApp("com.moodle.moodlemobile")
        })
        */


    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }

    private fun launchApp(packageName: String) {
        // Instancia al PackageManager
        val pm = getContext()?.packageManager

        // Instancia a new Intent
        var intent:Intent? = pm?.getLaunchIntentForPackage(packageName)

        // Agrego la categoría intent
        intent?.addCategory(Intent.CATEGORY_LAUNCHER)

        // Si es distinto a null inicio la aplicación
        if(intent!=null){
            //getContext()?.startActivity(intent)

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("moodlemobile://" + urlMoodle)
            startActivity(i)

        }else{

            /**
             * De lo contrario voy al Google Play, a buscar la aplicación para instalarla
             */
            intent = Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            startActivity(intent);
        }
    }

    private fun verificaAppInstalada(packageName: String) : Boolean {

        val response:Boolean

        // Instancia al PackageManager
        val pm = getContext()?.packageManager

        // Instancia a new Intent
        var intent:Intent? = pm?.getLaunchIntentForPackage(packageName)

        // Agrego la categoría intent
        intent?.addCategory(Intent.CATEGORY_LAUNCHER)

        // Si es distinto a null inicio la aplicación
        if(intent!=null){
            response = true
        }else{
            response =  false
        }

        return response
    }


}

