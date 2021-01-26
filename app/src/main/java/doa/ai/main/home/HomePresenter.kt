package doa.ai.main.home

import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenter {

    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null
    private var view: HomeView? = null

    fun homeContract() {
        service = Service.Create.service()
    }

    fun attachView(view: HomeView) {
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

    fun getDataIdeationPinned(token: String, page: Int) {

        val postIdea = service.getIdeaPinned(token, page, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.showDataIdeaPinned(it.results)
                            view?.hideLoad()


                        }, this::handleError
                )
        compositDisposible?.add(postIdea)
    }

    fun getDataIdeationPinnedAfter(token: String, page: Int) {

        val postIdea = service.getIdeaPinned(token, page, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.showDataIdeaPinned(it.results)
                            view?.hideLoad()


                        }, this::handleError
                )
        compositDisposible?.add(postIdea)

    }

    fun getHomeData(token: String) {
        val homeData = service.getHome(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showLoad()
                }
                .subscribe(
                        {
                            view?.hideLoad()
                            handleSuccesHome(it)
                        }
                        ,
                        {
                            handleError(it)
                            view?.hideLoad()
                        }
                )
        compositDisposible?.add(homeData)
    }

    private fun handleSuccesHome(homeResponse: HomeResponse) {
        view?.hideLoad()
        view?.onSuccessHome(homeResponse)
    }

    private fun handleError(t: Throwable) {
        view?.hideLoad()
        view?.onFailedHome(t.localizedMessage)
    }
}