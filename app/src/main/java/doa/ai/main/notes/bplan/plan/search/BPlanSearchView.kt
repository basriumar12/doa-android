package doa.ai.main.notes.bplan.plan.search

import doa.ai.main.notes.bplan.plan.PlanListResult
import doa.ai.main.notes.bplan.setup.PlanResponse
import doa.ai.profile.collab.ResultsItemForRequested

interface BPlanSearchView {

    fun onSuccessConnected(data: MutableList<ResultsItemForRequested>)

    fun onSuccessConnectedAfter(data: MutableList<ResultsItemForRequested>)


    fun onSuccessBPlans(response: MutableList<PlanListResult>)

    fun onSuccessBPlansAfter(response: MutableList<PlanListResult>)

    fun successEditBPLan(planResponse: PlanResponse)

    fun onFailedBPlan(error: String)

    fun onSuccess( message : String)



    fun hideLoad()

    fun showLoad()

    fun failed(error: String)
}