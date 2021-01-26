package doa.ai.main.notes.bplan.strategy

import android.util.Log
import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class StrategyPresenter {

    private lateinit var strategicOpportunities: MutableList<String>
    private lateinit var strategicStep: MutableList<String>
    private lateinit var view: StrategyView
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun strategyContract() {
        service = Service.Create.service()
    }

    fun attachView(view: StrategyView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun postSegmentStrategy(token: String, plan: Int, year: String,
                            strategicOpportunities: MutableList<String>,
                            strategicStep: MutableList<String>) {


        val strategy = StrategyModel(plan, year, strategicOpportunities, strategicStep)
        val sendSegmentMarket = service.postSegmentStrategy(token, strategy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                )
        compositDisposible?.add(sendSegmentMarket)
    }

    fun postSegmentStrategyEdit(token: String, plan: Int, year: String,
                            strategicOpportunities: MutableList<String>,
                            strategicStep: MutableList<String>) {


        val strategy = StrategyModel(plan, year, strategicOpportunities, strategicStep)
        val sendSegmentMarket = service.postSegmentStrategyEdit(token, strategy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                )
        compositDisposible?.add(sendSegmentMarket)
    }

    fun getDetailSegmentStrategy(token: String, id: Int) {

        val sendSegmentMarket = service.getDetailSegmentStrategy(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.onSuccessDetailStrategy(it)
                        },
                        {
                            Log.e("tag","error strategy detail ${it.message}")
                        }
                )
        compositDisposible?.add(sendSegmentMarket)
    }

    private fun handleSuccess(response: StrategyResponse) {
        view.onSuccessStrategy(response.result.id)
    }

    private fun handleError(t: Throwable) {
        view.onFailedStrategy(t.localizedMessage)

    }
}