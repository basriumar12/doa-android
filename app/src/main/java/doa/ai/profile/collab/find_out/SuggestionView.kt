package doa.ai.profile.collab.find_out

import doa.ai.profile.collab.ResultsItem
import doa.ai.profile.profiles.ProfileResult

interface SuggestionView {
    fun onSuccessSuggestion(data: MutableList<ResultsItem>)

    fun onSuccessSuggestionAfter(data: MutableList<ResultsItem>)

    fun onFailed(error: String)

    fun onSucces (message : String)

    fun showLoad ()

    fun hideLoad()

    fun showLoadBottom ()

    fun hideLoadBottom()

}