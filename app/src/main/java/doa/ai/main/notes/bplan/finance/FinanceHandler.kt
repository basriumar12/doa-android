package doa.ai.main.notes.bplan.finance

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import com.evrencoskun.tableview.TableView
import doa.ai.R
import doa.ai.extentions.Dialog
import doa.ai.main.notes.bplan.summary.SummaryActivity
import doa.ai.model.CashFlowTrX
import doa.ai.model.CurrencyResult
import doa.ai.model.IncomeStatementTr
import kotlinx.android.synthetic.main.activity_bplan_finance.*

class FinanceActivity: AppCompatActivity(), FinanceView {

    private var mTableAdapter: AdapterTable? = null
    var planID: Int? = 0
    var businessID: Int? = 0
    var marketID: Int? = 0
    var strategyID: Int? = 0
    var financeID: Int? = 0
    var tokenLogin: String? = ""
    var flags: Int = 0
    private lateinit var presenter: FinancePresenter
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bplan_finance)
        setSupportActionBar(toolbarBplanFinance)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = FinancePresenter()
        presenter.financeContract()

        prefs = applicationContext.getSharedPreferences("MyPref",0)
        tokenLogin = prefs.getString("loginToken","")
        planID = intent.getIntExtra("planID",0)
        businessID = intent.getIntExtra("businessID",0)
        marketID = intent.getIntExtra("marketID",0)
        strategyID = intent.getIntExtra("strategyID",0)
        presenter.getCurrency("token $tokenLogin", 1, "A")

        handleFinance()
        hanldeView()

    }

    private fun hanldeView() {
        imageInfoCurrency.setOnClickListener {
            Dialog.progressDialog(this,getString(R.string.simbol_mata_uang))
        }
        imageInfoYear.setOnClickListener {
            Dialog.progressDialog(this,getString(R.string.tahun_rencana_dimulai))
        }

        imageInfoInvest.setOnClickListener {
            Dialog.progressDialog(this,getString(R.string.uang_kas_investasi))
        }

        imageInfoSalesInYear.setOnClickListener {
            Dialog.progressDialog(this,getString(R.string.tentukan_penjualan_tahun_pertama))
        }

        imageInfoGrowthYear.setOnClickListener {
            Dialog.progressDialog(this,getString(R.string.growth_setiap_tahun))
        }

        imageInfoCashSales.setOnClickListener {
            Dialog.progressDialog(this,getString(R.string.biaya_penjualan))
        }

        imageInfoExpansesYear.setOnClickListener {
            Dialog.progressDialog(this,getString(R.string.pengeluaran_ditahun_pertama))
        }
        imageInfoExpanseGrowth.setOnClickListener {
            Dialog.progressDialog(this,getString(R.string.pertumbuhan_pengeluaran_setiap_tahun))
        }

        imageInfoDayCredit.setOnClickListener {
            Dialog.progressDialog(this,getString(R.string.tempo_yang_diberikan_pada_pelanggan))
        }

        imageInfoDaySuppliers.setOnClickListener {
            Dialog.progressDialog(this,getString(R.string.tempo_yang_diberikan_pada_pemasok))
        }


    }

    private fun initializeTableView(tableView: TableView, incomeStatementTr: MutableList<IncomeStatementTr>, flowTrX: MutableList<CashFlowTrX>) {

        // Create TableView Adapter
        mTableAdapter = AdapterTable(this, incomeStatementTr, flowTrX)
        tableView.adapter = mTableAdapter

    }

    private fun handleFinance() {
        financeCreate.setOnClickListener {
            if (flags == 0) {
                planID?.let { it1 ->
                    presenter.postSegmentFinance("token $tokenLogin", it1, textCurrencyFinancy.text.toString(),
                            editPlanYear.text.toString().toInt(),
                            editInvest.text.toString().toDouble(),
                            editSalesYear.text.toString().toDouble(),
                            editSalesGrowth.text.toString().toInt(),
                            editCashSales.text.toString().toInt(),
                            editExpanses.text.toString().toDouble(),
                            editExpanseGrowth.text.toString().toInt(),
                            editCreditCustomers.text.toString().toInt(),
                            editCreditSupplier.text.toString().toInt())
                }
            } else {
                startActivity(Intent(this, SummaryActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra("planID",planID)
                        .putExtra("businessID", businessID)
                        .putExtra("marketID", marketID)
                        .putExtra("strategyID", strategyID)
                        .putExtra("financeID", financeID))

                this.finish()
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
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSuccessCurency(curency: MutableList<CurrencyResult>) {
        textCurrencyFinancy.setOnClickListener { v ->

            val popUpmenu = PopupMenu(this, v)
            for (_currency in curency.iterator()) {
                popUpmenu.menu.add(_currency.id, _currency.id,0,_currency.currency_code)
                popUpmenu.setOnMenuItemClickListener { item ->
                    textCurrencyFinancy.text = item.title
                    false
                }
                popUpmenu.show()
            }
        }
    }

    override fun onSuccessFinance(data: FinanceResponse) {
        Log.d("table", data.result.analysis.tables.income_statement.tr.toString())
        financeID = data.result.id

        initializeTableView(tableViewIncome,
                data.result.analysis.tables.income_statement.tr,
                data.result.analysis.tables.cash_flow.tr)
        mTableAdapter?.setStatement()

        initializeTableView(tableViewCash,
                data.result.analysis.tables.income_statement.tr,
                data.result.analysis.tables.cash_flow.tr)
        mTableAdapter?.setCashFlow()
        flags = 1
        financeCreate.text = "Selesai"
    }

    override fun onFailedFinance(error: String) {
        val toast = Toast.makeText(this,"pastikan data yang input anda benar", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }
    override fun onSuccessFinanceData(data: FinanceResponse) {

    }

}