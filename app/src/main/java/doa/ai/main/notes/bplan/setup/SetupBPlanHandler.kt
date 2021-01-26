package doa.ai.main.notes.bplan.setup

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import doa.ai.App.Companion.context
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.database.AppDatabase
import doa.ai.database.modelDB.NotesEntry
import doa.ai.database.modelDB.TrashEntry
import doa.ai.extentions.*
import doa.ai.main.notes.bplan.business.BusinessActivity
import doa.ai.main.notes.bplan.detail.BplanDetailActivity
import doa.ai.main.notes.bplan.plan.AdapterConnectedListAddBplan
import doa.ai.main.notes.bplan.plan.BodyShare
import doa.ai.main.notes.bplan.tab_segment.SegmentBplanActivity
import doa.ai.main.notes.settings.purchasePremium.PurchasePremiumrActivity
import doa.ai.payment.PaymentActivity
import doa.ai.profile.collab.CollaborationActivity
import doa.ai.profile.collab.ResultsItemForRequested
import doa.ai.utils.pref.*
import kotlinx.android.synthetic.main.activity_bplan_setup.*
import kotlinx.android.synthetic.main.view_user_list_connected.view.*
import java.text.SimpleDateFormat
import java.util.*

class SetupBPlanActivity: BaseActivity(), SetupPlanView {


    private  var dataListUser: MutableList<ResultsItemForRequested> = mutableListOf()

    lateinit var adapterConnectedList: AdapterConnectedListAddBplan
    private lateinit var presenter: SetupPlanPresenter
    private lateinit var prefs: SharedPreferences
    private lateinit var mDB : AppDatabase

    private var repeat: String = "not-repeated"
    private var startDate: String = ""
    private var endDate: String = ""
    private var tokenLogin: String? = ""
    private var sectorsID: Int? = 0
    private var idSubSector: Int? = 0
    private var titleIdea: String? = ""
    private var imageIdea: String? = ""
    private var idIdea: Int? = 0

    @SuppressLint("SetTextI18n")
    private val startDatePickerListener = DatePickerDialog.OnDateSetListener{ _, year, monthOfYear, dayOfMonth ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR,year)
        calendar.set(Calendar.MONTH,monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
        val format = "E,dd/MM/yyyy"
        val formatServer = "yyyy-MM-dd hh:mm:ss"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val sdfServer = SimpleDateFormat(formatServer, Locale.getDefault())
        this.textStartDate.text = sdf.format(calendar.time)
        this.textEndDate.text = ""
        startDate = sdfServer.format(calendar.time)
    }

    @SuppressLint("SetTextI18n")
    private val endDatePickerListener = DatePickerDialog.OnDateSetListener{ _, year, monthOfYear, dayOfMonth ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR,year)
        calendar.set(Calendar.MONTH,monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
        val format = "E,dd/MM/yyyy"
        val formatServer = "yyyy-MM-dd hh:mm:ss"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val sdfServer = SimpleDateFormat(formatServer, Locale.getDefault())
        this.textEndDate.text = sdf.format(calendar.time)
        endDate = sdfServer.format(calendar.time)
    }

    @SuppressLint("SetTextI18n")
    private val reminderDatePickerListener = DatePickerDialog.OnDateSetListener{ _, year, monthOfYear, dayOfMonth ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR,year)
        calendar.set(Calendar.MONTH,monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
        val format = "E,dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        this.textDateReminder.text = sdf.format(calendar.time)
    }

