package doa.ai.model

data class CountryModel(
    val count: Int,
    val next: Any,
    val page_size: Int,
    val previous: Any,
    val results: List<CountryResult>,
    val status: Int
)

data class CountryResult(
    val code: String,
    val created_at: String,
    val currency_code: String,
    val deleted_at: Any,
    val id: Int,
    val name: String,
    val phone_code: String,
    val provinces: List<Province>,
    val updated_at: String
)

data class Province(
    val country_id: Int,
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val name: String,
    val updated_at: String
)