package doa.ai.base.base_model

import com.google.gson.annotations.SerializedName

data class BaseResponse(

	@field:SerializedName("result")
	val result: BaseResult? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int
)