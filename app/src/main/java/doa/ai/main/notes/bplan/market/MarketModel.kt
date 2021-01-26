package doa.ai.main.notes.bplan.market

data class MarketModel(
    val plan: Int,
    val product: String,
    val target_customer: String,
    val geographic_market: String,
    val competitive_advantage: String,
    val key_competitors: MutableList<KeyCompetitors>,
    val customer_managements: MutableList<String>,
    val customer_reach_ways: MutableList<String>
)


class KeyCompetitorsModel (var name : String, var weakNess : String)

data class MarketResponse(
    val result: Result,
    val status: Int,
    val message : String
)
data class MarketResponses(
        val result: Result,
    val status: Int,
    val message : String
)

data class KeyCompetitors(

        val key: String,
        val value: MutableList<String>
)
data class KeyCompetitor(
        val creator : Int,
        val id : Int,
        val code : String,
        val object_id : Int,
        val content_type : Int,
        val key: String,
        val value: MutableList<String>
)

data class Result(
    val competitive_advantage: String,
    val created_at: String,
    val deleted_at: Any,
    val geographic_market: String,
    val id: Int,
    val key_competitors: MutableList<KeyCompetitor>,
    val customer_managements: MutableList<String>,
    val customer_reach_ways: MutableList<String>,
    val plan: Int,
    val product: String,
    val target_customer: String,
    val updated_at: String,
    val short_summary : String

)