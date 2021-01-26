package doa.ai.database.modelDB

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "pinned")
class PinnedEntry(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                 var title: String? = null) {
    @Ignore
    constructor(): this(0,"")
}