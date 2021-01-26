package doa.ai.main.notes.bplan.finance

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import com.evrencoskun.tableview.TableView
import doa.ai.R
import doa.ai.base.BaseFragment
import doa.ai.extentions.Dialog
import doa.ai.main.notes.bplan.summary.SummaryActivity
import doa.ai.main.notes.bplan.summary.SummarysActivity
import doa.ai.model.CashFlowTrX
import doa.ai.model.CurrencyResult
import doa.ai.model.IncomeStatementTr
import doa.ai.utils.formatStringCurrency
import doa.ai.utils.pref.SavePrefId
import doa.ai.utils.pref.SavePrefSegmentFinnance
import doa.ai.utils.pref.SavePrefSegmentStartegy
import doa.ai.utils.pref.SavePrefTokenLogin
import doa.ai.utils.removePeriodString
import kotlinx.android.synthetic.main.activity_bplan_finance.*
import kotlinx.android.synthetic.main.activity_bplan_finance.view.*

class FinnanceFragment : BaseFragment(), FinanceView {


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
    lateinit var textCurrencyFinancy: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_bplan_finance, container, false)
        textCurrencyFinancy = view.textCurrencyFinancy

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = FinancePresenter()
        presenter.financeContract()

        val id = SavePrefId().getPlanID().toString()
        val businessId = SavePrefId().getBussinessId()
        val marketId = SavePrefId().getMarketId()
        val strategyId = SavePrefId().getStrategyId()

        planID = id.toInt()
        businessID = businessId.toInt()
        marketID = marketId.toInt()
        strategyID = strategyId.toInt()
        tokenLogin = SavePrefTokenLogin().getTokenLogin().toString()

        presenter.getCurrency("token $tokenLogin", 1, "A")
        val totalAmount= ""
      //  editInvest.text= Editable.Factory.getInstance().newEditable(totalAmount.formatStringCurrency())
        editInvest.addTextChangedListener(validateField(editInvest))
        editSalesYear.addTextChangedListener(validateField(editSalesYear))
        editExpanses.addTextChangedListener(validateField(editExpanses))
        editCreditCustomers.addTextChangedListener(validateField(editCreditCustomers))
        editCreditSupplier.addTextChangedListener(validateField(editCreditSupplier))
        editCashSales.addTextChangedListener(validateField(editCashSales))



        handleFinance()
        hanldeView()
        handleSave()

    }

    private fun handleSave() {
        editPlanYear.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentFinnance().setFinnancePlanning(p0.toString())

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editInvest.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                SavePrefSegmentFinnance().setFinnanceInvestMoney(p0.toString())

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editSalesYear.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                SavePrefSegmentFinnance().setFinnanceSalesFirstYear(p0.toString())

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editSalesGrowth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentFinnance().setFinnanceSalesGrowth(p0.toString())


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editCashSales.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentFinnance().setFinnanceCashSales(p0.toString())


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editExpanses.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentFinnance().setFinnanceExpansesFirstYear(p0.toString())


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editExpanseGrowth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentFinnance().setFinnanceExpansesGrowth(p0.toString())

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editCreditCustomers.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentFinnance().setFinnanceCreditCustomer(p0.toString())

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        editCreditSupplier.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentFinnance().setFinnanceCreditSupplier(p0.toString())

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        } )

        textCurrencyFinancy.text = SavePrefSegmentFinnance().getFinnanceCurrency()
        editPlanYear.setText(SavePrefSegmentFinnance().getFinnancePlanning())
        editInvest.setText(SavePrefSegmentFinnance().getFinnanceInvestMoney())
        editSalesYear.setText(SavePrefSegmentFinnance().getFinnanceSalesFirstYear())
        editSalesGrowth.setText(SavePrefSegmentFinnance().getFinnanceSalesFirstGrowth())
        editCashSales.setText(SavePrefSegmentFinnance().getFinnanceCashSales())
        editExpanses.setText(SavePrefSegmentFinnance().getFinnanceExpansesFirstYear())
        editExpanseGrowth.setText(SavePrefSegmentFinnance().getFinnanceExpansesGrowth())
        editCreditCustomers.setText(SavePrefSegmentFinnance().getFinnanceCreditCustomer())
        editCreditSupplier.setText(SavePrefSegmentFinnance().getFinnanceCreditSupplier())



    }


    private fun validateField(form: EditText): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                form.let {
                    it.removeTextChangedListener(this)
                    //nilaiNominal = if (p0.isNullOrBlank()) 0 else p0.toString().removePeriodString().toInt()
                    it.setText(p0.toString().removePeriodString().formatStringCurrency())
                    it.setSelection(it.length())
                    it.addTextChangedListener(this)
                }
            }
        }
    }
    private fun hanldeView() {
        imageInfoCurrency.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.simbol_mata_uang))
        }
        imageInfoYear.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.tahun_rencana_dimulai))
        }

        imageInfoInvest.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.uang_kas_investasi))
        }

        imageInfoSalesInYear.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.tentukan_penjualan_tahun_pertama))
        }

        imageInfoGrowthYear.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.growth_setiap_tahun))
        }

        imageInfoCashSales.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.biaya_penjualan))
        }

        imageInfoExpansesYear.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.pengeluaran_ditahun_pertama))
        }
        imageInfoExpanseGrowth.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.pertumbuhan_pengeluaran_setiap_tahun))
        }

        imageInfoDayCredit.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.tempo_yang_diberikan_pada_pelanggan))
        }

        imageInfoDaySuppliers.setOnClickListener {
            Dialog.progressDialog(activity!!, getString(R.string.tempo_yang_diberikan_pada_pemasok))
        }
    }

    private fun initializeTableView(tableView: TableView, incomeStatementTr: MutableList<IncomeStatementTr>, flowTrX: MutableList<CashFlowTrX>) {

        // Create TableView Adapter
        mTableAdapter = AdapterTable(activity!!, incomeStatementTr, flowTrX)
        tableView.adapter = mTableAdapter

    }


    private fun handleFinance() {
        financeCreate.setOnClickListener {
            if (SavePrefId().getBussinessId().equals("0")) {
                showInfoMessage(getString(R.string.segment_bisnis_belum_di_isi))
            } else if (SavePrefId().getMarketId().equals("0")) {

                showInfoMessage(getString(R.string.segment_market_belum_di_isi))
            } else if (SavePrefId().getStrategyId().equals("0")) {

                showInfoMessage(getString(R.string.segment_strategy_belum_di_isi))
            }

            else if (editPlanYear.text.toString().isEmpty()) {
                showErrorMessage(getString(R.string.data_harus_lengkap))
            } else if (editInvest.text.toString().isEmpty()) {
                showErrorMessage(getString(R.string.data_harus_lengkap))
            } else if (editSalesYear.text.toString().isEmpty()) {
                showErrorMessage(getString(R.string.data_harus_lengkap))
            } else if (editSalesGrowth.text.toString().isEmpty()) {
                showErrorMessage(getString(R.string.data_harus_lengkap))
            } else if (editCashSales.text.toString().isEmpty()) {
                showErrorMessage(getString(R.string.data_harus_lengkap))
            } else if (editExpanses.text.toString().isEmpty()) {
                showErrorMessage(getString(R.string.data_harus_lengkap))
            } else if (editExpanseGrowth.text.toString().isEmpty()) {
                showErrorMessage(getString(R.string.data_harus_lengkap))
            } else if (editCreditCustomers.text.toString().isEmpty()) {
                showErrorMessage(getString(R.string.data_harus_lengkap))
            } else if (editCreditSupplier.text.toString().isEmpty()) {
                showErrorMessage(getString(R.string.data_harus_lengkap))
            } else {

                if (flags == 0) {
                    planID?.let { it1 ->
                        presenter.postSegmentFinance("token $tokenLogin", it1, textCurrencyFinancy.text.toString(),
                                editPlanYear.text.toString().toInt(),
                                editInvest.text.toString().removePeriodString().toDouble(),
                                editSalesYear.text.toString().removePeriodString().toDouble(),
                                editSalesGrowth.text.toString().removePeriodString().toInt(),
                                editCashSales.text.toString().removePeriodString().toInt(),
                                editExpanses.text.toString().removePeriodString().toDouble(),
                                editExpanseGrowth.text.toString().removePeriodString().toInt(),
                                editCreditCustomers.text.toString().removePeriodString().toInt(),
                                editCreditSupplier.text.toString().removePeriodString().toInt()
                        )
                    }
                } else {
                    startActivity(Intent(activity!!, SummarysActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("planID", planID)
                            .putExtra("businessID", businessID)
                            .putExtra("marketID", marketID)
                            .putExtra("strategyID", strategyID)
                            .putExtra("financeID", financeID))

                    activity?.finish()
                }
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

    override fun onSuccessCurency(curency: MutableList<CurrencyResult>) {
        textCurrencyFinancy.setOnClickListener { v ->

            val popUpmenu = PopupMenu(activity!!, v)
            for (_currency in curency.iterator()) {
                popUpmenu.menu.add(_currency.id, _currency.id, 0, _currency.currency_code)
                popUpmenu.setOnMenuItemClickListener { item ->
                    SavePrefSegmentFinnance().setFinnanceCurrency(item.title.toString())
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
        SavePrefId().setFinnanceId(data.result.id.toString())
        //simpan summary id 1019000 untuk validasi di ringkasan usaha
        SavePrefId().setSummaryId("1019000")
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
        Log.e("tag", "error finnance $error")
        val toast = Toast.makeText(activity!!, "pastikan data yang input anda benar", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun onSuccessFinanceData(data: FinanceResponse) {

    }
}