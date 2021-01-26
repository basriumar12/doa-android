package doa.ai.main.notes.ideation.trashed

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import doa.ai.R
import doa.ai.base.BaseFragment
import doa.ai.database.AppDatabase
import doa.ai.database.modelDB.NotesEntry
import doa.ai.database.modelDB.TrashEntry
import doa.ai.extentions.AppExecutors
import doa.ai.main.writeIdeation.WriteIdeationActivity
import kotlinx.android.synthetic.main.fragment_ideation.*
import doa.ai.MainActivity

import android.app.Activity.RESULT_OK
import android.os.Build
import android.os.Handler
import android.util.SparseBooleanArray
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.*
import doa.ai.main.notes.bplan.plan.AdapterConnectedListAddBplan
import doa.ai.main.notes.bplan.plan.BodyCopyPlan
import doa.ai.main.notes.bplan.plan.BodyEditPlans
import doa.ai.main.notes.bplan.plan.BodyPinnedPlans
import doa.ai.main.notes.ideation.AdapterNotes
import doa.ai.main.notes.ideation.AdapterNotesHandlerFromDb
import doa.ai.main.notes.ideation.IdeationPresenter
import doa.ai.main.notes.ideation.IdeationView
import doa.ai.main.notes.ideation.model.*
import doa.ai.profile.collab.ResultsItemForRequested
import doa.ai.utils.adapter.RecyclerClick_Listener
import doa.ai.utils.adapter.RecyclerTouchListener
import kotlinx.android.synthetic.main.fragment_bplan.*
import kotlinx.android.synthetic.main.fragment_bplan.root_empty
import kotlinx.android.synthetic.main.fragment_ideation.fabWriteIdeation
import kotlinx.android.synthetic.main.fragment_ideation.listNotes
import kotlinx.android.synthetic.main.fragment_ideation.progressbar_idea
import kotlinx.android.synthetic.main.fragment_ideation.swipe_refresh_ideation
import kotlinx.android.synthetic.main.fragment_ideations.*


class IdeationTrashFragment : BaseFragment(), IdeationView {


    companion object {
        fun newInstance(): IdeationTrashFragment {
            return IdeationTrashFragment()
        }
    }
    lateinit var adapterConnectedList: AdapterConnectedListAddBplan
    private  var dataListUser: MutableList<ResultsItemForRequested> = mutableListOf()

    val idIdeations  : MutableList<Int> = mutableListOf()
    lateinit var adapterNotesHandlerFromDb: AdapterNotesHandlerFromDb
    private var idEdit = 0
    private var tokenLogin: String? = ""
    private lateinit var ideationPresenter: IdeationPresenter
    private lateinit var adapterNotes: AdapterNotes
    private lateinit var mDB: AppDatabase
    private var menu: Menu? = null
    private lateinit var prefs: SharedPreferences
    var dataResultItem: MutableList<ResultsItem> = mutableListOf()
    private var allDataLoaded = false
    var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mDB = AppDatabase.getInstance(context as Context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(doa.ai.R.layout.fragment_ideations, container, false)
    }

    override fun onStart() {
        super.onStart()
        ideationPresenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        ideationPresenter.detachView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setContentNotesGrid()
        setContentView()
        adapterNotesHandlerFromDb = AdapterNotesHandlerFromDb(dataResultItem, context,
                { dataId: ResultsItem -> addDatas(dataId) })

        adapterConnectedList = AdapterConnectedListAddBplan(dataListUser, activity!!,
                { dataId: ResultsItemForRequested -> addCollabUser(dataId) })

        prefs = context!!.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs.getString("loginToken", "")
        ideationPresenter = IdeationPresenter()
        ideationPresenter.ideationContract()
        ideationPresenter.getDataIdeation("token $tokenLogin", currentPage,"trashed")
        ideationPresenter.getlistConnected("token $tokenLogin",1,"","connected")

        setUpRecylerview()

        swipe_refresh_ideation.setOnRefreshListener {
            //ideationPresenter.getDataIdeation("token $tokenLogin", 1)
        }


//        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, p1: Int) {
//                AppExecutors.instance.diskIO().execute {
//                    val position = viewHolder.adapterPosition
//                    val notes = adapterNotes.getNotes()
//                    notes?.get(position)?.let { mDB.appsDao().deleteNotes(it) }
//                    val trash = TrashEntry(0
//                            , notes?.get(position)?.image
//                            , notes?.get(position)?.title
//                            , notes?.get(position)?.desc
//                            , notes?.get(position)?.label)
//                    mDB.appsDao().insertTrash(trash)
//                }
//            }
//        }).attachToRecyclerView(listNotes)
    }


