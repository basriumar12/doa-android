package doa.ai.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import android.util.Log
import doa.ai.database.modelDB.ArchiveEntry
import doa.ai.database.modelDB.NotesEntry
import doa.ai.database.modelDB.PinnedEntry
import doa.ai.database.modelDB.TrashEntry
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [(NotesEntry::class), (PinnedEntry::class), (ArchiveEntry::class), (TrashEntry::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appsDao(): AppDao

    companion object {

        private val LOG_TAG = AppDatabase::class.java.simpleName
        private const val DATABASE_NAME = "idealist"
        private var sInstance: AppDatabase? = null

        val MIGRATION_1_2: Migration = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {



            }
        }

        fun getInstance(context: Context): AppDatabase {
            if (sInstance == null) {
                synchronized(AppDatabase::class) {
                    Log.d(LOG_TAG, "Creating new database instance")
                    sInstance = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, AppDatabase.DATABASE_NAME)
                           .addMigrations(MIGRATION_1_2)
                            .build()
                }
            }
            Log.d(LOG_TAG, "Getting the database instance")
            return sInstance!!
        }

    }
}