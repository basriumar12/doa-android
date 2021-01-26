package doa.ai.login.logins

import com.google.gson.annotations.SerializedName
import doa.ai.base.base_model.BaseResult

data class Login(
        val username: String,
        val password: String,
        val fcm_id: String
)

data class LoginResponse(


        @field:SerializedName("status")
        val status: Int,
        @field:SerializedName("result")
        val result: Result? = null,
        @field:SerializedName("message")
        val message: String? = null,
        @field:SerializedName("success")
        val success: Boolean? = null,

        val user: UserLogin,
        val profile: Profile
)

class Result(
        val user: UserLogin,
        val profile: Profile
)

data class UserLogin(
        val email: String,
        val group: String,
        val key: String,
        val token: Token,
        val username: String,
        val phone: String
)

data class Profile(
        val email: String,
        val fullname: String,
        val phone: String,
        val profession: String,
        val organization: String,
        val about: String,
        val photo_url: String,
        val id: String,
        val user: String
)


data class Token(
        val expiration_date: String,
        val key: String,
        val was_expired: Boolean
)