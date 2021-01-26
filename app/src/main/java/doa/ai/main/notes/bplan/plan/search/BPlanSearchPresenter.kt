package doa.ai.main.notes.bplan.plan.search

import android.util.Log
import com.google.gson.Gson
import doa.ai.main.notes.bplan.plan.*
import doa.ai.main.notes.bplan.setup.SetupPlan
import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BPlanSearchPresenter {

    private var view: BPlanSearchView? = null
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun planContract() {
        service = Service.Create.service()
    }

    fun attachView(view: BPlanSearchView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun sendArchivedDataPlans(token: String, bodyEditBplan: BodyEditPlans) {


        val planSend = service.postEditPlans(token, bodyEditBplan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.hideLoad()
                            if (it.status == 400) {
                                view?.onSuccess(it.message.toString())
                            } else {
                                view?.successEditBPLan(it)
                                view?.onSuccess(it.message.toString())
                            }


                        },
                        {
                            view?.hideLoad()
                            view?.failed(it.message.toString())
                        }
                )
        compositDisposible?.add(planSend)
    }
    fun sendPinnedPlans(token: String, bodyPinnedPlan: BodyPinnedPlans) {


        val planSend = service.postPinnedPlans(token, bodyPinnedPlan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.hideLoad()
                            if (it.status == 400) {
                                view?.onSuccess(it.message.toString())
                            } else {
                                view?.successEditBPLan(it)
                                view?.onSuccess("Sukses rencana usaha di sematkan")
                            }


                        },
                        {
                            view?.hideLoad()
                            view?.failed(it.message.toString())
                        }
                )
        compositDisposible?.add(planSend)
    }
    fun sendArchivedDataPlan(token: String, bodyEditBplan: BodyEditBplan) {


        val planSend = service.postEditPlan(token, bodyEditBplan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.hideLoad()
                            if (it.status == 400) {
                                view?.onSuccess(it.message.toString())
                            } else {
                                view?.successEditBPLan(it)
                                view?.onSuccess("Sukses rencana usaha di arsip")
                            }


                        },
                        {
                            view?.hideLoad()
                            view?.failed(it.message.toString())
                        }
                )
        compositDisposible?.add(planSend)
    }

    fun sendCopyPlan(token: String, bodyCopyBplan: BodyCopyPlan) {


        val planSend = service.postCopyPlan(token, bodyCopyBplan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.hideLoad()
                            if (it.status == 400) {
                                view?.onSuccess(it.message.toString())
                            } else {
                                view?.successEditBPLan(it)
                                view?.onSuccess("Sukses copy rencana usaha")
                            }


                        },
                        {
                            view?.hideLoad()
                            view?.failed(it.message.toString())
                        }
                )
        compositDisposible?.add(planSend)
    }


    fun getlistBPlan(token: String, page: Int, search: String, status: String) {
        val bPlanSend = service.getPlanList(token, page, search,"false", status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            view?.onSuccessBPlans(it.results)


                        },
                        {
                            view?.hideLoad()
                            view?.failed(it.message.toString())


                        }
                )
        compositDisposible?.add(bPlanSend)


    }


    fun getlistBPlanAfter(token: String, page: Int, search: String, status: String) {
        val bPlanSend = service.getPlanList(token, page, search, "false",status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }.subscribe(
                        {
                            view?.hideLoad()
                            view?.onSuccessBPlansAfter(it.results)


                        },
                        {
                            view?.hideLoad()
                            view?.failed(it.message.toString())


                        }
                )
        compositDisposible?.add(bPlanSend)


    }



    fun sendTrashDataPlan(token: String, bodyEditBplan: BodyEditBplan) {


        val planSend = service.postEditPlan(token, bodyEditBplan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.hideLoad()

                            if (it.status == 400) {
                                view?.onSuccess(it.message.toString())
                            } else {
                                view?.successEditBPLan(it)
                                view?.onSuccess("Sukses rencana usaha di pindahkan")
                            }

                        },
                        {
                            view?.hideLoad()
                            view?.failed(it.message.toString())
                        }
                )
        compositDisposible?.add(planSend)
    }

    fun sendPinnedPlan(token: String, bodyPinnedPlan: BodyPinnedPlan) {


        val planSend = service.postPinnedPlan(token, bodyPinnedPlan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.hideLoad()
                            if (it.status == 400) {
                                view?.onSuccess(it.message.toString())
                            } else {
                                view?.successEditBPLan(it)
                                view?.onSuccess("Sukses rencana usaha di sematkan")
                            }


                        },
                        {
                            view?.hideLoad()
                            view?.failed(it.message.toString())
                        }
                )
        compositDisposible?.add(planSend)
    }

//    fun deletePinnedPlan(token: String, bodyPinnedPlan: BodyDelPinnedPlan) {
//
//
//        val planSend = service.deletePinnedPlan(token, bodyPinnedPlan)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe {
//                    view?.showLoad()
//                }
//                .subscribe(
//                        {
//                            view?.hideLoad()
//
//                            view?.successEditBPLan(it)
//                            view?.onSuccess("Sukses rencana usaha di hapus dari sematkan")
//
//
//                        },
//                        {
//                            view?.hideLoad()
//                            view?.failed(it.message.toString())
//                        }
//                )
//        compositDisposible?.add(planSend)
//    }




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
                            view?.onFailedBPlan(it.message.toString())


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
                            view?.onFailedBPlan(it.message.toString())


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
                            view?.onSuccess(it.message.toString())



                        },
                        {
                            view?.hideLoad()
                            view?.onFailedBPlan(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }


    private fun handleSuccess(planResponse: SummaryList) {

        // view?.onSuccessBPlan(planResponse.results)
    }

    private fun handleError(t: Throwable) {
        view?.onFailedBPlan(t.localizedMessage)
    }
}