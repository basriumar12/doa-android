package doa.ai.main.notes.trash

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
import doa.ai.database.modelDB.NotesEntry
import doa.ai.database.modelDB.TrashEntry
import doa.ai.extentions.AppExecutors
import kotlinx.android.synthetic.main.view_trash.view.*

class AdapterTrash(private val listNotes: MutableList<TrashEntry>? = null
                     ,private val context: Context?,private val mDB: AppDatabase): RecyclerView.Adapter<AdapterTrash.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, typeView: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_trash, parent, false))

    override fun getItemCount(): Int = listNotes!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listNotes?.get(holder.layoutPosition)?.let { holder.bindView(it, context,mDB) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(listTrash: TrashEntry, context: Context?,mDB: AppDatabase) {
            with(listTrash) {
                itemView.textTitleTrash.text = title
                itemView.textContentTrash.text = desc
                itemView.textTrashLabels.text = label
                if (image == "") {
                    itemView.imageTrash.visibility = View.GONE
                } else {
                    Picasso.get().load(image).fit().into(itemView.imageTrash)
                }
                itemView.imageMoreTrash.setOnClickListener {
                    showPopupMenu(itemView.imageMoreTrash,context
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
            inflater.inflate(R.menu.menu_trash, popupMenu.menu)
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

                R.id.action_add_ideation -> {
                    val notesEntry = NotesEntry(0,image,title,desc,label)
                    val trashEntry = TrashEntry(id,image,title,desc,label)
                    AppExecutors.instance.diskIO().execute {
                        mDB.appsDao().insertNotes(notesEntry)
                        mDB.appsDao().deleteTrash(trashEntry)
                    }
                }

                else -> {
                    val trashEntry = TrashEntry(id,image,title,desc,label)
                    AppExecutors.instance.diskIO().execute {
                        mDB.appsDao().deleteTrash(trashEntry)
                    }
                }
            }
            return false
        }

    }
}