    private fun setUpRecylerview() {
        listNotes?.layoutManager = GridLayoutManager(context, 2)
        listNotes.adapter = adapterNotesHandlerFromDb
        listNotes.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var directiorDown: Boolean = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                directiorDown = dy > 0
            }


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (recyclerView.canScrollVertically(1).not()
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && directiorDown
                ) {
                    currentPage += 1
                    ideationPresenter.getDataIdeation("token $tokenLogin", currentPage,"trashed")

                    if (allDataLoaded == true) {
                        showInfoMessage("Semua data di load")
                    }

                }
            }
        })

        listNotes.addOnItemTouchListener(RecyclerTouchListener(activity, listNotes, object : RecyclerClick_Listener {
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
    private fun addCollabUser(dataId: ResultsItemForRequested) {

        val receivers: MutableList<Int> = arrayListOf()
        dataId.receiver?.user?.id?.toInt()?.let { receivers.add(it) }
//        val bodyShare = BodyShare("idea",receivers,ListidIdeations)
//
//        presenter.sendShare("token $tokenLogin", bodyShare)

    }
    private fun getAllDataIdIdeation() {
        val selected: SparseBooleanArray
        selected = adapterNotesHandlerFromDb.selectedIds!!
        idIdeations.clear()
        for (data in 0 until selected.size() ) {
            if (selected.valueAt(data)) {
                val dataSelect = dataResultItem.get(selected.keyAt(data)).id
                Log.e("TAG", "data select $dataSelect")
                dataResultItem.get(selected.keyAt(data)).id?.let { idIdeations.add(it) }

            }

        }
    }
    private fun onListItemSelect(position: Int) {
        adapterNotesHandlerFromDb.toggleSelection(position)//Toggle the selection
        val hasCheckedItems = adapterNotesHandlerFromDb.selectedCount > 0 // && adapter!!.selectedCount < 2//Check if any items are already selected or not
        if (hasCheckedItems && actionMode == null)
            actionMode = activity?.startActionMode(mActionModeCallback)
        else if (!hasCheckedItems && actionMode != null)
            actionMode!!.finish()


    }


    var actionMode: ActionMode? = null
    private val mActionModeCallback = object : ActionMode.Callback {
        override fun onActionItemClicked(p0: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                doa.ai.R.id.action_add_pinned -> {

                    getAllDataIdIdeation()
                    val body = BodyIdeaPinned(idIdeations)
                    ideationPresenter.postIdeationPinned("token $tokenLogin", body)
                    actionMode?.finish()
                    Handler().postDelayed({
                        ideationPresenter.getDataIdeation("token $tokenLogin", currentPage,"trashed")
                    }, 1500)

                }

                doa.ai.R.id.action_add_trash -> {
                    getAllDataIdIdeation()
                    val body = BodyIdeaDeleteFull(idIdeations)
                    ideationPresenter.deleteFullIdeation("token $tokenLogin", body)
                    Handler().postDelayed({
                        ideationPresenter.getDataIdeation("token $tokenLogin", currentPage,"trashed")
                    }, 1500)

                    actionMode?.finish()



                }

                doa.ai.R.id.action_add_archived -> {
                    getAllDataIdIdeation()
                    val body = BodyIdeaUpdateStatus(idIdeations,"archived")
                    ideationPresenter.postUpdateStatusIdeation("token $tokenLogin", body)
                    Handler().postDelayed({
                        ideationPresenter.getDataIdeation("token $tokenLogin", currentPage,"trashed")
                    }, 1500)

                    actionMode?.finish()
                }

                doa.ai.R.id.action_add_colaborator -> {

                    actionMode?.finish()
                }

                doa.ai.R.id.action_add_copy -> {
                    getAllDataIdIdeation()
                    val body = BodyCopyIdea(idIdeations)
                    ideationPresenter.postCopyIdeatin("token $tokenLogin", body)
                    Handler().postDelayed({
                        ideationPresenter.getDataIdeation("token $tokenLogin", currentPage,"trashed")
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
            adapterNotesHandlerFromDb.removeSelection()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu
        inflater?.inflate(doa.ai.R.menu.menu_list_ideas, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setContentView() {
        fabWriteIdeation.setOnClickListener {
            val intent = Intent(context, WriteIdeationActivity::class.java)
            startActivityForResult(intent, 1)
            startActivity(Intent(context, WriteIdeationActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
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

    private fun addData(dataId: NotesEntry) {
        idEdit = dataId.id
        var labels = listOf(dataId.label)
        ideationPresenter.postIdeation("token $tokenLogin",
                dataId.desc.toString(),
                dataId.title.toString(),
                labels as List<String>

        )

    }

    private fun addDatas(dataId: ResultsItem) {
      //  dataId.id?.let { ideationPresenter.postIdeationPinned("token $tokenLogin", it) }

    }

    var titleDelete = ""
    var deskDelete = ""
    var labelDelete = ""
    private fun deleteData(dataId: NotesEntry) {
        idEdit = dataId.id
        titleDelete = dataId.title.toString()
        deskDelete = dataId.desc.toString()
        labelDelete = dataId.label.toString()
        val dataIdFromDb = dataId.idFromDb?.toInt()
        Log.e("tag", "dataid $dataIdFromDb")
        if (dataIdFromDb != null)
            ideationPresenter.deleteIdeation("token $tokenLogin", dataIdFromDb)


    }

    override fun onResume() {
        super.onResume()
//        val notes: LiveData<MutableList<NotesEntry>> = mDB.appsDao().loadAllNotes()
//        activity?.let {
//            notes.observe(it, Observer<MutableList<NotesEntry>> { t ->
//                t?.reverse()
//                adapterNotes = AdapterNotes(t, context, { dataId: NotesEntry -> addData(dataId) },{ dataId: NotesEntry -> deleteData(dataId) }, mDB)
//                listNotes?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//                listNotes?.adapter = adapterNotes
//            })
//        }
    }

    private fun setContentNotesGrid() {
        listNotes?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        listNotes?.adapter = adapterNotesHandlerFromDb


    }

    private fun setContentNotesList() {

        listNotes?.layoutManager = LinearLayoutManager(context)
        listNotes?.adapter = adapterNotesHandlerFromDb
        listNotes?.smoothScrollToPosition(RecyclerView.VERTICAL)

    }

    override fun showSucces(data: Result) {
        val labels = data.labels?.get(0).toString()
        val notesEntry = NotesEntry(idEdit, "", data.title, data.description, labels, "1", data.id.toString())
        AppExecutors.instance.diskIO().execute {
            mDB.appsDao().updateNotes(notesEntry)
        }

    }

    override fun failed() {

    }

    override fun message(message: String) {
        showInfoMessage(message)
        swipe_refresh_ideation.isRefreshing = false

    }

    override fun showLoad() {
        progressbar_idea.visibility = View.VISIBLE
    }

    override fun hideLoad() {
        swipe_refresh_ideation.isRefreshing = false
        progressbar_idea.visibility = View.GONE

    }

    override fun showSuccesDelete(message: String) {
        val notes = NotesEntry(idEdit, "", titleDelete, deskDelete, labelDelete, "1")
        AppExecutors.instance.diskIO().execute {
            mDB.appsDao().deleteNotes(notes)
        }


    }

    override fun onSuccessConnected(data: MutableList<ResultsItemForRequested>) {
        dataListUser.clear()
        dataListUser.addAll(data)
        adapterConnectedList.notifyDataSetChanged()

    }

    override fun showDataIdea(data: MutableList<ResultsItem>?) {
        swipe_refresh_ideation.isRefreshing = false
        dataResultItem.clear()
        if (data?.size == 0){
            root_empty.visibility = View.VISIBLE
            tv_empty_data_ideation.text = "Data Sampah Kosong"
        }
        data?.let { this.dataResultItem.addAll(it) }
        listNotes?.smoothScrollToPosition(RecyclerView.VERTICAL)
        adapterNotesHandlerFromDb.notifyDataSetChanged()
    }

    override fun showDataIdeaAfter(data: MutableList<ResultsItem>?) {
        if (data?.size != 0) {
            allDataLoaded = false
            data?.let { dataResultItem.addAll(it) }
            adapterNotesHandlerFromDb.notifyDataSetChanged()
        } else {
            allDataLoaded = true
        }

    }


}