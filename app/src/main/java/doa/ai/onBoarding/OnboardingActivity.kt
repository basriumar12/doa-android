package doa.ai.onBoarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import doa.ai.R
import doa.ai.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnBoardingActivity : AppCompatActivity(){

    private lateinit var adapter : AdapterOnboardingHandler
    var positionPages : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        setContentSkip()
       // setContentStart()

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.e("TAG", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    val msg = "message $token"

                    Log.e("TAG", msg)

                })



        val onBoarding: List<OnBoarding> = listOf(
                OnBoarding(0, "Ide & Rencana", "Susun ide kamu di satu wadah lalu\nwujudkan ide mu lewat rencana bisnis\nyang konkret dalam waktu singkat"),
                OnBoarding(1, "Kolaborasi", "Kembangkan & sempurnakan ide kamu\ndengan berkolaborasi bersama teman\natau temukan relasi baru"),
                OnBoarding(2, "Mulai Usahmu", "Buat investor terkesan dengan rencana\nusahamu dan dapatkan investasi untuk\nide-ide yang kamu miliki")
        )

        adapter = AdapterOnboardingHandler(this, onBoarding)
        viewPagerOnboarding.adapter = adapter
        indicatorOnBoarding.setupWithViewPager(viewPagerOnboarding,true)
        btnStart.setOnClickListener {
            viewPagerOnboarding.currentItem = 1
        }
        indicatorOnBoarding.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(position: TabLayout.Tab?) {

                val positionPage = position?.position
                when(positionPage){
                    0 -> btnStart.text = "Berikut"
                    1 -> btnStart.text = "Berikut"
                    2 -> btnStart.text = "Sekarang"
                }

                if (positionPage?.equals(0)!!)
                {
                    btnStart.setOnClickListener {
                        viewPagerOnboarding.currentItem = 1
                    }
                }

                if (positionPage.equals(1))
                {
                    btnStart.setOnClickListener {
                        viewPagerOnboarding.currentItem = 2
                    }
                }

                if (positionPage.equals(2))
                {
                    btnStart.setOnClickListener {
                        setContentStart()
                    }
                }
            }

        })


    }

    private fun setContentSkip(){
        btnSkip.setOnClickListener {
            startActivity(Intent(this,DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    private fun setContentStart(){
        btnStart.setOnClickListener {
            startActivity(Intent(this,DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }
}