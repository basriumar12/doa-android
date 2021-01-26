package doa.ai.model

data class DistrictModel(
    val count: Int,
    val next: Any,
    val page_size: Int,
    val previous: Any,
    val results: List<DistrictResult>,
    val status: Int
)

data class DistrictResult(
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val name: String,
    val province: Int,
    val sub_districts: List<SubDistrict>,
    val updated_at: String
)

data class SubDistrict(
    val created_at: String,
    val deleted_at: Any,
    val district: Int,
    val id: Int,
    val name: String,
    val name_label: String,
    val postal_code: String,
    val updated_at: String
)