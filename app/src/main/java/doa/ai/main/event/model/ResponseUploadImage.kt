package doa.ai.main.event.model

import com.google.gson.annotations.SerializedName

data class ResponseUploadImage(

		@field:SerializedName("result")
	val result: ResultUploadImage? = null,

		@field:SerializedName("success")
	val success: Boolean? = null,

		@field:SerializedName("message")
	val message: String? = null,

		@field:SerializedName("status")
	val status: Int? = null
)