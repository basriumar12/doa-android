package doa.ai.main.notes.bplan.strategy

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.vincent.filepicker.activity.BaseActivity
import doa.ai.R
import doa.ai.database.modelDB.NotesEntry
import doa.ai.extentions.Dialog
import doa.ai.main.notes.bplan.finance.FinanceActivity
import doa.ai.main.notes.bplan.strategy.adapter.AdapterStrategyOportunities
import doa.ai.main.notes.bplan.strategy.adapter.AdapterStrategySteps
import kotlinx.android.synthetic.main.activity_bplan_business.*
import kotlinx.android.synthetic.main.activity_bplan_strategy.*

class StrategyActivity : doa.ai.base.BaseActivity(), StrategyView {

    var planID: Int? = 0
    var businessID: Int? = 0
    var marketID: Int? = 0
    var tokenLogin: String? = ""
    private lateinit var presenter: StrategyPresenter
    private lateinit var prefs: SharedPreferences
    var stepsStrategy: MutableList<String> = ArrayList()
    var oportunitiesStrategy: MutableList<String> = ArrayList()
    lateinit var adapterStrategyOportunities: AdapterStrategyOportunities
    lateinit var adapterStrategySteps: AdapterStrategySteps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bplan_strategy)
        setSupportActionBar(toolbarBplanStrategy)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = StrategyPresenter()
        presenter.strategyContract()

        prefs = applicationContext.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs.getString("loginToken", "")
        planID = intent.getIntExtra("planID", 0)
        businessID = intent.getIntExtra("businessID", 0)
        marketID = intent.getIntExtra("marketID", 0)
        handleStrategy()
        handleView()
    }

    private fun deleteData(dataId: String) {
        oportunitiesStrategy.remove(dataId)
        adapterStrategyOportunities.notifyDataSetChanged()
    }

    private fun deleteDataSteps(dataId: String) {
        stepsStrategy.remove(dataId)
        adapterStrategySteps.notifyDataSetChanged()
    }

    private fun handleView() {
        oportunitiesStrategy = ArrayList()
        oportunitiesStrategy.add("")
        rv_strategi_oportunities.layoutManager = LinearLayoutManager(this)
        adapterStrategyOportunities = AdapterStrategyOportunities(oportunitiesStrategy as ArrayList<String>, this,
                { deleteData: String -> deleteData(deleteData) }
        )
        rv_strategi_oportunities.adapter = adapterStrategyOportunities

        tv_add_strategi_oportunities.setOnClickListener {
            oportunitiesStrategy.add("")
            rv_strategi_oportunities.adapter = adapterStrategyOportunities
            adapterStrategyOportunities.notifyDataSetChanged()

        }

        stepsStrategy = ArrayList()
        stepsStrategy.add("")
        rv_strategi_steps.layoutManager = LinearLayoutManager(this)
        adapterStrategySteps = AdapterStrategySteps(stepsStrategy as ArrayList<String>, this,
                { deleteData: String -> deleteDataSteps(deleteData) }
        )
        rv_strategi_steps.adapter = adapterStrategySteps

        tv_add_strategi_steps.setOnClickListener {
            stepsStrategy.add("")
            rv_strategi_steps.adapter = adapterStrategySteps
            adapterStrategySteps.notifyDataSetChanged()

        }



        img_tujuan_yang_dicapai_strategi.setOnClickListener {
            Dialog.progressDialog(this, getString(R.string.tujuan_strategi))
        }
        img_peluang_strategi.setOnClickListener {
            Dialog.progressDialog(this, getString(R.string.peluang_strategi))
        }
        img_langkah_strategi.setOnClickListener {
            Dialog.progressDialog(this, getString(R.string.langakah_strategi))
        }
    }

    private fun handleStrategy() {
        strategyCreate.setOnClickListener {
            if (oportunitiesStrategy.get(0).toString().isNullOrEmpty()){
                showErrorMessage(getString(R.string.data_harus_lengkap))
            }
            else if (stepsStrategy.get(0).isNullOrEmpty()){
                showErrorMessage(getString(R.string.data_harus_lengkap))
            }
            else if (editYearObjective.text.toString().isEmpty()){
                showErrorMessage(getString(R.string.data_harus_lengkap))
            }
            else

            planID?.let { it1 ->

                presenter.postSegmentStrategy("token $tokenLogin", it1,
                        editYearObjective.text.toString(),
                        oportunitiesStrategy,
                        stepsStrategy)
            }
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        return when (id) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSuccessStrategy(id: Int) {
        startActivity(Intent(this, FinanceActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra("planID", planID)
                .putExtra("businessID", businessID)
                .putExtra("marketID", marketID)
                .putExtra("strategyID", id))
        this.finish()
    }

    override fun onFailedStrategy(error: String) {
        val toast = Toast.makeText(this, "pastikan data yang input anda benar", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
    override fun onSuccessDetailStrategy(data: StrategyResponse) {

    }
}