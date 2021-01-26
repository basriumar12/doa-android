package doa.ai.main.notes.themes.theme

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import doa.ai.R
import doa.ai.main.notes.themes.detailThemes.DetailsThemesActivity
import kotlinx.android.synthetic.main.content_themes.view.*

class AdapterThemes (private val listThemes: ArrayList<Themes>, val context: Context)
    : RecyclerView.Adapter<AdapterThemes.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.content_themes, parent, false))

    override fun getItemCount(): Int {
        return listThemes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.bindView(listThemes[holder.layoutPosition],context)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(themes: Themes, context: Context){
            with(themes){
                Picasso.get().load(urlImageThemes).fit().into(itemView.imageThemes)
                itemView.textTitleThemes.text = titleThemes
                itemView.textCreditThemes.text = creditThemes
                itemView.textPurchaseThemes.text = purchaseThemes
                itemView.textTagsThemes.text = tagsThemes

                itemView.setOnClickListener {
                    context.startActivity(Intent(context, DetailsThemesActivity::class.java)
                            .putExtra("image",urlImageThemes)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }

                itemView.btnThemesLivePreview.setOnClickListener {
                    Log.d("testKlikLive","ini Klik Live")
                }

                itemView.btnThemesPurchase.setOnClickListener {
                    Log.d("testKlikPurchase","ini Klik Purchase")
                }
            }
        }
    }
}