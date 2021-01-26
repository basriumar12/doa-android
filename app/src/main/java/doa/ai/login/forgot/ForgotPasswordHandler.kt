package doa.ai.login.forgot

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.base.base_model.BaseResponse
import doa.ai.login.email.ForgotSendEmailActivity
import doa.ai.register.RegisterPresenter
import doa.ai.utils.Utils
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register_first.*
import kotlinx.android.synthetic.main.view_error_layout.*

class ForgotPasswordActivity : BaseActivity(), ForgotView{
    private lateinit var presenter: ForgotPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setSupportActionBar(toolbarforgotPassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = ForgotPasswordPresenter()
        presenter.forgotContract()

        btnSendResetPassword.setOnClickListener {
            if (textEmailForgotPass.text.toString().isEmpty()){
                errorDialog(getString(R.string.email_not_field))
                errorEmail()
            } else if (Utils().isValidEmailAddress(textEmailForgotPass.text.toString()) == false){
                errorEmail()
                errorDialog(getString(R.string.email_not_field))
            } else{
                presenter.sendForgotPass(textEmailForgotPass.text.toString())
            }

        }
        handleButtonReset(this)
    }

    fun errorEmail() {
        val customErrorDrawable = resources.getDrawable(doa.ai.R.drawable.ic_clear_black_24dp)
        textEmailForgotPass.setCompoundDrawablesWithIntrinsicBounds(null, null, customErrorDrawable, null)
        textEmailForgotPass.setText("")
        input_layout_email_forgot_pass.error = " "
        input_layout_email_forgot_pass.hint = getString(R.string.email_not_valid)


    }

    fun errorDialog(text: String) {

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
        dialog.setCancelable(false)

        dialog.show()
    }

    private fun handleButtonReset(context: Context) {
        textEmailForgotPass.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if (str.toString().trim().isNotEmpty()) {
                    btnSendResetPassword.isEnabled = true
                    btnSendResetPassword.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorPrimary))
                } else {
                    btnSendResetPassword.isEnabled = false
                    btnSendResetPassword.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorVeryLightPink_c1))
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
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        return when(id){
            android.R.id.home ->{
                finish()
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSuccessForgot(baseResponse: BaseResponse) {
        startActivity(Intent(this, ForgotSendEmailActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }

    override fun onFailedAuth(error: String) {
        showErrorMessage(error)

    }

    override fun showLoad() {
        progressbar_resetpass.visibility = View.VISIBLE

    }

    override fun hideLoad() {
        progressbar_resetpass.visibility = View.GONE

    }

}