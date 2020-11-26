package kronos.cacaomovil.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.splash.*
import kronos.cacaomovil.BuildConfig
import kronos.cacaomovil.R


class Splash : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 3500
    private lateinit var Session: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        Session = getSharedPreferences("datos", MODE_PRIVATE);

        val versionName: String = BuildConfig.VERSION_NAME
        txVersion.setText("Versión $versionName")


        var selectStation = Intent(this@Splash, HomeActivity::class.java)

        val extras = intent.extras
        if (extras != null) {
            if(intent.hasExtra("key_1")) {
                selectStation = Intent(this@Splash, ListadoNotificaciones::class.java)
            }
        }

        Handler().postDelayed(Runnable {
            startActivity(selectStation)

            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}
