package doa.ai.main.event.event_register

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.widget.PopupMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.NormalFilePickActivity
import com.vincent.filepicker.filter.entity.NormalFile
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.dashboard.DashboardActivity
import doa.ai.main.event.EventPresenter
import doa.ai.main.event.EventResponse
import doa.ai.main.event.EventView
import doa.ai.main.event.ResponseProfession
import doa.ai.main.event.model.ResponseUploadImage
import doa.ai.main.home.HomeActivity
import doa.ai.main.notes.bplan.plan.SummaryList
import doa.ai.main.notes.bplan.setup.SetupBPlanActivity
import doa.ai.model.CountryModel
import doa.ai.model.ProvinceModel
import doa.ai.network.Service
import doa.ai.utils.pref.SavePref
import kotlinx.android.synthetic.main.activity_event.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


import java.security.MessageDigest


class EventActivityFromLink : BaseActivity(), EventView {


    private lateinit var presenter: EventPresenter

    private lateinit var prefs: SharedPreferences
    private var tokenLogin: String? = ""
    var professionItem = ""
    var idDistrict: Int = 0
    var id: Int? = 0
    var idPlan: Int = 0
    var url: String? = ""
    var description: String? = ""
    var starDate: String? = ""
    var endDate: String? = ""
    var place: String? = ""
    var title: String? = ""
    var checkRegister: Boolean? = null
    var withBplan: Boolean? = null
    var withDocument: Boolean? = null
    lateinit var fileUpload: MultipartBody.Part
    var nameImageSha256 = ""
    var dataUrlDocument = ""
    var flag = ""

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(doa.ai.R.layout.activity_event)
        setSupportActionBar(toolbarEvent)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        presenter = EventPresenter()
        presenter.eventContract()

        prefs = applicationContext.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs.getString("loginToken", "")

        if (tokenLogin.equals("") || tokenLogin == null) {
            startActivity(Intent(this@EventActivityFromLink, DashboardActivity::class.java))
            finish()
        }

        val appLinkUri = Uri.parse(this.intent.dataString)
        val idEvent = appLinkUri.getQueryParameter("id")
        if (idEvent== null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        var phoneSave = SavePref().getPhone()
        if (phoneSave.equals("null")) {
            textEventPhone.setText("0")
        } else {
            textEventPhone.setText(phoneSave)
        }
        textEventName.setText(SavePref().getName())
        textEvenEmail.setText(SavePref().getEmail())


        permission()

        setUIEvent()
        //uiCheckRegisters()
        presenter.getDataPlan("token $tokenLogin")
        presenter.getProfession("token $tokenLogin")
        presenter.getProvinceList("token $tokenLogin", "indo")
        idEvent?.toInt()?.let { presenter.getCheckRegisterEvent("token $tokenLogin", it) }
        handleButton(this)
        uploadFile()


    }

    private fun setDataUiRegistrasion() {
        var nilaiBplan = 0
        var nilaiDocument = 0
        if (withBplan?.equals(true)!!) {
            nilaiBplan = 1
        }
        if (withDocument!!.equals(true)) {
            nilaiDocument = 1
        }
        //document and bpln = true
        if (nilaiBplan.equals(1) && nilaiDocument.equals(1)) {
            layout_upload_file.visibility = View.VISIBLE
            cardEventPlan.visibility = View.VISIBLE
            buttonRegisterEvent.setOnClickListener {
                sendDataRegisterWithDocumentandBplan()
            }
        }
        //document true
        else if (!nilaiBplan.equals(1) && nilaiDocument.equals(1)) {
            layout_upload_file.visibility = View.VISIBLE
            cardEventPlan.visibility = View.GONE
            buttonRegisterEvent.setOnClickListener {
                sendDataRegisterWithDocument()
            }
        }
        //bplan true
        else if (nilaiBplan.equals(1) && !nilaiDocument.equals(1)) {
            layout_upload_file.visibility = View.GONE
            cardEventPlan.visibility = View.VISIBLE
            buttonRegisterEvent.setOnClickListener {
                sendDataRegisterWithBplan()
            }
        }
        //dodcument and bplan = false
        else {
            buttonRegisterEvent.setOnClickListener {
                sendDataRegister()
            }
        }


    }

