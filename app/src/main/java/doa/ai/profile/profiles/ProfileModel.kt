package doa.ai.profile.profiles

import com.google.gson.annotations.SerializedName
import doa.ai.main.notes.bplan.business.edit.Country
import doa.ai.main.notes.bplan.business.edit.District
import doa.ai.main.notes.bplan.business.edit.Province

data class ProfileResponse(
    val result: ProfileResult,
    val status: Int
)

data class ProfileResult(
        val about: String,
        val created_at: String,
        val deleted_at: Any,
        val email: String,
        val fullname: String,
        val id: Int,
        val phone: String,
        val profession: String,
        val photo_url: String,
        val updated_at: String,
        val user: ProfileUser,
        val subscription_level : String,
        val interests: List<InterestsItem?>? = null,
        @field:SerializedName("country")
        val country: Country? = null,

        @field:SerializedName("province")
        val province: Province? = null,

        @field:SerializedName("district")
        val district: District? = null,

        val sub_district : subDistrict,

        val address: String,
        val village: String,
        val number: Int,
        val rt: Int,
        val rw: Int
        )



open class subDistrict(val id : Int, val name : String)
data class ProfileUser(
    val email: String,
    val first_name: String,
    val group: String,
    val id: Int,
    val last_name: String,
    val username: String
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