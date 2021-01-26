package doa.ai.main.home

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import doa.ai.R
import kotlinx.android.synthetic.main.content_sample_business.view.*

class AdapterSample(val homeSample: List<HomeSample>): RecyclerView.Adapter<AdapterSample.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.content_sample_business, parent, false))


    override fun getItemCount(): Int = homeSample.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindView(homeSample[p0.layoutPosition])
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(sample: HomeSample) {
            with(sample) {
                itemView.textSampleBusiness.text = textSample
                Picasso.get().load(urlSample).fit().into(itemView.imageBusinessSample)
            }
        }
    }
}