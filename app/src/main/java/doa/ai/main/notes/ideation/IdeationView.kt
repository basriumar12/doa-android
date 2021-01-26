package doa.ai.main.notes.ideation

import doa.ai.main.notes.ideation.model.Result
import doa.ai.main.notes.ideation.model.ResultsItem
import doa.ai.profile.collab.ResultsItemForRequested

interface IdeationView {

    fun showDataIdea (data: MutableList<ResultsItem>? )
    fun showDataIdeaAfter (data: MutableList<ResultsItem>? )
    fun onSuccessConnected(data: MutableList<ResultsItemForRequested>)
    fun showSucces(data : Result)
    fun showSuccesDelete(message: String)
    fun failed()
    fun message(message : String)
    fun showLoad()
    fun hideLoad()
}