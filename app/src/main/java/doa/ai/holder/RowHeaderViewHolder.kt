package doa.ai.holder

import androidx.core.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import doa.ai.R

class RowHeaderViewHolder(p_jItemView: View) : AbstractViewHolder(p_jItemView) {

    val rowHeaderTextview: TextView = p_jItemView.findViewById(R.id.row_header_textview)

    override fun setSelected(p_nSelectionState: SelectionState) {
        super.setSelected(p_nSelectionState)

        rowHeaderTextview.setTextColor(ContextCompat.getColor(rowHeaderTextview.context,
                R.color.colorBlack))
    }

}