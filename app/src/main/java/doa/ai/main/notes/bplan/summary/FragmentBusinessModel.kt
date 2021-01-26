package doa.ai.main.notes.bplan.summary

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import doa.ai.R
import doa.ai.base.BaseFragment
import doa.ai.main.notes.bplan.finance.AdapterTable
import doa.ai.main.notes.bplan.summary.adapter.*
import doa.ai.utils.pref.SavePrefId
import kotlinx.android.synthetic.main.view_business_model.*
import kotlinx.android.synthetic.main.view_dialog_business_partnerts.view.*
import kotlinx.android.synthetic.main.view_dialog_channel.view.*
import kotlinx.android.synthetic.main.view_dialog_competitive_advantages.view.*
import kotlinx.android.synthetic.main.view_dialog_cost_structure.view.*
import kotlinx.android.synthetic.main.view_dialog_income_stream.view.*
import kotlinx.android.synthetic.main.view_dialog_main_business_activities.view.*
import kotlinx.android.synthetic.main.view_dialog_main_resource.view.*
import kotlinx.android.synthetic.main.view_dialog_relationship_with_customer.view.*
import kotlinx.android.synthetic.main.view_dialog_segemnt_and_persona_customer.view.*

class FragmentBusinessModel : BaseFragment(), SummaryView {

    var incomeStream = ""
    var competitiveAdvantages = ""
    var segmentAndPersonaCustomer = ""
    var titlePlan = ""
    var idPlanDownloadPdf = 0
    var planID: Int = 0
    var businessID: Int = 0
    var marketID: Int = 0
    var strategyID: Int = 0
    var financeID: Int = 0
    var ID: Int = 0
    private var tokenLogin: String? = ""
    private var mTableAdapter: AdapterTable? = null
    private lateinit var presenter: SummaryPresenter
    private var prefs: SharedPreferences? = null
    var urlDownload = ""
    lateinit var adapterCustomerManagementsBusinessModel: AdapterCustomerManagementsBusinessModel
    lateinit var adapterCustomerReachWaysBusinessModel: AdapterCustomerReachWaysBusinessModel
    lateinit var adapterBusinessActivitiesBusinessModel: AdapterBusinessActivitiesBusinessModel
    lateinit var adapterBusinessPartnertsBusinessModel: AdapterBusinessPartnertsBusinessModel
    lateinit var adapterOperatingExpansesBusinessModel: AdapterOperatingExpansesBusinessModel
    lateinit var adaptermainResourceBusinessModel: AdaptermainResourceBusinessModel
    private var customerManagementsMarket: MutableList<String> = ArrayList()
    private var customerReachWaysMarket: MutableList<String> = ArrayList()
    var mainResource: MutableList<String> = ArrayList()
    var businessActivities: MutableList<String> = ArrayList()
    var businesspartners: MutableList<String> = ArrayList()
    var operatingExpenses: MutableList<String> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_channel.layoutManager = LinearLayoutManager(activity)
        rv_relationship_with_customer.layoutManager = LinearLayoutManager(activity)
        rv_main_bussiness_activities.layoutManager = LinearLayoutManager(activity)
        rv_business_partnerts.layoutManager = LinearLayoutManager(activity)
        rv_operating_expanses_business.layoutManager = LinearLayoutManager(activity)
        rv_main_resource.layoutManager = LinearLayoutManager(activity)

        customerManagementsMarket = ArrayList()
        customerReachWaysMarket = ArrayList()
        businessActivities = ArrayList()
        businesspartners = ArrayList()
        operatingExpenses = ArrayList()
        mainResource = ArrayList()


        adapterCustomerManagementsBusinessModel = AdapterCustomerManagementsBusinessModel(customerManagementsMarket as ArrayList<String>, context!!)

        adapterCustomerReachWaysBusinessModel = AdapterCustomerReachWaysBusinessModel(customerReachWaysMarket as ArrayList<String>, context!!)
        adapterBusinessActivitiesBusinessModel = AdapterBusinessActivitiesBusinessModel(businessActivities as ArrayList<String>, context!!)

        adapterBusinessPartnertsBusinessModel = AdapterBusinessPartnertsBusinessModel(businesspartners as ArrayList<String>, context!!)

        adapterOperatingExpansesBusinessModel = AdapterOperatingExpansesBusinessModel(operatingExpenses as ArrayList<String>, context!!)

