package doa.ai.login.email

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import doa.ai.R
import doa.ai.login.logins.LoginActivity
import doa.ai.login.reset.ResetPasswordActivity
import kotlinx.android.synthetic.main.activity_forgot_send_email.*

class ForgotSendEmailActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_send_email)

        btnBacktoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }
}