package doa.ai.main.notes.bplan.plan.archive

import android.util.Log
import doa.ai.main.notes.bplan.setup.SetupPlanView
import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ArchiveBplanPresenter {
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null
    private  var view: ArchiveBplanView? = null

    fun setupContract() {
        service = Service.Create.service()
    }

    fun attachView(view: ArchiveBplanView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()
    }

    fun getlistBPlan(token: String, page: Int, search: String, status: String) {
        val bPlanSend = service.getPlanList(token, page, search, "false",status)
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
                            Log.e("TAG","error ${it.message}")

                        }
                )
        compositDisposible?.add(bPlanSend)


    }

    fun getlistBPlanAfter(token: String, page: Int, search: String, status: String) {
        val bPlanSend = service.getPlanList(token, page, search,"false", status)
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
        compositDisposible?.add(bPlanSend)


    }
}