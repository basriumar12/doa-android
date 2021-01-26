package doa.ai.profile.collab.sent

import android.util.Log
import com.google.gson.Gson
import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SentPresenter {

    private  var view: SentView? = null
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun contract() {
        service = Service.Create.service()
    }

    fun attachView(view: SentView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun getlistSent(token: String, page: Int, search: String, status : String) {
        val sendData = service.getListCollabRequested(token, page, status, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            view?.onSuccess(it.results)



                        },
                        {
                        view?.hideLoad()
                            view?.onFailed(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }

    fun getlistSentAfter(token: String, page: Int, search: String, status : String) {
        val sendData = service.getListCollabRequested(token, page, status, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            view?.onSuccessAfter(it.results)



                        },
                        {
                            view?.hideLoad()
                            view?.onFailed(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }


}