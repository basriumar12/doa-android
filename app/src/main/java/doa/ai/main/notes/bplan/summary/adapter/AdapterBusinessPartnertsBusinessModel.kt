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
import kotlinx.android.synthetic.main.view_business_partnerts.view.*
import kotlinx.android.synthetic.main.view_business_partnerts_business_model.view.*

class AdapterBusinessPartnertsBusinessModel (private val item: ArrayList<String>, private val contex: Context)
    : RecyclerView.Adapter<AdapterBusinessPartnertsBusinessModel.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_business_partnerts_business_model, parent, false))

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

        holder.itemView.tv_business_partnerts.setText(item.get(position).toString())



    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(cart: String) {
            with(cart) {



            }
        }

    }
}