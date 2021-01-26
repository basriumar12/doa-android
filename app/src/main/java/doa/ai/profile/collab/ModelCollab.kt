package doa.ai.profile.collab

import com.google.gson.annotations.SerializedName

data class BodySendCollab(

        val receivers: List<Int?>? = null,

        val status: String? = null
)

data class BodyDeleteCollab(

        val id: Int? = null
)

data class ModelCollab(

        @field:SerializedName("next")
        val next: String? = null,

        @field:SerializedName("previous")
        val previous: Any? = null,

        @field:SerializedName("success")
        val success: Boolean? = null,

        @field:SerializedName("count")
        val count: Int? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("results")
        val results: MutableList<ResultsItem>,

        @field:SerializedName("current_page")
        val currentPage: Int? = null,

        @field:SerializedName("page_size")
        val pageSize: Int? = null,

        @field:SerializedName("status")
        val status: Int? = null
)

data class InterestsItem(

        @field:SerializedName("updated_at")
        val updatedAt: String? = null,

        @field:SerializedName("parent_id")
        val parentId: Int? = null,

        @field:SerializedName("created_at")
        val createdAt: String? = null,

        @field:SerializedName("description")
        val description: Any? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("deleted_at")
        val deletedAt: Any? = null
)

data class Profile(

        @field:SerializedName("subscription_level")
        val subscriptionLevel: String? = null,

        @field:SerializedName("profession")
        val profession: String? = null,

        @field:SerializedName("country")
        val country: Any? = null,

        @field:SerializedName("rt")
        val rt: Any? = null,

        @field:SerializedName("address")
        val address: Any? = null,

        @field:SerializedName("sub_district")
        val subDistrict: Any? = null,

        @field:SerializedName("rw")
        val rw: Any? = null,

        @field:SerializedName("about")
        val about: Any? = null,

        @field:SerializedName("created_at")
        val createdAt: String? = null,

        @field:SerializedName("full_address")
        val fullAddress: String? = null,

        @field:SerializedName("deleted_at")
        val deletedAt: Any? = null,

        @field:SerializedName("number")
        val number: Any? = null,

        @field:SerializedName("updated_at")
        val updatedAt: String? = null,

        @field:SerializedName("province")
        val province: Any? = null,

        @field:SerializedName("phone")
        val phone: Any? = null,

        @field:SerializedName("organization")
        val organization: Any? = null,

        @field:SerializedName("district")
        val district: Any? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("fullname")
        val fullname: String? = null,

        @field:SerializedName("photo_url")
        val photoUrl: String? = null,

        @field:SerializedName("interests")
        val interests: List<InterestsItem?>? = null,

        @field:SerializedName("village")
        val village: Any? = null,

        @field:SerializedName("email")
        val email: String? = null
)

data class ResultsItem(

        @field:SerializedName("profile")
        val profile: Profile? = null,

        @field:SerializedName("user")
        val user: User? = null
)

data class User(

        @field:SerializedName("profile")
        val profile: Int? = null,

        @field:SerializedName("last_name")
        val lastName: String? = null,

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("first_name")
        val firstName: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("username")
        val username: String? = null,

        @field:SerializedName("group")
        val group: String? = null
)


data class ModelCollabRequested(

        @field:SerializedName("next")
        val next: String? = null,

        @field:SerializedName("previous")
        val previous: Any? = null,

        @field:SerializedName("success")
        val success: Boolean? = null,

        @field:SerializedName("count")
        val count: Int? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("results")
        val results: MutableList<ResultsItemForRequested>,

        @field:SerializedName("current_page")
        val currentPage: Int? = null,

        @field:SerializedName("page_size")
        val pageSize: Int? = null,

        @field:SerializedName("status")
        val status: Int
)
data class ResultsItemForRequested(

        val total_mutual_connections : Int,
        @field:SerializedName("updated_at")
        val updatedAt: String? = null,

        @field:SerializedName("sender")
        val sender: ResultsItem? = null,

        @field:SerializedName("created_at")
        val createdAt: String? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("deleted_at")
        val deletedAt: Any? = null,

        @field:SerializedName("status")
        val status: String? = null,

        @field:SerializedName("receiver")
        val receiver: ResultsItem? = null


)