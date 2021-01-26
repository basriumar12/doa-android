package doa.ai.profile.collab.find_out

import android.util.Log
import com.google.gson.Gson
import doa.ai.main.notes.NoteView
import doa.ai.network.Service
import doa.ai.profile.collab.BodySendCollab
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SuggestionPresenter {

    private  var view: SuggestionView? = null
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun suggestionContract() {
        service = Service.Create.service()
    }

    fun attachView(view: SuggestionView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun postSendCollab(token: String, body : BodySendCollab) {
        val sendData = service.postSendCollab(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            view?.onSucces("sukses kirim permintaan")

                        },
                        {
                            view?.hideLoad()
                            view?.onFailed(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }

    fun getlistSuggestionSearch(token: String, page: Int, search : String, sector: String, order : String) {
        val sendData = service.getSuggestionCollabSearch(token, page, search ,sector,order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            view?.onSuccessSuggestion(it.results)



                        },
                        {
                            view?.hideLoad()
                            view?.onFailed(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }

  fun getlistSuggestion(token: String, page: Int, search: String) {
        val sendData = service.getSuggestionCollab(token, page, search, "asc")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            view?.onSuccessSuggestion(it.results)



                        },
                        {
                            view?.hideLoad()
                            view?.onFailed(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }

    fun getlistSuggestionAfter(token: String, page: Int, search: String) {
        val sendData = service.getSuggestionCollab(token, page, search, "asc")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoadBottom()
                }.subscribe(
                        {
                            view?.hideLoadBottom()
                            view?.onSuccessSuggestionAfter(it.results)



                        },
                        {
                            view?.hideLoadBottom()
                            view?.onFailed(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }

}