package doa.ai.main.notes.bplan.plan.archive

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import doa.ai.R
import doa.ai.main.notes.bplan.plan.PlanListResult
import doa.ai.main.notes.bplan.summary.SummaryActivity
import doa.ai.main.notes.bplan.summary.SummarysActivity
import doa.ai.main.notes.bplan.tab_segment.SegmentBplanActivity
import doa.ai.utils.pref.*
import kotlinx.android.synthetic.main.content_bplan.view.*

class AdapterBPlan(private val businessPlan: MutableList<PlanListResult>, private val context: Context?): RecyclerView.Adapter<AdapterBPlan.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.content_bplan, parent, false))

    override fun getItemCount(): Int = businessPlan.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        context?.let { holder.bindView(businessPlan[holder.layoutPosition], it) }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(businessPlan: PlanListResult, context: Context) {
            with(businessPlan) {
                itemView.textTitleBplan.text = title
                itemView.textCategotyBplan.text = sub_sector.title
                itemView.textContentBplan.text = scale
                itemView.textStatusBplan.text = status

                if (status.equals("drafted")) {
                    itemView.setOnClickListener {

                        //set id = 0
                        SavePrefId().setPlanID(id.toString())
                        SavePrefId().setBusinessId("0")
                        SavePrefId().setMarketId("0")
                        SavePrefId().setStrategyId("0")
                        SavePrefId().setFinnanceId("0")
                        //setBusiness
                        SavePrefSegmentBusiness().setBusinessProvince("")
                        SavePrefSegmentBusiness().setBusinessCity("")
                        SavePrefSegmentBusiness().setBusinessSubDistric("")
                        SavePrefSegmentBusiness().setBusinessRT("")
                        SavePrefSegmentBusiness().setBusinessRW("")
                        SavePrefSegmentBusiness().setBusinessStreet("")
                        SavePrefSegmentBusiness().setBusinessPostalCode("")
                        SavePrefSegmentBusiness().setBusinessNameFounder("")
                        SavePrefSegmentBusiness().setBusinessPositionFounder("")
                        SavePrefSegmentBusiness().setBusinessExpertisFounder("")
                        SavePrefSegmentBusiness().setBusinessBusinessActivities("")
                        SavePrefSegmentBusiness().setBusinessBusinessPartnerts("")
                        SavePrefSegmentBusiness().setBusinessKeySuccess("")
                        SavePrefSegmentBusiness().setBusinessOperatingExpaness("")

                        //setMarket
                        SavePrefSegmentMarket().setMarketCompetitive("")
                        SavePrefSegmentMarket().setMarketTargetCustomer("")
                        SavePrefSegmentMarket().setMarketProduct("")
                        SavePrefSegmentMarket().setMarketGeografic("")
                        SavePrefSegmentMarket().setMarketNameCompetitor("")
                        SavePrefSegmentMarket().setMarketWeaknessCompetitor("")

                        //setStrategy
                        SavePrefSegmentStartegy().setStrategyStep("")
                        SavePrefSegmentStartegy().setStrategyOportunity("")
                        SavePrefSegmentStartegy().setStrategyGoals("")

                        //setFinnance
                        SavePrefSegmentFinnance().setFinnancePlanning("")
                        SavePrefSegmentFinnance().setFinnanceCurrency("")
                        SavePrefSegmentFinnance().setFinnanceInvestMoney("")
                        SavePrefSegmentFinnance().setFinnanceSalesFirstYear("")
                        SavePrefSegmentFinnance().setFinnanceSalesGrowth("")
                        SavePrefSegmentFinnance().setFinnanceCashSales("")
                        SavePrefSegmentFinnance().setFinnanceExpansesFirstYear("")
                        SavePrefSegmentFinnance().setFinnanceExpansesGrowth("")
                        SavePrefSegmentFinnance().setFinnanceCreditCustomer("")
                        SavePrefSegmentFinnance().setFinnanceCreditSupplier("")

                        context.startActivity(Intent(context, SegmentBplanActivity::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .putExtra("planID",id))





                    }
                }
            }
        }
    }

}