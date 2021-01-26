package doa.ai.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import doa.ai.BuildConfig
import doa.ai.R
import doa.ai.main.home.HomeActivity
import doa.ai.main.notes.NotesActivity
import doa.ai.onBoarding.OnBoardingActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity(){

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 1500
    private lateinit var prefs: SharedPreferences

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            if (prefs.getBoolean("login",false)) {
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(applicationContext, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        prefs = applicationContext.getSharedPreferences("MyPref",0)

        mDelayHandler = Handler()

        mDelayHandler?.postDelayed(mRunnable, SPLASH_DELAY)

        val appVersionGradle =  BuildConfig.VERSION_NAME
       tv_version.text = appVersionGradle

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler?.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }
}