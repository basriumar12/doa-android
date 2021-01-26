package doa.ai.main.notes.bplan.strategy

interface StrategyView {

    fun onSuccessStrategy(id: Int)

    fun onSuccessDetailStrategy(data: StrategyResponse)

    fun onFailedStrategy(error: String)
}