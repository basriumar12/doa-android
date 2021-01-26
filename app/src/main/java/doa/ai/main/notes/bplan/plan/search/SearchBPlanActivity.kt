package doa.ai.main.notes.bplan.plan.search

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.SparseBooleanArray
import android.view.*
import android.widget.RadioGroup
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.main.notes.bplan.plan.*
import doa.ai.main.notes.bplan.setup.PlanResponse
import doa.ai.profile.collab.BodySendCollab
import doa.ai.profile.collab.ResultsItem
import doa.ai.profile.collab.ResultsItemForRequested
import doa.ai.profile.collab.find_out.AdapterSuggestionList
import doa.ai.profile.collab.find_out.SuggestionPresenter
import doa.ai.utils.adapter.RecyclerClick_Listener
import doa.ai.utils.adapter.RecyclerTouchListener
import kotlinx.android.synthetic.main.activity_search_bplan.*
import kotlinx.android.synthetic.main.fragment_bplan.*
import kotlinx.android.synthetic.main.view_search_collab.*

class SearchBPlanActivity : BaseActivity(), BPlanSearchView {


    val idPlans : MutableList<Int> = mutableListOf()
    var idPlan = 0
    var currentPage = 1
    private var allDataLoaded = false
    private lateinit var prefs: SharedPreferences
    private var tokenLogin: String? = ""
    private lateinit var presenter: BPlanSearchPresenter
    var sector = ""
    var sortir = "asc"
    var querySearch =""

    private lateinit var adapter: AdapterBplanSearch
    private lateinit var listResult: MutableList<PlanListResult>
    lateinit var result: PlanListResult