    @SuppressLint("SetTextI18n")
    private val timePickerListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE,minute)
        val format = "hh:mm:ss"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        this.textTimeReminder.text = sdf.format(calendar.time)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bplan_setup)
        setSupportActionBar(toolbarSetup)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        mDB = AppDatabase.getInstance(this)
        adapterConnectedList = AdapterConnectedListAddBplan(dataListUser, this,
                { dataId: ResultsItemForRequested -> addCollabUser(dataId) })

        presenter = SetupPlanPresenter()
        presenter.setupContract()

        prefs = applicationContext.getSharedPreferences("MyPref",0)
        tokenLogin = prefs.getString("loginToken","")
        titleIdea = intent.getStringExtra("title")
        idIdea = intent.getIntExtra("id",0)
        presenter.getlistConnected("token $tokenLogin",1,"","connected")


        editTitle.setText(titleIdea)
        setView()
        actionClick()
    }

    private fun actionClick() {
        img_add_collab.setOnClickListener {
            dialogAddCollab()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onSuccessPlanSectors(sectors: MutableList<ResultPlan>) {
        cardViewSector.setOnClickListener { v ->
            val popMenu = PopupMenu(this,v)
            for (_sectors in sectors.iterator()) {
                popMenu.menu.add(_sectors.id, _sectors.id,0,_sectors.title)
                popMenu.setOnMenuItemClickListener { item ->
                    presenter.setPlanSubSector("token $tokenLogin", item.itemId)
                    textSector.text = item.title
                    textSubSector.text = ""
                    textSubSector.hint = "Sub Sektor"
                    false
                }
                popMenu.show()
            }

        }
    }

    private fun addCollabUser(dataId: ResultsItemForRequested) {

        val receivers: MutableList<Int> = arrayListOf()
        dataId.receiver?.user?.id?.toInt()?.let { receivers.add(it) }
       // val bodyShare = BodyShare("plan",receivers,idPlan)

      ///  presenter.sendShare("token $tokenLogin", bodyShare)

    }
    fun dialogAddCollab(){
        val mBottomSheetDialog = RoundedBottomSheetDialog(this)
        val dialogView = LayoutInflater.from(this).inflate(doa.ai.R.layout.view_user_list_connected, null)
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.show()
        dialogView.tv_search.setOnClickListener {
            startActivity(Intent(this, CollaborationActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("flags","suggest"))

        }
        dialogView.tv_cancel_collab.setOnClickListener {
            mBottomSheetDialog.dismiss()
        }
        dialogView.rv_connected_collab.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        dialogView.rv_connected_collab.adapter = adapterConnectedList
    }
    override fun onSuccesPlanSubSectors(subSectors: MutableList<SubSectorPlan>) {
        cardViewSubSector.setOnClickListener { v ->
            val subMenu = PopupMenu(this, v)
            for (_subSector in subSectors.iterator()) {
                subMenu.menu.add(_subSector.id, _subSector.id,0,_subSector.title)
                subMenu.setOnMenuItemClickListener { item ->
                    idSubSector = item.itemId
                    textSubSector.text = item.title
                    false
                }
                subMenu.show()
            }
        }
    }

    override fun onSuccessSetup(idPlan: Int) {
        AppExecutors.instance.diskIO().execute {
            val notes = idIdea?.let { NotesEntry(it, imageIdea, titleIdea, "","") }
            notes?.let { mDB.appsDao().deleteNotes(it) }
        }

//        startActivity(Intent(this,BusinessActivity::class.java)
//                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                .putExtra("planID",idPlan))


        //set id = 0
        SavePrefId().setPlanID(idPlan.toString())
        SavePrefId().setBusinessId("0")
        SavePrefId().setMarketId("0")
        SavePrefId().setStrategyId("0")
        SavePrefId().setFinnanceId("0")
        //setBusiness
        SavePrefSegmentBusiness().setBusinessProvince("")
        SavePrefSegmentBusiness().setBusinessCity("")
        SavePrefSegmentBusiness().setBusinessSubDistric("")
        SavePrefSegmentBusiness().setBusinessRT("")
        SavePrefSegmentBusiness().setBusinessRW("")
        SavePrefSegmentBusiness().setBusinessStreet("")
        SavePrefSegmentBusiness().setBusinessPostalCode("")
        SavePrefSegmentBusiness().setBusinessNameFounder("")
        SavePrefSegmentBusiness().setBusinessPositionFounder("")
        SavePrefSegmentBusiness().setBusinessExpertisFounder("")
        SavePrefSegmentBusiness().setBusinessBusinessActivities("")
        SavePrefSegmentBusiness().setBusinessBusinessPartnerts("")
        SavePrefSegmentBusiness().setBusinessKeySuccess("")
        SavePrefSegmentBusiness().setBusinessOperatingExpaness("")

        //setMarket
        SavePrefSegmentMarket().setMarketCompetitive("")
        SavePrefSegmentMarket().setMarketTargetCustomer("")
        SavePrefSegmentMarket().setMarketProduct("")
        SavePrefSegmentMarket().setMarketGeografic("")
        SavePrefSegmentMarket().setMarketNameCompetitor("")
        SavePrefSegmentMarket().setMarketWeaknessCompetitor("")
        SavePrefSegmentMarket().setMarketCustomerReachWays("")
        SavePrefSegmentMarket().setMarketCustomerManagement("")

        //setStrategy
        SavePrefSegmentStartegy().setStrategyStep("")
        SavePrefSegmentStartegy().setStrategyOportunity("")
        SavePrefSegmentStartegy().setStrategyGoals("")

        //setFinnance
        SavePrefSegmentFinnance().setFinnancePlanning("")
        SavePrefSegmentFinnance().setFinnanceCurrency("")
        SavePrefSegmentFinnance().setFinnanceInvestMoney("")
        SavePrefSegmentFinnance().setFinnanceSalesFirstYear("")
        SavePrefSegmentFinnance().setFinnanceSalesGrowth("")
        SavePrefSegmentFinnance().setFinnanceCashSales("")
        SavePrefSegmentFinnance().setFinnanceExpansesFirstYear("")
        SavePrefSegmentFinnance().setFinnanceExpansesGrowth("")
        SavePrefSegmentFinnance().setFinnanceCreditCustomer("")
        SavePrefSegmentFinnance().setFinnanceCreditSupplier("")

        startActivity(Intent(this,SegmentBplanActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra("planID",idPlan))
        this.finish()

    }

    override fun onFailedSetup(error: String) {
        val toast = Toast.makeText(this,error, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()

    }

    private fun setStartDate() {
        val datePickerDialog = DatePickerDialog(this,startDatePickerListener, getCurrentYear(), getCurrentMonth(), getCurrentDay())
        datePickerDialog.show()
    }

    private fun setFinishDate() {
        val datePickerDialog = DatePickerDialog(this,endDatePickerListener, getCurrentYear(), getCurrentMonth(), getCurrentDay())
        datePickerDialog.show()
    }

    private fun setReminderDate() {
        val datePickerDialog = DatePickerDialog(this,reminderDatePickerListener, getCurrentYear(), getCurrentMonth(), getCurrentDay())
        datePickerDialog.show()
    }

    private fun setTimeReminder() {
        val timePickerDialog = TimePickerDialog(this,timePickerListener, getCurrentHour(), getCurrentMinute(),true)
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setView() {

        presenter.setPlanSector("token $tokenLogin")

        bplanCreate.setOnClickListener {
            when {
                editTitle.text.toString().isEmpty() -> editTitle.error = "Lengkapi informasi ini dengan benar"

                textSector.text.toString().isEmpty() -> Toast.makeText(this,"Lengkapi sektor anda", Toast.LENGTH_LONG).show()

                textSubSector.text.toString().isEmpty() -> Toast.makeText(this,"Lengkapi sub sektor anda", Toast.LENGTH_LONG).show()

                textStartDate.text.toString().isEmpty() -> Toast.makeText(this,"Lengkapi tanggal mulai anda", Toast.LENGTH_LONG).show()

                textEndDate.text.toString().isEmpty() -> Toast.makeText(this,"Lengkapi tanggal selesai anda", Toast.LENGTH_LONG).show()

                else -> idSubSector?.let { id -> presenter.sendDataPlan("token $tokenLogin",editTitle.text.toString(), id,startDate,endDate,false,"06:06:06",repeat) }

            }
        }

        switchSetupBplan.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                linearReminder.visibility = View.VISIBLE
            } else {
                linearReminder.visibility = View.GONE
            }
        }

        linearReminder.setOnClickListener {
            setReminderDate()
        }

        linearTimeReminder.setOnClickListener {
            setTimeReminder()
        }

        linearRepeat.setOnClickListener {

                if (textRepeat.text.toString().equals( "Repeat")) {
                    repeat = "every-1-days"
                }
                else {
                textRepeat.text = "Not repeat"
                repeat = "not-repeated"
            }
        }

        cardViewStartDate.setOnClickListener {
            setStartDate()
        }

        cardViewEndDate.setOnClickListener {
            setFinishDate()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            } else -> super.onOptionsItemSelected(item)
        }



    }
    override fun onUpgradeToPremium() {
        startActivity(Intent(this, PurchasePremiumrActivity::class.java))
    }

    override fun errorMessage(message: String) {
        showErrorMessage(message)

    }

    override fun showLoad() {
        progressbar_plan.visibility = View.VISIBLE
      }

    override fun hideLoad() {
        progressbar_plan.visibility = View.GONE

    }

    override fun onSuccessConnected(data: MutableList<ResultsItemForRequested>) {
        dataListUser.clear()
        dataListUser.addAll(data)
        adapterConnectedList.notifyDataSetChanged()
    }

}