package doa.ai.main.notes.bplan.summary

import doa.ai.main.notes.bplan.market.KeyCompetitors
import doa.ai.model.CashFlowTrX
import doa.ai.model.IncomeStatementTr

data class SummaryBody(
        val plan: Int,
        val business: Int,
        val market: Int,
        val strategy: Int,
        val finance: Int,
        val attachment: String,
        val conclusion: String
)

data class SummaryResponse(
    val result: Result,
    val status: Int,
    val message : String,
    val success : Boolean
)

data class Result(
    val attachment: String,
    val business: Business,
    val conclusion: String,
    val created_at: String,
    val deleted_at: Any,
    val finance: Finance,
    val id: Int,
    val market: Market,
    val plan: Plan,
    val strategy: Strategy,
    val updated_at: String,
    val short_summary: String

)

data class Market(
        val competitive_advantage: String,
        val deleted_at: Any,
        val geographic_market: String,
        val id: Int,
        val key_competitors: MutableList<KeyCompetitors>,
        val plan: Int,
        val product: String,
        val target_customer: String,
        val short_summary: String,
        val customer_managements: MutableList<String>,
        val customer_reach_ways: MutableList<String>
)

data class Strategy(
    val deleted_at: Any,
    val id: Int,
    val plan: Int,
    val strategic_opportunities: List<String>,
    val strategic_steps: List<String>,
    val year_objective: String,
    val short_summary: String


)

data class Business(
    val address: String,
    val deleted_at: Any,
    val id: Int,
    val key_personals: List<KeyPersonal>,
    val business_partners: MutableList<String>,
    val business_activities: MutableList<String>,
    val operating_expenses: MutableList<String>,
    val key_resources: List<String>,
    val number: Int,
    val plan: Int,
    val rt: Int,
    val rw: Int,
    val structure: String,
    val sub_district: Int,
    val village: String,
    val short_summary: String
)

data class KeyPersonal(
    val content_type: Int,
    val creator: Int,
    val deleted_at: Any,
    val id: Int,
    val key: String,
    val object_id: Int,
    val value: List<String>
)

data class Finance(
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
    val updated_at: String,
    val short_summary: String
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

data class DurationTotal(
    val roy_in_days: Int,
    val roy_in_months: Int,
    val roy_in_years: Int
)

data class Duration(
    val days: Int,
    val months: Int,
    val years: Int
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
    val td: List<Double>
)

data class Plan(
    val created_at: String,
    val deleted_at: Any,
    val end_date: String,
    val id: Int,
    val reminder: Boolean,
    val reminder_time: String,
    val repeat: Boolean,
    val scale: String,
    val start_date: String,
    val sub_sector: SubSector,
    val title: String,
    val updated_at: String
)

data class SubSector(
    val deleted_at: Any,
    val description: Any,
    val id: Int,
    val parent: Int,
    val title: String
)