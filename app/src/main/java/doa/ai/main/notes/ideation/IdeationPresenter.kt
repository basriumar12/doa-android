package doa.ai.main.notes.ideation

import android.util.Log
import com.google.gson.Gson
import doa.ai.main.notes.bplan.plan.BodyShare
import doa.ai.main.notes.ideation.model.*

import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class IdeationPresenter {

    private var view: IdeationView? = null
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun ideationContract() {
        service = Service.Create.service()
    }

    fun attachView(view: IdeationView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun postIdeation(token: String, desc: String, title: String, labels: List<String>) {
        val bodyIdea = BodyIdea(desc, title, labels)
        val postIdea = service.setPostIdea(token, bodyIdea)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            it.result?.let { it1 -> view?.showSucces(it1) }
                            view?.hideLoad()
                            handleSuccess(it)

                        }, this::handleError
                )
        compositDisposible?.add(postIdea)

    }

    fun postUpdateStatusIdeation(token: String, body: BodyIdeaUpdateStatus) {

        val postIdea = service.setPostUpdateStatusIdea(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            it.result?.let { it1 -> view?.showSucces(it1) }
                            view?.hideLoad()
                            handleSuccess(it)

                        }, this::handleError
                )
        compositDisposible?.add(postIdea)

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
                            view?.message(it.message.toString())

                        }
                )
        compositDisposible?.add(sendData)


    }

    fun sendShare(token: String, body : BodyShare) {
        val sendData = service.postShare(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            view?.message(it.message.toString())



                        },
                        {
                            view?.hideLoad()
                            view?.showSuccesDelete(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }

    fun postIdeationPinned(token: String, body : BodyIdeaPinned) {

        val postIdea = service.setPostIdeationPinned(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            it.result?.let { it1 -> view?.showSucces(it1) }
                            view?.hideLoad()
                            handleSuccess(it)

                        }, this::handleError
                )
        compositDisposible?.add(postIdea)

    }

    fun postCopyIdeatin(token: String,bodyCopyIdea: BodyCopyIdea) {

        val postIdea = service.setPostCopyIdea(token, bodyCopyIdea)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            it.result?.let { it1 -> view?.showSucces(it1) }
                            view?.hideLoad()
                            handleSuccess(it)
                            view?.message(it.message.toString())

                        }, this::handleError
                )
        compositDisposible?.add(postIdea)

    }


    fun getDataIdeationArchived(token: String, page: Int) {

        val postIdea = service.getIdeaArchived(token, page, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.showDataIdea(it.results)
                            view?.hideLoad()


                        }, this::handleError
                )
        compositDisposible?.add(postIdea)

    }

    fun getDataIdeationTrashed(token: String, page: Int) {

        val postIdea = service.getIdeaTrashed(token, page, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.showDataIdea(it.results)
                            view?.hideLoad()


                        }, this::handleError
                )
        compositDisposible?.add(postIdea)

    }

    fun getDataIdeation(token: String , page: Int, status: String) {

        val postIdea = service.getIdea(token, page, status,"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.showDataIdea(it.results)
                            view?.hideLoad()


                        }, this::handleError
                )
        compositDisposible?.add(postIdea)

    }

    fun getDataIdeationAfter(token: String, page: Int, status: String) {

        val postIdea = service.getIdea(token, page, status,"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.showDataIdeaAfter(it.results)
                            view?.hideLoad()


                        }, this::handleError
                )
        compositDisposible?.add(postIdea)

    }

    fun deleteIdeation(token: String, id: Int) {
        val bodyIdea = BodyIdeaDelete(id)
        val postIdea = service.deletePostIdea(token, bodyIdea)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            it.result?.let { it1 -> view?.showSucces(it1) }
                            view?.hideLoad()
                            view?.message("Sukses Delete")
                            view?.showSuccesDelete("Sukses")


                        }, {
                    view?.message(it.message.toString())
                }
                )
        compositDisposible?.add(postIdea)

    }

    fun deleteFullIdeation(token: String, bodyIdea : BodyIdeaDeleteFull) {

        val postIdea = service.deleteFullPostIdea(token, bodyIdea)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            it.result?.let { it1 -> view?.showSucces(it1) }
                            view?.hideLoad()
                            view?.message("Sukses Delete")
                            view?.showSuccesDelete("Sukses")


                        }, {
                    view?.message(it.message.toString())
                }
                )
        compositDisposible?.add(postIdea)

    }

    private fun handleSuccess(responseIdea: ResponseIdea) {
        view?.hideLoad()
    }

    private fun handleError(t: Throwable) {
        Log.e("tag", "error get data idea ${t.message}")
        view?.hideLoad()
        t.message?.let { view?.message("Failed dapatkan data, error : " + it) }


    }
}