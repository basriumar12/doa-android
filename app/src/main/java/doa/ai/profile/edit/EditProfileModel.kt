package doa.ai.profile.edit

data class EditProfileBody(
        val fullname: String,
        val phone: String,
        val email: String,
        val profession: String,
        val about: String,
        val photo_url: String,
        val sub_district: Int,
        val address: String,
        val village: String,
        val number: Int,
        val rt: Int,
        val rw: Int


)

data class EditProfileResponse(
    val result: EditProfileResult,
    val status: Int
)

data class EditProfileResult(
    val about: String,
    val created_at: String,
    val deleted_at: Any,
    val email: String,
    val fullname: String,
    val id: Int,
    val phone: String,
    val profession: String,
    val updated_at: String,
    val user: EditProfileUser
)

data class EditProfileUser(
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val username: String
)
