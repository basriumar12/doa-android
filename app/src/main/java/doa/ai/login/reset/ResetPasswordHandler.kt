package doa.ai.login.reset

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.base.base_model.BaseResponse
import doa.ai.login.logins.LoginActivity
import kotlinx.android.synthetic.main.activity_reset_password.*
import java.util.regex.Pattern


class ResetPasswordActivity : BaseActivity(), ResetPasswordView{
    private lateinit var presenter: ResetPasswordPresenter
    var uid = ""
    var token =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(doa.ai.R.layout.activity_reset_password)
        setSupportActionBar(toolbarResetPassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = ResetPasswordPresenter()
        presenter.resetPasswordContract()

        val appLinkUri = Uri.parse(this.intent.dataString)
         uid = appLinkUri.getQueryParameter("uid")
        token = appLinkUri.getQueryParameter("token")

        handleButtonReset(this)

        btnResetPassword.setOnClickListener {
            handleResetPassword()

        }


    }

    private fun handleResetPassword() {
        var passwordhere = textNewPassword.text.toString()
        var confirmhere = textConfirmNewPassword.text.toString()
        val specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        val UpperCasePatten = Pattern.compile("[A-Z ]")
        val lowerCasePatten = Pattern.compile("[a-z ]")
        val digitCasePatten = Pattern.compile("[0-9 ]")

        if (passwordhere.isEmpty()) {
            textNewPassword.error = getString(R.string.field_masih_kosong)
        } else if (confirmhere.isEmpty()) {
            textConfirmNewPassword.error = getString(R.string.field_masih_kosong)
        }
        else if (passwordhere.length < 8){
            showErrorMessage(getString(R.string.pass_kurang_dari_empat))
        }
        else if (passwordhere != confirmhere) {
            showErrorMessage(getString(R.string.pass_doesnt_match))
        } else if (!specailCharPatten.matcher(passwordhere).find()) {
            showErrorMessage(getString(R.string.pass_tidak_punya_spesial_kar))
        } else if (!UpperCasePatten.matcher(passwordhere).find()) {
            showErrorMessage(getString(R.string.pass_harus_punya_huruf_besar))
        } else if (!lowerCasePatten.matcher(passwordhere).find()) {
            showErrorMessage(getString(R.string.pass_harus_punya_huruf_kecil))
        } else if (!digitCasePatten.matcher(passwordhere).find()) {
            showErrorMessage(getString(R.string.pass_harus_ada_angka))
        } else if (passwordhere.isEmpty()) {
            textNewPassword.error = getString(R.string.field_masih_kosong)
        } else if (confirmhere.isEmpty()) {
            textConfirmNewPassword.error = getString(R.string.field_masih_kosong)
        } else{

            presenter.sendDataResetPassword(uid, token, passwordhere, confirmhere)

        }
    }

    private fun handleButtonReset(context: Context) {
        textConfirmNewPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if (str.toString().trim().isNotEmpty()) {
                    btnResetPassword.isEnabled = true
                    btnResetPassword.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorPrimary))
                } else {
                    btnResetPassword.isEnabled = false
                    btnResetPassword.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorVeryLightPink_c1))
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
    override fun onSuccessForgot(baseResponse: BaseResponse) {
        showInfoMessage(baseResponse.message.toString())
        startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    override fun onFailedAuth(error: String) {
        showErrorMessage(error)

       }

    override fun showLoad() {
        progressbar_resetpassword.visibility = View.VISIBLE
       }

    override fun hideLoad() {
        progressbar_resetpassword.visibility = View.GONE
        }

    override fun showError(message: String) {
        showErrorMessage(message)
       }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        return when(id){
            android.R.id.home ->{
                finish()
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }
}