package doa.ai.main.home

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import doa.ai.R
import doa.ai.main.businessPermit.permit.BusinessPermitActivity
import doa.ai.main.notes.NotesActivity
import kotlinx.android.synthetic.main.content_ideas.view.*

class AdapterIdea(val homeIdea: List<HomeIdea>, val context: Context): RecyclerView.Adapter<AdapterIdea.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.content_ideas,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return homeIdea.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(homeIdea[holder.layoutPosition],context)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(homeIdea: HomeIdea, context: Context){
            with(homeIdea){
                itemView.textIdea.text = textIdea
                Picasso.get().load(urlImageHome).into(itemView.imageIdea)
                when(id){
                    1 -> itemView.setOnClickListener { context.startActivity(Intent(context,NotesActivity::class.java)) }
                    2 -> itemView.setOnClickListener { context.startActivity(Intent(context,NotesActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("flags","bplan")) }
                    3 -> itemView.setOnClickListener {
//                        context.startActivity(Intent(context, BusinessPermitActivity::class.java))
                        val toast = Toast.makeText(context,"Segera Hadir", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER,0,0)
                        toast.show()
                    }
                    4 -> itemView.setOnClickListener {
                        val toast = Toast.makeText(context,"Segera Hadir", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER,0,0)
                        toast.show()
//                        context.startActivity(Intent(context,NotesActivity::class.java)
//                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            .putExtra("flags","themes"))
                    }
                }
            }
        }
    }
}