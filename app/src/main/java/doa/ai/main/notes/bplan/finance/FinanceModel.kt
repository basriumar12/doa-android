package doa.ai.main.notes.bplan.finance

import doa.ai.model.CashFlowTrX
import doa.ai.model.IncomeStatementTr
import java.math.BigDecimal

data class FinanceModel(
    val plan: Int,
    val currency: String,
    val start_year_plan: Int,
    val cash_invested: Double,
    val sales_year: Double,
    val sales_growth_year: Int,
    val cash_of_sale: Int,
    val expanses_first_year: Double,
    val expanses_growth_year: Int,
    val customer_day_credit: Int,
    val supplier_day_credit: Int
)

data class FinanceResponse(
    val result: Result,
    val status: Int
)

data class Result(
    val analysis: Analysis,
    val cash_invested: Double,
    val cash_of_sale: Double,
    val created_at: String,
    val currency: String,
    val customer_day_credit: Int,
    val deleted_at: Any,
    val expanses_first_year: Double,
    val expanses_growth_year: Double,
    val id: Int,
    val plan: Int,
    val sales_growth_year: Double,
    val sales_year: Double,
    val start_year_plan: Int,
    val supplier_day_credit: Int,
    val updated_at: String
)

data class Analysis(
    val roy_data: RoyData,
    val tables: Tables
)

data class RoyData(
    val duration: Duration,
    val duration_label: String,
    val duration_total: DurationTotal,
    val first_gross_profit_margin: Double,
    val first_net_profit_margin: Double
)

data class Duration(
    val days: Int,
    val months: Int,
    val years: Int
)

data class DurationTotal(
    val roy_in_days: Int,
    val roy_in_months: Int,
    val roy_in_years: Int
)

data class Tables(
    val cash_flow: CashFlow,
    val income_statement: IncomeStatement,
    val roy_analysis: RoyAnalysis
)

data class CashFlow(
    val label: String,
    val tr: MutableList<CashFlowTrX>
)

data class IncomeStatement(
    val label: String,
    val tr: MutableList<IncomeStatementTr>
)

data class RoyAnalysis(
    val label: String,
    val tr: List<TrXX>
)

data class TrXX(
    val bold: Boolean,
    val label: String,
    val td: List<BigDecimal>
)

