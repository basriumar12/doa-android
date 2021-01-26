package doa.ai.register

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import doa.ai.R
import kotlinx.android.synthetic.main.content_interest.view.*

class AdapterRegister(val interest: ArrayList<String>): RecyclerView.Adapter<AdapterRegister.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.content_interest, parent, false))


    override fun getItemCount(): Int = interest.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindView(interest[p0.layoutPosition])

        p0.itemView.img_close_interest.setOnClickListener {
               removeAt(p0.adapterPosition)
               notifyDataSetChanged()

        }
    }

    fun removeAt(position: Int) {
        interest.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(sample: String) {
            with(sample) {
                itemView.textContentInterest.text = sample
            }
        }
    }
}