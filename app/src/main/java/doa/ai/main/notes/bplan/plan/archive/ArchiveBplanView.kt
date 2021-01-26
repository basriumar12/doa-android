package doa.ai.main.notes.bplan.plan.archive

import doa.ai.main.notes.bplan.plan.PlanList
import doa.ai.main.notes.bplan.plan.PlanListResult

interface ArchiveBplanView {


    fun onSuccess(planList: MutableList<PlanListResult>)

    fun onSuccessAfter(planList: MutableList<PlanListResult>)

    fun onFailed(error: String)


    fun errorMessage(message : String)

    fun showLoad ()

    fun hideLoad ()
}