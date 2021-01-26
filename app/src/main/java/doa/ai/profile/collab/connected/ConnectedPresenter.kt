package doa.ai.profile.collab.connected

import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ConnectedPresenter {

    private  var view: ConnectedView? = null
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun connectedContract() {
        service = Service.Create.service()
    }

    fun attachView(view: ConnectedView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun getlistConnected(token: String, page: Int, search: String, status : String) {
        val sendData = service.getListCollab(token, page, status, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            view?.onSuccessConnected(it.results)



                        },
                        {
                            view?.hideLoad()
                            view?.onFailed(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }

    fun getlistConnectedAfter(token: String, page: Int, search: String, status : String) {
        val sendData = service.getListCollab(token, page, status, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            view?.onSuccessConnectedAfter(it.results)



                        },
                        {
                            view?.hideLoad()
                            view?.onFailed(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }


}