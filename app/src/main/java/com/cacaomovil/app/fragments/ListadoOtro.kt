package com.cacaomovil.app.fragments

import android.app.Activity
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

        //tabLayout = context!!.findViewById(R.id.tab_layout) as TabLayout

        /**
         * Abro el webview con el evento click
         */
        val btnAbriWebView = abrirWebView.findViewById<Button>(R.id.abrirWebView)
        btnAbriWebView.setOnClickListener(View.OnClickListener {
            val intent = Intent(this.context, WebView::class.java)
            startActivity(intent)
        })

        val btnAbrirMoodle = abrirMoodle.findViewById<Button>(R.id.abrirMoodle)
        btnAbrirMoodle.setOnClickListener(View.OnClickListener {


            launchApp("com.moodle.moodlemobile")

        })
    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }

    private fun launchApp(packageName: String) {
        // Get an instance of PackageManager
        val pm = getContext()?.packageManager

        // Initialize a new Intent
        var intent:Intent? = pm?.getLaunchIntentForPackage(packageName)

        // Add category to intent
        intent?.addCategory(Intent.CATEGORY_LAUNCHER)

        // If intent is not null then launch the app
        if(intent!=null){
            getContext()?.startActivity(intent)
        }else{

            intent = Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            startActivity(intent);
        }
    }


}

