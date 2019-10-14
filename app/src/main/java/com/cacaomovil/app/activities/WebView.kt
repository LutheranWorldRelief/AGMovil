package com.cacaomovil.app.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.cacaomovil.app.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebView : AppCompatActivity() {

    private val BASE_URL = "https://developer.brianpalma.com/";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo

        /**
         * Verifico si tiene acceso a Internet y con que red se esta conectando
         */
        if(networkInfo != null && networkInfo.isConnected){
            if(networkInfo.type == ConnectivityManager.TYPE_WIFI){
                Toast.makeText(baseContext, "Connectado vía WIFI", Toast.LENGTH_SHORT).show()
            }
            if(networkInfo.type == ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(baseContext, "Connectado vía red Móvil", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(baseContext, "Sin conexión a Internet", Toast.LENGTH_SHORT).show()

            /**
             * Si no tiene acceso a Internet lo envío al HomeActividy
             */
            val intent = Intent(this.baseContext, HomeActivity::class.java)
            startActivity(intent)

            //this.finish()
        }
/*
        swipeRefresh.setOnRefreshListener {
            webView.reload();
        }
*/
        webView.webViewClient = object : WebViewClient(){

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false;
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                //swipeRefresh.isRefreshing = true;
            }

        }

        val setting = webView.settings
        setting.javaScriptEnabled = true
        setting.domStorageEnabled = true
        webView.loadUrl(BASE_URL)

    }

    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }else{
            super.onBackPressed()
        }
    }
}
