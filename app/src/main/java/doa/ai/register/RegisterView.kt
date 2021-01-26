package doa.ai.register

interface RegisterView {

    fun onSuccessAuth(token: String)

    fun onFailedAuth(error: String)

    fun existUser ( code  : Int)

    fun showLoad ()

    fun hideLoad ()


}