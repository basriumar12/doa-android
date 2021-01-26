package doa.ai.profile.profiles

interface ProfileView {

    fun onSuccessProfile(profile: ProfileResult)

    fun onFailedProfile(error: String)

    fun showLoad()

    fun hideLoad()
}