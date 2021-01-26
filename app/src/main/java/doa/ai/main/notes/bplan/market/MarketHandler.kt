package doa.ai.main.notes.bplan.market

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.extentions.Dialog
import doa.ai.main.notes.bplan.business.adapter.AdapterKeySuccess
import doa.ai.main.notes.bplan.market.adapter.AdapterCompotitorsMarket
import doa.ai.main.notes.bplan.strategy.StrategyActivity
import doa.ai.main.notes.bplan.strategy.adapter.AdapterStrategyOportunities
import doa.ai.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_bplan_business.*
import kotlinx.android.synthetic.main.activity_bplan_market.*
import kotlinx.android.synthetic.main.activity_bplan_strategy.*
import kotlinx.android.synthetic.main.view_compotitors_market.*

class MarketActivity : BaseActivity(), MarketView {


    var planID: Int? = 0
    var businessID: Int? = 0
    var tokenLogin: String? = ""
    private lateinit var presenter: MarketPresenter
    private lateinit var prefs: SharedPreferences
    var compotitorsMarket: ArrayList<KeyCompetitorsModel> = ArrayList()
    private  var nameCompotitorsMarket: MutableList<String> = ArrayList()
    private  var wekanessCompotitorsMarket: MutableList<String> = ArrayList()

    lateinit var adapterCompotitorsMarket: AdapterCompotitorsMarket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bplan_market)
        setSupportActionBar(toolbarBplanMarket)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = MarketPresenter()
        presenter.marketContract()

        prefs = applicationContext.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs.getString("loginToken", "")
        planID = intent.getIntExtra("planID", 0)
        businessID = intent.getIntExtra("businessID", 0)
        handlePostSegmentMarket()
        handleView()
    }

    private fun handleView() {
        compotitorsMarket = ArrayList()
        compotitorsMarket.add(KeyCompetitorsModel("",""))
        rv_compotitors_market.layoutManager = LinearLayoutManager(this)
        adapterCompotitorsMarket = AdapterCompotitorsMarket(compotitorsMarket, this)
        rv_compotitors_market.adapter = adapterCompotitorsMarket

        tv_add_compotitor_market.setOnClickListener {

            compotitorsMarket.add(KeyCompetitorsModel("",""))
            rv_compotitors_market.adapter = adapterCompotitorsMarket
            adapterCompotitorsMarket.notifyDataSetChanged()
        }

        //delete item
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_compotitors_market.adapter as AdapterCompotitorsMarket
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv_compotitors_market)




        img_produk_jasa_pasar.setOnClickListener {
            Dialog.progressDialog(this, getString(R.string.produk_ditawrkan))
        }
        img_target_pelanggan_pasar.setOnClickListener {
            Dialog.progressDialog(this, getString(R.string.target_pelanggan))
        }
        img_geografis_dari_pasar.setOnClickListener {
            Dialog.progressDialog(this, getString(R.string.georrafis_dari_pasar))
        }
        img_keunggulan_kompetitif_pasar.setOnClickListener {
            Dialog.progressDialog(this, getString(R.string.keunggulan_kompotetif_pasar))
        }
        img_pesaing_pasar.setOnClickListener {
            Dialog.progressDialog(this, getString(R.string.pesaing_pasar))
        }


    }

    private fun handlePostSegmentMarket() {
        marketCreate.setOnClickListener {
            planID?.let { it1 ->

                if (editMarket.text.toString().isEmpty()) {
                    showErrorMessage(getString(R.string.data_harus_lengkap))
                } else if (editTargetCustomer.text.toString().isEmpty()) {
                    showErrorMessage(getString(R.string.data_harus_lengkap))
                } else if (editGeografic.text.toString().isEmpty()) {
                    showErrorMessage(getString(R.string.data_harus_lengkap))
                } else if (editCompetitive.text.toString().isEmpty()) {
                    showErrorMessage(getString(R.string.data_harus_lengkap))
                }

                else if (compotitorsMarket.get(0).name.isNullOrEmpty()) {
                    showErrorMessage(getString(R.string.data_harus_lengkap))
                }
                else if (compotitorsMarket.get(0).weakNess.isNullOrEmpty()) {
                    showErrorMessage(getString(R.string.data_harus_lengkap))
                }

                else {

                    for (i in 0 until compotitorsMarket.size){
                        val dataName = compotitorsMarket.get(i).name
                        val dataWeakness = compotitorsMarket.get(i).weakNess
                        nameCompotitorsMarket.add(dataName)
                        wekanessCompotitorsMarket.add(dataWeakness)

                    }

//                    presenter.postSegmentMarket("token $tokenLogin",
//                            it1,
//                            editMarket.text.toString(),
//                            editTargetCustomer.text.toString(),
//                            editGeografic.text.toString(),
//                            editCompetitive.text.toString(),
//                            nameCompotitorsMarket,
//                            wekanessCompotitorsMarket)
                }

            }
        }
    }

    override fun onSuccessMarket(id: Int) {
        startActivity(Intent(this, StrategyActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra("planID", planID)
                .putExtra("businessID", businessID)
                .putExtra("marketID", id))
        this.finish()
    }

    override fun onFailedMarket(error: String) {
       showErrorMessage(error)
    }
    override fun showfaildRequest(message: String) {
        showErrorMessage(message)
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onSuccesDetailMarket(data: MarketResponse) {

    }
}