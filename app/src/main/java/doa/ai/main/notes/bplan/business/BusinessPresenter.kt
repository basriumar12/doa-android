package doa.ai.main.notes.bplan.business

import android.util.Log
import doa.ai.model.CountryModel
import doa.ai.model.DistrictModel
import doa.ai.model.ProvinceModel
import doa.ai.model.SubDistrictModel
import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BusinessPresenter {

    private lateinit var keyResources: MutableList<String>
    private lateinit var namePersonal: MutableList<String>
    private lateinit var positionPersonal: MutableList<String>
    private lateinit var expertisPersonal: MutableList<String>
    private lateinit var keyPersonal: MutableList<KeyPersonal>
    private lateinit var view: BusinessView
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun businessContract() {
        service = Service.Create.service()
    }

    fun attachView(view: BusinessView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun postBusinessSegment(token: String,plan: Int, structure: String,
                            location: String, subDistrict: Int,
                            village: String,
                             keyResourcess: MutableList<String>,
                            number: Int, rt: Int, rw:Int,
                            namePersonals: MutableList<String>,
                            positionPersonals: MutableList<String>,
                            expertisPersonals: MutableList<String>,
                            businesspartners: MutableList<String>,
                            businessActivities: MutableList<String>,
                            operatingExpenses: MutableList<String>

                            ) {

        keyPersonal = ArrayList()
        keyPersonal.add(KeyPersonal("Nama",namePersonals))
        keyPersonal.add(KeyPersonal("Posisi",positionPersonals))
        keyPersonal.add(KeyPersonal("Keahlian",expertisPersonals))

        val businessSegment = BusinessModel(plan, structure, location,subDistrict,village,number,rt,rw, keyResourcess,
                businesspartners,
                businessActivities,
                operatingExpenses,
                keyPersonal)
        val sendBusiness = service.posSegmentBusiness(token, businessSegment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                )
        compositDisposible?.add(sendBusiness)
    }

    fun postBusinessEditSegment(token: String,plan: Int, structure: String,
                            location: String, subDistrict: Int,
                            village: String,
                             keyResourcess: MutableList<String>,
                            number: Int, rt: Int, rw:Int,
                            namePersonals: MutableList<String>,
                            positionPersonals: MutableList<String>,
                            expertisPersonals: MutableList<String>,
                            businesspartners: MutableList<String>,
                            businessActivities: MutableList<String>,
                            operatingExpenses: MutableList<String>

                            ) {

        keyPersonal = ArrayList()
        keyPersonal.add(KeyPersonal("Nama",namePersonals))
        keyPersonal.add(KeyPersonal("Posisi",positionPersonals))
        keyPersonal.add(KeyPersonal("Keahlian",expertisPersonals))

        val businessSegment = BusinessModel(plan, structure, location,subDistrict,village,number,rt,rw, keyResourcess,
                businesspartners,
                businessActivities,
                operatingExpenses,
                keyPersonal)
        val sendBusiness = service.posSegmentEditBusiness(token, businessSegment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                )
        compositDisposible?.add(sendBusiness)
    }


    fun getDetailBusiness(token: String, id: Int) {
        val getProvince = service.getDetailSegmentBusiness(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.onSuccessgetDataBusiness(it.result)
                        },
                        this::handleError
                )
        compositDisposible?.add(getProvince)
    }


    fun getProvinceList(token: String, search: String) {
        val getProvince = service.getProvince(token, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccessProvince,
                        this::handleError
                )
        compositDisposible?.add(getProvince)
    }
    fun getDetailSubDistrict(token: String, id: Int) {
        val getProvince = service.getDetailSubDistrict(token, id, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view?.onSubDistrictDetail(it)
                        },
                        this::handleError
                )
        compositDisposible?.add(getProvince)
    }

    fun getDistrictList(token: String, search: String) {
        val getDistrict = service.getDistrict(token, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccessDistrict,
                        this::handleError
                )
        compositDisposible?.add(getDistrict)
    }

    fun getSubDistrict(token: String, search: String) {
        val getSubDistrict = service.getSubDistrict (token, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccessSubDistrict,
                        this::handleError
                )
        compositDisposible?.add(getSubDistrict)
    }

    fun getPostalCode(token: String, search: String) {
        val postalCode = service.getPostalCode(token, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccessPostalCode,
                        this::handleError
                )
        compositDisposible?.add(postalCode)
    }

    private fun handleSuccessPostalCode(response: SubDistrictModel) {
        view.onSuccessPostalCode(response)
    }

    private fun handleSuccessSubDistrict(response: DistrictModel) {
        view.onSuccessSubDistrict(response)
    }

    private fun handleSuccessDistrict(response: ProvinceModel) {
        view.onSuccessDistrict(response)
    }

    private fun handleSuccessProvince(response: CountryModel) {
        view.onSuccessProvince(response)
    }

    private fun handleSuccess (response: BusinessSegmentResponse){
        view.onSuccessGetID(response.result.id)
    }

    private fun handleError(t: Throwable){
        view.onFailedGetID(t.localizedMessage)

    }
}