package doa.ai.main.home

data class HomeResponse(
    val result: Result,
    val status: Int
)

data class Result(
    val events: List<Event>,
    val pinned_ideas: List<Any>,
    val sample_business: List<Any>
)

data class Event(
    val category: Category,
    val coach: Int,
    val condition: String,
    val deleted_at: Any,
    val description: String,
    val end_date: String,
    val id: Int,
    val im_registered_here: Boolean,
    val image_url: String,
    val line: String,
    val period: String,
    val published_at: String,
    val qualitative_explanation: String,
    val start_date: String,
    val status: String,
    val sub_sectors: List<SubSector>,
    val table_bookmarks: List<Int>,
    val table_impressions: List<Int>,
    val table_participants: List<Int>,
    val table_periods: List<String>,
    val table_shareds: List<Int>,
    val table_views: List<Int>,
    val title: String,
    val total_bookmarks: Int,
    val total_impressions: Int,
    val total_participants: Int,
    val total_shareds: Int,
    val total_views: Int,
    val use_document: Boolean,
    val use_plan: Boolean
)

data class Category(
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val slug: String,
    val title: String,
    val updated_at: String
)

data class SubSector(
    val created_at: String,
    val deleted_at: Any,
    val description: Any,
    val id: Int,
    val label: String,
    val parent_id: Int,
    val title: String,
    val updated_at: String
)
