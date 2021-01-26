package doa.ai.profile.edit

import doa.ai.main.notes.bplan.business.edit.ResponseSubDistrict
import doa.ai.model.CountryModel
import doa.ai.model.DistrictModel
import doa.ai.model.ProvinceModel
import doa.ai.profile.profiles.ProfileResult

interface EditProfileView {

    fun onSuccerEditProfile(response: EditProfileResult)
    fun onFailedEditProfile(error: String)
    fun onSuccessProfile(profile: ProfileResult)
    fun onSuccessSubDistrict(data: DistrictModel)
    fun onSuccessDistrict(data: ProvinceModel)

    fun onSuccessProvince(data: CountryModel)

    fun onSubDistrictDetail (data : ResponseSubDistrict)
}