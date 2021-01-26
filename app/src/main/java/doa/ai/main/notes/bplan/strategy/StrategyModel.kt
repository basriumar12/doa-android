package doa.ai.main.notes.bplan.strategy

data class StrategyModel(
    val plan: Int,
    val year_objective: String,
    val strategic_opportunities: List<String>,
    val strategic_steps: List<String>
)

data class StrategyResponse(
    val result: Result,
    val status: Int
)

data class Result(
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val plan: Int,
    val strategic_opportunities: List<String>,
    val strategic_steps: List<String>,
    val updated_at: String,
    val year_objective: String,
    val short_summary: String
)