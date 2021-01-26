package doa.ai.database.modelDB

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "archive")
class ArchiveEntry(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                 var image: String? = null,
                 var title: String? = null,
                 var desc: String? = null,
                 var label: String? = null  ) {
    @Ignore
    constructor(): this(0,"","","","")
}