package doa.ai.login.reset

import doa.ai.base.base_model.BaseResponse

interface ResetPasswordView {

    fun onSuccessForgot (baseResponse: BaseResponse)

    fun onFailedAuth (error: String)

    fun showLoad ()

    fun hideLoad ()

    fun showError(message : String)
}