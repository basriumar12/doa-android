package doa.ai.database

import androidx.lifecycle.LiveData
import androidx.room.*
import doa.ai.database.modelDB.ArchiveEntry
import doa.ai.database.modelDB.NotesEntry
import doa.ai.database.modelDB.PinnedEntry
import doa.ai.database.modelDB.TrashEntry

@Dao
interface AppDao {

    //notes
    @Query("SELECT * FROM notes")
    fun loadAllNotes(): LiveData<MutableList<NotesEntry>>

    @Insert
    fun insertNotes(notesEntry: NotesEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateNotes(notesEntry: NotesEntry)

    @Delete
    fun deleteNotes(notesEntry: NotesEntry)

    //pinned
    @Query("SELECT * FROM pinned")
    fun loadAllPinned(): LiveData<MutableList<PinnedEntry>>

    @Insert
    fun insertPinned(pinnedEntry: PinnedEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePinned(pinnedEntry: PinnedEntry)

    @Delete
    fun deletePinned(pinnedEntry: PinnedEntry)

    //archive
    @Query("SELECT * FROM archive")
    fun loadAllArchive(): LiveData<MutableList<ArchiveEntry>>

    @Insert
    fun insertArchive(archiveEntry: ArchiveEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateArchive(archiveEntry: ArchiveEntry)

    @Delete
    fun deleteArchive(archiveEntry: ArchiveEntry)

    //trash
    @Query("SELECT * FROM trash")
    fun loadAllTrash(): LiveData<MutableList<TrashEntry>>

    @Insert
    fun insertTrash(trashEntry: TrashEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTrash(trashEntry: TrashEntry)

    @Delete
    fun deleteTrash(trashEntry: TrashEntry)
}