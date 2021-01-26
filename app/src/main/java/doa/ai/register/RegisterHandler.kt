package doa.ai.register

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.login.logins.LoginActivity
import doa.ai.utils.Utils
import kotlinx.android.synthetic.main.activity_register_first.*
import kotlinx.android.synthetic.main.activity_register_second.*
import kotlinx.android.synthetic.main.activity_register_third.*
import kotlinx.android.synthetic.main.view_error_layout.*
import kotlin.collections.ArrayList
import java.util.regex.Pattern


class FirstRegisterActivity : BaseActivity(), RegisterView {
    private lateinit var presenter: RegisterPresenter
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(doa.ai.R.layout.activity_register_first)
        setSupportActionBar(toolbarRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        presenter = RegisterPresenter()
        presenter.registerContract()
        prefs = applicationContext.getSharedPreferences("MyPref", 0)

        firstRegisterButton.setOnClickListener {
            handleRegister()
        }
        handleButtonRegister(this)

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

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    fun errorEmail() {
        val customErrorDrawable = resources.getDrawable(doa.ai.R.drawable.ic_clear_black_24dp)
        textRegistEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, customErrorDrawable, null)
        textRegistEmail.setText("")
        input_layout_email_register.error = " "
        input_layout_email_register.hint = getString(R.string.email_not_valid)


    }

    fun errorName() {
        val customErrorDrawable = resources.getDrawable(doa.ai.R.drawable.ic_clear_black_24dp)
        textRegistName.setCompoundDrawablesWithIntrinsicBounds(null, null, customErrorDrawable, null)
        textRegistName.setText("")
        input_layout_name_register.error = " "
        input_layout_name_register.hint = getString(R.string.name_cant_empty)


    }


    fun errorPhone() {
        val customErrorDrawable = resources.getDrawable(doa.ai.R.drawable.ic_clear_black_24dp)
        textRegistPhone.setCompoundDrawablesWithIntrinsicBounds(null, null, customErrorDrawable, null)
        input_layout_phone_register.error = " "
        input_layout_phone_register.hint = getString(R.string.no_hp_cant_empty)
        textRegistPhone.setText("")
    }

    private fun handleRegister() {
        var email = textRegistEmail.text.toString()
        when {
            textRegistName.text.toString().isEmpty() -> {

                errorDialog(getString(R.string.lengkapi_informasi_dengan_benar))
                errorName()
            }
            textRegistEmail.text.toString().isEmpty() -> {
                errorDialog(getString(R.string.lengkapi_informasi_dengan_benar))
                errorEmail()
            }
            textRegistPhone.text.toString().isEmpty() -> {
                errorDialog(getString(R.string.lengkapi_informasi_dengan_benar))
                errorPhone()
            }

            Utils().isValidEmailAddress(email) == false -> {
                errorDialog(getString(R.string.email_not_valid))
                errorEmail()
            }


            else -> {

                presenter.sendCheckUser(textRegistName.text.toString(),
                        textRegistEmail.text.toString(),
                        textRegistPhone.text.toString()
                        )

            }


        }
    }

