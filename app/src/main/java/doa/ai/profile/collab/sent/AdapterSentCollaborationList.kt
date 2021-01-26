package doa.ai.profile.collab.sent

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import doa.ai.R
import doa.ai.main.notes.bplan.market.KeyCompetitors
import doa.ai.main.notes.bplan.market.KeyCompetitorsModel
import doa.ai.main.notes.themes.shopping.ShoppingCart
import doa.ai.profile.collab.ResultsItem
import doa.ai.profile.collab.ResultsItemForRequested
import doa.ai.utils.pref.SavePrefSegmentMarket
import kotlinx.android.synthetic.main.content_for_fundings.view.*
import kotlinx.android.synthetic.main.view_compotitors_market.view.*
import kotlinx.android.synthetic.main.view_suggestion_list.view.*

class AdapterSentCollaborationList (private val item: MutableList<ResultsItemForRequested>, private val contex: Context)
    : RecyclerView.Adapter<AdapterSentCollaborationList.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_sent_list, parent, false))

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


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(resultsItem: ResultsItem) {
            with(resultsItem) {


            }
        }

    }
}