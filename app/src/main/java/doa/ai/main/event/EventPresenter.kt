package doa.ai.main.event


import android.util.Log
import com.google.gson.Gson
import doa.ai.main.notes.bplan.plan.SummaryList
import doa.ai.model.ProvinceModel
import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EventPresenter {

    private lateinit var service: Service
    private lateinit var services: Service
    private var compositDisposible: CompositeDisposable? = null
    private  var view: EventView? = null

    fun eventContract() {
        service = Service.Create.service()
        services = Service.CreateOther.service()
    }

    fun attachView(view: EventView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()
    }


    fun getProfession(token : String) {

        val eventData = service.getProfession(token, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.hideLoad()
                            handleSuccessProfession(it)
                        },
                        this::handleError
                )
        compositDisposible?.add(eventData)
    }

    fun getProvinceList(token: String, search: String) {
        val getProvince = service.getProvince(token, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view?.hideLoad()
                            view?.onSuccessProvince(it)},
                        this::handleError
                )
        compositDisposible?.add(getProvince)
    }


    fun getDistrictList(token: String, search : String) {
        val getDistrict = service.getDistrict(token, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.hideLoad()
                            view?.onSuccessDistrict(it)
                        },
                        this::handleError
                )
        compositDisposible?.add(getDistrict)
    }



    fun postEventSync(fullname: String, email: String, phone: String
                  ) {
        val evenModel = EventModelSync(email, fullname, phone)
        val eventData = services.postEventsync(evenModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            Log.e("tag","sukses sync post event register ${Gson().toJson(it)}")

                        },
                        this::handleError
                )
        compositDisposible?.add(eventData)
    }

    fun postEvent(token: String, fullname: String, email: String, phone: String, program: Int, plan: Int, organization : String,
                  profession : String, document : String, district : Int
                  ) {
        val evenModel = EventModel(email, fullname, phone, plan, program, organization, profession, document, district)
        val eventData = service.postEvent(token, evenModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            handleSuccessEvent(it)},
                        {  view?.onFailedEvent("Gagal request, error :  ${it.message}") }
                )
        compositDisposible?.add(eventData)
    }


    fun postEventwithoutBplan(token: String, fullname: String, email: String, phone: String, program: Int, organization : String,
                  profession : String, document : String, district : Int
                  ) {
        val evenModel = EventModelWithoutBplan(email, fullname, phone, program, organization, profession, document, district)
        val eventData = service.postEventWithoutBplan(token, evenModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            handleSuccessEvent(it)},
                        {  view?.onFailedEvent("Gagal request, error :  ${it.message}") }
                )
        compositDisposible?.add(eventData)
    }
    fun postEventWithoutBplanDocument(token: String, fullname: String, email: String, phone: String, program: Int,  organization : String,
                  profession : String, idDistrict : Int
                  ) {
        val evenModel = EventModelWithoutBplanDocument(email, fullname, phone,  program, organization, profession,idDistrict)
        val eventData = service.postEventWithoutBplanDocument(token, evenModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {handleSuccessEvent(it)},
                        {  view?.onFailedEvent("Gagal request, error :  ${it.message}") }
                )
        compositDisposible?.add(eventData)
    }

    fun getDataPlan(token: String) {
        val loginSend = service.getSummaryList(token,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.onSuccessPlan(it)
                            handleSuccessPlan(it)},
                        this::handleError
                )
        compositDisposible?.add(loginSend)
    }

    fun getCheckRegisterEvent(token: String, id : Int) {
        val loginSend = service.getCheckRegisterEvent(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.onSuccessCheckRegisterEvent(it)
                            view?.hideLoad()

                        },
                        this::handleError
                )
        compositDisposible?.add(loginSend)
    }

    private fun handleSuccessPlan (planResponse: SummaryList){
        view?.onSuccessPlan(planResponse)
    }
    private fun handleSuccessProfession (responseProfession: ResponseProfession){
        view?.onSuccessGetProfession(responseProfession)
    }

    private fun handleSuccessEvent(eventResponse: EventResponse){
        Log.e("Tag","error status ${eventResponse.status}")
        view?.hideLoad()
        if (eventResponse.status.equals(400)){
            view?.onFailedEvent(eventResponse.message.toString())
        } else if (eventResponse.status.equals(404)){
            view?.onFailedEvent(eventResponse.message.toString())
        }else {
            view?.onSuccessEvent(eventResponse)
        }
    }

    private fun handleError(t: Throwable){
        view?.hideLoad()
      //  view?.onFailedEvent("Gagal request ${t.message}")
        Log.e("tag","errror ${t.message}")
    }

}