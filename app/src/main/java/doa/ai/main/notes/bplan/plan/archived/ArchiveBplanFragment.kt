package doa.ai.main.notes.bplan.plan.archived

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.SparseBooleanArray
import android.view.*
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuItemCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.*
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.google.gson.Gson


import doa.ai.base.BaseFragment
import doa.ai.main.notes.bplan.plan.archive.ArchiveBPlanActivity
import doa.ai.main.notes.bplan.setup.PlanResponse
import doa.ai.main.notes.bplan.setup.SetupBPlanActivity
import doa.ai.profile.collab.ResultsItemForRequested
import doa.ai.profile.collab.connected.AdapterConnectedList
import doa.ai.utils.adapter.RecyclerClick_Listener
import doa.ai.utils.adapter.RecyclerTouchListener
import kotlinx.android.synthetic.main.fragment_bplan.*
import kotlinx.android.synthetic.main.view_search_collab.*
import kotlinx.android.synthetic.main.view_user_list_connected.*
import kotlinx.android.synthetic.main.view_user_list_connected.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import doa.ai.main.notes.NotesActivity
import doa.ai.main.notes.bplan.plan.*
import doa.ai.main.notes.bplan.plan.search.SearchBPlanActivity
import doa.ai.profile.collab.CollaborationActivity
import kotlin.Result


class ArchiveBplanFragment : BaseFragment(), BPlanView {



    val idPlans : MutableList<Int> = mutableListOf()
    lateinit var adapterConnectedList: AdapterConnectedListAddBplan

    var currentPageUser = 1
    private var allDataLoadedUser = false
    private  var dataListUser: MutableList<ResultsItemForRequested> = mutableListOf()

    var idPlan = 0
    private lateinit var presenter: BPlanPresenter
    private var prefs: SharedPreferences? = null
    private var tokenLogin: String? = ""

    private lateinit var adapter: AdapterBPlan
    private lateinit var adapterPinned: AdapterBplanPinned
   // private lateinit var adapterBplanMain: AdapterBplanMain
    private lateinit var listResult: MutableList<PlanListResult>
    private lateinit var listResultWithCollab: MutableList<PlanListResult>
    private lateinit var listResultPinned: MutableList<PlanListResult>
    private lateinit var listPinned: MutableList<PlanPinned>
    private lateinit var bplanMain: MutableList<BplanMain>
    private var menu: Menu? = null
    var currentPage = 1
    var currentPagePinned = 1
    private var allDataLoaded = false
    var menuValidate = 0
    lateinit var result: PlanListResult
    lateinit var resultPinned: PlanPinned


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        presenter = BPlanPresenter()
        presenter.planContract()
        listResult = ArrayList()
        listResultWithCollab = ArrayList()
        listPinned = ArrayList()
        listResultPinned = ArrayList()
        prefs = context?.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs?.getString("loginToken", "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(doa.ai.R.layout.fragment_bplan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bplanMain = ArrayList()

        adapter = AdapterBPlan(listResult, context as Context, { data: PlanListResult -> addArchive(data) }, { data: PlanListResult -> addTrash(data) }, { data: PlanListResult -> addPinned(data) },
                { data: PlanListResult -> showToolbar(data) })

        adapterPinned = AdapterBplanPinned(listResultPinned,listPinned, context as Context, { data: PlanListResult -> addArchive(data) }, { data: PlanListResult -> addTrash(data) }, { data: PlanListResult -> addPinned(data) },
                { data: PlanPinned -> showToolbarForPinned(data) })

        adapterConnectedList = AdapterConnectedListAddBplan(dataListUser, activity!!,
                { dataId: ResultsItemForRequested -> addCollabUser(dataId) })


        shimmerPlan.visibility = View.VISIBLE
        tv_empty_data_bplan.text ="Kumpulan arsip rencana akan muncul disini"
//        adapterBplanMain = AdapterBplanMain(bplanMain,activity)
//        listBplanMain.layoutManager = GridLayoutManager(activity,1)
//        listBplanMain.smoothScrollToPosition(RecyclerView.VERTICAL)

        presenter.getlistBPlan("token $tokenLogin", 1, "", "archived")
        presenter.getlistConnected("token $tokenLogin",1,"","connected")
        setupRecylerview()
        // presenter.getDataPlan("token $tokenLogin")

        swipe_bplan.setOnRefreshListener {
            presenter.getlistBPlan("token $tokenLogin", 1, "", "archived")

            currentPage = 1
            currentPagePinned = 1
//            bplanMain.clear()
//            adapterBplanMain.notifyDataSetChanged()


        }

        fabBplan.setOnClickListener {
            context?.startActivity(Intent(context, SetupBPlanActivity::class.java))
        }





    }


