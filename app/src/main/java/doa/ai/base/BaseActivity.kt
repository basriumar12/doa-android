package doa.ai.base

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.orhanobut.hawk.Hawk
import com.valdesekamdem.library.mdtoast.MDToast
import doa.ai.dashboard.DashboardActivity
import doa.ai.utils.pref.SavePrefTokenLogin
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

open class BaseActivity : AppCompatActivity() {

    lateinit var loading: MaterialDialog
    private lateinit var prefs: SharedPreferences
    private var tokenLogin: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = applicationContext.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs.getString("loginToken", "")

        val materialDialog = MaterialDialog.Builder(this!!)
        materialDialog.progress(true, 0)
        materialDialog.cancelable(false)
        loading = materialDialog.build()


        val mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mainViewModel.getType().observe(this, Observer { typeModel ->
            if (typeModel.connect.equals(false)){
                showErrorMessage("tidak connect internet")
            }

        })
    }

    private fun filterPref() {
        try {
            val dataToken = SavePrefTokenLogin().getTokenLogin()
            Log.e("tag", "$dataToken")
            if (dataToken == null) {
                prefs?.edit()?.remove("loginToken")?.apply()
                prefs?.edit()?.remove("login")?.apply()
                prefs?.edit()?.putBoolean("login", false)?.apply()
                finish()
                Hawk.deleteAll()
                startActivity(Intent(this, DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }


        } catch (e: Exception) {
            Log.e("TAG", "error $e")
            prefs?.edit()?.remove("loginToken")?.apply()
            prefs?.edit()?.remove("login")?.apply()
            prefs?.edit()?.putBoolean("login", false)?.apply()
            finish()
            Hawk.deleteAll()
            startActivity(Intent(this, DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
    }

    class MainViewModel(application: Application) : AndroidViewModel(application) {
        private val disposable = CompositeDisposable()

        override fun onCleared() {
            super.onCleared()
            disposable.clear()
        }

        private val isConnected: MutableLiveData<Boolean> = MutableLiveData()
        private val connectivityTypeName: MutableLiveData<String> = MutableLiveData()

        private val typeModel: MutableLiveData<TypeModel> = MutableLiveData()

        init {
            val rxNetwork = ReactiveNetwork.observeNetworkConnectivity(application)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { connect ->
                        isConnected.postValue(connect.available())
                        connectivityTypeName.postValue(connect.typeName())

                        val type = TypeModel(connect.available(), connect.typeName())
                        typeModel.postValue(type)
                    }

            disposable.add(rxNetwork)
        }


        fun getListenerNetwork() = isConnected
        fun getTypeName() = connectivityTypeName
        fun getType() = typeModel
    }

    data class TypeModel(val connect: Boolean,
                         val connectType: String)

    fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()


   fun showToastCenter(message : String){
        val toast = Toast.makeText(this,message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }
    fun intentTo(target:Class<*>){
        startActivity(Intent(this,target))
    }

    fun showErrorMessage(message: String?) {
        MDToast.makeText(this, message, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show()
    }

    fun showSuccessMessage(message: String?) {
        MDToast.makeText(this, message, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show()
    }

    fun showInfoMessage(message: String?) {
        MDToast.makeText(this, message, MDToast.LENGTH_LONG, MDToast.TYPE_INFO).show()
    }

    fun showLongErrorMessage(message: String?) {
        MDToast.makeText(this, message, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show()
    }

    fun showLongSuccessMessage(message: String?) {
        MDToast.makeText(this, message, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show()
    }

    fun showLongInfoMessage(message: String?) {
        MDToast.makeText(this, message, MDToast.LENGTH_LONG, MDToast.TYPE_INFO).show()
    }

    fun showProgressDialog() {
        loading.setContent("Loading")
        loading.show()
    }
    fun showProgressDialog(message: String?) {
        loading.setContent(message)
        loading.show()
    }

    fun updateMessageDialog(message: String?) {
        loading.setContent(message)
    }

    fun dismissProgressDialog() {
        if (loading.isShowing)
            loading.dismiss()
    }
}