package doa.ai.profile.collab.sent

import doa.ai.profile.collab.ResultsItem
import doa.ai.profile.collab.ResultsItemForRequested
import doa.ai.profile.profiles.ProfileResult

interface SentView {
    fun onSuccess(data: MutableList<ResultsItemForRequested>)

    fun onSuccessAfter(data: MutableList<ResultsItemForRequested>)

    fun onFailed(error: String)

    fun onSucces (message : String)

    fun showLoad ()

    fun hideLoad()

    fun showLoadBottom ()

    fun hideLoadBottom()

}