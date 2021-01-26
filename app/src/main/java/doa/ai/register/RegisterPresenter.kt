package doa.ai.register

import android.util.Log
import com.google.gson.JsonParser
import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class RegisterPresenter {

    private lateinit var view: RegisterView
    private lateinit var service: Service
    private lateinit var compositDisposible: CompositeDisposable

    fun registerContract() {
        service = Service.Create.service()
    }

    fun attachView(view: RegisterView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        compositDisposible.dispose()
    }

    fun sendDataAuth(username: String, email:String, phone : String,
                     password1: String, password2:String,
                     interests: List<String>, fullname: String) {
        val auth = AuthBody(username, email,phone, password1, password2, interests, fullname)
        val authSend = service.postAuth(auth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{
                    view.showLoad()
                }
                .subscribe({
                    view.hideLoad()
                    if (it.status.equals(400)){
                        view.onFailedAuth(it.message.toString())
                    } else{
                        handleSuccess(it)
                    }
                },
                        this::handleError

                )
        compositDisposible.add(authSend)
    }

    fun sendCheckUser(username: String, email:String, phone : String) {
        val auth = CheckUserBody(username, email,phone)
        val authSend = service.postCheckUser(auth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{
                    view.showLoad()
                }
                .subscribe({
                    view.hideLoad()
                    val status = it.status
                    if (status.equals(400)){
                        view.onFailedAuth(it.message.toString())
                    } else{
                        view.onSuccessAuth(it.message.toString())
                    }
                },
                        {
                            view.onFailedAuth(it.message.toString())
                        }

                )
        compositDisposible.add(authSend)
    }

    private fun handleSuccess (authResponse: AuthResponse){
        view.onSuccessAuth(authResponse.result?.user?.key.toString())
        view.hideLoad()
    }

    private fun handleError(t: Throwable){
        view.onFailedAuth("Gagal request data : ${t.message}")
        view.hideLoad()
    }

}