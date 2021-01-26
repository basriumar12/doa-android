package doa.ai.main.notes.ideation

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build

import android.util.Log
import android.util.SparseBooleanArray
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
import doa.ai.main.home.AdapterMenu
import doa.ai.main.home.HomeMenu
import doa.ai.main.home.HomePresenter
import doa.ai.main.notes.bplan.setup.SetupBPlanActivity
import doa.ai.main.notes.ideation.model.Result
import doa.ai.main.notes.ideation.model.ResultsItem
import doa.ai.main.writeIdeation.WriteIdeationActivity
import doa.ai.utils.adapter.PaginationAdapterCallback
import kotlinx.android.synthetic.main.content_menu.view.*
import kotlinx.android.synthetic.main.view_notes.view.*

class AdapterNotesHandlerFromDb(private val listNotes: MutableList<ResultsItem>? = null
                   ,private val context: Context?,
                                val addData: (ResultsItem) -> Unit
                   ): RecyclerView.Adapter<AdapterNotesHandlerFromDb.ViewHolder>(){

    var selectedIds: SparseBooleanArray? = null
        private set

    //Get total selected count
    val selectedCount: Int
        get() = selectedIds!!.size()

    init {
        selectedIds = SparseBooleanArray()

    }
    fun toggleSelection(position: Int) {
        selectView(position, !selectedIds!!.get(position))
    }


    //Remove selected selections
    fun removeSelection() {
        selectedIds = SparseBooleanArray()
        notifyDataSetChanged()
    }


    //Put or delete selected position into SparseBooleanArray
    fun selectView(position: Int, value: Boolean) {
        if (value)
            selectedIds!!.put(position, value)
        else
            selectedIds!!.delete(position)

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, typeView: Int):
            ViewHolder= ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_notes, parent, false))



    override fun getItemCount(): Int =  listNotes?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listNotes?.get(holder.layoutPosition)?.let { holder.bindView(it,context, addData)
            holder.itemView
                    .setBackgroundColor(if (selectedIds!!.get(position))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            context?.getColor(R.color.lightGrey)!!
                        } else {
                            Color.DKGRAY
                        }
                    else
                        Color.WHITE)
        }
    }

    fun getNotes(): MutableList<ResultsItem>? {
        return listNotes
    }



    open class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){


        fun bindView(listNotes: ResultsItem, context: Context?, addData: (ResultsItem) -> Unit
        ){
            if (listNotes.status.equals("0")){
                itemView.view_divider_line.setBackgroundResource(R.color.colorOrangeyRed)
            } else if (listNotes.status.equals("1")){
                itemView.view_divider_line.setBackgroundResource(R.color.colorLightGreen)
            }

            with(listNotes){
                itemView.textTitleNotes.text = title
                itemView.textContentNotes.text = description
                itemView.textIdeationLabels.text = labels?.get(0).toString()
                itemView.imageNotes.visibility = View.GONE
//                if (image == "") {
//                    itemView.imageNotes.visibility = View.GONE
//                }else{
//                    Picasso.get().load(image).fit().into(itemView.imageNotes)
//                }
                itemView.imageMoreIdea.setOnClickListener {
                    id?.let { it1 ->
                        showPopupMenu(itemView.imageMoreIdea,context
                                , it1
                                ,title.toString()
                                ,description.toString()
                                ,labels.toString()
                                ,status.toString()
                                ,addData
                        )
                    }
                }

                itemView.setOnClickListener {
                    context?.startActivity(Intent(context, WriteIdeationActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("title",title)
                            .putExtra("id",id)
                            .putExtra("image", "")
                            .putExtra("label", labels?.get(0).toString())
                            .putExtra("desc",description))
                }
            }
        }

        private fun showPopupMenu(view: View, context: Context?
                                  ,id: Int
                                  ,title: String
                                  ,desc: String
                                  ,label:String
                                  ,status: String,
                                   addData: (ResultsItem) -> Unit

        ) {
            val popupMenu = PopupMenu(context as Context,view)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.menu_idea, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(MyMenuItemClickListener(id, title, desc, label,status,addData))
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
                                  ,private val title: String
                                  ,private val desc: String
                                  ,private val label: String
                                  ,private val status: String,
                                   private val addData: (ResultsItem) -> Unit
    ): PopupMenu.OnMenuItemClickListener {


        override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
            when(menuItem?.itemId) {
                R.id.action_add_pinned -> {
                    val notes = ResultsItem("","",desc,id,title,"", listOf(label),status)
                    addData(notes)
//                    val pinnedEntry = PinnedEntry(0, title)
//                    AppExecutors.instance.diskIO().execute {
//                        mDB.appsDao().insertPinned(pinnedEntry)
//                    }
                }

                R.id.action_add_archive -> {
//                    val archiveEntry = ArchiveEntry(0,image,title,desc,label)
//                    val notes = NotesEntry(id,image,title,desc,label)
//                    AppExecutors.instance.diskIO().execute {
//                        mDB.appsDao().insertArchive(archiveEntry)
//                        mDB.appsDao().deleteNotes(notes)
//                    }
                }
                R.id.action_add_sync -> {
//                    val notes = Result(id,image,title,desc,label,"1")
//                    addData(notes)
                }

                else -> {

//                    val trashEntry = TrashEntry(0,image,title,desc,label)
//                    val notes = NotesEntry(id,image,title,desc,label,status,dataIdFromDb)
//                    Log.e("tag","status $status")
//                    if (status.equals("0")) {
//                        AppExecutors.instance.diskIO().execute {
//                            mDB.appsDao().insertTrash(trashEntry)
//                            mDB.appsDao().deleteNotes(notes)
//                        }
//                    }else {
//
//                        deleteData(notes)
//                    }
                }
            }
            return false
        }

    }

}