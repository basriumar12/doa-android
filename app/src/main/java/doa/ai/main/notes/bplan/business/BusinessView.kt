package doa.ai.main.notes.bplan.business

import doa.ai.main.notes.bplan.business.edit.ResponseSubDistrict
import doa.ai.model.CountryModel
import doa.ai.model.DistrictModel
import doa.ai.model.ProvinceModel
import doa.ai.model.SubDistrictModel

interface BusinessView {

    fun onSuccessPostalCode(data: SubDistrictModel)

    fun onSuccessSubDistrict(data: DistrictModel)

    fun onSuccessDistrict(data: ProvinceModel)

    fun onSuccessProvince(data: CountryModel)

    fun onSubDistrictDetail (data : ResponseSubDistrict)

    fun onSuccessgetDataBusiness(data: Result)



    fun onSuccessGetID(id: Int)

    fun onFailedGetID(error: String)
}