package doa.ai.profile.collab.connected

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import doa.ai.R
import doa.ai.main.notes.bplan.market.KeyCompetitors
import doa.ai.main.notes.bplan.market.KeyCompetitorsModel
import doa.ai.main.notes.bplan.plan.AdapterBPlan
import doa.ai.main.notes.bplan.plan.PlanListResult
import doa.ai.main.notes.themes.shopping.ShoppingCart
import doa.ai.profile.collab.ResultsItem
import doa.ai.profile.collab.ResultsItemForRequested
import kotlinx.android.synthetic.main.view_connected_list.view.*
import kotlinx.android.synthetic.main.view_connected_list.view.img_suggestion
import kotlinx.android.synthetic.main.view_connected_list.view.tv_name_suggestion



class AdapterConnectedList (private val item: MutableList<ResultsItemForRequested>, private val contex: Context,
                            val addCollab: (ResultsItemForRequested) -> Unit,
                            val showConnection: (ResultsItemForRequested) -> Unit
                            )
    : RecyclerView.Adapter<AdapterConnectedList.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_connected_list, parent, false))

    override fun getItemCount(): Int {
        var size = item.size
        if (size == null || size== 0){
            size = 0
        } else{
            size
        }
        return size
    }

    fun removeAt(position: Int) {
        item.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val urlPhoto = item[position].receiver?.profile?.photoUrl
        var fullname = item[position].receiver?.profile?.fullname
        if (fullname.equals(null) || fullname.equals("")){
            fullname =  item[position].receiver?.user?.username
        }

        if (!urlPhoto.equals(null))
        Picasso.get().load(urlPhoto).error(R.drawable.ic_menu_profiles).fit().into(holder.itemView.img_suggestion)
        holder.itemView.tv_name_suggestion.text = fullname
        holder.itemView.tv_profession_suggestion.text = item[position].receiver?.profile?.profession
        holder.itemView.img_popup_connected.setOnClickListener {
            showPopupMenu(holder.itemView.img_popup_connected, contex,0,"",addCollab,showConnection, item.get(position))
        }


    }

    class MyMenuItemClickListener(private val id: Int
                                  , private val title: String,
                                 val addCollab: (ResultsItemForRequested) -> Unit,
                                  val showConnection: (ResultsItemForRequested) -> Unit,
                                  val data : ResultsItemForRequested



    ) : PopupMenu.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {

                R.id.action_collab -> {
                    addCollab(data)




                }
                R.id.action_show_connection -> {
                    showConnection(data)

                }


            }
            return false
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(resultsItem: ResultsItemForRequested) {
            with(resultsItem) {


            }
        }

    }
}

private fun showPopupMenu(view: View, context: Context?
                          , id: Int
                          , title: String,
                          addCollab: (ResultsItemForRequested) -> Unit,
                          showConnection: (ResultsItemForRequested) -> Unit,
                          data : ResultsItemForRequested


) {
    val popupMenu = PopupMenu(context as Context, view)
    val inflater = popupMenu.menuInflater
    inflater.inflate(R.menu.menu_connected, popupMenu.menu)
    popupMenu.setOnMenuItemClickListener(AdapterConnectedList.MyMenuItemClickListener(id, title, addCollab, showConnection,data))
    popupMenu.show()
}