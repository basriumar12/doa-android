package doa.ai.login.logins

interface LoginView {

    fun onSucces (loginResponse: LoginResponse)

    fun onSuccessLogin(token: String)

    fun onFailedLogin(error: String)

    fun showError(message : String)

    fun showLoad()

    fun hideLoad()

}