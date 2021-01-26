package doa.ai.main.notes.bplan.business

data class BusinessModel(
        val plan: Int,
        val structure: String,
        val address: String,
        val sub_district: Int,
        val village: String,
        val number: Int,
        val rt: Int,
        val rw: Int,
        val key_resources: MutableList<String>,
        val business_partners: MutableList<String>,
        val business_activities: MutableList<String>,
        val operating_expenses: MutableList<String>,
        val key_personals: MutableList<KeyPersonal>
)

data class KeyPersonal(
    val key: String,
    val value: MutableList<String>
)

data class BusinessSegmentResponse(
    val result: Result,
    val status: Int
)

data class Result(
        val address: String,
        val deleted_at: Any,
        val id: Int,
        val key_personals: List<KeyPersonalResponse>,
        val key_resources: List<String>,
        val business_partners: MutableList<String>,
        val business_activities: MutableList<String>,
        val operating_expenses: MutableList<String>,
        val number: Int,
        val plan: Int,
        val rt: Int,
        val rw: Int,
        val structure: String,
        val sub_district: Int,
        val village: String
)

data class KeyPersonalResponse(
        val content_type: Int,
        val creator: Int,
        val deleted_at: Any,
        val id: Int,
        val key: String,
        val object_id: Int,
        val value: List<String>
)