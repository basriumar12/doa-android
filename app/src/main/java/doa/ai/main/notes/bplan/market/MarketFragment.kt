package doa.ai.main.notes.bplan.market

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import doa.ai.R
import doa.ai.base.BaseFragment
import doa.ai.extentions.Dialog
import doa.ai.main.notes.bplan.market.adapter.AdapterCompotitorsMarket
import doa.ai.main.notes.bplan.market.adapter.AdapterCustomerManagements
import doa.ai.main.notes.bplan.market.adapter.AdapterCustomerReachWays
import doa.ai.utils.SwipeToDeleteCallback
import doa.ai.utils.pref.SavePrefId
import doa.ai.utils.pref.SavePrefSegmentBusiness
import doa.ai.utils.pref.SavePrefSegmentMarket
import doa.ai.utils.pref.SavePrefTokenLogin
import kotlinx.android.synthetic.main.activity_bplan_market.*

class MarketFragment : BaseFragment(), MarketView {
    var planID: Int? = 0
    var businessID: Int? = 0
    var tokenLogin: String? = ""
    private lateinit var presenter: MarketPresenter
    private lateinit var prefs: SharedPreferences
    var compotitorsMarket: ArrayList<KeyCompetitorsModel> = ArrayList()
    private  var nameCompotitorsMarket: MutableList<String> = ArrayList()
    private  var wekanessCompotitorsMarket: MutableList<String> = ArrayList()
    private  var customerManagementsMarket: MutableList<String> = ArrayList()
    private  var customerReachWaysMarket: MutableList<String> = ArrayList()

    lateinit var adapterCompotitorsMarket: AdapterCompotitorsMarket
    lateinit var adapterCustomerManagements: AdapterCustomerManagements
    lateinit var adapterCustomerReachWays: AdapterCustomerReachWays

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_bplan_market, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = MarketPresenter()
        presenter.marketContract()
        val id = SavePrefId().getPlanID().toString()
        val businessId = SavePrefId().getBussinessId()
        planID = id.toInt()
        businessID = businessId.toInt()
        tokenLogin = SavePrefTokenLogin().getTokenLogin().toString()


        handlePostSegmentMarket()
        handleView()
        handleSave()

    }

    private fun handleSave() {
        editMarket.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

               SavePrefSegmentMarket().setMarketProduct(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editTargetCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

               SavePrefSegmentMarket().setMarketTargetCustomer(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editGeografic.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

               SavePrefSegmentMarket().setMarketGeografic(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editCompetitive.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

               SavePrefSegmentMarket().setMarketCompetitive(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editMarket.setText(SavePrefSegmentMarket().getMarketProduct())
        editGeografic.setText(SavePrefSegmentMarket().getMarketGeografic())
        editTargetCustomer.setText(SavePrefSegmentMarket().getMarketTargetCustomer())
        editCompetitive.setText(SavePrefSegmentMarket().getMarketCompetitive())
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
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
                else if (customerManagementsMarket.get(0).isNullOrEmpty()) {
                    showErrorMessage(getString(R.string.data_harus_lengkap))
                }
                else if (customerReachWaysMarket.get(0).isNullOrEmpty()) {
                    showErrorMessage(getString(R.string.data_harus_lengkap))
                }

                else {

                    for (i in 0 until compotitorsMarket.size){
                        val dataName = compotitorsMarket.get(i).name
                        val dataWeakness = compotitorsMarket.get(i).weakNess
                        nameCompotitorsMarket.add(dataName)
                        wekanessCompotitorsMarket.add(dataWeakness)

                    }

                    presenter.postSegmentMarket("token $tokenLogin",
                            it1,
                            editMarket.text.toString(),
                            editTargetCustomer.text.toString(),
                            editGeografic.text.toString(),
                            editCompetitive.text.toString(),
                            nameCompotitorsMarket,
                            wekanessCompotitorsMarket,
                            customerManagementsMarket,
                            customerReachWaysMarket

                    )
                }

            }
        }
    }

    private fun handleView() {

        compotitorsMarket = ArrayList()
        val nameCompetitor = SavePrefSegmentMarket().getMarketNameCompetitor()
        val weaknessCompetitor = SavePrefSegmentMarket().getMarketWeaknessCompetitor()
        compotitorsMarket.add(KeyCompetitorsModel(nameCompetitor,weaknessCompetitor))
        rv_compotitors_market.layoutManager = LinearLayoutManager(activity!!)
        adapterCompotitorsMarket = AdapterCompotitorsMarket(compotitorsMarket, activity!!)
        rv_compotitors_market.adapter = adapterCompotitorsMarket

        tv_add_compotitor_market.setOnClickListener {

            compotitorsMarket.add(KeyCompetitorsModel("",""))
            rv_compotitors_market.adapter = adapterCompotitorsMarket
            adapterCompotitorsMarket.notifyDataSetChanged()
        }

        //delete item
        val swipeHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_compotitors_market.adapter as AdapterCompotitorsMarket
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv_compotitors_market)


        //customer managements
        customerManagementsMarket = ArrayList()
        val dataCustomerManagements = SavePrefSegmentMarket().getMarketCustomerManagement()
        customerManagementsMarket.add(dataCustomerManagements)
        rv_customer_managements_market.layoutManager = LinearLayoutManager(activity)
        adapterCustomerManagements = AdapterCustomerManagements(customerManagementsMarket as ArrayList<String>,activity!!)
        rv_customer_managements_market.adapter =adapterCustomerManagements
        tv_add_customer_managements_market.setOnClickListener {
            customerManagementsMarket.add("")
            rv_customer_managements_market.adapter =adapterCustomerManagements
            adapterCustomerManagements.notifyDataSetChanged()
        }

        //customer reach ways
        customerReachWaysMarket = ArrayList()
        val dataCustomerReachWays = SavePrefSegmentMarket().getMarketCustomerReachWays()
        customerReachWaysMarket.add(dataCustomerReachWays)
        rv_customer_reach_ways_market.layoutManager = LinearLayoutManager(activity)
        adapterCustomerReachWays = AdapterCustomerReachWays(customerReachWaysMarket as ArrayList<String>,activity!!)
        rv_customer_reach_ways_market.adapter =adapterCustomerReachWays
        tv_add_customer_reach_ways_market.setOnClickListener {
            customerReachWaysMarket.add("")
            rv_customer_reach_ways_market.adapter =adapterCustomerReachWays
            adapterCustomerReachWays.notifyDataSetChanged()
        }






        img_produk_jasa_pasar.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.produk_ditawrkan))
        }
        img_target_pelanggan_pasar.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.target_pelanggan))
        }
        img_geografis_dari_pasar.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.georrafis_dari_pasar))
        }
        img_keunggulan_kompetitif_pasar.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.keunggulan_kompotetif_pasar))
        }
        img_pesaing_pasar.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.pesaing_pasar))
        }

    }

    interface FragmentCallback {
        fun saveIdMarket(id: String)
    }

    private lateinit var fragmentCallback: FragmentCallback
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentCallback = context as FragmentCallback

    }


    override fun onSuccessMarket(id: Int) {

        showInfoMessage("Sukses ")
        fragmentCallback.saveIdMarket(id.toString())

    }

    override fun onFailedMarket(error: String) {
        showErrorMessage(error)
        Log.e("tag","error market $error")
    }

    override fun showfaildRequest(message: String) {
        showErrorMessage(message)

    }
    override fun onSuccesDetailMarket(data: MarketResponse) {

    }
}