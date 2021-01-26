package doa.ai.login.forgot

import android.util.Log
import doa.ai.base.base_model.BaseResponse
import doa.ai.network.Service
import doa.ai.register.AuthBody
import doa.ai.register.AuthResponse
import doa.ai.register.RegisterView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ForgotPasswordPresenter {

    private lateinit var view: ForgotView
    private lateinit var service: Service
    private lateinit var compositDisposible: CompositeDisposable


    fun forgotContract() {
        service = Service.Create.service()
    }

    fun attachView(view: ForgotView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        compositDisposible.dispose()
    }

    fun sendForgotPass (email : String){
        val email = emailBody(email)
        val emailSend = service.setPostResetPass(email)
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
        compositDisposible.add(emailSend)

    }

    private fun handleSuccess (baseResponse: BaseResponse){
        view.onSuccessForgot(baseResponse)
        view.hideLoad()
    }

    private fun handleError(t: Throwable){
        view.onFailedAuth("Gagal request data  ")
        view.hideLoad()
    }

}