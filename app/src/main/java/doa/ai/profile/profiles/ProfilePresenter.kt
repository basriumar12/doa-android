package doa.ai.profile.profiles

import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfilePresenter {

    private  var view: ProfileView? = null
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun profileContract() {
        service = Service.Create.service()
    }

    fun attachView(view: ProfileView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun getProfile(token: String) {
        val sendProfile = service.getProfile(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                     view?.showLoad()
                }
                .subscribe(
                        { handleSuccess(it) },
                        { handleError(it) }
                )
        compositDisposible?.add(sendProfile)
    }

    private fun handleSuccess(response: ProfileResponse) {
        view?.onSuccessProfile(response.result)
        view?.hideLoad()
    }

    private fun handleError(t: Throwable) {
        view?.onFailedProfile(t.localizedMessage)
          view?.hideLoad()
    }
}