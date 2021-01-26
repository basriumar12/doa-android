package doa.ai.main.notes.bplan.finance

import android.view.Gravity
import doa.ai.model.*
import java.text.DecimalFormat

class MyTableViewModel(private val incomeStatement: MutableList<IncomeStatementTr>, private val flowTrX: MutableList<CashFlowTrX>) {

    var columHeaderModeList: List<ColumnHeaderModel>? = null

    var rowHeaderModelList: List<RowHeaderModel>? = null

    var cellModelList: List<List<CellModel>>? = null

    fun getCellItemViewType(column: Int): Int {

        return when (column) {
            1 ->

                GENDER_TYPE
            2 ->

                MONEY_TYPE
            else -> 0
        }
    }

    fun getColumnTextAlign(column: Int): Int {
        when (column) {
            // Id
            0 -> return Gravity.CENTER
            // Name
            1 -> return Gravity.START
            // Nickname
            2 -> return Gravity.START
            // Email
            3 -> return Gravity.START
            // BirthDay
            4 -> return Gravity.CENTER
            // Gender (Sex)
            5 -> return Gravity.CENTER
            // Age
            6 -> return Gravity.CENTER
            // Job
            7 -> return Gravity.START
            // Salary
            8 -> return Gravity.CENTER
            // CreatedAt
            9 -> return Gravity.CENTER
            // UpdatedAt
            10 -> return Gravity.CENTER
            // Address
            11 -> return Gravity.START
            // Zip Code
            12 -> return Gravity.END
            // Phone
            13 -> return Gravity.END
            // Fax
            14 -> return Gravity.END

            else -> return Gravity.CENTER
        }

    }

    private fun createColumnHeaderModelList(): List<ColumnHeaderModel> {
        val list = ArrayList<ColumnHeaderModel>()

        list.add(ColumnHeaderModel("Penjualan"))
        list.add(ColumnHeaderModel("Biaya"))
        list.add(ColumnHeaderModel("Laba Kotor"))
        list.add(ColumnHeaderModel("Pengeluaran"))
        list.add(ColumnHeaderModel("Laba Bersih"))
        return list
    }

    private fun createColumnHeaderCashFlow(): List<ColumnHeaderModel> {
        val list = ArrayList<ColumnHeaderModel>()

        list.add(ColumnHeaderModel("Kas dari piutang"))
        list.add(ColumnHeaderModel("Kas ke utang"))
        list.add(ColumnHeaderModel("Pengeluaran terbayar"))
        list.add(ColumnHeaderModel("Kas bersih"))
        list.add(ColumnHeaderModel("Kas periode sebelumnya"))
        list.add(ColumnHeaderModel("Kas periode selanjutnya"))
        return list
    }

    private fun createCellModelList(): List<List<CellModel>> {
        val lists = ArrayList<List<CellModel>>()
        for (i in 1 until incomeStatement.size) {

            val listModel = ArrayList<CellModel>()
            listModel.add(CellModel(i.toString(), incomeStatement[1].td[i - 1]))
            listModel.add(CellModel(i.toString(), incomeStatement[2].td[i - 1]))
            listModel.add(CellModel(i.toString(), incomeStatement[3].td[i - 1]))
            listModel.add(CellModel(i.toString(), incomeStatement[4].td[i - 1]))
            listModel.add(CellModel(i.toString(), incomeStatement[5].td[i - 1]))
            lists.add(listModel)
        }
        return lists
    }

    private fun createCellCashFlow(): List<List<CellModel>> {
        val lists = ArrayList<List<CellModel>>()

        for (i in 1 until 6) {
            val listModel = ArrayList<CellModel>()

            listModel.add(CellModel(i.toString(), flowTrX[1].td[i - 1]))
            listModel.add(CellModel(i.toString(), flowTrX[2].td[i - 1]))
            listModel.add(CellModel(i.toString(), flowTrX[3].td[i - 1]))
            listModel.add(CellModel(i.toString(), flowTrX[4].td[i - 1]))
            listModel.add(CellModel(i.toString(), flowTrX[5].td[i - 1]))
            listModel.add(CellModel(i.toString(), flowTrX[6].td[i - 1]))

            lists.add(listModel)
        }

        return lists
    }

    private fun createRowHeaderList(): List<RowHeaderModel> {
        val list = ArrayList<RowHeaderModel>()
        for (i in incomeStatement[0].td.iterator()) {
            val format = DecimalFormat("#.#")
            list.add(RowHeaderModel(format.format(i)))
        }

        return list
    }


    fun generateListStatement() {
        columHeaderModeList = createColumnHeaderModelList()
        cellModelList = createCellModelList()
        rowHeaderModelList = createRowHeaderList()
    }

    fun generateListCashFlow() {
        columHeaderModeList = createColumnHeaderCashFlow()
        cellModelList = createCellCashFlow()
        rowHeaderModelList = createRowHeaderList()
    }

    companion object {
        // View Types
        const val GENDER_TYPE = 1
        const val MONEY_TYPE = 2
    }

}