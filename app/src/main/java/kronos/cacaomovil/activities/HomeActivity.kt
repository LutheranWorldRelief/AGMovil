package kronos.cacaomovil.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.text.Html
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
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
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.paillapp.app.adapter.AdapterNotificacion
import com.squareup.picasso.Picasso
import kronos.cacaomovil.Constants
import kronos.cacaomovil.R
import kronos.cacaomovil.database.PrecioDB
import kronos.cacaomovil.fragments.HomeOpciones
import kronos.cacaomovil.models.NotificacionM
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener


class HomeActivity : AppCompatActivity(), View.OnClickListener {

    //Defining Variables
    private var toolbar: Toolbar? = null
    private var imagBell: ImageView? = null
    private var txNotificacion: TextView? = null
    private var drawerLayout: DrawerLayout? = null
    internal var IDU: String? = null
    internal lateinit var context: Activity
    var img: ImageView? = null
    var txUno: TextView? = null
    var txDos: TextView? = null
    var txFuente: TextView? = null
    var txTres: TextView? = null
    internal var adapterP: AdapterNotificacion? = null
    internal var listNotificacion: MutableList<NotificacionM> = ArrayList<NotificacionM>()
    var navigationDrawerList: RecyclerView? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var btMas:Button? = null
    var txNoRed:TextView? = null


    lateinit var PrecioData: PrecioDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_main)

        context = this@HomeActivity

        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        PrecioData = PrecioDB(context!!)

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        imagBell = findViewById<View>(R.id.imagBell) as ImageView
        txNotificacion = findViewById<View>(R.id.txNotificacion) as TextView
        img = findViewById<View>(R.id.img) as ImageView
        txUno = findViewById<View>(R.id.txUno) as TextView
        txFuente = findViewById<View>(R.id.txFuente) as TextView
        txFuente!!.setOnClickListener(this)
        txFuente!!.setText(Html.fromHtml("<u>es.investing.com</u>"))

        txDos = findViewById<View>(R.id.txDos) as TextView
        txTres = findViewById<View>(R.id.txTres) as TextView
        navigationDrawerList = findViewById<View>(R.id.navigation_drawer_list) as RecyclerView
        swipeRefreshLayout = findViewById<View>(R.id.swipeContainer) as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener { // Esto se ejecuta cada vez que se realiza el gesto
            actualizarMenuLateral()
        }

        btMas = findViewById<View>(R.id.btMas) as Button
        txNoRed = findViewById<View>(R.id.txNoRed) as TextView
        btMas!!.setOnClickListener(this)

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        //Log.w("TAG", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    Constants.token = task.result?.token.toString()
                    enviarToken(Constants.token)
                    obtenerNotificaciones()
                })


        adapterP = AdapterNotificacion(context, listNotificacion,"main")
        listNotificacion.clear()

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
        when (v.id) {
            R.id.btMas ->{
                val i = Intent(this, ListadoNotificaciones::class.java)
                startActivity(i)
                drawerLayout!!.closeDrawer(GravityCompat.END)
            }
            R.id.txFuente ->{
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://es.investing.com/commodities/us-cocoa")
                startActivity(openURL)
            }

        }
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
        obtenerPrecioCacao()
        obtenerNotificaciones()
    }

    fun enviarToken(token:String){
        var requestQueue = Volley.newRequestQueue(context)

        val params = JSONObject()
        params.put("token",token)

        val jsonObjRequestHome = object : StringRequest(
                Method.POST,
                Constants.SAVE_TOKEN,
                Response.Listener { response ->
                    System.out.println("token $response")

                }, Response.ErrorListener { error ->
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=UTF-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return params.toString().toByteArray()
            }
        }


        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)
    }


    fun obtenerPrecioCacao(){
        var requestQueue = Volley.newRequestQueue(context)

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.PRICE,
                Response.Listener { response ->
                    val res = JSONObject(response)
                    swipeRefreshLayout!!.setRefreshing(false)

                    var pc = res.getString("pc")
                    var cocoa_price = res.getString("cocoa_price")
                    var pcp = res.getString("pcp")
                    colocarDatosPrecio(cocoa_price,pc,pcp)

                    PrecioData.eliminarTodo()
                    PrecioData.insertar(cocoa_price = cocoa_price, pcp = pcp, pc = pc)

                }, Response.ErrorListener { error ->
            swipeRefreshLayout!!.setRefreshing(false)
            val obtenerRegistro = PrecioData.getPrecioCacao()
            if(obtenerRegistro.count>0){
                if (obtenerRegistro.moveToFirst())
                    do {
                        var pc = obtenerRegistro.getString(obtenerRegistro.getColumnIndex(PrecioDB.pc))
                        var cocoa_price = obtenerRegistro.getString(obtenerRegistro.getColumnIndex(PrecioDB.cocoa_price))
                        var pcp = obtenerRegistro.getString(obtenerRegistro.getColumnIndex(PrecioDB.pcp))
                        colocarDatosPrecio(cocoa_price,pc,pcp)
                    } while (obtenerRegistro.moveToNext())
            }

        }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)
    }

    private fun colocarDatosPrecio(cocoaPrice: String, pc: String, pcp: String) {
        txUno!!.setText(cocoaPrice)
        txDos!!.setText(pc)
        txTres!!.setText("(${pcp})")

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
    }

    fun obtenerNotificaciones(){
        if(!Constants.token.toString().equals("")){
            var requestQueue = Volley.newRequestQueue(context)
            System.out.println(Constants.NOTIFICATIONS+"?token="+Constants.token)

            val jsonObjRequestHome = object : StringRequest(
                    Method.GET,
                    Constants.NOTIFICATIONS+"?token="+Constants.token,
                    Response.Listener { response ->
                        listNotificacion.clear()
                        var res = JSONObject(response)
                        var count = res.getInt("count")
                        if(count == 0){
                            txNotificacion!!.visibility = View.GONE
                        }else{
                            txNotificacion!!.setText("$count")
                            txNotificacion!!.visibility = View.VISIBLE
                        }

                        if(res.has("notifications")){
                            val jsonNotifications = res["notifications"]
                            if (jsonNotifications is JSONArray){
                                var notifications = res.getJSONArray("notifications")
                                for (i in 0 until notifications.length()) {
                                    if(i<5){
                                        val item = notifications.getJSONObject(i)
                                        listNotificacion.add(NotificacionM(item.getString("id"),item.getString("title"),item.getString("message")))
                                    }
                                }
                            }
                        }


                        adapterP!!.notifyDataSetChanged()

                        btMas!!.visibility = View.VISIBLE
                        txNoRed!!.visibility = View.GONE
                        navigationDrawerList!!.visibility = View.VISIBLE

                    }, Response.ErrorListener { error ->
                btMas!!.visibility = View.GONE
                txNoRed!!.visibility = View.VISIBLE
                navigationDrawerList!!.visibility = View.GONE

            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=UTF-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return params.toString().toByteArray()
                }
            }

            // Añadir petición a la cola
            requestQueue!!.add(jsonObjRequestHome)
        }
    }

}
