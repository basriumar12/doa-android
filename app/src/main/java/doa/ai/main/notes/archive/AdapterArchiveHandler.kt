package doa.ai.main.notes.archive

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import doa.ai.R
import doa.ai.database.AppDatabase
import doa.ai.database.modelDB.ArchiveEntry
import doa.ai.database.modelDB.NotesEntry
import doa.ai.database.modelDB.TrashEntry
import doa.ai.extentions.AppExecutors
import kotlinx.android.synthetic.main.view_archive.view.*

class AdapterArchive(private val listNotes: MutableList<ArchiveEntry>? = null
                   ,private val context: Context?,private val mDB: AppDatabase): RecyclerView.Adapter<AdapterArchive.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, typeView: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_archive, parent, false))

    override fun getItemCount(): Int = listNotes!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listNotes?.get(holder.layoutPosition)?.let { holder.bindView(it, context,mDB) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(listNotes: ArchiveEntry, context: Context?,mDB: AppDatabase) {
            with(listNotes) {
                itemView.textTitleArchive.text = title
                itemView.textContentArchive.text = desc
                itemView.textArchiveLabels.text = label
                if (image == "") {
                    itemView.imageArchive.visibility = View.GONE
                } else {
                    Picasso.get().load(image).fit().into(itemView.imageArchive)
                }
                itemView.imageMoreArchive.setOnClickListener {
                    showPopupMenu(itemView.imageMoreArchive,context
                            ,id
                            ,image.toString()
                            ,title.toString()
                            ,desc.toString()
                            ,label.toString()
                            ,mDB)
                }
            }
        }

        private fun showPopupMenu(view: View, context: Context?
                                  ,id: Int
                                  ,image: String
                                  ,title: String
                                  ,desc: String
                                  ,label: String
                                  ,mDB: AppDatabase) {
            val popupMenu = PopupMenu(context as Context, view)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.menu_archive, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(MyMenuItemClickListener(id, image, title, desc, label, mDB))
            popupMenu.show()
        }

    }

    class MyMenuItemClickListener(private val id: Int
                                  ,private val image: String
                                  ,private val title: String
                                  ,private val desc: String
                                  ,private val label: String
                                  ,private val mDB: AppDatabase): PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
            when(menuItem?.itemId) {

                R.id.action_add_unarchive -> {
                    val notesEntry = NotesEntry(0,image,title,desc,label)
                    val archive = ArchiveEntry(id,image,title,desc,label)
                    AppExecutors.instance.diskIO().execute {
                        mDB.appsDao().insertNotes(notesEntry)
                        mDB.appsDao().deleteArchive(archive)
                    }
                }

                else -> {
                    val trashEntry = TrashEntry(0,image,title,desc,label)
                    val archive = ArchiveEntry(id,image,title,desc,label)
                    AppExecutors.instance.diskIO().execute {
                        mDB.appsDao().insertTrash(trashEntry)
                        mDB.appsDao().deleteArchive(archive)
                    }
                }
            }
            return false
        }

    }
}