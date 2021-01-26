package doa.ai.login.logins

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.login.forgot.ForgotPasswordActivity
import doa.ai.main.home.HomeActivity
import doa.ai.utils.hasNetwork
import doa.ai.utils.pref.SavePref
import doa.ai.utils.pref.SavePrefTokenLogin
import kotlinx.android.synthetic.main.activity_login.*

import android.app.ActionBar
import android.app.Dialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import doa.ai.main.notes.NotesActivity
import kotlinx.android.synthetic.main.activity_bplan_summary.*
import kotlinx.android.synthetic.main.view_error_layout.*


class LoginActivity : BaseActivity(), LoginView {


    private lateinit var presenter: LoginPresenter
    private lateinit var prefs: SharedPreferences
    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(doa.ai.R.layout.activity_login)
        setSupportActionBar(toolbarLogin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = LoginPresenter()
        presenter.loginContract()
        prefs = applicationContext.getSharedPreferences("MyPref",0)

        getFcmId()
        btnLogin.setOnClickListener {
            if(hasNetwork(this)) {
                handlePostLogin()
            } else{
                showToastCenter(getString(doa.ai.R.string.no_internet))
            }
        }

        handleButton(this)

        textForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    private fun getFcmId() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.e("TAG", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token
                    this.token = task.result?.token.toString()
                    // Log and toast
                    val msg = "message $token"
                    Log.e("TAG", msg)

                })

    }

    private fun handlePostLogin() {
        when {
            textUsernameLogin.text.toString().isEmpty() -> textUsernameLogin.error = "Silahkan masukan username anda"
            else -> presenter.sendDataLogin(textUsernameLogin.text.toString(), textPasswordLogin.text.toString(),token)
        }

    }

    private fun handleButton(context: Context) {
        textPasswordLogin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if(str.toString().trim().isNotEmpty()){
                    btnLogin.isEnabled = true
                    btnLogin.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorPrimary))
                }else{
                    btnLogin.isEnabled = false
                    btnLogin.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorVeryLightPink_c1))
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    fun showMessage(message : String){
        val toast = Toast.makeText(this,message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }

    override fun onSuccessLogin(token: String) {

        prefs.edit().putString("loginToken",token).apply()
        prefs.edit().putBoolean("login",true).apply()
       // startActivity(Intent(this,NotesActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        startActivity(Intent(this,HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    override fun onFailedLogin(error: String) {
       showErrorMessage(error)



    }

    fun errorDialog (text : String){

        val dialog = Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar)

        // Setting dialogview
        val window = dialog.getWindow()
        window.setGravity(Gravity.TOP)

        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT)
        dialog.setTitle(null)
        dialog.setContentView(doa.ai.R.layout.view_error_layout)
        dialog.tv_error.text = text
        dialog.img_dismiss.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(true)

        dialog.show()
    }

    @SuppressLint("ResourceAsColor")
    override fun showError(message: String) {
        showErrorMessage(message)
        val customErrorDrawable = resources.getDrawable(doa.ai.R.drawable.ic_clear_black_24dp)
        //customErrorDrawable.setBounds(0, 0, customErrorDrawable.intrinsicWidth, customErrorDrawable.intrinsicHeight)
        textUsernameLogin.setCompoundDrawablesWithIntrinsicBounds(null, null , customErrorDrawable,null)
        textPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(null, null , customErrorDrawable,null)

        textUsernameLogin.setText("")
        textPasswordLogin.setText("")
        text_input_layout_username.error =" "
        text_input_layout_username.hint = getString(R.string.periksa_kembali_email)
        text_input_layout_username.setBoxBackgroundColorResource(doa.ai.R.color.colorOrangeyRed)
        text_input_layout_login.error =" "
        text_input_layout_login.hint = getString(R.string.periksa_kembali_sandi)
        text_input_layout_login.setBoxBackgroundColorResource(doa.ai.R.color.colorOrangeyRed)
        errorDialog(getString(R.string.lengkapi_informasi_dengan_benar))


    }

    override fun showLoad() {
        progressbar_login.visibility = View.VISIBLE

    }

    override fun hideLoad() {
        progressbar_login.visibility = View.GONE

    }

    override fun onSucces(loginResponse: LoginResponse) {
        SavePref().setName(loginResponse.result?.profile?.fullname.toString())
        SavePref().setEmail(loginResponse.result?.user?.email.toString())
        SavePref().setPhone(loginResponse.result?.profile?.phone.toString())
        SavePrefTokenLogin().setTokenLogin(loginResponse.result?.user?.key.toString())



    }

}
