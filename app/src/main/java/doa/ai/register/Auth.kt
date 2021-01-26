package doa.ai.register

import com.google.gson.annotations.SerializedName
import doa.ai.base.base_model.BaseResult
import doa.ai.login.logins.UserLogin

data class AuthBody(
        val username: String,
        val email: String,
        val phone: String,
        val password1: String,
        val password2: String,
        val interests: List<String>,
        val fullname: String
)

data class CheckUserBody(
        val username: String,
        val email: String,
        val phone: String
)
data class AuthResponse(
        val detail: String,
        val status: Int,
        @field:SerializedName("result")
        val result: Result? = null,
        @field:SerializedName("message")
        val message: String? = null,
        @field:SerializedName("success")
        val success: Boolean? = null
        )

class Result(
        val user: User,
        val profile: Profile
)
data class User(
    val email: String,
    val group: String,
    val key: String,
    val username: String
)

data class Profile(
    val about: String,
    val email: String,
    val fullname: String,
    val interests: List<InterestUser>,
    val phone: String,
    val photo_url: String,
    val profession: String,
    val user: Int
)

data class InterestUser(
    val created_at: String,
    val deleted_at: Any,
    val description: Any,
    val id: Int,
    val parent_id: Int,
    val title: String,
    val updated_at: String
)

data class Interest (val interest: String)