package doa.ai.main.notes.themes.shopping

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import doa.ai.R
import kotlinx.android.synthetic.main.content_cart.view.*

class AdapterCart (private val itemcart: ArrayList<ShoppingCart>, private val contex: Context)
    : RecyclerView.Adapter<AdapterCart.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.content_cart, parent, false))

    override fun getItemCount(): Int {
        return itemcart.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(itemcart[holder.layoutPosition])
    }

    class ViewHolder (itemView : View): RecyclerView.ViewHolder(itemView) {
        fun bindView(cart: ShoppingCart){
            with(cart){
                Picasso.get().load(urlImageThemes).fit().into(itemView.imageItemCart)
                itemView.textTitleThemesCart.text = titleThemes
                itemView.textCreditThemesCart.text = creditThemes
                itemView.textPurchaseThemesCart.text = purchaseThemes
            }
        }

    }

}