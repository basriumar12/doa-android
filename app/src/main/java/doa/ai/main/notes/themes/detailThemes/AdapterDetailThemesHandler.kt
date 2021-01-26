package doa.ai.main.notes.themes.detailThemes

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import doa.ai.R
import kotlinx.android.synthetic.main.content_themes_detail.view.*

class AdapterDetailThemes (private  val list: ArrayList<ThemesDetail>, private val context: Context)
    : RecyclerView.Adapter<AdapterDetailThemes.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.content_themes_detail, parent, false))

    override fun getItemCount(): Int{
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.bindView(list[holder.layoutPosition])
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView (detail: ThemesDetail){
            with(detail){
                Picasso.get().load(urlImageThemes).into(itemView.imageThemesDetail)
            }
        }
    }
}