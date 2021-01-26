package doa.ai.login.forgot


class emailBody (
        val email : String?
)

class confirmPassword (
        val uid : String,
        val token : String,
        val new_password1 : String,
        val new_password2 : String

)