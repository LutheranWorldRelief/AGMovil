package kronos.cacaomovil.activities

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.paillapp.app.adapter.AdapterNotificacion
import com.squareup.picasso.Picasso
import kronos.cacaomovil.Constants
import kronos.cacaomovil.R
import kronos.cacaomovil.fragments.HomeOpciones
import kronos.cacaomovil.models.NotificacionM
import org.json.JSONObject


class HomeActivity : AppCompatActivity(), View.OnClickListener {

    //Defining Variables
    private var toolbar: Toolbar? = null
    private var imagBell: ImageView? = null
    private var drawerLayout: DrawerLayout? = null
    internal var IDU: String? = null
    internal lateinit var context: Activity
    var img: ImageView? = null
    var txUno: TextView? = null
    var txDos: TextView? = null
    var txTres: TextView? = null
    internal var adapterP: AdapterNotificacion? = null
    internal var listNotificacion: MutableList<NotificacionM> = ArrayList<NotificacionM>()
    var navigationDrawerList: RecyclerView? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_main)

        context = this@HomeActivity

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        imagBell = findViewById<View>(R.id.imagBell) as ImageView
        img = findViewById<View>(R.id.img) as ImageView
        txUno = findViewById<View>(R.id.txUno) as TextView
        txDos = findViewById<View>(R.id.txDos) as TextView
        txTres = findViewById<View>(R.id.txTres) as TextView
        navigationDrawerList = findViewById<View>(R.id.navigation_drawer_list) as RecyclerView
        swipeRefreshLayout = findViewById<View>(R.id.swipeContainer) as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener { // Esto se ejecuta cada vez que se realiza el gesto
            actualizarMenuLateral()
        }

        adapterP = AdapterNotificacion(context, listNotificacion)

        listNotificacion.add(NotificacionM("1","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno"))
        listNotificacion.add(NotificacionM("1","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno"))
        listNotificacion.add(NotificacionM("1","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno"))
        listNotificacion.add(NotificacionM("1","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno"))
        listNotificacion.add(NotificacionM("1","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno"))
        listNotificacion.add(NotificacionM("1","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno"))
        listNotificacion.add(NotificacionM("1","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno"))

        navigationDrawerList!!.setAdapter(adapterP)
        navigationDrawerList!!.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        setSupportActionBar(toolbar)


        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        Handler().postDelayed(Runnable {
            //----------------------------
            refresh()
            //----------------------------
        }, 2000)


        //Initializing NavigationView
        /*navigationView = findViewById<View>(R.id.navigation_view) as NavigationView

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // This method will trigger on item Click of navigation menu
            //Checking if the item is in checked state or not, if not make it in checked state
            if (menuItem.isChecked)
                menuItem.isChecked = false
            else
                menuItem.isChecked = true

            //Closing drawer on item click
            drawerLayout!!.closeDrawers()

            //Check to see which item was being clicked and perform appropriate action
            when (menuItem.itemId) {


                //Replacing the main content with ContentFragment Which is our Inbox View;
                R.id.inbox ->
                    //Toast.makeText(getApplicationContext(),"Inicio",Toast.LENGTH_SHORT).show();
                    /*HomeNoticias fragment = new HomeNoticias();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,fragment);
                        fragmentTransaction.commit();*/
                    true
                else -> {
                    Toast.makeText(applicationContext, "Funcion en proceso", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }*/


        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = findViewById<View>(R.id.drawer) as DrawerLayout
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

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
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false)

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState()


        imagBell!!.setOnClickListener {
            drawerLayout!!.openDrawer(GravityCompat.END)
        }

        /*val headerLayout = navigationView.inflateHeaderView(R.layout.header)*/

        val fragment = HomeOpciones()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commitAllowingStateLoss()

    }

    public override fun onResume() {
        super.onResume()
        actualizarMenuLateral()

    }


    override fun onClick(v: View) {

    }

    companion object {
        lateinit var navigationView: NavigationView
    }

    fun refresh() {
        val rotation: Animation = AnimationUtils.loadAnimation(context, R.anim.clockwise_refresh)
        rotation.repeatCount = 10
        imagBell!!.startAnimation(rotation)
    }

    fun actualizarMenuLateral(){
        var requestQueue = Volley.newRequestQueue(context)

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.PRICE,
                Response.Listener { response ->
                    val res = JSONObject(response)
                    swipeRefreshLayout!!.setRefreshing(false)

                    var pc = res.getString("pc")
                    txUno!!.setText(res.getString("cocoa_price"))
                    txDos!!.setText(pc)
                    txTres!!.setText("(${res.getString("pcp")})")

                    if(pc.startsWith("+")){
                        txDos!!.setTextColor(Color.parseColor("#01B516"))
                        txTres!!.setTextColor(Color.parseColor("#01B516"))
                        img!!.visibility = View.VISIBLE

                        Picasso.get()
                                .load(R.drawable.arriba)
                                .into(img)
                    }else if(pc.startsWith("-")){
                        txDos!!.setTextColor(Color.parseColor("#ff0000"))
                        txTres!!.setTextColor(Color.parseColor("#ff0000"))
                        img!!.visibility = View.VISIBLE

                        Picasso.get()
                                .load(R.drawable.abajo)
                                .into(img)
                    }else{
                        txDos!!.setTextColor(Color.parseColor("#000000"))
                        txTres!!.setTextColor(Color.parseColor("#000000"))
                        img!!.visibility = View.INVISIBLE
                    }

                }, Response.ErrorListener { error ->
                    VolleyLog.d(Constants.TAG, "Error: " + error.message)
                }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)
    }
}