    private lateinit var interest: MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_bplan)
        setSupportActionBar(toolbarSearchBplan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        listResult = ArrayList()
        adapter = AdapterBplanSearch(listResult, this,
                { data: PlanListResult -> showToolbar(data) })

        presenter = BPlanSearchPresenter()
        presenter.planContract()

        prefs = applicationContext.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs.getString("loginToken", "")

        presenter.getlistConnected("token $tokenLogin",1,"","connected")


        onClickAction()
        spinnerInterest()

        setUpRecylerview()
    }
    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    private fun getAllDataIdPlans() {
        val selected: SparseBooleanArray
        selected = adapter.selectedIds!!
        idPlans.clear()
        for (data in 0 until selected.size() ) {
            if (selected.valueAt(data)) {
                val dataSelect = listResult.get(selected.keyAt(data)).id
                Log.e("TAG", "data select $dataSelect")
                idPlans.add( listResult.get(selected.keyAt(data)).id)
            }

        }
    }

    var actionMode: ActionMode? = null
    private val mActionModeCallback = object : ActionMode.Callback {
        override fun onActionItemClicked(p0: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                doa.ai.R.id.action_add_pinned -> {
                    getAllDataIdPlans()

                    val bodyPinnedPlan = BodyPinnedPlans(idPlans)
                    presenter.sendPinnedPlans("token $tokenLogin", bodyPinnedPlan)
                    Handler().postDelayed({
                        presenter.getlistBPlan("token $tokenLogin", 1, querySearch, "completed_or_drafted")

                    }, 1500)


                    actionMode?.finish()

                }

                doa.ai.R.id.action_add_trash -> {
                    getAllDataIdPlans()

                    val body = BodyEditPlans(idPlans,"trashed")
                    presenter.sendArchivedDataPlans("token $tokenLogin", body)
                    Handler().postDelayed({
                        presenter.getlistBPlan("token $tokenLogin", 1, querySearch, "completed_or_drafted")

                    }, 1500)

                    actionMode?.finish()
                }

                doa.ai.R.id.action_add_archived -> {

                    getAllDataIdPlans()

                    val body = BodyEditPlans(idPlans,"archived")
                    presenter.sendArchivedDataPlans("token $tokenLogin", body)
                    Handler().postDelayed({
                        presenter.getlistBPlan("token $tokenLogin", 1, querySearch, "completed_or_drafted")

                    }, 1500)

                    actionMode?.finish()
                }

                doa.ai.R.id.action_add_colaborator -> {
                    idPlan = result.id
                    //funShowUserConnected()
                    actionMode?.finish()
                }

                doa.ai.R.id.action_add_copy -> {
                     getAllDataIdPlans()
                    val bodyCopyPlan = BodyCopyPlan(idPlans)
                    presenter.sendCopyPlan("token $tokenLogin", bodyCopyPlan)
                    Handler().postDelayed({
                        presenter.getlistBPlan("token $tokenLogin", 1, querySearch, "completed_or_drafted")

                    }, 1500)

                    actionMode?.finish()

                }


            }


            return true
        }

        override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            p0?.menuInflater?.inflate(doa.ai.R.menu.menu_pinned, p1)

            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, menu: Menu?): Boolean {

            if (Build.VERSION.SDK_INT < 11) {
                MenuItemCompat.setShowAsAction(menu?.findItem(doa.ai.R.id.action_add_pinned), MenuItemCompat.SHOW_AS_ACTION_NEVER)
                MenuItemCompat.setShowAsAction(menu?.findItem(doa.ai.R.id.action_add_colaborator), MenuItemCompat.SHOW_AS_ACTION_NEVER)
                MenuItemCompat.setShowAsAction(menu?.findItem(doa.ai.R.id.action_add_copy), MenuItemCompat.SHOW_AS_ACTION_NEVER)
            } else {
                menu?.findItem(doa.ai.R.id.action_add_pinned)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                menu?.findItem(doa.ai.R.id.action_add_colaborator)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                menu?.findItem(doa.ai.R.id.action_add_copy)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                menu?.findItem(doa.ai.R.id.action_add_archived)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                menu?.findItem(doa.ai.R.id.action_add_trash)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            }

            return false
        }

        override fun onDestroyActionMode(p0: ActionMode?) {
            actionMode = null
            adapter.removeSelection()
        }
    }

    private fun onListItemSelect(position: Int) {
        adapter.toggleSelection(position)//Toggle the selection

        val hasCheckedItems = adapter.selectedCount > 0 //&& adapter!!.selectedCount < 2//Check if any items are already selected or not
        if (hasCheckedItems && actionMode == null)
            actionMode = this?.startActionMode(mActionModeCallback)
        else if (!hasCheckedItems && actionMode != null)
        // there no selected items, finish the actionMode
            actionMode!!.finish()

//        if (actionMode != null)
//        //set action mode title on item selection
//            actionMode!!.title = adapter!!
//                    .selectedCount.toString() + " dipilih"


    }


    private fun showToolbar(result: PlanListResult) {
        this.result = result


    }
    private fun setUpRecylerview() {
        rv_search_bplan.layoutManager = LinearLayoutManager(this)
        rv_search_bplan.adapter = adapter

        rv_search_bplan.addOnItemTouchListener(RecyclerTouchListener(this, rv_search_bplan, object : RecyclerClick_Listener {
            override fun onLongClick(view: View?, position: Int) {
                onListItemSelect(position)
            }

            override fun onClick(view: View?, position: Int) {
                if (actionMode!=null){
                    onListItemSelect(position)

                }
            }
        } ))


    }

    private fun onClickAction() {
        img_filter.setOnClickListener {
            dialogSearch()
        }


        search_bplan.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                querySearch = query
                presenter.getlistBPlan("token $tokenLogin", 1, querySearch, "completed_or_drafted")
                return true
            }

        })


    }

    private fun spinnerInterest() {
        interest = ArrayList()

        interest.add("Alas Kaki")
        interest.add("Advertising, Printing & Media")
        interest.add("Aplikasi dan pengembangan permainan")
        interest.add("Arsitektur")
        interest.add("Asuransi")
        interest.add("Batu-Batuan")
        interest.add("Batubara")
        interest.add("Desain produk")
        interest.add("Desain interior")
        interest.add("Elektronik")
        interest.add("Energy")
        interest.add("Farmasi")
        interest.add("Fesyen")
        interest.add("Film, Animasi & Video")
        interest.add("Fotografi")
        interest.add("Jasa Computer & Perangkatnya")
        interest.add("Jalan Tol, Pelabuhan, Bandara")
        interest.add("Kabel")
        interest.add("Kayu & Pengolahannya")
        interest.add("Keramik, Porselen & Kaca")
        interest.add("Kehutanan")
        interest.add("Kimia")
        interest.add("Komunikasi Visual")
        interest.add("Kriya")
        interest.add("Kuliner")
        interest.add("Kontruksi Bangunan")
        interest.add("Kosmetik & Keperluan Rumah Tangga")
        interest.add("Lembaga Pembiayaan")
        interest.add("Logam & Sejenisnya")
        interest.add("Logam & Mineral Lainnya")
        interest.add("Makanan & Minuman")
        interest.add("Mesin & Alat Berat")
        interest.add("Minyak & Gas Bumi")
        interest.add("Musik")
        interest.add("Otomotif & Komponennya")
        interest.add("Pakan Ternak")
        interest.add("Perkebunan")
        interest.add("Peternakan")
        interest.add("Perikanan")
        interest.add("Perdagangan Besar (Barang Produksi & Konsumsi)")
        interest.add("Perdagangan Eceran")
        interest.add("Peralatan Rumah Tanga")
        interest.add("Penerbitan")
        interest.add("Periklanan")
        interest.add("Perusahaan Investasi")
        interest.add("Perusahaan Efek")
        interest.add("Plastik & Kemasan")
        interest.add("Pulp & Kertas")
        interest.add("Properti & Real Estate")

        interest.add("Reksa Dana")
        interest.add("Restoran, Hotel & Pariwisata")
        interest.add("Rokok")
        interest.add("Semen")
        interest.add("Seni Pertunjukan")
        interest.add("Senirupa")
        interest.add("Tanaman Pangan")
        interest.add("Telekomunikasi")
        interest.add("Televisi & Radio")
        interest.add("Tekstil & Garmen")
        interest.add("Transportasi")
        interest.add("Lainnya")


    }

    fun dialogSearch() {

        val dialog = Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar)

        // Setting dialogview
        val window = dialog.getWindow()
        window.setGravity(Gravity.TOP)

        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT)
        dialog.setTitle(null)
        dialog.setContentView(doa.ai.R.layout.view_search_collab)
        dialog.img_left.setOnClickListener { view ->
            val data: List<String>
            data = interest

            val subMenu = PopupMenu(this, view)
            for (data in data) {

                subMenu.menu.add(0, 0, 0, data.toString())
                subMenu.setOnMenuItemClickListener {
                    dialog.tv_sector.text = it.title
                    sector = it.title.toString()
                    false
                }
                subMenu.show()
            }



        }

        dialog.radiogroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                if (p1 == R.id.radio_az) {
                    sortir = "asc"
                }
                if (p1 == R.id.radio_za) {
                    sortir = "desc"
                }
            }

        })

        dialog.tv_cancel.setOnClickListener {
            dialog.dismiss()
            sortir =""
            sector =""
        }

        dialog.tv_apply.setOnClickListener {
            dialog.dismiss()
            Log.e("Tag","data popmenu $sortir $sector")
        }


        dialog.setCancelable(true)

        dialog.show()
    }

    private fun addCollab(dataId: ResultsItem) {
        val receivers: MutableList<Int> = arrayListOf()
        dataId.user?.id?.let { receivers.add(it) }
        val body = BodySendCollab(receivers, "requested")
       // presenter.postSendCollab("token $tokenLogin", body)


    }

    override fun onSuccessConnected(data: MutableList<ResultsItemForRequested>) {

    }

    override fun onSuccessConnectedAfter(data: MutableList<ResultsItemForRequested>) {
    }

    override fun onSuccessBPlans(response: MutableList<PlanListResult>) {
        Log.e("Tag","data search ${Gson().toJson(response)}")
        if (response.size == 0) {
            tv_empty_data.visibility = View.VISIBLE
            img_empty_search.visibility = View.VISIBLE
        }
        listResult.clear()
        listResult.addAll(response)
        adapter.notifyDataSetChanged()
    }

    override fun onSuccessBPlansAfter(response: MutableList<PlanListResult>) {
    }

    override fun successEditBPLan(planResponse: PlanResponse) {
    }

    override fun onFailedBPlan(error: String) {
    }

    override fun onSuccess(message: String) {
        showInfoMessage(message)
    }

    override fun hideLoad() {
        progress_bplan.visibility = View.GONE
    }

    override fun showLoad() {
        progress_bplan.visibility = View.VISIBLE
    }

    override fun failed(error: String) {
    }


}
