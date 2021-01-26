package doa.ai.main.home

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import doa.ai.R
import doa.ai.database.modelDB.PinnedEntry
import doa.ai.main.notes.ideation.model.ResultsItem
import kotlinx.android.synthetic.main.content_pinned_idea.view.*

class AdapterPinned(  //private val pinnedIdea:MutableList<PinnedEntry>?
        private val pinnedIdea:MutableList<ResultsItem>?

): RecyclerView.Adapter<AdapterPinned.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.content_pinned_idea,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pinnedIdea!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        pinnedIdea?.get(holder.layoutPosition)?.let { holder.bindView(it) }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //fun bindView(pinned: PinnedEntry){
        fun bindView(pinned: ResultsItem){
            with(pinned){
                itemView.textPinned.text = title
            }
        }
    }
}