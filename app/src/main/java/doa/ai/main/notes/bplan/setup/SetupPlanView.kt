package doa.ai.main.notes.bplan.setup

import doa.ai.profile.collab.ResultsItemForRequested

interface SetupPlanView {

    fun onSuccessPlanSectors(sectors: MutableList<ResultPlan>)

    fun onSuccesPlanSubSectors(subSectors: MutableList<SubSectorPlan>)


    fun onSuccessConnected(data: MutableList<ResultsItemForRequested>)

    fun onUpgradeToPremium()

    fun onSuccessSetup(idPlan: Int)

    fun onFailedSetup(error: String)

    fun errorMessage(message : String)

    fun showLoad ()

    fun hideLoad ()
}