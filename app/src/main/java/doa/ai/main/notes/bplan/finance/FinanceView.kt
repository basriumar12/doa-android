package doa.ai.main.notes.bplan.finance

import doa.ai.model.CurrencyResult

interface FinanceView {

    fun onSuccessCurency(curency: MutableList<CurrencyResult>)
    fun onSuccessFinance(data: FinanceResponse)
    fun onSuccessFinanceData(data: FinanceResponse)
    fun onFailedFinance(error: String)
}