        adaptermainResourceBusinessModel = AdaptermainResourceBusinessModel(mainResource as ArrayList<String>, context!!)



        presenter = SummaryPresenter()
        presenter.summaryContract()
        prefs = context!!.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs?.getString("loginToken", "")
        val intent = Intent()

        planID = intent.getIntExtra("planID", 0)
        businessID = intent.getIntExtra("businessID", 0)
        marketID = intent.getIntExtra("marketID", 0)
        strategyID = intent.getIntExtra("strategyID", 0)
        financeID = intent.getIntExtra("financeID", 0)

        ID = SavePrefId().getSummaryId().toInt()
        idPlanDownloadPdf = SavePrefId().getPlanID().toInt()
        if (!ID.equals(1019000)) {

            presenter.detailSummary("token $tokenLogin", ID)
            //idPlanDownloadPdf =intent.getIntExtra("IDPLAN", 0)
        } else {


//            if (businessID != null || !businessID.equals(0)) {
//                presenter.summary("token $tokenLogin",
//                        planID, businessID, marketID,
//                        strategyID, financeID, "summary for all segment", "")
//            } else {
            val id = SavePrefId().getPlanID().toString()
            val businessId = SavePrefId().getBussinessId()
            val marketId = SavePrefId().getMarketId()
            val strategyId = SavePrefId().getStrategyId()
            val finnanceId = SavePrefId().getFinnanceId()

            idPlanDownloadPdf = id.toInt()
            planID = id.toInt()
            businessID = businessId.toInt()
            marketID = marketId.toInt()
            strategyID = strategyId.toInt()
            financeID = finnanceId.toInt()
            presenter.summary("token $tokenLogin",
                    planID, businessID, marketID,
                    strategyID, financeID, "summary for all segment", "")
            // }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_business_model, container, false)

    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onSuccessSummary(response: SummaryResponse) {
        tv_segment_and_persona_customer.text = response.result.market.target_customer
        // tv_channel.text = response.result.market.geographic_market
        tv_competitive_advantages.text = response.result.market.competitive_advantage
        tv_income_stream.text = response.result.market.product
        competitiveAdvantages = response.result.market.competitive_advantage
        incomeStream = response.result.market.product
        segmentAndPersonaCustomer = response.result.market.target_customer
        //tv_relationship_with_customer.text = response.result.market.key_competitors.get(0).value.get(0)

        response.result.market.customer_managements.forEach {
            customerManagementsMarket.addAll(listOf(it))
        }
        rv_relationship_with_customer.adapter = adapterCustomerManagementsBusinessModel
        adapterCustomerManagementsBusinessModel.notifyDataSetChanged()


        response.result.market.customer_reach_ways.forEach {
            customerReachWaysMarket.addAll(listOf(it))
        }
        rv_channel.adapter = adapterCustomerReachWaysBusinessModel
        adapterCustomerReachWaysBusinessModel.notifyDataSetChanged()


        response.result.business.business_activities.forEach {
            businessActivities.addAll(listOf(it))
        }

        rv_main_bussiness_activities.adapter = adapterBusinessActivitiesBusinessModel
        adapterBusinessActivitiesBusinessModel.notifyDataSetChanged()

        response.result.business.business_partners.forEach {
            businesspartners.addAll(listOf(it))
        }

        rv_business_partnerts.adapter = adapterBusinessPartnertsBusinessModel
        adapterBusinessPartnertsBusinessModel.notifyDataSetChanged()


        response.result.business.operating_expenses.forEach {
            operatingExpenses.addAll(listOf(it))
        }

        rv_operating_expanses_business.adapter = adapterOperatingExpansesBusinessModel
        adapterOperatingExpansesBusinessModel.notifyDataSetChanged()


        response.result.business.key_resources.forEach {
            mainResource.addAll(listOf(it))
        }

        rv_main_resource.adapter = adaptermainResourceBusinessModel
        adaptermainResourceBusinessModel.notifyDataSetChanged()

        rootCompetitiveAdvantages.setOnClickListener {
            dialogConvetitiveAdavantages()
        }

        rootdialogIncomeStream.setOnClickListener {
            dialogIncomeStream()
        }

        rootSegmenPersonaCustomer.setOnClickListener {
            dialogSegmentAndPersonaCustomer()
        }

        rootChannel.setOnClickListener {
            dialogChannel()
        }
        rootRelationshipwithCustomer.setOnClickListener {
            dialogRelationshipWithCustomer()
        }
        rootCostStructure.setOnClickListener {
            dialogCostStructure()
        }
        rootBusinessPartnerts.setOnClickListener {
            dialogBusinessPartnerts()
        }

        rootMainResource.setOnClickListener {
            dialogMainResousrce()
        }

        rootMainBusinessActivities.setOnClickListener {
            dialogMainBusinessActivities()
        }

    }

    private fun dialogMainBusinessActivities() {
        val mBottomSheetDialog = RoundedBottomSheetDialog(context!!)
        val dialogView = LayoutInflater.from(context).inflate(doa.ai.R.layout.view_dialog_main_business_activities, null)
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.show()
        dialogView.rv_main_bussiness_activities.layoutManager = LinearLayoutManager(context)
        dialogView.rv_main_bussiness_activities.adapter =adapterBusinessActivitiesBusinessModel

    }

    fun dialogIncomeStream() {
        val mBottomSheetDialog = RoundedBottomSheetDialog(context!!)
        val dialogView = LayoutInflater.from(context).inflate(doa.ai.R.layout.view_dialog_income_stream, null)
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.show()
        dialogView.tv_income_stream.text = incomeStream

    }
    fun dialogRelationshipWithCustomer() {
        val mBottomSheetDialog = RoundedBottomSheetDialog(context!!)
        val dialogView = LayoutInflater.from(context).inflate(doa.ai.R.layout.view_dialog_relationship_with_customer, null)
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.show()
        dialogView.rv_relationship_with_customer.layoutManager = LinearLayoutManager(context)
        dialogView.rv_relationship_with_customer.adapter =adapterCustomerManagementsBusinessModel


    }
    fun dialogMainResousrce() {
        val mBottomSheetDialog = RoundedBottomSheetDialog(context!!)
        val dialogView = LayoutInflater.from(context).inflate(doa.ai.R.layout.view_dialog_main_resource, null)
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.show()
        dialogView.rv_main_resource.layoutManager = LinearLayoutManager(context)
        dialogView.rv_main_resource.adapter =adaptermainResourceBusinessModel


    }
    fun dialogBusinessPartnerts() {
        val mBottomSheetDialog = RoundedBottomSheetDialog(context!!)
        val dialogView = LayoutInflater.from(context).inflate(doa.ai.R.layout.view_dialog_business_partnerts, null)
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.show()
        dialogView.rv_business_partnerts.layoutManager = LinearLayoutManager(context)
        dialogView.rv_business_partnerts.adapter =adapterBusinessPartnertsBusinessModel


    }
    fun dialogCostStructure() {
        val mBottomSheetDialog = RoundedBottomSheetDialog(context!!)
        val dialogView = LayoutInflater.from(context).inflate(doa.ai.R.layout.view_dialog_cost_structure, null)
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.show()
        dialogView.rv_operating_expanses_business.layoutManager = LinearLayoutManager(context)
        dialogView.rv_operating_expanses_business.adapter =adapterOperatingExpansesBusinessModel


    }

    fun dialogConvetitiveAdavantages() {
        val mBottomSheetDialog = RoundedBottomSheetDialog(context!!)
        val dialogView = LayoutInflater.from(context).inflate(doa.ai.R.layout.view_dialog_competitive_advantages, null)
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.show()
        dialogView.tv_competitive_advantages.text = competitiveAdvantages

    }

    fun dialogSegmentAndPersonaCustomer() {
        val mBottomSheetDialog = RoundedBottomSheetDialog(context!!)
        val dialogView = LayoutInflater.from(context).inflate(doa.ai.R.layout.view_dialog_segemnt_and_persona_customer, null)
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.show()
        dialogView.tv_segment_and_persona_customer.text = segmentAndPersonaCustomer

    }
    fun dialogChannel() {
        val mBottomSheetDialog = RoundedBottomSheetDialog(context!!)
        val dialogView = LayoutInflater.from(context).inflate(doa.ai.R.layout.view_dialog_channel, null)
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.show()
        dialogView.rv_channel.layoutManager = LinearLayoutManager(context)
        dialogView.rv_channel.adapter = adapterCustomerReachWaysBusinessModel

    }

    override fun onFailedSummary(error: String) {

    }
}