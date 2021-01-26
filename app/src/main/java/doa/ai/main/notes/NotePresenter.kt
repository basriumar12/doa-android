package doa.ai.main.notes

import doa.ai.network.Service
import doa.ai.profile.profiles.ProfileResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NotePresenter {
    private lateinit var view: NoteView
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun notesContract() {
        service = Service.Create.service()
    }

    fun attachView(view: NoteView) {
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
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                )
        compositDisposible?.add(sendProfile)
    }

    private fun handleSuccess(response: ProfileResponse) {
        view.onSuccessProfile(response.result)
    }

    private fun handleError(t: Throwable){
        view.onFailedProfile(t.localizedMessage)
    }
}