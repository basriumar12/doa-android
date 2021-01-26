package doa.ai.model

import com.evrencoskun.tableview.sort.ISortableModel

data class CellModel(private val mId: String, val data: Any) : ISortableModel {

    override fun getId(): String {
        return mId
    }

    override fun getContent(): Any {
        return data
    }

}