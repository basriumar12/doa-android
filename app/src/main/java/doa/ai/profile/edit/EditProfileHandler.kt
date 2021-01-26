package doa.ai.profile.edit

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.vincent.filepicker.Constant
import com.vincent.filepicker.Constant.*
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.filter.entity.ImageFile
import doa.ai.R
import doa.ai.extentions.Constants
import doa.ai.main.event.model.ResponseUploadImage
import doa.ai.main.notes.bplan.business.edit.ResponseSubDistrict
import doa.ai.model.CountryModel
import doa.ai.model.DistrictModel
import doa.ai.model.ProvinceModel
import doa.ai.network.Service
import doa.ai.profile.profiles.ProfileResult
import doa.ai.utils.Constans
import doa.ai.utils.pref.SavePrefSegmentBusiness
import kotlinx.android.synthetic.main.activity_bplan_business.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.security.MessageDigest

class EditProfileActivity : doa.ai.base.BaseActivity(), EditProfileView {


    var subDistrict: Int = 0
    var tokenLogin: String? = ""
    private lateinit var presenter: EditProfilePresenter
    private var prefs: SharedPreferences? = null
    lateinit var fileUpload: MultipartBody.Part
    var nameImageSha256 = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setSupportActionBar(toolbarEditProfile)
        setTitle("Edit Profile")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        presenter = EditProfilePresenter()
        presenter.editProfileContract()

        prefs = applicationContext.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs?.getString("loginToken", "")
        presenter.getProfile("token $tokenLogin")
        presenter.getProvinceList("token $tokenLogin", "indo")


        presenter.getDetailSubDistrict("token $tokenLogin", subDistrict)

        postUpdate()
        permission()
        actionClick()

    }

    private fun actionClick() {
        editUploadPhoto.setOnClickListener {
            val i = Intent(this, ImagePickActivity::class.java)
            i.putExtra(MAX_NUMBER, 1)
            startActivityForResult(i, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {


            try {
                val pickedImg = data.getParcelableArrayListExtra<ImageFile>(RESULT_PICK_IMAGE)[0]?.path

                if (pickedImg != null) {


                } else {

                    showErrorMessage(getString(R.string.file_belum_dipilih))
                }


                val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), File(pickedImg))
                fileUpload = MultipartBody.Part.createFormData("image", File(pickedImg)?.name, requestBody)
                val encryptName = encryptStringTosha256(File(pickedImg).name)
                nameImageSha256 = getUploadToken(encryptName)
                Log.e("TAG", "name ${File(pickedImg).name} , convert $nameImageSha256")

                val dataFile = File(pickedImg)
                uploadFileReady(dataFile)


            } catch (e: IndexOutOfBoundsException) {

            }
        }

    }

    fun uploadFileReady(file: File) {
        val loading = ProgressDialog(this)
        loading.setCancelable(false)
        loading.setMessage("Upload progress")
        loading.show()

        var message = ""

        AndroidNetworking.upload(Constans.Staging.DEVEL + "uploader/image/")
                .addMultipartFile("image", file)
                .addMultipartParameter("folder", "images")
                .addHeaders("Authorization-Uploader", "Token $nameImageSha256")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(object : UploadProgressListener {
                    override fun onProgress(bytesUploaded: Long, totalBytes: Long) {
                        loading.show()
                    }
                })
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onError(anError: ANError?) {
                        Log.e("TAG", "error upload image ${Gson().toJson(anError)}")
                        loading.dismiss()
                        showInfoMessage("gagal upload ")

                    }

                    override fun onResponse(response: JSONObject?) {
                        loading.dismiss()

                        val jsonObject = JSONObject(response.toString())
                        if (jsonObject.getString("status").toString().equals("200")) {
                            showInfoMessage("Sukses upload")
                            val urlFromJsonObject = jsonObject.getString("result")
                            val url = JSONObject(urlFromJsonObject.toString()).getString("image_url")
                            editUploadPhoto.text = url
                            Log.e("TAG", "sukses upload image $url and $urlFromJsonObject")
                        } else {
                            showErrorMessage("Gagal upload")
                        }

                    }
                })
