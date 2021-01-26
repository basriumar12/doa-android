package doa.ai.main.notes.bplan.plan

import com.google.gson.annotations.SerializedName
import doa.ai.main.notes.bplan.market.KeyCompetitors
import doa.ai.main.notes.ideation.model.ResultsItem

data class PlanList(
        val count: Int,
        val next: Any,
        val page_size: Int,
        val previous: Any,
        val results: MutableList<PlanListResult>,
        val status: Int,
        val message : String,
        val success : Boolean
)

data class PlanListPinned(
        val count: Int,
        val next: Any,
        val page_size: Int,
        val previous: Any,
        val results: MutableList<PlanPinned>,
        val status: Int,
        val message : String,
        val success : Boolean
)

data class PlanPinned(
        val id : Int,
        val updated_at: String,
        val created_at: String,
        val deleted_at: Any,
        val plan : PlanListResult

        )
data class BodyShare(

        val content_type: String? = null,

        val receivers: List<Int?>? = null,

        val object_id: Int? = null
)

data class PlanListResult(
        val created_at: String,
        val deleted_at: Any,
        val end_date: String,
        val id: Int,
        val reminder: Boolean,
        val reminder_time: String,
        val repeat: String,
        val scale: String,
        val start_date: String,
        val sub_sector: SubSector,
        val title: String,
        val updated_at: String,
        val label: String,
        val status: String,
        val business : Int,
        val market : Int,
        val strategy : Int,
        val finance : Int,
        val summary : Int,
        val partners : MutableList<Partners>
)

data class BodyPinnedPlan(val plan : Int)

data class BodyPinnedPlans(val plans : MutableList<Int>)

data class BodyDelPinnedPlan(val id : Int)

data class BodyDelPinnedPlans(val pinned_plans : MutableList<Int>)

data class BodyDelPlan(val plans : MutableList<Int>)

data class BodyCopyPlan(val plans : MutableList<Int>)

data class BplanMain (
         val businessPlanPinned: MutableList<PlanListResult>,
         val businessPlanPinnedList: MutableList<PlanPinned>,
         val businessPlanOther: MutableList<PlanListResult>,
         val businessPlanOtherWithCollab: MutableList<PlanListResult>,
        val type: Int,
        val typeModel: Int

)

data class BodyEditPlans(
        val plans : MutableList<Int>,
        val status : String
)
data class BodyEditBplan(

        @field:SerializedName("end_date")
        val endDate: String? = null,

        @field:SerializedName("reminder_time")
        val reminderTime: String? = null,

        @field:SerializedName("reminder")
        val reminder: Boolean? = null,

        @field:SerializedName("partners")
        val partners: List<Int?>? = null,

        @field:SerializedName("sub_sector")
        val subSector: Int? = null,

        @field:SerializedName("repeat")
        val repeat: String? = null,

        @field:SerializedName("scale")
        val scale: String? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("start_date")
        val startDate: String? = null,

        @field:SerializedName("status")
        val status: String? = null
)
data class Partners(val data : String)

data class SummaryList(
    val count: Int,
    val next: Any,
    val page_size: Int,
    val previous: Any,
    val results: MutableList<Result>,
    val status: Int
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
    val updated_at: String
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
        val short_summary: String
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

data class Finance(
    val cash_invested: Double,
    val cash_of_sale: Int,
    val created_at: String,
    val currency: String,
    val customer_day_credit: Int,
    val deleted_at: Any,
    val expanses_first_year: Int,
    val expanses_growth_year: Int,
    val id: Int,
    val plan: Int,
    val sales_growth_year: Int,
    val sales_year: Int,
    val start_year_plan: Int,
    val supplier_day_credit: Int,
    val updated_at: String,
    val short_summary: String
)

data class Business(
    val address: String,
    val deleted_at: Any,
    val id: Int,
    val key_personals: List<KeyPersonal>,
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

data class Plan(
    val created_at: String,
    val deleted_at: Any,
    val end_date: String,
    val id: Int,
    val reminder: Boolean,
    val reminder_time: String,
    val repeat: String,
    val scale: String,
    val label: String,
    val start_date: String,
    val sub_sector: SubSector,
    val title: String,
    val status: String,
    val updated_at: String
)

data class SubSector(
    val deleted_at: Any,
    val description: Any,
    val id: Int,
    val parent: Int,
    val title: String
)