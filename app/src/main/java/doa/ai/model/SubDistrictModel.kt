package doa.ai.model

data class SubDistrictModel(
    val count: Int,
    val next: Any,
    val page_size: Int,
    val previous: Any,
    val results: List<SubDistrictResult>,
    val status: Int
)

data class SubDistrictResult(
    val created_at: String,
    val deleted_at: Any,
    val district: Int,
    val id: Int,
    val name: String,
    val name_label: String,
    val postal_code: String,
    val updated_at: String
)