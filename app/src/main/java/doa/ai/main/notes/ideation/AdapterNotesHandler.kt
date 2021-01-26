package doa.ai.main.notes.ideation

import android.content.Context
import android.content.Intent

import android.util.Log
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import doa.ai.R
import doa.ai.database.AppDatabase
import doa.ai.database.modelDB.ArchiveEntry
import doa.ai.database.modelDB.NotesEntry
import doa.ai.database.modelDB.PinnedEntry
import doa.ai.database.modelDB.TrashEntry
import doa.ai.extentions.AppExecutors
import doa.ai.main.home.HomePresenter
import doa.ai.main.notes.bplan.setup.SetupBPlanActivity
import doa.ai.main.writeIdeation.WriteIdeationActivity
import kotlinx.android.synthetic.main.view_notes.view.*

class AdapterNotes(private val listNotes: MutableList<NotesEntry>? = null
                   ,private val context: Context?,
                   val addData: (NotesEntry) -> Unit,
                   val deleteData: (NotesEntry) -> Unit
                   ,private val mDB: AppDatabase): RecyclerView.Adapter<AdapterNotes.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, typeView: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_notes, parent, false))

    override fun getItemCount(): Int = listNotes!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listNotes?.get(holder.layoutPosition)?.let { holder.bindView(it,context,mDB,addData,deleteData) }
    }

    fun getNotes(): MutableList<NotesEntry>? {
        return listNotes
    }


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(listNotes: NotesEntry, context: Context?, mDB: AppDatabase,  addData: (NotesEntry) -> Unit,
                     deleteData: (NotesEntry) -> Unit
        ){
            if (listNotes.status.equals("0")){
                itemView.view_divider_line.setBackgroundResource(R.color.colorOrangeyRed)
            } else if (listNotes.status.equals("1")){
                itemView.view_divider_line.setBackgroundResource(R.color.colorLightGreen)
            }

            with(listNotes){
                itemView.textTitleNotes.text = title
                itemView.textContentNotes.text = desc
                itemView.textIdeationLabels.text = label
                if (image == "") {
                    itemView.imageNotes.visibility = View.GONE
                }else{
                    Picasso.get().load(image).fit().into(itemView.imageNotes)
                }
                itemView.imageMoreIdea.setOnClickListener {
                    showPopupMenu(itemView.imageMoreIdea,context
                            ,id
                            ,image.toString()
                            ,title.toString()
                            ,desc.toString()
                            ,label.toString()
                            ,status.toString()
                            ,idFromDb.toString()
                            ,mDB, addData, deleteData)
                }

                itemView.setOnClickListener {
                    context?.startActivity(Intent(context, WriteIdeationActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("title",title)
                            .putExtra("id",id)
                            .putExtra("image", image)
                            .putExtra("label", label)
                            .putExtra("desc",desc))
                }
            }
        }

        private fun showPopupMenu(view: View, context: Context?
                                  ,id: Int
                                  ,image: String
                                  ,title: String
                                  ,desc: String
                                  ,label:String
                                  ,status: String
                                  ,dataIdFromDb: String
                                  ,mDB: AppDatabase
                                    ,  addData: (NotesEntry) -> Unit
                                    ,  deleteData: (NotesEntry) -> Unit

        ) {
            val popupMenu = PopupMenu(context as Context,view)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.menu_idea, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(MyMenuItemClickListener(id, image, title, desc, label,status,dataIdFromDb , mDB,addData, deleteData))
            popupMenu.show()
        }
    }
    private var mOnItemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    class MyMenuItemClickListener(private val id: Int
                                  ,private val image: String
                                  ,private val title: String
                                  ,private val desc: String
                                  ,private val label: String
                                  ,private val status: String
                                  ,private val dataIdFromDb: String
                                  ,private val mDB: AppDatabase
                                  , val addData: (NotesEntry) -> Unit
                                  , val deleteData: (NotesEntry) -> Unit
    ): PopupMenu.OnMenuItemClickListener {


        override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
            when(menuItem?.itemId) {
                R.id.action_add_pinned -> {
                    val pinnedEntry = PinnedEntry(0, title)
                    AppExecutors.instance.diskIO().execute {
                        mDB.appsDao().insertPinned(pinnedEntry)
                    }
                }

                R.id.action_add_archive -> {
                    val archiveEntry = ArchiveEntry(0,image,title,desc,label)
                    val notes = NotesEntry(id,image,title,desc,label)
                    AppExecutors.instance.diskIO().execute {
                        mDB.appsDao().insertArchive(archiveEntry)
                        mDB.appsDao().deleteNotes(notes)
                    }
                }
                R.id.action_add_sync -> {
                    val notes = NotesEntry(id,image,title,desc,label,"1")
                    addData(notes)
                }

                else -> {

                    val trashEntry = TrashEntry(0,image,title,desc,label)
                    val notes = NotesEntry(id,image,title,desc,label,status,dataIdFromDb)
                    Log.e("tag","status $status")
                    if (status.equals("0")) {
                        AppExecutors.instance.diskIO().execute {
                            mDB.appsDao().insertTrash(trashEntry)
                            mDB.appsDao().deleteNotes(notes)
                        }
                    }else {

                        deleteData(notes)
                    }
                }
            }
            return false
        }

    }

}