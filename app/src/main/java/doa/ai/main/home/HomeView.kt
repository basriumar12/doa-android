package doa.ai.main.home

import doa.ai.main.notes.ideation.model.ResultsItem
import doa.ai.profile.profiles.ProfileResult

interface HomeView {

    fun showDataIdeaPinned (data: MutableList<ResultsItem>? )
    fun showDataIdeaPinnedAfter (data: MutableList<ResultsItem>? )
    fun onSuccessHome(response: HomeResponse)
    fun onSuccessProfile(profile: ProfileResult)
    fun onFailedHome(error: String)
    fun showLoad()
    fun hideLoad()


}