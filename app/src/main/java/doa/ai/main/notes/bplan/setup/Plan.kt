package doa.ai.main.notes.bplan.setup

import com.google.gson.annotations.SerializedName

data class SetupPlan(
        val title: String,
        val sub_sector: Int,
        val start_date: String,
        val end_date: String,
        val reminder: Boolean,
        val reminder_time: String,
        val repeat: String
)

data class SubSector(
    val deleted_at: Any,
    val description: Any,
    val id: Int,
    val parent: Int,
    val title: String
)

data class PlanResponse(
        val result: PlanResult,
        val status: Int,
        @field:SerializedName("message")
        val message: String? = null,
        @field:SerializedName("success")
        val success: Boolean? = null
)

data class PlanResult(
        val created_at: String,
        val deleted_at: Any,
        val end_date: String,
        val id: Int,
        val market: Int,
        val business: Int,
        val strategy: Int,
        val finance: Int,
        val summary: Int,
        val reminder: Boolean,
        val reminder_time: String,
        val repeat: String,
        val scale: String,
        val start_date: String,
        val sub_sector: SubSector,
        val title: String,
        val status: String,
        val label: String,
        val updated_at: String

)

data class PlanSectors(
        val count: Int,
        val next: Any,
        val page_size: Int,
        val previous: Any,
        val results: MutableList<ResultPlan>,
        val status: Int
)

data class ResultPlan(
        val created_at: String,
        val deleted_at: Any,
        val description: Any,
        val id: Int,
        val parent: Any,
        val sub_sectors: MutableList<SectorPlan>,
        val title: String,
        val updated_at: String
)

data class SectorPlan(
    val created_at: String,
    val deleted_at: Any,
    val description: Any,
    val id: Int,
    val parent_id: Int,
    val title: String,
    val updated_at: String
)

data class PlanSubSectors(
        val result: ResultSubSectorPlan,
        val status: Int
)

data class ResultSubSectorPlan(
        val created_at: String,
        val deleted_at: Any,
        val description: Any,
        val id: Int,
        val parent: Any,
        val sub_sectors: MutableList<SubSectorPlan>,
        val title: String,
        val updated_at: String
)

data class SubSectorPlan(
    val created_at: String,
    val deleted_at: Any,
    val description: Any,
    val id: Int,
    val parent_id: Int,
    val title: String,
    val updated_at: String
)