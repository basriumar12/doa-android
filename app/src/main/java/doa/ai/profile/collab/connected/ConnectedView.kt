package doa.ai.profile.collab.connected

import doa.ai.profile.collab.ResultsItem
import doa.ai.profile.collab.ResultsItemForRequested
import doa.ai.profile.profiles.ProfileResult

interface ConnectedView {
    fun onSuccessConnected(data: MutableList<ResultsItemForRequested>)

    fun onSuccessConnectedAfter(data: MutableList<ResultsItemForRequested>)

    fun onFailed(error: String)

    fun onSucces (message : String)

    fun showLoad ()

    fun hideLoad()

    fun showLoadBottom ()

    fun hideLoadBottom()

}