package doa.ai.profile.collab.request

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import doa.ai.R
import doa.ai.profile.collab.ResultsItem
import doa.ai.profile.collab.ResultsItemForRequested
import kotlinx.android.synthetic.main.view_connected_list.view.*
import kotlinx.android.synthetic.main.view_requested_list.view.*
import kotlinx.android.synthetic.main.view_requested_list.view.img_suggestion
import kotlinx.android.synthetic.main.view_requested_list.view.tv_name_suggestion
import kotlinx.android.synthetic.main.view_suggestion_list.view.*

class AdapterRequestCollabList (private val item: MutableList<ResultsItemForRequested>, private val contex: Context,
                                val approve: (ResultsItemForRequested) -> Unit,
                                val delete: (ResultsItemForRequested) -> Unit
                                )
    : RecyclerView.Adapter<AdapterRequestCollabList.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_requested_list, parent, false))

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
        val urlPhoto = item[position].sender?.profile?.photoUrl
        var fullname = item[position].sender?.profile?.fullname
        var muttualConnection = item[position].total_mutual_connections.toString()
        if (fullname.equals(null) || fullname.equals("")){
            fullname =  item[position].sender?.user?.username
        }

        if (!urlPhoto.equals(null))
            Picasso.get().load(urlPhoto).error(R.drawable.ic_menu_profiles).fit().into(holder.itemView.img_suggestion)
        holder.itemView.tv_name_suggestion.text = fullname
        if (muttualConnection.equals("0")){
            holder.itemView.tv_profession_requested.text = contex.getString(R.string.no_mutual_connection)
        } else{
            holder.itemView.tv_profession_requested.text = "$muttualConnection mutual connections"
        }

        holder.itemView.img_popup_requested.setOnClickListener {
            showPopupMenu(holder.itemView.img_popup_requested, contex,
                    0, "", approve, delete, item.get(position))
        }
        holder.itemView.tv_delete.setOnClickListener {
            delete(item.get(position))
        }
        holder.itemView.tv_approve.setOnClickListener {
            approve(item.get(position))
        }



    }

    class MyMenuItemClickListener(private val id: Int
                                  , private val title: String,
                                  val approve: (ResultsItemForRequested) -> Unit,
                                  val delete: (ResultsItemForRequested) -> Unit,
                                  val data : ResultsItemForRequested



    ) : PopupMenu.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {

                R.id.action_approve -> {
                    approve(data)




                }
                R.id.action_delete -> {
                    delete(data)

                }


            }
            return false
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(resultsItem: ResultsItem) {
            with(resultsItem) {


            }
        }

    }
}

private fun showPopupMenu(view: View, context: Context?
                          , id: Int
                          , title: String,
                          approve: (ResultsItemForRequested) -> Unit,
                          delete: (ResultsItemForRequested) -> Unit,
                          data : ResultsItemForRequested


) {
    val popupMenu = PopupMenu(context as Context, view)
    val inflater = popupMenu.menuInflater
    inflater.inflate(R.menu.menu_requested, popupMenu.menu)
   popupMenu.setOnMenuItemClickListener(AdapterRequestCollabList.MyMenuItemClickListener(id, title, approve, delete, data))
    popupMenu.show()
}