    fun permission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {}
                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.addCategory(Intent.CATEGORY_HOME)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)

                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        AlertDialog.Builder(this@EventActivityFromLink)
                                .setTitle("Storage Permission Needed")
                                .setMessage("This app needs the read storage permission")
                                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                                    //Prompt the user once explanation has been shown
                                    token.continuePermissionRequest()
                                })
                                .create()
                                .show()
                    }
                }).check()


    }


    fun uploadFile() {


        btn_upload_file.setOnClickListener {

            val i = Intent(this, NormalFilePickActivity::class.java)
            i.putExtra(Constant.MAX_NUMBER, 1)
            val data = arrayOf("xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf")
            i.putExtra(NormalFilePickActivity.SUFFIX, data)
            startActivityForResult(i, Constant.REQUEST_CODE_PICK_FILE)
        }

    }

    fun uploadFileReady() {
        val loading = ProgressDialog(this)
        loading.setCancelable(false)
        loading.setMessage("Upload progress")
        loading.show()
        val convertFolderName = RequestBody.create(MediaType.parse("text/plain"), "files")
        val service = Service.Create.service()


        service.postUploadFile("Token $nameImageSha256", convertFolderName, fileUpload).enqueue(object : retrofit2.Callback<ResponseUploadImage> {
            override fun onFailure(call: Call<ResponseUploadImage>, t: Throwable) {
                showInfoMessage("Failead Upload")
                loading.hide()

            }

            override fun onResponse(call: Call<ResponseUploadImage>, response: Response<ResponseUploadImage>) {
                loading.hide()
                if (response.isSuccessful) {
                    if (response.body()?.status?.equals(200)!!) {
                        showInfoMessage("sukses Upload")
                        tv_link_upload_file.text = response.body()?.result?.file_name.toString()
                        dataUrlDocument = response.body()?.result?.file_url.toString()

                        btn_upload_file.visibility = View.GONE

                    } else {
                        showInfoMessage("gagal upload file :" + response.body()!!.status.toString())
                    }

                } else {

                    showInfoMessage("gagal upload file :" + response.code())
                }
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK && data != null) {


            try {
                val pickedImg = data.getParcelableArrayListExtra<NormalFile>(Constant.RESULT_PICK_FILE)[0]?.path
                if (pickedImg != null) {


                } else {

                    showErrorMessage(getString(R.string.file_belum_dipilih))
                }
                val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), File(pickedImg))
                fileUpload = MultipartBody.Part.createFormData("file", File(pickedImg)?.name, requestBody)
                val encryptName = encryptStringTosha256(File(pickedImg).name)
                nameImageSha256 = getUploadToken(encryptName)

                uploadFileReady()


            } catch (e: IndexOutOfBoundsException) {

            }
        }

    }

    fun getUploadToken(string: String): String {
        var encode = string
        var tokens = listOf('1', '2', '3', '4', '5', 'a', 'b', 'c')
        var replacers = listOf('6', '7', '8', '9', '0', 'z', 'y', 'x')

        for (a in 0 until encode.length) {
            for (b in 0 until tokens.size) {

                if (encode.get(a).toString() == tokens.get(b).toString()) {
                    encode = encode.replace(tokens[b].toString(), replacers[b].toString())


                }
            }
        }



        Log.e("tag", "after encrypt $encode")
        return encode


    }

    private fun encryptStringTosha256(email: String): String {
        val charset = Charsets.UTF_8
        val byteArray = email.toByteArray(charset)
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(byteArray)
        return hash.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun uiCheckRegisters() {
        if (checkRegister!!.equals(true)) {
            relativeSuccessRegister.visibility = View.VISIBLE
            inputEventName.visibility = View.GONE
            inputEventEmail.visibility = View.GONE
            inputEventPhone.visibility = View.GONE
            inputEventPekerjaan.visibility = View.GONE
            inputEventOrganisasi.visibility = View.GONE
            layout_profession.visibility = View.GONE
            layout_city.visibility = View.GONE
            layout_province.visibility = View.GONE
            layout_upload_file.visibility = View.GONE
            cardEventPlan.visibility = View.GONE
            buttonRegisterEvent.visibility = View.GONE
        }


    }

    private fun handleButton(context: Context) {
        textEventPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if (str.toString().trim().isNotEmpty()) {
                    buttonRegisterEvent.isEnabled = true
                    buttonRegisterEvent.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorPrimary))
                } else {
                    buttonRegisterEvent.isEnabled = false
                    buttonRegisterEvent.setBackgroundColor(ContextCompat.getColor(context, doa.ai.R.color.colorVeryLightPink_c1))
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

    }

    private fun setUIEvent() {
        if (flag.equals("info")) {
            viewInfo.visibility = View.VISIBLE
            viewRegister.visibility = View.GONE
            linearEventInfo.visibility = View.VISIBLE
            linearEventRegister.visibility = View.GONE
            buttonRegisterEvent.visibility = View.GONE
        }

        if (flag.equals("register")) {
            viewInfo.visibility = View.GONE
            viewRegister.visibility = View.VISIBLE
            linearEventInfo.visibility = View.GONE
            linearEventRegister.visibility = View.VISIBLE
            buttonRegisterEvent.visibility = View.VISIBLE
        }


        linearInfo.setOnClickListener {
            viewInfo.visibility = View.VISIBLE
            viewRegister.visibility = View.GONE
            linearEventInfo.visibility = View.VISIBLE
            linearEventRegister.visibility = View.GONE
            buttonRegisterEvent.visibility = View.GONE

        }

        linearRegister.setOnClickListener {
            viewInfo.visibility = View.GONE
            viewRegister.visibility = View.VISIBLE
            linearEventInfo.visibility = View.GONE
            linearEventRegister.visibility = View.VISIBLE
            buttonRegisterEvent.visibility = View.VISIBLE
            //uiCheckRegisters()
        }
    }

    private fun sendDataRegister() {

        if (textEventPhone.text.toString().isEmpty()) {
            showInfoMessage(getString(R.string.no_belum_di_isi))
        } else if (idDistrict.equals(0)) {
            showErrorMessage("Belum memilih kota")
        } else {

            id?.let {
                presenter.postEventWithoutBplanDocument("token $tokenLogin"
                        , textEventName.text.toString()
                        , textEvenEmail.text.toString()
                        , textEventPhone.text.toString()
                        , it,
                        textEventOrganisasi.text.toString(),
                        professionItem,
                        idDistrict
                )

                presenter.postEventSync(textEventName.text.toString()
                        , textEvenEmail.text.toString()
                        , textEventPhone.text.toString()
                )
            }

        }
    }

    private fun sendDataRegisterWithBplan() {
        if (idDistrict.equals(0)) {
            showInfoMessage(getString(R.string.belum_memilih_kota))
        } else if (idPlan.equals(0)) {
            showInfoMessage(getString(R.string.rencana_usaha_belum_dipilih))
        } else if (textEventPhone.text.toString().isEmpty()) {
            showInfoMessage(getString(R.string.no_belum_di_isi))
        } else {
            id?.let {
                presenter.postEvent("token $tokenLogin"
                        , textEventName.text.toString()
                        , textEvenEmail.text.toString()
                        , textEventPhone.text.toString()
                        , it,
                        idPlan.toInt(),
                        textEventOrganisasi.text.toString(),
                        professionItem,
                        null.toString(),
                        idDistrict
                )

                presenter.postEventSync(textEventName.text.toString()
                        , textEvenEmail.text.toString()
                        , textEventPhone.text.toString()
                )
            }
        }
    }

    private fun sendDataRegisterWithDocument() {
        if (idDistrict.equals(0)) {

            showInfoMessage(getString(R.string.belum_memilih_kota))
        } else if (dataUrlDocument.equals("")) {
            showInfoMessage(getString(R.string.file_belum_dipilih))
        } else if (textEventPhone.text.toString().isEmpty()) {
            showInfoMessage(getString(R.string.no_belum_di_isi))
        } else {
            id?.let {
                presenter.postEventwithoutBplan("token $tokenLogin"
                        , textEventName.text.toString()
                        , textEvenEmail.text.toString()
                        , textEventPhone.text.toString()
                        , it
                        , textEventOrganisasi.text.toString(),
                        professionItem,
                        dataUrlDocument,
                        idDistrict
                )

                presenter.postEventSync(textEventName.text.toString()
                        , textEvenEmail.text.toString()
                        , textEventPhone.text.toString()
                )
            }
        }
    }

    private fun sendDataRegisterWithDocumentandBplan() {
        if (idDistrict.equals(0)) {

            showInfoMessage(getString(R.string.belum_memilih_kota))
        } else if (dataUrlDocument.equals("")) {
            showInfoMessage(getString(R.string.file_belum_dipilih))
        } else if (idPlan.equals(0)) {
            showInfoMessage(getString(R.string.rencana_usaha_belum_dipilih))
        } else if (textEventPhone.text.toString().isEmpty()) {
            showInfoMessage(getString(R.string.no_belum_di_isi))
        } else {
            id?.let {
                presenter.postEvent("token $tokenLogin"
                        , textEventName.text.toString()
                        , textEvenEmail.text.toString()
                        , textEventPhone.text.toString()
                        , it,
                        idPlan,
                        textEventOrganisasi.text.toString(),
                        professionItem,
                        dataUrlDocument,
                        idDistrict
                )

                presenter.postEventSync(textEventName.text.toString()
                        , textEvenEmail.text.toString()
                        , textEventPhone.text.toString()
                )
            }
        }
    }

    override fun onSuccessPlan(response: SummaryList) {
        if (response.results.isNullOrEmpty()) {
            cardEventPlan.setOnClickListener { v ->
                startActivity(Intent(this, SetupBPlanActivity::class.java))
                finish()
            }
        } else {
            cardEventPlan.setOnClickListener { v ->
                val subMenu = PopupMenu(this, v)
                for (summary in response.results.iterator()) {
                    subMenu.menu.add(summary.id, summary.id, 0, summary.plan.title)
                    subMenu.setOnMenuItemClickListener { item ->
                        idPlan = item.itemId
                        textEventPlan.text = item.title
                        false
                    }
                    subMenu.show()
                }
            }
        }
    }

    override fun onSuccessCheckRegisterEvent(response: EventResponse) {
        id = response.result.id
        url = response.result.image_url
        title = response.result.title
        starDate = response.result.start_date
        endDate = response.result.end_date
        place = intent.getStringExtra("place")
        description = response.result.description
        // checkRegister = response.result.im_registered_here.toString()
        withBplan = response.result.use_plan
        withDocument = response.result.use_document
        Picasso.get().load(url).error(R.drawable.ic_logo_doa).fit().into(imageEvent)

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatterEvent = SimpleDateFormat("EEEE, dd MM yyy")
        val dateStart = formatter.parse(starDate)
        val dateStartEvent = formatterEvent.format(dateStart)
        val dateEnd = formatter.parse(endDate)
        val dateEndEvent = formatterEvent.format(dateEnd)

        textEventInfo.text = title
        textEventStartDate.text = ": " + dateStartEvent
        textEventEndDate.text = ": " + dateEndEvent
        textEventStartDateRegister.text = ": " + dateStartEvent
        textEventEndDateRegister.text = ": " + dateEndEvent
        textEventPlace.text = ": " + place
        textEventInfoDesc.text = Html.fromHtml(description)

        setDataUiRegistrasion()


        Log.e("tag", "response ${Gson().toJson(response)}")
        if (response.result.im_registered_here.equals(true)) {
            showInfoMessage(getString(doa.ai.R.string.sukses_register_event))
            relativeSuccessRegister.visibility = View.VISIBLE
            inputEventName.visibility = View.GONE
            inputEventEmail.visibility = View.GONE
            inputEventPhone.visibility = View.GONE
            inputEventPekerjaan.visibility = View.GONE
            inputEventOrganisasi.visibility = View.GONE
            layout_profession.visibility = View.GONE
            layout_city.visibility = View.GONE
            layout_province.visibility = View.GONE
            layout_upload_file.visibility = View.GONE
            cardEventPlan.visibility = View.GONE
            buttonRegisterEvent.visibility = View.GONE


        }
    }

    override fun onSuccessEvent(response: EventResponse) {
        if (response.status == 200) {
            showInfoMessage(getString(doa.ai.R.string.sukses_register_event))
            relativeSuccessRegister.visibility = View.VISIBLE
            inputEventName.visibility = View.GONE
            inputEventEmail.visibility = View.GONE
            inputEventPhone.visibility = View.GONE
            inputEventPekerjaan.visibility = View.GONE
            inputEventOrganisasi.visibility = View.GONE
            layout_profession.visibility = View.GONE
            layout_city.visibility = View.GONE
            layout_province.visibility = View.GONE
            layout_upload_file.visibility = View.GONE

            cardEventPlan.visibility = View.GONE
            buttonRegisterEvent.visibility = View.GONE
            //     layout_date_register.visibility = View.GONE
        }
    }

    override fun onFailedEvent(error: String) {
        showErrorMessage(error)
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun showLoad() {
        progressbar_event.visibility = View.VISIBLE

    }

    override fun hideLoad() {
        progressbar_event.visibility = View.GONE

    }

    override fun onSuccessGetProfession(responseProfession: ResponseProfession) {
        var profession: ArrayList<String> = ArrayList()
        responseProfession.results?.forEach {
            it?.name?.let { it1 -> profession.add(it1) }
        }
        val dataAdapter = ArrayAdapter<String>(this, doa.ai.R.layout.textview_spinner, profession)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPekerjaan.prompt = "Select"
        spinnerPekerjaan.adapter = dataAdapter
        spinnerPekerjaan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = parent?.getItemAtPosition(position).toString()
                professionItem = profession.get(position).toString()
            }
        }

    }

    override fun onSuccessProvince(data: CountryModel) {
        layout_province.setOnClickListener {
            val subMenu = PopupMenu(this, it)
            for (province in data.results[0].provinces) {
                subMenu.menu.add(province.id, province.id, 0, province.name)
                subMenu.setOnMenuItemClickListener { item ->
                    spinnerProvince.text = item.title
                    presenter.getDistrictList("token $tokenLogin", item.title.toString())
                    false
                }
                subMenu.show()
            }

        }
    }

    override fun onSuccessDistrict(response: ProvinceModel) {
        layout_city.setOnClickListener { v ->
            Log.e("tag", "${Gson().toJson(response)}")
            val subMenu = PopupMenu(this, v)
            for (district in response.results[0].districts) {
                subMenu.menu.add(district.id, district.id, 0, district.name)
                subMenu.setOnMenuItemClickListener { item ->
                    spinnerCity.text = item.title
                    idDistrict = item.itemId

                    false
                }
                subMenu.show()
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
