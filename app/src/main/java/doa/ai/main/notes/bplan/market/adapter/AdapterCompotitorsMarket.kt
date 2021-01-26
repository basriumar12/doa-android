package doa.ai.main.notes.bplan.market.adapter

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
import doa.ai.utils.pref.SavePrefSegmentMarket
import kotlinx.android.synthetic.main.view_compotitors_market.view.*

class AdapterCompotitorsMarket (private val item: ArrayList<KeyCompetitorsModel>, private val contex: Context)
    : RecyclerView.Adapter<AdapterCompotitorsMarket.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_compotitors_market, parent, false))

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
        holder.itemView.editNameCompetitors.setText(item.get(position).name.toString())
        holder.itemView.editNameCompetitors.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentMarket().setMarketNameCompetitor(p0.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    item[position].name = holder.itemView.editNameCompetitors.text.toString()

            }
        } )
        holder.itemView.editNameweakness.setText(item.get(position).weakNess.toString())
        holder.itemView.editNameweakness.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentMarket().setMarketWeaknessCompetitor(p0.toString())


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    item[position].weakNess = holder.itemView.editNameweakness.text.toString()

            }
        } )



    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(cart: KeyCompetitorsModel) {

            with(cart) {



            }
        }

    }
}