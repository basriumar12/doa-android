package doa.ai.login.reset

import doa.ai.base.base_model.BaseResponse
import doa.ai.login.forgot.confirmPassword
import doa.ai.login.logins.Login
import doa.ai.login.logins.LoginView
import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ResetPasswordPresenter {
    private lateinit var view: ResetPasswordView
    private lateinit var service: Service
    private lateinit var compositDisposible: CompositeDisposable


    fun resetPasswordContract() {
        service = Service.Create.service()
    }

    fun attachView(view: ResetPasswordView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        compositDisposible.dispose()
    }

    fun sendDataResetPassword(uid : String, token : String, newPassword1 : String, newPassword2 : String) {
        val data = confirmPassword(uid, token, newPassword1, newPassword2)
        val dataSend = service.postResetPassword(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view.showLoad()
                }

                .subscribe(

                        {
                            if (it.status.equals(400)){
                                view.showError(it.message.toString())
                            } else{
                                handleSuccess(it)
                            }

                        },
                        this::handleError
                )
        compositDisposible.add(dataSend)


    }
    private fun handleSuccess (baseResponse: BaseResponse){
        view.onSuccessForgot(baseResponse)

    }

    private fun handleError(t : Throwable) {
        view.onFailedAuth(t.localizedMessage)

    }


}