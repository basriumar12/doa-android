package doa.ai.main.notes.bplan.plan.archive

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.main.notes.bplan.plan.PlanListResult
import kotlinx.android.synthetic.main.activity_archive_bplan.*

class ArchiveBPlanActivity : BaseActivity(), ArchiveBplanView {

    private lateinit var presenter: ArchiveBplanPresenter
    private lateinit var prefs: SharedPreferences
    private var tokenLogin: String? = ""
    private var status: String? = ""
    lateinit var adapterBPlan: AdapterBPlan
    var data: MutableList<PlanListResult> = mutableListOf()
    var currentPage = 1
    private var allDataLoaded = false
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_bplan)
        setSupportActionBar(toolbarBplanArchive)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        adapterBPlan = AdapterBPlan(data, this)
        prefs = applicationContext.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs.getString("loginToken", "")
        status = intent.getStringExtra("status")

        presenter = ArchiveBplanPresenter()
        presenter.setupContract()
        presenter.getlistBPlan("token $tokenLogin", 1, "", status.toString())
        setupRecylerview()
    }

    private fun setupRecylerview() {
        rv_bPlan.layoutManager = GridLayoutManager(this, 2)
        rv_bPlan.adapter = adapterBPlan
        rv_bPlan.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                    presenter.getlistBPlanAfter("token $tokenLogin", currentPage, "", status.toString())

                    if (allDataLoaded == true) {
                        showInfoMessage("Semua data di load")
                    }

                }
            }
        })


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
            doa.ai.R.id.actions_grid -> {
                setContentBplanGrid()
                showOption(doa.ai.R.id.actions_list)
                hideOption(doa.ai.R.id.actions_grid)
                true
            }
            doa.ai.R.id.actions_list -> {
                setContentBplanList()
                showOption(doa.ai.R.id.actions_grid)
                hideOption(doa.ai.R.id.actions_list)
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        getMenuInflater().inflate(R.menu.menu_list_bplan, menu);
        return true

    }

    private fun setContentBplanGrid() {


        rv_bPlan?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_bPlan?.adapter = adapterBPlan



    }
    private fun setContentBplanList() {
        rv_bPlan?.layoutManager = LinearLayoutManager(this)
        rv_bPlan?.adapter = adapterBPlan

    }

    override fun onSuccess(planList: MutableList<PlanListResult>) {

        data.clear()
        data.addAll(planList)
        rv_bPlan.smoothScrollToPosition(RecyclerView.VERTICAL)
        adapterBPlan.notifyDataSetChanged()

        if (planList.size == 0) {
            showInfoMessage(getString(R.string.data_kosong))
        }
    }

    override fun onSuccessAfter(planList: MutableList<PlanListResult>) {
        if (data.size != 0) {
            allDataLoaded = false
            data.addAll(planList)
            adapterBPlan.notifyDataSetChanged()
        } else {
            allDataLoaded = true
        }
    }

    override fun onFailed(error: String) {
       // showErrorMessage("Error : $error")

    }

    override fun errorMessage(message: String) {
    }

    override fun showLoad() {
        progress_bplan.visibility = View.VISIBLE
    }

    override fun hideLoad() {
        progress_bplan.visibility = View.GONE
    }
}
