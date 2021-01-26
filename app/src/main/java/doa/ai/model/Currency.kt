package doa.ai.model

data class Currency(
        val count: Int,
        val next: String,
        val page_size: Int,
        val previous: Any,
        val results: MutableList<CurrencyResult>,
        val status: Int
)