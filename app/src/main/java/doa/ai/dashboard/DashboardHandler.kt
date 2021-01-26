package doa.ai.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rollbar.android.Rollbar
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.login.logins.LoginActivity
import doa.ai.register.FirstRegisterActivity
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Rollbar.init(this)
        Rollbar.instance()

        textLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        textRegister.setOnClickListener {
            startActivity(Intent(this,FirstRegisterActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        btnLoginFacebook.setOnClickListener {
         showInfoMessage("Segera Hadir")
        }
    }
}
