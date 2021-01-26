package doa.ai.main.notes.bplan.finance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import doa.ai.R
import doa.ai.holder.*
import doa.ai.model.*

class AdapterTable(context: Context, incomeStatement: MutableList<IncomeStatementTr>, flowTrX: MutableList<CashFlowTrX>): AbstractTableAdapter<ColumnHeaderModel,RowHeaderModel,CellModel>(context) {

    private val myTableViewModel = MyTableViewModel(incomeStatement,flowTrX)

    override fun onCreateColumnHeaderViewHolder(parent: ViewGroup?, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(mContext).inflate(R.layout
                .table_column_header_layout, parent, false)

        return ColumnHeaderViewHolder(layout)
    }

    override fun onBindColumnHeaderViewHolder(holder: AbstractViewHolder?, columnHeaderItemModel: Any?, columnPosition: Int) {
        val columnHeader = columnHeaderItemModel as ColumnHeaderModel

        // Get the holder to update cell item text
        val columnHeaderViewHolder = holder as ColumnHeaderViewHolder

        columnHeaderViewHolder.setColumnHeaderModel(columnHeader)
    }

    override fun onBindRowHeaderViewHolder(holder: AbstractViewHolder?, rowHeaderItemModel: Any?, rowPosition: Int) {
        val (data) = rowHeaderItemModel as RowHeaderModel

        val rowHeaderViewHolder = holder as RowHeaderViewHolder
        rowHeaderViewHolder.rowHeaderTextview.text = data
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup?, viewType: Int): AbstractViewHolder {
        // Get Row Header xml Layout
        val layout = LayoutInflater.from(mContext).inflate(R.layout.table_row_header_layout,
                parent, false)

        // Create a Row Header ViewHolder
        return RowHeaderViewHolder(layout)
    }

    override fun getCellItemViewType(position: Int): Int {
        return myTableViewModel.getCellItemViewType(position);
    }

    override fun onCreateCellViewHolder(parent: ViewGroup?, viewType: Int): AbstractViewHolder {

        val layout: View = LayoutInflater.from(mContext).inflate(R.layout.table_cell_layout,
                parent, false)

        return CellViewHolder(layout)

    }

    override fun onCreateCornerView(): View {
        return LayoutInflater.from(mContext).inflate(R.layout.table_corner_layout, null, false);
    }

    override fun onBindCellViewHolder(holder: AbstractViewHolder?, cellItemModel: Any?, columnPosition: Int, rowPosition: Int) {
        val cell = cellItemModel as CellModel

        if (holder is CellViewHolder) {
            // Get the holder to update cell item text
            holder.setCellModel(cell)
        }
    }

    override fun getColumnHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getRowHeaderItemViewType(position: Int): Int {
        return 0
    }

    fun setStatement() {
        // Generate the lists that are used to TableViewAdapter
        myTableViewModel.generateListStatement()

        // Now we got what we need to show on TableView.
        setAllItems(myTableViewModel.columHeaderModeList, myTableViewModel
                .rowHeaderModelList, myTableViewModel.cellModelList)
    }

    fun setCashFlow(){
        myTableViewModel.generateListCashFlow()

        // Now we got what we need to show on TableView.
        setAllItems(myTableViewModel.columHeaderModeList, myTableViewModel
                .rowHeaderModelList, myTableViewModel.cellModelList)
    }

}