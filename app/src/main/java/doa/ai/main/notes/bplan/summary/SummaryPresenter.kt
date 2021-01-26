package doa.ai.main.notes.bplan.summary

import android.util.Log
import com.google.gson.Gson
import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SummaryPresenter {

    private  var view: SummaryView? = null
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun summaryContract() {
        service = Service.Create.service()
    }

    fun attachView(view: SummaryView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun summary(token: String,plan: Int, business: Int, market: Int, strategy: Int, finance: Int, conclusion: String, attachment: String) {
        val summary = SummaryBody(plan, business, market, strategy, finance, attachment, conclusion)
        val sendSummary = service.postSummary(token,summary)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                )
        compositDisposible?.add(sendSummary)

    }

    fun detailSummary(token: String, id: Int) {
        val getSummary = service.getSummaryDetail(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            handleSuccess(it)
                            Log.e("tag","get data summary ${it.status}")

                        }
                        ,
                        this::handleError
                )
        compositDisposible?.add(getSummary)
    }

    private fun handleSuccess(response: SummaryResponse) {

        view?.onSuccessSummary(response)
    }

    private fun handleError(t: Throwable) {
        Log.e("tag","error summary ${t.message}")
        view?.onFailedSummary(t.localizedMessage)
    }
}