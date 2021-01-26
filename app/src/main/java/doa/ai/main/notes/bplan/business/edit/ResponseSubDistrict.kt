package doa.ai.main.notes.bplan.business.edit

import com.google.gson.annotations.SerializedName
data class ResponseSubDistrict(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
data class Result(

		@field:SerializedName("country")
		val country: Country? = null,

		@field:SerializedName("updated_at")
		val updatedAt: String? = null,

		@field:SerializedName("province")
		val province: Province? = null,

		@field:SerializedName("district")
		val district: District? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("created_at")
		val createdAt: String? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("postal_code")
		val postalCode: String? = null,

		@field:SerializedName("deleted_at")
		val deletedAt: Any? = null,

		@field:SerializedName("name_label")
		val nameLabel: String? = null
)
data class Province(

		@field:SerializedName("country")
		val country: Int? = null,

		@field:SerializedName("updated_at")
		val updatedAt: String? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("created_at")
		val createdAt: String? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("deleted_at")
		val deletedAt: Any? = null
)
data class District(

		@field:SerializedName("updated_at")
		val updatedAt: String? = null,

		@field:SerializedName("province")
		val province: Int? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("created_at")
		val createdAt: String? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("deleted_at")
		val deletedAt: Any? = null
)

data class Country(

		@field:SerializedName("code")
		val code: String? = null,

		@field:SerializedName("updated_at")
		val updatedAt: String? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("created_at")
		val createdAt: String? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("deleted_at")
		val deletedAt: Any? = null,

		@field:SerializedName("currency_code")
		val currencyCode: String? = null,

		@field:SerializedName("phone_code")
		val phoneCode: String? = null
)