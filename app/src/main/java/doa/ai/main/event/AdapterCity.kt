package doa.ai.main.event

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import doa.ai.App.Companion.context
import doa.ai.R
import doa.ai.model.District
import doa.ai.model.ProvinceResult

class AdapterCity(private val context: Context,private val dataSource: List<District>) : BaseAdapter() {
    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.spinner_city_view, p2, false)

        val titleTextView = rowView.findViewById(R.id.tv_city) as TextView
        titleTextView.text = dataSource.get(p0).name
        return rowView
    }

    override fun getItem(p0: Int): Any {
        return dataSource[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }
}