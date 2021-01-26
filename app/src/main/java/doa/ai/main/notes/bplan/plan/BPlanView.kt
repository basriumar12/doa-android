package doa.ai.main.notes.bplan.plan

import doa.ai.main.notes.bplan.setup.PlanResponse
import doa.ai.profile.collab.ResultsItemForRequested

interface BPlanView {

    fun onSuccessConnected(data: MutableList<ResultsItemForRequested>)

    fun onSuccessConnectedAfter(data: MutableList<ResultsItemForRequested>)

    fun onSuccessBPlan(response: MutableList<Result>)

    fun onSuccessBPlans(response: MutableList<PlanListResult>)

    fun onSuccessBPlansAfter(response: MutableList<PlanListResult>)

    fun onSuccessBPlanPinned(response: MutableList<PlanPinned>)

    fun onSuccessBPlanPinnedAfter(response: MutableList<PlanPinned>)


    fun successEditBPLan(planResponse: PlanResponse)

    fun onFailedBPlan(error: String)

    fun onSuccess( message : String)



    fun hideLoad()

    fun showLoad()

    fun failed(error: String)
}