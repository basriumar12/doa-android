package doa.ai.profile.collab.find_out

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import doa.ai.R
import doa.ai.base.BaseFragment
import doa.ai.main.home.HomeMenu
import doa.ai.profile.collab.BodySendCollab
import doa.ai.profile.collab.ResultsItem
import doa.ai.profile.profiles.ProfilePresenter
import kotlinx.android.synthetic.main.fragment_collabolator_suggestion.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CollabolatorFindoutFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class CollabolatorSugestionFragment : BaseFragment(), SuggestionView {


    lateinit var adapterSuggestionList: AdapterSuggestionList

    var currentPage = 1
    private var allDataLoaded = false
    private var prefs: SharedPreferences? = null
    private var tokenLogin: String? = ""
    private  var dataSuggestionList: MutableList<ResultsItem> = mutableListOf()
    private lateinit var presenter: SuggestionPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_collabolator_suggestion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapterSuggestionList = AdapterSuggestionList(dataSuggestionList, activity!!,
                { dataId: ResultsItem -> addCollab(dataId) })
        presenter = SuggestionPresenter()
        presenter.suggestionContract()
        prefs = context?.applicationContext?.getSharedPreferences("MyPref",0)
        tokenLogin = prefs?.getString("loginToken","")
        presenter.getlistSuggestion("token $tokenLogin",1,"")

        setUpRecylerview()

    }

    private fun addCollab(dataId: ResultsItem) {
        val receivers: MutableList<Int> = arrayListOf()
        dataId.user?.id?.let { receivers.add(it) }
        val body = BodySendCollab(receivers,"requested")
        presenter.postSendCollab("token $tokenLogin", body)


    }

    private fun setUpRecylerview() {
        rv_suggestion_collab.layoutManager = GridLayoutManager(activity,1)
        rv_suggestion_collab.adapter = adapterSuggestionList
        rv_suggestion_collab.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                    presenter.getlistSuggestionAfter("token $tokenLogin",currentPage,"")

                    if (allDataLoaded == true) {
                        showInfoMessage("Semua data di load")
                    }

                }
            }
        })

    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }

    override fun onSuccessSuggestion(data: MutableList<ResultsItem>) {
        dataSuggestionList.clear()
        dataSuggestionList.addAll(data)
        adapterSuggestionList.notifyDataSetChanged()
        if (data.size == 0) {
            showInfoMessage(getString(R.string.data_kosong))
        }

    }

    override fun onSuccessSuggestionAfter(data: MutableList<ResultsItem>) {
        Log.e("Tag","data suggestion list after ${Gson().toJson(data)}")
        if (data.size != 0) {
            allDataLoaded = false
            dataSuggestionList.addAll(data)
            adapterSuggestionList.notifyDataSetChanged()
        } else {
            allDataLoaded = true
        }
    }

    override fun onFailed(error: String) {

    }

    override fun onSucces(message: String) {
        showInfoMessage(message)
        //adapterSuggestionList.removeAt()
    }

    override fun showLoad() {
        progress_suggestion_collab.visibility = View.VISIBLE

    }

    override fun hideLoad() {
//        progress_suggestion_collab.visibility = View.GONE

    }

    override fun showLoadBottom() {
        progress_suggestion_collab_bottom.visibility = View.VISIBLE

    }

    override fun hideLoadBottom() {
        progress_suggestion_collab_bottom.visibility = View.GONE

    }




}
