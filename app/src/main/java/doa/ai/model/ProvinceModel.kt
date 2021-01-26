package doa.ai.model

data class ProvinceModel(
    val count: Int,
    val next: Any,
    val page_size: Int,
    val previous: Any,
    val results: List<ProvinceResult>,
    val status: Int
)

data class ProvinceResult(
        val country: Int,
        val created_at: String,
        val deleted_at: Any,
        val districts: List<District>,
        val id: Int,
        val name: String,
        val updated_at: String
)

data class District(
        val created_at: String,
        val deleted_at: Any,
        val id: Int,
        val name: String,
        val province_id: Int,
        val updated_at: String
)