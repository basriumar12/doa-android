package doa.ai.main.notes.bplan.plan

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import doa.ai.R
import doa.ai.main.notes.bplan.summary.SummarysActivity
import doa.ai.main.notes.bplan.tab_segment.SegmentBplanActivity
import doa.ai.utils.pref.*
import kotlinx.android.synthetic.main.content_bplan.view.*

class AdapterBplanPinned(private val businessPlan: MutableList<PlanListResult>,
                         private val pinnedList: MutableList<PlanPinned>,
                         private val context: Context?
                         , val archive: (PlanListResult) -> Unit,
                         val trash: (PlanListResult) -> Unit,
                         val pinned: (PlanListResult) -> Unit,
                         val showToolbarForPinned: (PlanPinned) -> Unit
) : RecyclerView.Adapter<AdapterBplanPinned.ViewHolder>() {

    var selectedIds: SparseBooleanArray? = null
        private set

    //Get total selected count
    val selectedCount: Int
        get() = selectedIds!!.size()

    init {
        selectedIds = SparseBooleanArray()

    }

    fun toggleSelection(position: Int) {
        selectView(position, !selectedIds!!.get(position))
    }


    //Remove selected selections
    fun removeSelection() {
        selectedIds = SparseBooleanArray()
        notifyDataSetChanged()
    }


    //Put or delete selected position into SparseBooleanArray
    fun selectView(position: Int, value: Boolean) {
        if (value)
            selectedIds!!.put(position, value)
        else
            selectedIds!!.delete(position)

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.content_bplan, parent, false))

    override fun getItemCount(): Int = businessPlan.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        context?.let {
            holder.bindView(businessPlan[holder.layoutPosition], it
                    , archive, trash, pinned, showToolbarForPinned
            )
        }
        holder.itemView
                .setBackgroundColor(if (selectedIds!!.get(position))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        context?.getColor(R.color.lightGrey)!!
                    } else {
                        Color.DKGRAY
                    }
                else
                    Color.WHITE)

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {

                mOnItemClickListener?.onItemClick(position)
                var data: PlanPinned = pinnedList.get(position)
                showToolbarForPinned(data)
                return true
            }
        })
    }


    private var mOnItemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: AdapterBplanPinned.OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    class MyMenuItemClickListener(private val id: Int
                                  , private val title: String,
                                  val archive: (PlanListResult) -> Unit,
                                  val trash: (PlanListResult) -> Unit,
                                  val pinned: (PlanListResult) -> Unit,
                                  private val result: PlanListResult

    ) : PopupMenu.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {

                R.id.action_add_archive -> {

                    archive(result)


                }
                R.id.action_add_trash -> {
                    trash(result)

                }
                R.id.action_add_pinned -> {
                    pinned(result)

                }

            }
            return false
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(businessPlan: PlanListResult, context: Context
                     , archive: (PlanListResult) -> Unit,
                     trash: (PlanListResult) -> Unit,
                     pinned: (PlanListResult) -> Unit,
                     showToolbar: (PlanPinned) -> Unit

        ) {
            with(businessPlan) {
                itemView.textTitleBplan.text = title
                itemView.textCategotyBplan.text = sub_sector.title
                itemView.textContentBplan.text = scale
                itemView.textStatusBplan.text = label
                itemView.imageMoreBplan.setOnClickListener {
                    // showPopupMenu(itemView.imageMoreBplan, context, id, title, archive, trash, pinned, businessPlan)
                }


                itemView.setOnClickListener {
                    if (label.equals("Draft") || label.equals("Draf")) {
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
                        SavePrefSegmentMarket().setMarketCustomerReachWays("")
                        SavePrefSegmentMarket().setMarketCustomerManagement("")

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
                                .putExtra("planID", id))


                    } else {

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
                        SavePrefSegmentMarket().setMarketCustomerReachWays("")
                        SavePrefSegmentMarket().setMarketCustomerManagement("")

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

                        SavePrefId().setPlanID(id.toString())
                        SavePrefId().setBusinessId(business.toString())
                        SavePrefId().setMarketId(market.toString())
                        SavePrefId().setStrategyId(strategy.toString())
                        SavePrefId().setFinnanceId(finance.toString())

                        SavePrefId().setPlanID(id.toString())
                        SavePrefId().setSummaryId(summary.toString())
                        context.startActivity(Intent(context, SummarysActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .putExtra("ID", summary)
                                .putExtra("IDPLAN", id)

                        )
                    }
                }
            }
        }
    }


}

private fun showPopupMenu(view: View, context: Context?
                          , id: Int
                          , title: String,
                          archive: (PlanListResult) -> Unit,
                          trash: (PlanListResult) -> Unit,
                          pinned: (PlanListResult) -> Unit,
                          result: PlanListResult

) {
    val popupMenu = PopupMenu(context as Context, view)
    val inflater = popupMenu.menuInflater
    inflater.inflate(R.menu.menu_bplan, popupMenu.menu)
    popupMenu.setOnMenuItemClickListener(AdapterBPlan.MyMenuItemClickListener(id, title, archive, trash, pinned, result))
    popupMenu.show()
}

