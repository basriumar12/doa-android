package doa.ai.login.forgot

import doa.ai.base.base_model.BaseResponse

interface ForgotView {

    fun onSuccessForgot (baseResponse: BaseResponse)

    fun onFailedAuth (error: String)

    fun showLoad ()

    fun hideLoad ()


}