package doa.ai.profile.edit

import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EditProfilePresenter {

    private lateinit var service: Service
    private lateinit var view: EditProfileView
    private var compositDisposible: CompositeDisposable? = null

    fun editProfileContract() {
        service = Service.Create.service()
    }

    fun attachView(view: EditProfileView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun getProfile(token: String) {
        val sendProfile = service.getProfile(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view?.onSuccessProfile(it.result)
                        },
                        this::handleError
                )
        compositDisposible?.add(sendProfile)
    }

    fun postProfile(token: String, fullname: String, phone: String, email: String, profession: String,
                    about: String, photo: String,
                    subDistrict : Int,
                    address : String,
                    village : String,
                    rt : Int,
                    rw : Int
                    ) {
        val profile = EditProfileBody(fullname, phone, email, profession, about, photo,
                                        subDistrict,address,village,0,rt,rw
                )
        val sendProfile = service.postProfile(token, profile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                )
        compositDisposible?.add(sendProfile)
    }

    private fun handleSuccess(response: EditProfileResponse) {
        view.onSuccerEditProfile(response.result)
    }

    private fun handleError(t: Throwable) {
        view.onFailedEditProfile(t.localizedMessage)
    }


    fun getProvinceList(token: String, search: String) {
        val getProvince = service.getProvince(token, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view?.onSuccessProvince(it)
                        },
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
                        {
                            view?.onSuccessDistrict(it)
                        },
                        this::handleError
                )
        compositDisposible?.add(getDistrict)
    }

    fun getSubDistrict(token: String, search: String) {
        val getSubDistrict = service.getSubDistrict(token, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.onSuccessSubDistrict(it) },
                        this::handleError
                )
        compositDisposible?.add(getSubDistrict)
    }
}