//        val convertFolderName = RequestBody.create(MediaType.parse("text/plain"), "images")
//        val service = Service.Create.service()
//
//
//        service.postUploadImage("Token $nameImageSha256", convertFolderName, fileUpload).enqueue(object : retrofit2.Callback<ResponseUploadImage> {
//            override fun onFailure(call: Call<ResponseUploadImage>, t: Throwable) {
//                showInfoMessage("Failead Upload")
//                loading.hide()
//
//            }
//
//            override fun onResponse(call: Call<ResponseUploadImage>, response: Response<ResponseUploadImage>) {
//                loading.hide()
//                if (response.isSuccessful) {
//                    if (response.body()?.status?.equals(200)!!) {
//                        showInfoMessage("sukses Upload")
//                        editUploadPhoto.text = response.body()?.result?.file_url.toString()
//
//
//                    } else {
//                        showInfoMessage("gagal upload file :" + response.body()!!.message.toString())
//                    }
//
//                } else {
//
//                    showInfoMessage("gagal upload file :" + response.code())
//                }
//            }
//        })


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

        return encode


    }

    private fun encryptStringTosha256(email: String): String {
        val charset = Charsets.UTF_8
        val byteArray = email.toByteArray(charset)
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(byteArray)
        return hash.fold("", { str, it -> str + "%02x".format(it) })
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
                        AlertDialog.Builder(applicationContext)
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


    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    private fun postUpdate() {
        updateProfileButton.setOnClickListener {
            if (editRwProfile.text.toString().isNullOrEmpty()) {
                showInfoMessage("RW belum di isi")
            } else if (editRtProfile.text.toString().isNullOrEmpty()) {
                showInfoMessage("RT belum di isi")
            } else if (editCityProfile.text.toString().isNullOrEmpty()) {
                showInfoMessage("Kota belum di isi")
            } else if (editProfessionProfile.text.toString().isEmpty()) {
                showInfoMessage("Profesi belum di isi")
            } else if (editAboutProfile.text.toString().isEmpty()) {
                showInfoMessage("Tentang anda belum di isi")
            } else if (editEmailProfile.text.toString().isEmpty()) {
                showInfoMessage("Email anda belum di isi")
            } else if (editStreetProfile.text.toString().isEmpty()) {
                showInfoMessage("Jalan belum di isi")
            } else if (editFullnameProfile.text.toString().isEmpty()) {
                showInfoMessage("Nama belum di isi")
            } else if (editUploadPhoto.text.toString().isEmpty()) {
                showInfoMessage("Photo belum di dipilih")
            } else {
                val rt = editRtProfile.text.toString()
                val rw = editRwProfile.text.toString()
                presenter.postProfile("token $tokenLogin",
                        editFullnameProfile.text.toString(),
                        editPhoneNumberProfile.text.toString(),
                        editEmailProfile.text.toString(),
                        editProfessionProfile.text.toString(),
                        editAboutProfile.text.toString(),
                        editUploadPhoto.text.toString(),
                        subDistrict,
                        editStreetProfile.text.toString(),
                        editSubDistrictProfile.text.toString(),
                        rt.toInt(),
                        rw.toInt()
                )
            }
        }
    }

    override fun onSuccerEditProfile(response: EditProfileResult) {
        val toast = Toast.makeText(this, "Berhasil ubah profil pengguna", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
        finish()
    }

    override fun onFailedEditProfile(error: String) {
        val toast = Toast.makeText(this, "Gagal ubah profil pengguna", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        // toast.show()
    }

    override fun onSuccessProfile(profile: ProfileResult) {
        editFullnameProfile.setText(profile.fullname)
        editProfessionProfile.setText(profile.profession)
        editAboutProfile.setText(profile.about)
        editEmailProfile.setText(profile.email)
        editPhoneNumberProfile.setText(profile.phone)
        editUploadPhoto.setText(profile.photo_url)
        subDistrict = profile.sub_district.id

        editCityProfile.setText(profile.district?.name.toString())
        editProvinceProfile.setText(profile.province?.name.toString())
        editSubDistrictProfile.setText(profile.village)
        editStreetProfile.setText(profile.address)
        editRtProfile.setText(profile.rt.toString())
        editRwProfile.setText(profile.rw.toString())


    }

    override fun onSuccessSubDistrict(data: DistrictModel) {
        editSubDistrictProfile.setOnClickListener { v ->
            val subMenu = PopupMenu(this, v)
            for (subdistrict in data.results[0].sub_districts) {
                subMenu.menu.add(subdistrict.id, subdistrict.id, 0, subdistrict.name_label)
                subMenu.setOnMenuItemClickListener { item ->
                    SavePrefSegmentBusiness().setBusinessSubDistric(item.title.toString())
                    SavePrefSegmentBusiness().setBusinessProvinceId(item.itemId.toString())

                    subDistrict = item.itemId
                    editSubDistrictProfile.setText(item.title)


                    //  presenter.getPostalCode("token $tokenLogin", item.title.toString())
                    false
                }
                subMenu.show()
            }
        }

    }

    override fun onSuccessDistrict(data: ProvinceModel) {
        editCityProfile.setOnClickListener { v ->
            val subMenu = PopupMenu(this, v)
            for (district in data.results[0].districts) {
                subMenu.menu.add(district.id, district.id, 0, district.name)
                subMenu.setOnMenuItemClickListener { item ->
                    SavePrefSegmentBusiness().setBusinessCity(item.title.toString())
                    editCityProfile.setText(item.title)
                    presenter.getSubDistrict("token $tokenLogin", item.title.toString())
                    false
                }
                subMenu.show()
            }
        }
    }

    override fun onSuccessProvince(data: CountryModel) {
        editProvinceProfile.setOnClickListener { v ->
            val subMenu = PopupMenu(this, v)
            for (province in data.results[0].provinces) {
                subMenu.menu.add(province.id, province.id, 0, province.name)
                subMenu.setOnMenuItemClickListener { item ->

                    editProvinceProfile.setText(item.title.toString())
                    presenter.getDistrictList("token $tokenLogin", item.title.toString())
                    false
                }
                subMenu.show()
            }
        }

    }

    override fun onSubDistrictDetail(data: ResponseSubDistrict) {

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