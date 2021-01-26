package doa.ai.main.businessPermit.entity

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import doa.ai.R

import kotlinx.android.synthetic.main.view_permit.view.*


class AdapterBusinessPermit(val context: Context, val listPermit : MutableList<Permit>)
    : RecyclerView.Adapter<AdapterBusinessPermit.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_permit, parent, false))

    override fun getItemCount(): Int = listPermit.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binView(listPermit[holder.layoutPosition],context)
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun binView(data: Permit, context: Context){
            itemView.textPermit.text = data.titlePermit
            itemView.viewPager.adapter = SliderAdapter(context, data.subtitlePermit)
            itemView.indicator.setupWithViewPager(itemView.viewPager,true)
        }
    }
}





