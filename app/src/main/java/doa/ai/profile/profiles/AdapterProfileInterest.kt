package doa.ai.profile.profiles

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import doa.ai.R
import kotlinx.android.synthetic.main.content_interest.view.*

class AdapterProfileInterest(val interest: List<InterestsItem>): RecyclerView.Adapter<AdapterProfileInterest.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.content_interest_at_profile, parent, false))


    override fun getItemCount(): Int = interest.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindView(interest[p0.layoutPosition])
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(sample: InterestsItem) {
            with(sample) {
                itemView.textContentInterest.text = sample.title
            }
        }
    }
}