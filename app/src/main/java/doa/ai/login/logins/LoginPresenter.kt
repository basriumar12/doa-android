package doa.ai.login.logins

import android.database.Observable
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.rollbar.android.Rollbar.init
import doa.ai.R
import doa.ai.base.base_model.BaseResponse
import doa.ai.network.RetrofitException
import doa.ai.network.Service
import doa.ai.utils.pref.SavePref
import io.reactivex.Flowable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.util.HalfSerializer.onNext
import io.reactivex.observers.DisposableObserver
import io.reactivex.plugins.RxJavaPlugins.onSubscribe
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import io.reactivex.subscribers.ResourceSubscriber
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.net.SocketTimeoutException
import org.json.JSONObject
import java.net.ConnectException
import java.net.UnknownHostException
import java.net.UnknownServiceException


class LoginPresenter {
    private lateinit var view: LoginView
    private lateinit var service: Service
    private lateinit var compositDisposible: CompositeDisposable

    fun loginContract() {
        service = Service.Create.service()
    }

    fun attachView(view: LoginView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        compositDisposible.dispose()
    }


    fun sendDataLogin(username: String, password: String, fcm_id : String) {
        val login = Login(username, password, fcm_id)
        val loginSend = service.postLogin(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view.showLoad()
                }

                .subscribe(

                        {
                            view.hideLoad()
                            if (it.status.equals(400)){
                                view.showError(it.message.toString())
                            } else {
                                handleSuccess(it)
                                view.onSucces(it)
                            }
                        },
                        this::handleError
                )
        compositDisposible.add(loginSend)
    }


    private fun handleError(t: Throwable) {
        view.hideLoad()
        view.showError("Gagal request data : ${t.message}")

        // view.onFailedLogin(t.localizedMessage)

    }

    private fun handleSuccess(loginResponse: LoginResponse) {

        view.hideLoad()
        view.onSuccessLogin(loginResponse.result?.user?.key.toString())


    }

}



