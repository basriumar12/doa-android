package doa.ai.main.notes.bplan.market

import android.util.Log
import com.google.gson.Gson
import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MarketPresenter {

    private lateinit var keyCompetitors: ArrayList<KeyCompetitors>
    private lateinit var view: MarketView
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun marketContract() {
        service = Service.Create.service()
    }

    fun attachView(view: MarketView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun postSegmentMarket(token: String, plan: Int, product: String,
                          targetCustomer: String,
                          geographicMarket: String,
                          competitiveAdvantage: String,
                          name: MutableList<String>,
                          weakeness: MutableList<String>,
                          customer_managements: MutableList<String>,
                          customer_reach_ways: MutableList<String>

                        ) {


        keyCompetitors = ArrayList()
        keyCompetitors.add(KeyCompetitors("Nama", name))
        keyCompetitors.add(KeyCompetitors("Kelemahan", weakeness))

        val market = MarketModel(plan, product, targetCustomer, geographicMarket, competitiveAdvantage,keyCompetitors, customer_managements, customer_reach_ways)
        Log.e("tag ","market ${Gson().toJson(market)}")
        val sendSegmentMarket = service.postSegmentMarket(token, market)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                )
        compositDisposible?.add(sendSegmentMarket)
    }

    fun postSegmentMarketEdit(token: String, plan: Int, product: String,
                          targetCustomer: String,
                          geographicMarket: String,
                          competitiveAdvantage: String,
                          name: MutableList<String>,
                          weakeness: MutableList<String>,
                          customer_managements: MutableList<String>,
                          customer_reach_ways: MutableList<String>

                        ) {


        keyCompetitors = ArrayList()
        keyCompetitors.add(KeyCompetitors("Nama", name))
        keyCompetitors.add(KeyCompetitors("Kelemahan", weakeness))

        val market = MarketModel(plan, product, targetCustomer, geographicMarket, competitiveAdvantage,keyCompetitors, customer_managements, customer_reach_ways)
        Log.e("tag ","market ${Gson().toJson(market)}")
        val sendSegmentMarket = service.postSegmentMarketEdit(token, market)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                )
        compositDisposible?.add(sendSegmentMarket)
    }

    fun getDetailSegmentMarket (token : String, id : Int){
        val data = service.postDetailSegmentMarket(token, id.toLong())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(

                        {
                            view.onSuccesDetailMarket(it)
                        },
                        {
                        }
                )
        compositDisposible?.add(data)

//        service.postDetailSegmentMarket(token, id).enqueue(object : retrofit2.Callback<MarketResponse>{
//            override fun onFailure(call: Call<MarketResponse>, t: Throwable) {
//                Log.e("tag"," error market ${t.message}")
//            }
//
//            override fun onResponse(call: Call<MarketResponse>, response: Response<MarketResponse>) {
//                Log.e("tag"," market ${response.body()?.message}")
//                //response.body()?.let { view.onSuccesDetailMarket(it) }
//            }
//        })

    }

    private fun handleSuccess (response: MarketResponse){
        if (response.status.equals(400)){
            view.showfaildRequest(response.message)
        } else {
            view.onSuccessMarket(response.result.id)
        }
    }

    private fun handleError(t: Throwable){
        view.onFailedMarket("Gagal request data ke server : "+t.localizedMessage)

    }
}