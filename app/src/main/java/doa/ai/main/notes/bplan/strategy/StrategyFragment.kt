package doa.ai.main.notes.bplan.strategy

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import doa.ai.R
import doa.ai.base.BaseFragment
import doa.ai.extentions.Dialog
import doa.ai.main.notes.bplan.strategy.adapter.AdapterStrategyOportunities
import doa.ai.main.notes.bplan.strategy.adapter.AdapterStrategySteps
import doa.ai.utils.pref.SavePrefId
import doa.ai.utils.pref.SavePrefSegmentMarket
import doa.ai.utils.pref.SavePrefSegmentStartegy
import doa.ai.utils.pref.SavePrefTokenLogin
import kotlinx.android.synthetic.main.activity_bplan_strategy.*

class StrategyFragment : BaseFragment(), StrategyView {

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_bplan_strategy,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = StrategyPresenter()
        presenter.strategyContract()
        val id = SavePrefId().getPlanID().toString()
        val businessId = SavePrefId().getBussinessId()
        val marketId = SavePrefId().getMarketId()
        
        planID = id.toInt()
        businessID = businessId.toInt()
        marketID = marketId.toInt()
        tokenLogin = SavePrefTokenLogin().getTokenLogin().toString()
        handleStrategy()
        handleView()
        handleSave()


    }

    private fun handleSave() {
        editYearObjective.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                SavePrefSegmentStartegy().setStrategyGoals(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editYearObjective.setText(SavePrefSegmentStartegy().getStrategyGoals())
    }

    private fun deleteData(dataId: String) {
        oportunitiesStrategy.remove(dataId)
        adapterStrategyOportunities.notifyDataSetChanged()
    }

    private fun deleteDataSteps(dataId: String) {
        stepsStrategy.remove(dataId)
        adapterStrategySteps.notifyDataSetChanged()
    }

    private fun handleStrategy() {
        strategyCreate.setOnClickListener {
            if (oportunitiesStrategy.get(0).toString().isNullOrEmpty()){
                showErrorMessage(getString(R.string.data_harus_lengkap))
            } else if (editYearObjective.text.toString().isEmpty()){
                showErrorMessage(getString(R.string.data_harus_lengkap))
            }else if (stepsStrategy.get(0).isNullOrEmpty()){
                showErrorMessage(getString(R.string.data_harus_lengkap))
            } else

                planID?.let { it1 ->

                    presenter.postSegmentStrategy("token $tokenLogin", it1,
                            editYearObjective.text.toString(),
                            oportunitiesStrategy,
                            stepsStrategy)
                }
        }
    }

    private fun handleView() {
        oportunitiesStrategy = ArrayList()
        val oportunities = SavePrefSegmentStartegy().getStrategyOportunity()
        oportunitiesStrategy.add(oportunities)
        rv_strategi_oportunities.layoutManager = LinearLayoutManager(activity!!)
        adapterStrategyOportunities = AdapterStrategyOportunities(oportunitiesStrategy as ArrayList<String>, activity!!,
                { deleteData: String -> deleteData(deleteData) }
        )
        rv_strategi_oportunities.adapter = adapterStrategyOportunities

        tv_add_strategi_oportunities.setOnClickListener {
            oportunitiesStrategy.add("")
            rv_strategi_oportunities.adapter = adapterStrategyOportunities
            adapterStrategyOportunities.notifyDataSetChanged()

        }

        stepsStrategy = ArrayList()
        val steps = SavePrefSegmentStartegy().getStrategyStep()
        stepsStrategy.add(steps)
        rv_strategi_steps.layoutManager = LinearLayoutManager(activity!!)
        adapterStrategySteps = AdapterStrategySteps(stepsStrategy as ArrayList<String>, activity!!,
                { deleteData: String -> deleteDataSteps(deleteData) }
        )
        rv_strategi_steps.adapter = adapterStrategySteps

        tv_add_strategi_steps.setOnClickListener {
            stepsStrategy.add("")
            rv_strategi_steps.adapter = adapterStrategySteps
            adapterStrategySteps.notifyDataSetChanged()

        }



        img_tujuan_yang_dicapai_strategi.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.tujuan_strategi))
        }
        img_peluang_strategi.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.peluang_strategi))
        }
        img_langkah_strategi.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.langakah_strategi))
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

    interface FragmentCallback {
        fun saveStrategyId(id: String)
    }

    private lateinit var fragmentCallback: FragmentCallback
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentCallback = context as FragmentCallback
    }


    override fun onSuccessStrategy(id: Int) {
//        startActivity(Intent(this, FinanceActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                .putExtra("planID", planID)
//                .putExtra("businessID", businessID)
//                .putExtra("marketID", marketID)
//                .putExtra("strategyID", id))
//        this.finish()
        showInfoMessage("Sukses ")

        fragmentCallback.saveStrategyId(id.toString())
    }

    override fun onFailedStrategy(error: String) {
      showErrorMessage(error)
        Log.e("tag","error strategy $error")
    }
    override fun onSuccessDetailStrategy(data: StrategyResponse) {

    }


}