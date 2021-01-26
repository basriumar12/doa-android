package doa.ai.profile.collab.request

import doa.ai.profile.collab.ResultsItem
import doa.ai.profile.collab.ResultsItemForRequested
import doa.ai.profile.profiles.ProfileResult

interface RequestedView {
    fun onSuccess(data: MutableList<ResultsItemForRequested>)

    fun onSuccessAfter(data: MutableList<ResultsItemForRequested>)

    fun onSuccessApproveDelete(data: MutableList<ResultsItemForRequested>)

    fun onFailed(error: String)

    fun onSucces (message : String)

    fun showLoad ()

    fun hideLoad()

    fun showLoadBottom ()

    fun hideLoadBottom()

}