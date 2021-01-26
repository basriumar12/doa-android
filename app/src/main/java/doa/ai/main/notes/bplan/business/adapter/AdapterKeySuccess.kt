package doa.ai.main.notes.bplan.business.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import doa.ai.R
import doa.ai.main.notes.themes.shopping.ShoppingCart
import doa.ai.utils.pref.SavePrefSegmentBusiness
import kotlinx.android.synthetic.main.content_cart.view.*
import kotlinx.android.synthetic.main.view_key_success.view.*

class AdapterKeySuccess (private val item: ArrayList<String>, private val contex: Context)
    : RecyclerView.Adapter<AdapterKeySuccess.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_key_success, parent, false))

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
       holder.itemView.img_close.setOnClickListener {
           removeAt(position)
       }
        holder.bindView(item[holder.layoutPosition])
        holder.itemView.edt_key_success.setText(item.get(position).toString())
        holder.itemView.edt_key_success.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            SavePrefSegmentBusiness().setBusinessKeySuccess(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    item[position] = holder.itemView.edt_key_success.text.toString()

            }
        } )



    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(cart: String) {
            val edtKeySuccess = itemView.edt_key_success
            with(cart) {



            }
        }

    }
}