    private fun addCollabUser(dataId: ResultsItemForRequested) {

        val receivers: MutableList<Int> = arrayListOf()
        dataId.receiver?.user?.id?.toInt()?.let { receivers.add(it) }
        val bodyShare = BodyShare("plan",receivers,idPlan)

        presenter.sendShare("token $tokenLogin", bodyShare)

    }

    private fun setupRecylerview() {

        scroll_plan.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {

                if(v?.getChildAt(v?.getChildCount() - 1) != null) {
                    if ((scrollY >= (v?.getChildAt(v?.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        currentPage += 1
                        presenter.getlistBPlanAfter("token $tokenLogin", currentPage, "", "archived")

//                        currentPagePinned += 1
//                        presenter.getlistBPlanPinnedAfter("token $tokenLogin", currentPagePinned, "")


                    }
                }
            }
        })

        listBplan.layoutManager = GridLayoutManager(context, 2)
        listBplan.adapter = adapter

        listBplanPinned.layoutManager = GridLayoutManager(context, 2)
        listBplanPinned.adapter = adapterPinned

        listBplan.addOnItemTouchListener(RecyclerTouchListener(activity, listBplan, object : RecyclerClick_Listener{
            override fun onLongClick(view: View?, position: Int) {
                onListItemSelect(position)
            }

            override fun onClick(view: View?, position: Int) {
                if (actionMode!=null){
                    onListItemSelect(position)

                }
            }
        } ))
        adapterPinned.setOnItemClickListener(object : AdapterBplanPinned.OnItemClickListener {
            override fun onItemClick(position: Int) {
                onListItemSelectPinned(position)


            }
        })

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

    private fun onListItemSelect(position: Int) {
        adapter.toggleSelection(position)//Toggle the selection

        val hasCheckedItems = adapter.selectedCount > 0 // && adapter!!.selectedCount < 2//Check if any items are already selected or not
        if (hasCheckedItems && actionMode == null)
            actionMode = activity?.startActionMode(mActionModeCallback)
        else if (!hasCheckedItems && actionMode != null)
        // there no selected items, finish the actionMode
            actionMode!!.finish()

//        if (actionMode != null)
//        //set action mode title on item selection
//            actionMode!!.title = adapter!!
//                    .selectedCount.toString() + " dipilih"


    }

    private fun onListItemSelectPinned(position: Int) {
        adapterPinned.toggleSelection(position)//Toggle the selection

        val hasCheckedItems = adapterPinned.selectedCount > 0 && adapterPinned!!.selectedCount < 2//Check if any items are already selected or not
        if (hasCheckedItems && actionModeUnPinned == null)
            actionModeUnPinned = activity?.startActionMode(mActionModeCallbackUnPinned)
        else if (!hasCheckedItems && actionModeUnPinned != null)
        // there no selected items, finish the actionMode
            actionModeUnPinned!!.finish()

//        if (actionModeUnPinned != null)
//        //set action mode title on item selection
//            actionModeUnPinned!!.title = adapterPinned!!
//                    .selectedCount.toString() + " dipilih"


    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onSuccessBPlan(response: MutableList<doa.ai.main.notes.bplan.plan.Result>) {

    }

    override fun onFailedBPlan(error: String) {
        val toast = Toast.makeText(context as Context, "Data tidak ada", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()

         swipe_bplan.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu

        inflater?.inflate(doa.ai.R.menu.menu_list_archive, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            doa.ai.R.id.actions_search -> {
                startActivity(Intent(activity,SearchBPlanActivity::class.java))
                true
            }
            doa.ai.R.id.actions_grid -> {
                setContentNotesGrid()
                showOption(doa.ai.R.id.actions_list)
                hideOption(doa.ai.R.id.actions_grid)
                true
            }
            doa.ai.R.id.actions_list -> {
                setContentNotesList()
                showOption(doa.ai.R.id.actions_grid)
                hideOption(doa.ai.R.id.actions_list)
                true
            }

            doa.ai.R.id.actions_archive -> {
                context?.startActivity(Intent(context, ArchiveBPlanActivity::class.java)
                        .putExtra("status", "archived"))
                true
            }
            doa.ai.R.id.actions_trash -> {
                context?.startActivity(Intent(context, ArchiveBPlanActivity::class.java)
                        .putExtra("status", "trashed"))
                true
            }

            doa.ai.R.id.actions_draft -> {
                context?.startActivity(Intent(context, ArchiveBPlanActivity::class.java)
                        .putExtra("status", "drafted"))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideOption(id: Int) {
        val item = menu?.findItem(id)
        item?.isVisible = false
    }

    private fun showOption(id: Int) {
        val item = menu?.findItem(id)
        item?.isVisible = true
    }

    private fun addArchive(result: PlanListResult) {
        val partners: MutableList<Int> = mutableListOf()
        val bodyEditBplan = BodyEditBplan(result.end_date,
                result.reminder_time,
                result.reminder,
                partners,
                result.sub_sector.id,
                result.repeat,
                result.scale,
                result.id,
                result.title,
                result.start_date,
                "archived"
        )
        presenter.sendArchivedDataPlan("token $tokenLogin", bodyEditBplan)


    }

    private fun addTrash(result: PlanListResult) {

        val partners: MutableList<Int> = mutableListOf()
        val bodyEditBplan = BodyEditBplan(result.end_date,
                result.reminder_time,
                result.reminder,
                partners,
                result.sub_sector.id,
                result.repeat,
                result.scale,
                result.id,
                result.title,
                result.start_date,
                "trashed"
        )
        presenter.sendTrashDataPlan("token $tokenLogin", bodyEditBplan)


    }

    private fun addPinned(result: PlanListResult) {
        val bodyPinnedPlan = BodyPinnedPlan(result.id)
        presenter.sendPinnedPlan("token $tokenLogin", bodyPinnedPlan)

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
                        presenter.getlistBPlan("token $tokenLogin", 1, "", "archived")
                        presenter.getlistBPlanPinned("token $tokenLogin", 1, "")

                    }, 1500)
                    actionMode?.finish()

                }

                doa.ai.R.id.action_add_trash -> {
                    getAllDataIdPlans()

                    val body = BodyEditPlans(idPlans,"trashed")
                    presenter.sendArchivedDataPlans("token $tokenLogin", body)
                    Handler().postDelayed({
                        presenter.getlistBPlan("token $tokenLogin", 1, "", "archived")

                    }, 1500)


                    actionMode?.finish()


                }

                doa.ai.R.id.action_add_archived -> {
                    getAllDataIdPlans()

                    val body = BodyEditPlans(idPlans,"published")
                    presenter.sendArchivedDataPlans("token $tokenLogin", body)
                    Handler().postDelayed({
                        presenter.getlistBPlan("token $tokenLogin", 1, "", "archived")
                        presenter.getlistBPlanPinned("token $tokenLogin", 1, "")

                    }, 1500)

                    actionMode?.finish()

                }

                doa.ai.R.id.action_add_colaborator -> {
                    idPlan = result.id
                    funShowUserConnected()
                    actionMode?.finish()
                }

                doa.ai.R.id.action_add_copy -> {

                    getAllDataIdPlans()
                    val bodyCopyPlan = BodyCopyPlan(idPlans)
                    presenter.sendCopyPlan("token $tokenLogin", bodyCopyPlan)
                    actionMode?.finish()
                    Handler().postDelayed({
                        presenter.getlistBPlan("token $tokenLogin", 1, "", "archived")

                    }, 1500)


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

    private fun funShowUserConnected() {

        dialogUserList()
    }

    fun dialogUserList() {


        val mBottomSheetDialog = RoundedBottomSheetDialog(context!!)
        val dialogView = LayoutInflater.from(context).inflate(doa.ai.R.layout.view_user_list_connected, null)
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.show()
        dialogView.tv_search.setOnClickListener {
            startActivity(Intent(context, CollaborationActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("flags","suggest"))

        }
        dialogView.rv_connected_collab.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
//        dialogView.rv_connected_collab.addItemDecoration(
//                DividerItemDecoration(context,   DividerItemDecoration.VERTICAL))
        dialogView.rv_connected_collab.adapter = adapterConnectedList

        dialogView.tv_cancel_collab.setOnClickListener {
            mBottomSheetDialog.dismiss()
        }


    }


    var actionModeUnPinned: ActionMode? = null
    private val mActionModeCallbackUnPinned = object : ActionMode.Callback {
        override fun onActionItemClicked(p0: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                doa.ai.R.id.action_add_unpinned -> {


                }


            }


            return true
        }

        override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            p0?.menuInflater?.inflate(doa.ai.R.menu.menu_unpinned, p1)

            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onDestroyActionMode(p0: ActionMode?) {
            actionModeUnPinned = null
            adapterPinned.removeSelection()
        }
    }

    private fun showToolbar(result: PlanListResult) {
        this.result = result


    }

    private fun showToolbarForPinned(result: PlanPinned) {

        resultPinned = result
        idPlan = result.plan.id


    }

    private fun setContentNotesGrid() {

        listBplan.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        listBplan.adapter = adapter

        listBplanPinned.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        listBplanPinned.adapter = adapterPinned



    }

    private fun setContentNotesList() {
        listBplan.layoutManager = LinearLayoutManager(context)
        listBplan.adapter = adapter

        listBplanPinned.layoutManager = LinearLayoutManager(context)
        listBplanPinned.adapter = adapterPinned

    }

    override fun onSuccessBPlans(response: MutableList<PlanListResult>) {
        shimmerPlan.visibility = View.GONE
        swipe_bplan.isRefreshing = false
        if (response.size == 0) {
            showInfoMessage("data rencana usaha lainnya kosong")
            root_empty.visibility = View.VISIBLE
            root_data_bplan.visibility = View.GONE

            tv_other_bplan.visibility = View.GONE
        }
        listResult.clear()
        listResult.addAll(response)
        adapter.notifyDataSetChanged()
//        bplanMain.add(BplanMain(listResultPinned, listPinned, listResult,listResultWithCollab,2,2))
//        listBplanMain.adapter = adapterBplanMain
//        adapterBplanMain.notifyDataSetChanged()


    }

    override fun onSuccessBPlansAfter(response: MutableList<PlanListResult>) {
        if (response.size != 0) {
            allDataLoaded = false
            listResult.addAll(response)
            adapter.notifyDataSetChanged()


        } else {
            allDataLoaded = true
        }
    }

    override fun onSuccessBPlanPinned(response: MutableList<PlanPinned>) {
        Log.e("TAG","pinned ${Gson().toJson(response)}")
        swipe_bplan.isRefreshing = false
        if (response.size != 0) {
            layout_pinned.visibility = View.VISIBLE
            root_empty.visibility = View.VISIBLE
            root_data_bplan.visibility = View.GONE

            // tv_other_bplan.visibility = View.VISIBLE
            allDataLoaded = false
            listResultPinned.clear()
            listPinned.clear()
            response.forEach {
                listResultPinned.addAll(listOf(it.plan))
            }

            listPinned.addAll(response)
            adapterPinned.notifyDataSetChanged()
//
//            bplanMain.add(BplanMain(listResultPinned, listPinned, listResult,listResultWithCollab,1,2))
//            listBplanMain.adapter = adapterBplanMain
//            adapterBplanMain.notifyDataSetChanged()



        } else {
            layout_pinned.visibility = View.GONE
            tv_other_bplan.visibility = View.GONE
            allDataLoaded = false
            listResultPinned.clear()
            response.forEach {
                listResultPinned.addAll(listOf(it.plan))
            }
            listPinned.addAll(response)
            adapterPinned.notifyDataSetChanged()
            allDataLoaded = true
        }
    }

    override fun onSuccessBPlanPinnedAfter(response: MutableList<PlanPinned>) {
        if (response.size != 0) {
            allDataLoaded = false
            response.forEach {
                listResultPinned.addAll(listOf(it.plan))
            }

            listPinned.addAll(response)
            adapterPinned.notifyDataSetChanged()


        } else {
            allDataLoaded = true
        }
    }


    override fun successEditBPLan(planResponse: PlanResponse) {

    }

    override fun hideLoad() {

    }

    override fun showLoad() {


    }

    override fun onSuccess(message: String) {
        showInfoMessage(message)
    }

    override fun failed(error: String) {
        // showErrorMessage("Error : $error")
    }

    override fun onSuccessConnected(data: MutableList<ResultsItemForRequested>) {
        dataListUser.clear()
        dataListUser.addAll(data)
        adapterConnectedList.notifyDataSetChanged()

    }

    override fun onSuccessConnectedAfter(data: MutableList<ResultsItemForRequested>) {

    }

}