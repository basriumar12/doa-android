package doa.ai.main.notes.bplan.strategy.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import doa.ai.R
import doa.ai.utils.pref.SavePrefSegmentStartegy
import kotlinx.android.synthetic.main.view_strategy_steps.view.*

class AdapterStrategySteps (private val item: ArrayList<String>, private val contex: Context,
                            val deleteData: (String) -> Unit
                            )
    : RecyclerView.Adapter<AdapterStrategySteps.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_strategy_steps, parent, false))

    override fun getItemCount(): Int {
        var size = item.size
        if (size == null || size== 0){
            size = 0
        } else{
            size
        }
        return size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(item[holder.layoutPosition], deleteData)
        holder.itemView.editStrategySteps.setText(item.get(position).toString())
        holder.itemView.editStrategySteps.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentStartegy().setStrategyStep(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    item[position] = holder.itemView.editStrategySteps.text.toString()

            }
        } )



    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(cart: String,  deleteData: (String) -> Unit) {
            val imgClose = itemView.img_close_strategy
            with(cart) {
                imgClose.setOnClickListener {
                    deleteData(cart)

                }


            }
        }

    }
}