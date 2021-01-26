package doa.ai.main.notes.bplan.market

interface MarketView {
    fun onSuccesDetailMarket(data : MarketResponse)
    fun onSuccessMarket(id: Int)
    fun onFailedMarket(error: String)
    fun showfaildRequest(message : String)
}