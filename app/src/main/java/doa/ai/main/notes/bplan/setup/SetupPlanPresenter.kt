package doa.ai.main.notes.bplan.setup

import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SetupPlanPresenter {

    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null
    private  var view: SetupPlanView? = null

    fun setupContract() {
        service = Service.Create.service()
    }

    fun attachView(view: SetupPlanView) {
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
                            //view?.onFailedBPlan(it.message.toString())


                        }
                )
        compositDisposible?.add(sendData)


    }

    fun sendDataPlan(token: String,title: String, sub_sector: Int, start_date: String, end_date: String, reminder: Boolean, reminder_time: String, repeat: String) {

        val plan = SetupPlan(title, sub_sector, start_date, end_date, reminder, reminder_time, repeat)
        val planSend = service.postPlan(token,plan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.hideLoad()
                            if(it.status.equals(400)){
                                view?.errorMessage(it.message.toString())
                                if (it.message.equals("Free member only can create 1 business plan.")){
                                    view?.onUpgradeToPremium()
                                }
                                if (it.message.equals("Anggota gratis hanya dapat membuat 1 rencana bisnis.")){
                                    view?.onUpgradeToPremium()
                                }

                            } else {
                                handleSuccess(it)
                            }

                        },
                        this::handleError
                )
        compositDisposible?.add(planSend)
    }

    fun setPlanSector(token: String) {
        val planSector = service.getSectorPlan(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(
                        this::handleSuccesPlanSectors,
                        this::handleError
                )
        compositDisposible?.add(planSector)
    }

    fun setPlanSubSector(token: String, id: Int) {
        val planSubSector = service.getSubSectorPlan(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccessPlanSubSectors,
                        this::handleError
                )
        compositDisposible?.add(planSubSector)
    }

    private fun handleSuccessPlanSubSectors(planSubSectorsResponse: PlanSubSectors) {
        view?.onSuccesPlanSubSectors(planSubSectorsResponse.result.sub_sectors)
    }

    private fun handleSuccesPlanSectors(planSectorResponse: PlanSectors){
        view?.onSuccessPlanSectors(planSectorResponse.results)
    }

    private fun handleSuccess (planResponse: PlanResponse){
        view?.hideLoad()
        view?.onSuccessSetup(planResponse.result.id)
    }

    private fun handleError(t: Throwable){
        view?.hideLoad()
        view?.onFailedSetup(t.localizedMessage)
    }
}