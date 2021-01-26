package doa.ai.main.notes.trash

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.*
import doa.ai.R
import doa.ai.database.AppDatabase
import doa.ai.database.modelDB.TrashEntry
import kotlinx.android.synthetic.main.fragment_trash.*

class TrashFragment: Fragment(){

    companion object {
        fun newInstance(): TrashFragment {
            return TrashFragment()
        }
    }

    private lateinit var adapterTrash: AdapterTrash
    private var menu : Menu? = null
    private lateinit var mDB: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mDB = AppDatabase.getInstance(context as Context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trash,container,false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu
        inflater?.inflate(R.menu.menu_list,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentNotesGrid()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.actions_grid -> {
                setContentNotesGrid()
                showOption(R.id.actions_list)
                hideOption(R.id.actions_grid)
                true
            }
            R.id.actions_list -> {
                setContentNotesList()
                showOption(R.id.actions_grid)
                hideOption(R.id.actions_list)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideOption(id: Int) {
        val item = menu?.findItem(id)
        item?.isVisible = false
    }

    private fun showOption(id: Int) {
        val item = menu?.findItem(id)
        item?.isVisible = true
    }

    override fun onResume() {
        super.onResume()
        val trash: LiveData<MutableList<TrashEntry>> = mDB.appsDao().loadAllTrash()
        activity?.let {
            trash.observe(it, Observer<MutableList<TrashEntry>> { t ->
                t?.reverse()
                adapterTrash = AdapterTrash(t, context, mDB)
                listTrash?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                listTrash?.adapter = adapterTrash
            })
        }
    }

    private fun setContentNotesGrid(){
        val trash: LiveData<MutableList<TrashEntry>> = mDB.appsDao().loadAllTrash()
        activity?.let {
            trash.observe(it, Observer<MutableList<TrashEntry>> { t ->
                t?.reverse()
                adapterTrash = AdapterTrash(t, context, mDB)
                listTrash?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                listTrash?.adapter = adapterTrash
            })
        }
    }

    private fun setContentNotesList(){

        val trash: LiveData<MutableList<TrashEntry>> = mDB.appsDao().loadAllTrash()
        activity?.let {
            trash.observe(it, Observer<MutableList<TrashEntry>> { t ->
                t?.reverse()
                adapterTrash = AdapterTrash(t, context, mDB)
                listTrash?.layoutManager = LinearLayoutManager(context)
                listTrash?.adapter = adapterTrash
                listTrash?.smoothScrollToPosition(RecyclerView.VERTICAL)
            })
        }
    }
}