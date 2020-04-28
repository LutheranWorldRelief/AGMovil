package kronos.cacaomovil.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import kronos.cacaomovil.R
import kronos.cacaomovil.fragments.HomeOpciones


class HomeActivity : AppCompatActivity(), View.OnClickListener {

    //Defining Variables
    private var toolbar: Toolbar? = null
    //private var drawerLayout: DrawerLayout? = null
    internal var IDU: String? = null
    internal lateinit var context: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_main)

        context = this@HomeActivity




        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

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
        }


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

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState()


        val headerLayout = navigationView.inflateHeaderView(R.layout.header)*/

        val fragment = HomeOpciones()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commitAllowingStateLoss()

    }

    public override fun onResume() {
        super.onResume()


    }


    override fun onClick(v: View) {

    }

    companion object {
        lateinit var navigationView: NavigationView
    }
}
