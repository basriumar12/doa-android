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
import doa.ai.utils.pref.SavePrefSegmentBusiness
import kotlinx.android.synthetic.main.view_owner_person.view.*

class AdapterOwnerAndPerson (private val item: ArrayList<OwnerAndPersonModel>, private val contex: Context)
    : RecyclerView.Adapter<AdapterOwnerAndPerson.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_owner_person, parent, false))

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
           SavePrefSegmentBusiness().setBusinessNameFounder("")
           SavePrefSegmentBusiness().setBusinessPositionFounder("")
           SavePrefSegmentBusiness().setBusinessExpertisFounder("")
       }

        holder.bindView(item[holder.layoutPosition])


        holder.itemView.editName.setText(item.get(position).name)
        holder.itemView.editExpertis.setText(item.get(position).skill)
        holder.itemView.editPosition.setText(item.get(position).position)

        holder.itemView.editName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentBusiness().setBusinessNameFounder(p0.toString())

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    item[position].name = holder.itemView.editName.text.toString()

            }
        } )

        holder.itemView.editPosition.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentBusiness().setBusinessPositionFounder(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    item[position].position = holder.itemView.editPosition.text.toString()

            }
        } )
        holder.itemView.editExpertis.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentBusiness().setBusinessExpertisFounder(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    item[position].skill = holder.itemView.editExpertis.text.toString()

            }
        } )



    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(cart: OwnerAndPersonModel) {

        }

    }
}