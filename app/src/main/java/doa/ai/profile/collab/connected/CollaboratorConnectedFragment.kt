package doa.ai.profile.collab.connected

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import doa.ai.R
import doa.ai.base.BaseFragment
import doa.ai.profile.collab.ResultsItem
import doa.ai.profile.collab.ResultsItemForRequested
import kotlinx.android.synthetic.main.fragment_collaborator_connected.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CollaboratorConnectedFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CollaboratorConnectedFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CollaboratorConnectedFragment : BaseFragment(),ConnectedView {



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null


    lateinit var adapterConnectedList: AdapterConnectedList

    var currentPage = 1
    private var allDataLoaded = false
    private var prefs: SharedPreferences? = null
    private var tokenLogin: String? = ""
    private  var dataList: MutableList<ResultsItemForRequested> = mutableListOf()
    private lateinit var presenter: ConnectedPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collaborator_connected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterConnectedList = AdapterConnectedList(dataList, activity!!,
                { dataId: ResultsItemForRequested -> addCollab(dataId) },
                {  dataId: ResultsItemForRequested -> showConnection(dataId) })
        presenter = ConnectedPresenter()
        presenter.connectedContract()
        prefs = context?.applicationContext?.getSharedPreferences("MyPref",0)
        tokenLogin = prefs?.getString("loginToken","")
        presenter.getlistConnected("token $tokenLogin",1,"","connected")
        setUpRecylerview()

    }
    private fun addCollab(dataId: ResultsItemForRequested) {


    }
    private fun showConnection(dataId: ResultsItemForRequested) {


    }
    private fun setUpRecylerview() {
        rv_connected_collab.layoutManager = GridLayoutManager(activity,1)
        rv_connected_collab.adapter = adapterConnectedList
        rv_connected_collab.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var directiorDown: Boolean = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                directiorDown = dy > 0
            }


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (recyclerView.canScrollVertically(1).not()
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && directiorDown
                ) {
                    currentPage += 1
                    presenter.getlistConnectedAfter("token $tokenLogin",currentPage,"","connected")

                    if (allDataLoaded == true) {
                        showInfoMessage("Semua data di load")
                    }

                }
            }
        })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onSuccessConnected(data: MutableList<ResultsItemForRequested>) {
        dataList.clear()
        dataList.addAll(data)
        adapterConnectedList.notifyDataSetChanged()
        if (data.size == 0) {
           // showInfoMessage(getString(R.string.data_kosong))
            tv_empty_data.visibility = View.VISIBLE
        }
    }

    override fun onSuccessConnectedAfter(data: MutableList<ResultsItemForRequested>) {
        if (data.size != 0) {
            allDataLoaded = false
            dataList.addAll(data)
            adapterConnectedList.notifyDataSetChanged()
        } else {
            allDataLoaded = true
        }
    }

    override fun onFailed(error: String) {

    }

    override fun onSucces(message: String) {

    }

    override fun showLoad() {
        progress_connected_collab.visibility = View.VISIBLE
    }

    override fun hideLoad() {
//        progress_connected_collab.visibility = View.GONE

    }

    override fun showLoadBottom() {
  //      progress_connected_collab_bottom.visibility = View.VISIBLE
    }

    override fun hideLoadBottom() {
        progress_connected_collab_bottom.visibility = View.GONE

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }


}
