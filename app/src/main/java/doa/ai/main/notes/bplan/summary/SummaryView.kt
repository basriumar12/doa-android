package doa.ai.main.notes.bplan.summary

interface SummaryView {

    fun onSuccessSummary(response: SummaryResponse)
    fun onFailedSummary(error: String)
}