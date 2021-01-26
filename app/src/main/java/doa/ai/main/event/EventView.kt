package doa.ai.main.event

import doa.ai.main.notes.bplan.plan.SummaryList
import doa.ai.model.CountryModel
import doa.ai.model.ProvinceModel

interface EventView {

    fun onSuccessEvent(response: EventResponse)

    fun onSuccessCheckRegisterEvent(response: EventResponse)

    fun onSuccessPlan(response: SummaryList)

    fun onSuccessDistrict(response: ProvinceModel)

    fun onSuccessGetProfession (responseProfession: ResponseProfession)

    fun onSuccessProvince(data: CountryModel)

    fun onFailedEvent(error: String)

    fun showLoad()

    fun hideLoad()
}