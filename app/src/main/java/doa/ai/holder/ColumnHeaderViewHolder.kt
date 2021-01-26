package doa.ai.holder

import androidx.core.content.ContextCompat
import androidx.cardview.widget.CardView
import android.view.View
import android.widget.RelativeLayout
import doa.ai.model.ColumnHeaderModel
import android.widget.TextView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import doa.ai.R

class ColumnHeaderViewHolder(itemView: View) : AbstractSorterViewHolder(itemView) {

    private val columnHeaderContainer: CardView = itemView.findViewById(R.id.column_header_container)
    private val columnHeaderTextview: TextView = itemView.findViewById(R.id.column_header_textView)

    fun setColumnHeaderModel(pColumnHeaderModel: ColumnHeaderModel) {

        // Set text data
        columnHeaderTextview.text = pColumnHeaderModel.data

    }

    override fun setSelected(p_nSelectionState: SelectionState) {
        super.setSelected(p_nSelectionState)

        columnHeaderContainer.setBackgroundColor(ContextCompat.getColor(columnHeaderContainer
                .context, R.color.colorPrimary))
        columnHeaderTextview.setTextColor(ContextCompat.getColor(columnHeaderContainer
                .context, R.color.colorWhite))
    }

}