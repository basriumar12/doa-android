package doa.ai.main.notes

import doa.ai.profile.profiles.ProfileResult

interface NoteView {

    fun onSuccessProfile(profile: ProfileResult)

    fun onFailedProfile(error: String)
}