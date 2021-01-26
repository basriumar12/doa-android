package doa.ai.main.notes.bplan.finance

import android.util.Log
import doa.ai.main.notes.bplan.strategy.StrategyView
import doa.ai.model.Currency
import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class FinancePresenter {

    private lateinit var view: FinanceView
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun financeContract() {
        service = Service.Create.service()
    }

    fun attachView(view: FinanceView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun postSegmentFinance(token: String, plan: Int, currency: String, startYear: Int,
                           cashInvested: Double, salesYear: Double, salesGrowth: Int, cashSales: Int,
                           expansesFirst: Double, expansesGrowth: Int, customerDay: Int,supplier: Int) {

        val finance = FinanceModel(plan, currency,
                startYear,
                cashInvested,salesYear,salesGrowth,cashSales,expansesFirst,expansesGrowth,customerDay,supplier)

        val sendSegmentFinance = service.postSegmentFinance(token, finance)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                )
        compositDisposible?.add(sendSegmentFinance)
    }

    fun postSegmentFinanceEdit(token: String, plan: Int, currency: String, startYear: Int,
                           cashInvested: Double, salesYear: Double, salesGrowth: Int, cashSales: Int,
                           expansesFirst: Double, expansesGrowth: Int, customerDay: Int,supplier: Int) {

        val finance = FinanceModel(plan, currency,
                startYear,
                cashInvested,salesYear,salesGrowth,cashSales,expansesFirst,expansesGrowth,customerDay,supplier)

        val sendSegmentFinance = service.postSegmentFinanceEdit(token, finance)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                )
        compositDisposible?.add(sendSegmentFinance)
    }

    fun getDetailSegmentFinance(token: String, id: Int) {

        val sendSegmentFinance = service.getDetailSegmentFinance(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {view.onSuccessFinanceData(it)},
                        {
                            Log.e("tag","error finnance detail ${it.message}")
                        }
                )
        compositDisposible?.add(sendSegmentFinance)
    }

    fun getCurrency(token: String, page: Int, search: String) {
         val getCurrency = service.getCurrency(token, page, search)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(
                         this::handleCurrency,
                         this::handleError
                 )
        compositDisposible?.add(getCurrency)
    }

    private fun handleCurrency(response: Currency) {
        view.onSuccessCurency(response.results)
    }

    private fun handleSuccess (response: FinanceResponse){
        view.onSuccessFinance(response)
    }

    private fun handleError(t: Throwable){
        view.onFailedFinance(t.localizedMessage)
    }
}