    private fun handleButtonRegister(context: Context) {
        textRegistEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if (str.toString().trim().isNotEmpty()) {
                    firstRegisterButton.isEnabled = true
                    firstRegisterButton.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorPrimary))
                } else {
                    firstRegisterButton.isEnabled = false
                    firstRegisterButton.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorVeryLightPink_c1))
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }

    override fun onSuccessAuth(token: String) {
        showInfoMessage(token)

        startActivity(Intent(this, SecondRegisterActivity::class.java)
                .putExtra("username", textRegistName.text.toString())
                .putExtra("email", textRegistEmail.text.toString())
                .putExtra("phone", textRegistPhone.text.toString())
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

    }

    override fun existUser(code : Int) {


    }
    override fun onFailedAuth(error: String) {
        val errors = Html.fromHtml(error).toString()
        showErrorMessage(errors)

    }

    override fun showLoad() {
        progressbarRegisterCheck.visibility = View.VISIBLE
    }

    override fun hideLoad() {
        progressbarRegisterCheck.visibility = View.GONE

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

class SecondRegisterActivity : BaseActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var interest: ArrayList<String>
    private lateinit var addInterest: ArrayList<String>
    private lateinit var adapter: AdapterRegister
    var username: String = ""
    var email: String = ""
    var phone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(doa.ai.R.layout.activity_register_second)
        setSupportActionBar(toolbarSecondRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        prefs = applicationContext.getSharedPreferences("MyPref", 0)
        username = intent.getStringExtra("username")
        email = intent.getStringExtra("email")
        phone = intent.getStringExtra("phone")

        addInterest = ArrayList()
        spinnerInterest()

        secondRegisteButton.setOnClickListener {
            startActivity(Intent(this, ThirdRegisterActivity::class.java)
                    .putExtra("username", username)
                    .putExtra("email", email)
                    .putExtra("phone", phone)
                    .putExtra("interest", addInterest)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

    }

    private fun spinnerInterest() {
        interest = ArrayList()
        interest.add("Alas Kaki")
        interest.add("Advertising, Printing & Media")
        interest.add("Aplikasi dan pengembangan permainan")
        interest.add("Arsitektur")
        interest.add("Asuransi")
        interest.add("Batu-Batuan")
        interest.add("Batubara")
        interest.add("Desain produk")
        interest.add("Desain interior")
        interest.add("Elektronik")
        interest.add("Energy")
        interest.add("Farmasi")
        interest.add("Fesyen")
        interest.add("Film, Animasi & Video")
        interest.add("Fotografi")
        interest.add("Jasa Computer & Perangkatnya")
        interest.add("Jalan Tol, Pelabuhan, Bandara")
        interest.add("Kabel")
        interest.add("Kayu & Pengolahannya")
        interest.add("Keramik, Porselen & Kaca")
        interest.add("Kehutanan")
        interest.add("Kimia")
        interest.add("Komunikasi Visual")
        interest.add("Kriya")
        interest.add("Kuliner")
        interest.add("Kontruksi Bangunan")
        interest.add("Kosmetik & Keperluan Rumah Tangga")
        interest.add("Lembaga Pembiayaan")
        interest.add("Logam & Sejenisnya")
        interest.add("Logam & Mineral Lainnya")
        interest.add("Makanan & Minuman")
        interest.add("Mesin & Alat Berat")
        interest.add("Minyak & Gas Bumi")
        interest.add("Musik")
        interest.add("Otomotif & Komponennya")
        interest.add("Pakan Ternak")
        interest.add("Perkebunan")
        interest.add("Peternakan")
        interest.add("Perikanan")
        interest.add("Perdagangan Besar (Barang Produksi & Konsumsi)")
        interest.add("Perdagangan Eceran")
        interest.add("Peralatan Rumah Tanga")
        interest.add("Penerbitan")
        interest.add("Periklanan")
        interest.add("Perusahaan Investasi")
        interest.add("Perusahaan Efek")
        interest.add("Plastik & Kemasan")
        interest.add("Pulp & Kertas")
        interest.add("Properti & Real Estate")

        interest.add("Reksa Dana")
        interest.add("Restoran, Hotel & Pariwisata")
        interest.add("Rokok")
        interest.add("Semen")
        interest.add("Seni Pertunjukan")
        interest.add("Senirupa")
        interest.add("Tanaman Pangan")
        interest.add("Telekomunikasi")
        interest.add("Televisi & Radio")
        interest.add("Tekstil & Garmen")
        interest.add("Transportasi")
        interest.add("Lainnya")

        val dataAdapter = ArrayAdapter<String>(this, doa.ai.R.layout.textview_spinner, interest)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerInterest.prompt = "Select"
        spinnerInterest.adapter = dataAdapter
        spinnerInterest.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = parent?.getItemAtPosition(position).toString()
                if (addInterest.size <= 10) {
                    addInterest.add(selected)
                    adapter = AdapterRegister(addInterest)
                    recyclerInterest.setHasFixedSize(true)
                    recyclerInterest.layoutManager = LinearLayoutManager(this@SecondRegisterActivity)
                    recyclerInterest.smoothScrollToPosition(RecyclerView.VERTICAL)
                    recyclerInterest.adapter = adapter
                    adapter.notifyDataSetChanged()


                } else {
                    showErrorMessage(getString(R.string.interest_maksimal_sepuluh))

                }
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}


class ThirdRegisterActivity : BaseActivity(), RegisterView {

    private lateinit var presenter: RegisterPresenter
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(doa.ai.R.layout.activity_register_third)
        setSupportActionBar(toolbarRegisterThird)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = RegisterPresenter()
        presenter.registerContract()
        prefs = applicationContext.getSharedPreferences("MyPref", 0)

        thirdRegisterButton.setOnClickListener {
            handleService()
        }
        handleButtonPass(this)

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

    fun errorPassword1() {
        val customErrorDrawable = resources.getDrawable(doa.ai.R.drawable.ic_clear_black_24dp)
        textRegistPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, customErrorDrawable, null)
        input_layout_password1_register.error = " "
        input_layout_password1_register.hint = getString(R.string.password)
        textRegistPassword.setText("")
    }

    fun errorPassword2() {
        val customErrorDrawable = resources.getDrawable(doa.ai.R.drawable.ic_clear_black_24dp)
        textRegistConfPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, customErrorDrawable, null)
        input_layout_password2_register.error = " "
        input_layout_password2_register.hint =  getString(R.string.password)
        textRegistConfPassword.setText("")
    }


    private fun handleService() {
        var passwordhere = textRegistPassword.text.toString()
        var confirmhere = textRegistConfPassword.text.toString()
        val specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        val UpperCasePatten = Pattern.compile("[A-Z ]")
        val lowerCasePatten = Pattern.compile("[a-z ]")
        val digitCasePatten = Pattern.compile("[0-9 ]")


        if (passwordhere.isEmpty()) {
            textRegistPassword.error = getString(R.string.field_masih_kosong)
            errorDialog(getString(R.string.field_masih_kosong))
            errorPassword1()
        } else if (confirmhere.isEmpty()) {
            textRegistPassword.error = getString(R.string.field_masih_kosong)
            errorDialog(getString(R.string.field_masih_kosong))
            errorPassword2()
        } else if (passwordhere.length < 4) {
            errorPassword1()
            errorDialog(getString(R.string.pass_kurang_dari_empat))
        } else if (passwordhere != confirmhere) {
            errorPassword2()
            errorDialog(getString(R.string.pass_doesnt_match))

        } else if (!specailCharPatten.matcher(passwordhere).find()) {
            errorPassword1()
            errorPassword2()
            errorDialog(getString(R.string.pass_tidak_punya_spesial_kar))
        } else if (!UpperCasePatten.matcher(passwordhere).find()) {
            errorDialog(getString(R.string.pass_harus_punya_huruf_besar))
            errorPassword1()
            errorPassword2()
        } else if (!lowerCasePatten.matcher(passwordhere).find()) {
            errorPassword1()
            errorPassword2()
            errorDialog(getString(R.string.pass_harus_punya_huruf_kecil))
        } else if (!digitCasePatten.matcher(passwordhere).find()) {
            errorDialog(getString(R.string.pass_harus_ada_angka))
            errorPassword1()
            errorPassword2()
        } else {

            presenter.sendDataAuth(
                    intent.getStringExtra("username"),
                    intent.getStringExtra("email"),
                    intent.getStringExtra("phone"),
                    textRegistConfPassword.text.toString(),
                    textRegistConfPassword.text.toString(),
                    intent.getStringArrayListExtra("interest"),
                    intent.getStringExtra("username"))
        }

//        when {
//            textRegistPassword.text.toString().isEmpty() -> textRegistPassword.error = getString(R.string.field_masih_kosong)
//            textRegistConfPassword.text.toString().isEmpty() -> textRegistConfPassword.error = getString(R.string.field_masih_kosong)
//
//
//            else -> presenter.sendDataAuth(
//                    intent.getStringExtra("username"),
//                    intent.getStringExtra("email"),
//                    textRegistConfPassword.text.toString(),
//                    textRegistConfPassword.text.toString(),
//                    intent.getStringArrayListExtra("interest"),
//                    intent.getStringExtra("username"))
//        }
    }


    private fun handleButtonPass(context: Context) {
        textRegistConfPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if (str.toString().trim().isNotEmpty()) {
                    thirdRegisterButton.isEnabled = true
                    thirdRegisterButton.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorPrimary))
                } else {
                    thirdRegisterButton.isEnabled = false
                    thirdRegisterButton.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorVeryLightPink_c1))
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }

    override fun onSuccessAuth(token: String) {
        prefs.edit().putString("token", token).apply()
        startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        showInfoMessage("Sukses daftar")
        finish()
    }

    override fun onFailedAuth(error: String) {

        showErrorMessage(Html.fromHtml(error).toString())
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
        return when (id) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLoad() {
        progressbar_register.visibility = View.VISIBLE

    }

    override fun hideLoad() {
        progressbar_register.visibility = View.GONE
    }
    override fun existUser(code : Int) {

    }

}

