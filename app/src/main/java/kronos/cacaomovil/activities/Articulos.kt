package kronos.cacaomovil.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.articulos.*
import kronos.cacaomovil.Constants
import kronos.cacaomovil.R
import kronos.cacaomovil.database.ArticulosDB
import kronos.cacaomovil.materialShowCase.MaterialShowcaseSequence
import kronos.cacaomovil.materialShowCase.MaterialShowcaseView
import kronos.cacaomovil.materialShowCase.ViewTarget
import kronos.cacaomovil.models.ArticlesM
import org.json.JSONObject
import java.util.*


class Articulos : AppCompatActivity(), View.OnClickListener {

    //Defining Variables
    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    internal lateinit var context: Activity
    internal var id:String = ""
    lateinit var navigationView: NavigationView
    lateinit var dialog: ACProgressFlower
    internal var listArticles: MutableList<ArticlesM> = ArrayList<ArticlesM>()
    private var txTitle: TextView? = null
    var mWebView: WebView? = null
    lateinit var ArticulosData: ArticulosDB
    var linkShare = ""
    var escala = 0
    var fabB:FloatingActionButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.articulos)

        context = this@Articulos

        val extras = intent.extras
        if (extras != null) {
            id = extras.getString("id").toString()
        }

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        txTitle = findViewById<View>(R.id.txTitle) as TextView

        ArticulosData = ArticulosDB(context!!)


        fabB = findViewById<View>(R.id.fab) as FloatingActionButton
        fabB!!.setOnClickListener {
            drawerLayout?.openDrawer(Gravity.LEFT)
        }

        icons_left.setOnClickListener {
            finish()
        }

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        //supportActionBar!!.setHomeAsUpIndicator(R.drawable.icons_left)
        toolbar!!.setNavigationIcon(null)
        supportActionBar!!.setHomeAsUpIndicator(null)

        //Initializing NavigationView
        navigationView = findViewById<View>(R.id.navigationViewArticulos) as NavigationView

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // This method will trigger on item Click of navigation menu

            //Closing drawer on item click
            drawerLayout!!.closeDrawers()

            navigationView.menu.getItem(0).subMenu.getItem(0).setChecked(false)



            for(i in 0..listArticles.size-1) {
                val itemsArticles = listArticles.get(i)

                if(menuItem.title == itemsArticles.name){
                    txTitle!!.setText(itemsArticles.title)
                    loadWeb(itemsArticles.content)

                    if(!itemsArticles.link.toString().equals("")){
                        linkShare = itemsArticles.link
                        contShare.visibility = View.VISIBLE
                    }else{
                        linkShare = ""
                        contShare.visibility = View.GONE
                    }
                }
            }

            //txTitle!!.setText(itemsArticles.getString("name"))

            true
        }

        //addMenuItemInNavMenuDrawer()

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = findViewById<View>(R.id.drawerArticulos) as DrawerLayout
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, null, R.string.openDrawer, R.string.closeDrawer) {

            override fun onDrawerClosed(drawerView: View) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView)
            }
        }

        //Setting the actionbarToggle to drawer layout
        drawerLayout!!.setDrawerListener(actionBarDrawerToggle)

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState()


        mWebView = findViewById<View>(R.id.webview) as WebView
        mWebView!!.getSettings().setJavaScriptEnabled(true);
        mWebView!!.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //v1
        mWebView!!.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView!!.getSettings().setSupportMultipleWindows(true);
        mWebView!!.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView!!.getSettings().setBuiltInZoomControls(true);
        mWebView!!.getSettings().setSupportZoom(true);

        val settings = mWebView!!.settings
        settings.defaultTextEncodingName = "utf-8"

        loadArticulos()
        contShare.setOnClickListener(this)
        //share.setColorFilter(Color.parseColor("#B2BB1E"), android.graphics.PorterDuff.Mode.MULTIPLY)
        webMenos.setOnClickListener(this)
        webMas.setOnClickListener(this)
        escala = 0




    }


    fun show() {


        val sequence = MaterialShowcaseSequence(this, "1")

        val oneShowcaseView: MaterialShowcaseView = MaterialShowcaseView.Builder(this).setDismissText("Cerrar")
                .setTarget(findViewById<View>(R.id.fab), ViewTarget.CentreArea.CENTER).setContentText("Para ver más contenido da click aquí").build()

        sequence.addSequenceItem(oneShowcaseView)

        sequence.start()

    }

    public override fun onResume() {
        super.onResume()


    }

    fun loadArticulos() {

        dialog = ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading")
                .fadeColor(Color.DKGRAY).build()

        dialog.setCancelable(false)
        dialog.show()

        listArticles.clear()

        var requestQueue = Volley.newRequestQueue(context)

        //System.out.println(Constants.ARTICLES+id)
        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.ARTICLES+id,
                Response.Listener { response ->
                   cargarArticulos(response)

                    dialog.dismiss()
                    //System.out.println("response "+response)

                }, Response.ErrorListener { error ->
                    cargarArticulos("")
                    VolleyLog.d(Constants.TAG, "Error: " + error.message)
                    dialog.dismiss()
                }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)


    }

    private fun cargarArticulos(response: String?) {

        val menu = navigationView.menu
        val submenu = menu.addSubMenu("Artículo de la temática")


        if(!response.toString().equals("")){
            val res = JSONObject(response)
            var articles = res.getJSONArray("articles")

            for(i in 0..articles.length()-1) {
                val itemsArticles = articles.getJSONObject(i)
                if(i==0){
                    txTitle!!.setText(itemsArticles.getString("title"))
                    loadWeb(itemsArticles.getString("content"))
                    if(itemsArticles.has("link")){
                        linkShare = itemsArticles.getString("link")
                        contShare.visibility = View.VISIBLE
                    }else{
                        linkShare = ""
                        contShare.visibility = View.GONE
                    }
                    submenu.add(itemsArticles.getString("name")).setCheckable(true).setChecked(true)
                }else{
                    submenu.add(itemsArticles.getString("name")).setCheckable(true).setChecked(false)
                }

                var link = ""
                if(itemsArticles.has("link")){
                    link = itemsArticles.getString("link")
                }


                listArticles.add(ArticlesM(itemsArticles.getString("id"), itemsArticles.getString("name"), itemsArticles.getString("title"),itemsArticles.getString("description"), itemsArticles.getString("content"),link))


            }
        }else{
            var i = 0
            val cArticulos = ArticulosData.getArticulos(id)
            if (cArticulos.moveToFirst())
                do {

                    //no mostrar acceso agil
                    val idArticulo = cArticulos.getString(cArticulos.getColumnIndex(ArticulosDB.idWeb))
                    val name = cArticulos.getString(cArticulos.getColumnIndex(ArticulosDB.name))
                    val title = cArticulos.getString(cArticulos.getColumnIndex(ArticulosDB.title))
                    val descripcion = cArticulos.getString(cArticulos.getColumnIndex(ArticulosDB.descripcion))
                    val content = cArticulos.getString(cArticulos.getColumnIndex(ArticulosDB.content))

                    if(i==0){
                        txTitle!!.setText(title)
                        loadWeb(content)
                        submenu.add(name).setCheckable(true).setChecked(true)
                    }else{
                        submenu.add(name).setCheckable(true).setChecked(false)
                    }

                    listArticles.add(ArticlesM(idArticulo,name,title,descripcion,content,""))

                    i++

                } while (cArticulos.moveToNext())
        }

        navigationView.invalidate()

        if(listArticles.size > 1){
            fabB!!.show()
            show()
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.contShare ->{
                var i = Intent(Intent.ACTION_SEND)
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Cacao Móvil");
                i.putExtra(Intent.EXTRA_TEXT, linkShare);
                startActivity(Intent.createChooser(i, "Compartir"));
            }
            R.id.webMas ->{
                escala++
                mWebView!!.setInitialScale(escala)
            }
            R.id.webMenos ->{
                if(escala != 0){
                    escala--
                }
                mWebView!!.setInitialScale(escala)
            }
        }
    }

    fun loadWeb(contenido:String){

        val textoWeb = "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {" +
                "    font-family: MyFont;\n" +
                "    src: url(\"file:///android_asset/fonts/RobotoRegular.ttf\")" +
                "}" +
                "body {font-family: MyFont;font-size: medium;font-weight: 300;font-size: 16px;line-height: 21px;margin-left: 16px;margin-right: 16px;}" +
                ".nota_text2 {font-weight: 300;font-size: 16px;line-height: 21px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                contenido +
                "<br/><br/><br/></body>" +
                "</html>"
        mWebView!!.loadDataWithBaseURL(" https://admin.cacaomovil.com", textoWeb, "text/html", "UTF-8", null)

        //mWebView!!.loadData(textoWeb, "text/html; charset=utf-8", "UTF-8")
    }
}
