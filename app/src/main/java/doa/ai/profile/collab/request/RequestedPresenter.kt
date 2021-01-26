package doa.ai.profile.collab.request

import android.util.Log
import doa.ai.network.Service
import doa.ai.profile.collab.BodyDeleteCollab
import doa.ai.profile.collab.BodySendCollab
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RequestedPresenter {

    private  var view: RequestedView? = null
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun contract() {
        service = Service.Create.service()
    }

    fun attachView(view: RequestedView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun postSendApproveCollab(token: String, body : BodySendCollab) {
        val sendData = service.postSendApproveCollab(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            if (it.status.equals(400)){
                                view?.onSucces(it.message.toString())
                            } else{
                                view?.onSucces("Sukses setujui")
                                view?.onSuccessApproveDelete(it.results)
                            }

                        },
                        {
                            view?.hideLoad()
                            view?.onFailed(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }

    fun postSendDeleteCollab(token: String, body : BodyDeleteCollab) {
        val sendData = service.postSendDeleteCollab(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            Log.e("TAG","status delete ${it.status}")
                            if (it.status == 400){
                                view?.onSucces(it.message.toString())
                            } else{
                                view?.onSucces("Sukses hapus")
                                view?.onSuccessApproveDelete(it.results)
                            }

                        },
                        {
                            view?.hideLoad()
                            view?.onFailed(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }

    fun getlistConnected(token: String, page: Int, search: String, status : String) {
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

    fun getlistConnectedAfter(token: String, page: Int, search: String, status : String) {
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