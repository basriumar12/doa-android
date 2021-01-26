package doa.ai.main.event

import com.google.gson.annotations.SerializedName

data class EventModel(
    val email: String,
    val fullname: String,
    val phone: String,
    val plan: Int,
    val program: Int,
    val organization : String,
    val profession : String,
    val document_url : String,
    val district : Int
)

data class EventModelWithoutBplan(
    val email: String,
    val fullname: String,
    val phone: String,
    val program: Int,
    val organization : String,
    val profession : String,
    val document_url : String,
    val district : Int
)
class EventModelWithoutBplanDocument(
    val email: String,
    val fullname: String,
    val phone: String,
    val program: Int,
    val organization : String,
    val profession : String,
    val district : Int
)
class EventModelSync(
    val email: String,
    val fullname: String,
    val phone: String
)

data class EventResponse(
        val result: Result,
        val status: Int,
        @field:SerializedName("message")
        val message: String? = null,
        @field:SerializedName("success")
        val success: Boolean? = null
)

data class Result(
    val created_at: String,
    val deleted_at: Any,
    val email: String,
    val fullname: String,
    val id: Int,
    val phone: String,
    val plan: Int,
    val program: Int,
    val registration_number: String,
    val status: String,
    val updated_at: String,
    val condition: String,
    val im_registered_here: String,
    val description: String,
    val title: String,
    val image_url: String,
    val start_date: String,
    val end_date: String,
    val published_at: String,
    val qualitative_explanation: String,
    val use_plan: Boolean,
    val use_document: Boolean

)

data class ResultsItemProfession(

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("description")
        val description: Any? = null
)

data class ResponseProfession(

        @field:SerializedName("next")
        val next: Any? = null,

        @field:SerializedName("previous")
        val previous: Any? = null,

        @field:SerializedName("success")
        val success: Boolean? = null,

        @field:SerializedName("count")
        val count: Int? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("results")
        val results: List<ResultsItemProfession?>? = null,

        @field:SerializedName("current_page")
        val currentPage: Int? = null,

        @field:SerializedName("status")
        val status: Int? = null,

        @field:SerializedName("page_size")
        val pageSize: Int? = null
)