package doa.ai.main.notes.bplan.summary.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import doa.ai.R
import doa.ai.utils.pref.SavePrefSegmentBusiness
import kotlinx.android.synthetic.main.view_operating_expanses.view.*
import kotlinx.android.synthetic.main.view_operating_expanses_business_model.view.*

class AdapterOperatingExpansesBusinessModel (private val item: ArrayList<String>, private val contex: Context)
    : RecyclerView.Adapter<AdapterOperatingExpansesBusinessModel.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_operating_expanses_business_model, parent, false))

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
       holder.itemView.tv_operating_expanses_business.setText(item.get(position).toString())




    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(cart: String) {
            with(cart) {



            }
        }

    }
}