package doa.ai.holder

import androidx.core.content.ContextCompat
import android.view.View
import android.widget.FrameLayout
import doa.ai.model.CellModel
import android.widget.TextView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import doa.ai.R

class CellViewHolder(itemView: View) : AbstractViewHolder(itemView) {

    private val cellTextview: TextView = itemView.findViewById(R.id.cell_data)
    private val cellContainer: FrameLayout = itemView.findViewById(R.id.cell_container)

    fun setCellModel(p_jModel: CellModel) {
        // Set text
        cellTextview.text = p_jModel.data.toString()

    }

    override fun setSelected(p_nSelectionState: AbstractViewHolder.SelectionState) {
        super.setSelected(p_nSelectionState)

        if (p_nSelectionState == AbstractViewHolder.SelectionState.SELECTED) {
            cellContainer.setBackgroundColor(ContextCompat.getColor(cellContainer.context, R.color
                    .colorPrimary))
            cellTextview.setTextColor(ContextCompat.getColor(cellTextview.context,R.color.colorWhite))
        } else {
            cellContainer.setBackgroundColor(ContextCompat.getColor(cellContainer.context, R.color
                    .colorWhite))
            cellTextview.setTextColor(ContextCompat.getColor(cellTextview.context,R.color.colorBlack))
        }
    }

}