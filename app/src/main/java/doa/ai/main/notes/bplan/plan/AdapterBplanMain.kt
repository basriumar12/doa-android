//package doa.ai.main.notes.bplan.plan
//
//import android.content.Context
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.gson.Gson
//import doa.ai.App.Companion.context
//import doa.ai.R
//import doa.ai.main.home.AdapterIdea
//import doa.ai.main.home.AdapterMenu
//import doa.ai.main.home.HomeMenu
//import kotlinx.android.synthetic.main.view_others_plan.view.*
//import kotlinx.android.synthetic.main.view_pinned_plan.view.*
//
//class AdapterBplanMain(private val businessPlan: List<BplanMain>,
//                         private val context: Context?
//
//) : RecyclerView.Adapter<AdapterBplanMain.ViewHolders>() {
//
//    companion object {
//        const val TYPE_1 = 1
//        const val TYPE_2 = 2
//        const val TYPE_3 = 3
//    }
//
//
//    private lateinit var adapterBplanPinned: AdapterBplanPinned
//    private lateinit var adapterBPlan: AdapterBPlan
//
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
//            ViewHolders {
//        val view: View
//
//        when (viewType) {
//            TYPE_1 -> {
//                view = LayoutInflater.from(parent.context).inflate(R.layout.view_pinned_plan, parent, false)
//                return ViewHolderPinned(view)
//
//            }
//
//            TYPE_2 -> {
//                view = LayoutInflater.from(parent.context).inflate(R.layout.view_others_plan, parent, false)
//                return ViewHolderOther(view)
//            }
//
//            else -> {
//                view = LayoutInflater.from(parent.context).inflate(R.layout.content_menu, parent, false)
//                return ViewHolderOtherCollab(view)
//            }
//        }
//    }
//
//    override fun getItemCount(): Int = businessPlan.size
//
//    override fun getItemViewType(position: Int): Int {
//      if (businessPlan != null){
//          val list : BplanMain? = businessPlan.get(position)
//          if (list != null){
//              return list.type
//          }
//
//      }
//        return 0
//
//    }
//
//    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
//        val type: BplanMain? = businessPlan?.get(position)
//        if (type != null){
//            when(type.type){
//                TYPE_1 ->{
//                    val pinned = holder as ViewHolderPinned
//                    businessPlan?.get(holder.layoutPosition)?.let { pinned.bindView(it) }
//
//                }
//                TYPE_2 ->{
//                    val other = holder as ViewHolderOther
//                    businessPlan?.get(holder.layoutPosition)?.let { other.bindView(it,position) }
//                }
//                TYPE_3 ->{
//                    val otherCollab = holder as ViewHolderOtherCollab
//                    businessPlan?.get(holder.layoutPosition)?.let { otherCollab.bindView(it) }
//                }
//            }
//        }
//
//    }
//
//
//    private var mOnItemClickListener: OnItemClickListener? = null
//
//
//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }
//
//    fun setOnItemClickListener(mItemClickListener: AdapterBplanMain.OnItemClickListener) {
//        this.mOnItemClickListener = mItemClickListener
//    }
//
//
//    open class ViewHolders(itemView: View): RecyclerView.ViewHolder(itemView)
//
//
//    inner class ViewHolderOther(itemView: View) : ViewHolders(itemView) {
//        fun bindView(list : BplanMain,position: Int) {
//            with(list) {
//                adapterBPlan = AdapterBPlan(businessPlanOther,context)
//                itemView.listPlanOther.setHasFixedSize(true)
//                itemView.listPlanOther.layoutManager = GridLayoutManager(context,typeModel)
//                itemView.listPlanOther.adapter = adapterBPlan
//               itemView.setOnClickListener {
//
//                    mOnItemClickListener?.onItemClick(position)
//
//                }
//                }
//            }
//        }
//
//
//    inner class ViewHolderPinned(itemView: View) : AdapterBplanMain.ViewHolders(itemView) {
//        fun bindView(list : BplanMain) {
//            with(list) {
//
//                if (businessPlanPinned.size != 0){
//                    itemView.view_pinned_plan.visibility = View.VISIBLE
//                }
//                Log.e("tag","data pinned ${Gson().toJson(businessPlanPinned)}")
//                adapterBplanPinned = AdapterBplanPinned(businessPlanPinned,businessPlanPinnedList,context)
//                itemView.listPinnedPlan.setHasFixedSize(true)
//                itemView.listPinnedPlan.layoutManager = GridLayoutManager(context,typeModel)
//                itemView.listPinnedPlan.adapter = adapterBplanPinned
//
//                }
//            }
//        }
//
//    inner class ViewHolderOtherCollab(itemView: View) : AdapterBplanMain.ViewHolders(itemView) {
//        fun bindView(list : BplanMain) {
//            with(list) {
//
//                }
//            }
//        }
//    }
//
//
//
//
//
//
