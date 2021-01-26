package doa.ai.main.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import doa.ai.R
import doa.ai.database.AppDatabase
import doa.ai.main.notes.NotesActivity
import doa.ai.main.writeIdeation.WriteIdeationActivity
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import io.fabric.sdk.android.Fabric
import com.orhanobut.hawk.Hawk
import doa.ai.base.BaseFragment
import doa.ai.dashboard.DashboardActivity
import doa.ai.main.notes.ideation.model.ResultsItem
import doa.ai.profile.profiles.ProfileResult
import doa.ai.utils.hasNetwork
import doa.ai.utils.pref.SavePrefTokenLogin
import kotlinx.android.synthetic.main.activity_home_fragment.imageIdePen
import kotlinx.android.synthetic.main.activity_home_fragment.imageProfileUser
import kotlinx.android.synthetic.main.activity_home_fragment.imageSettingUser
import kotlinx.android.synthetic.main.activity_home_fragment.listHomeMenu
import kotlinx.android.synthetic.main.activity_home_fragment.progressHome
import kotlinx.android.synthetic.main.activity_home_fragment.textHomeWriteIdeation
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment(), HomeView {
    override fun onSuccessProfile(profile: ProfileResult) {

    }


    private lateinit var presenter: HomePresenter
    private lateinit var adapter: AdapterMenu

    private lateinit var prefs: SharedPreferences
    private var tokenLogin: String? = ""

    private lateinit var menuHome: MutableList<HomeMenu>
    private lateinit var idea: MutableList<HomeIdea>
    private lateinit var pinned: MutableList<ResultsItem>
    private lateinit var sample: MutableList<HomeSample>


    private lateinit var mDB: AppDatabase

    private var menu: Menu? = null
   // var pinned: LiveData<MutableList<PinnedEntry>>? = null

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_home_fragment,container,false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Fabric.with(activity, Crashlytics())
        // setContentView(doa.ai.R.layout.activity_home)

        presenter = HomePresenter()
        presenter.homeContract()
        prefs = context?.getSharedPreferences("MyPref", 0)!!
        tokenLogin = prefs.getString("loginToken", "")

        mDB = AppDatabase.getInstance(context!!)
       // setAppBar()
        setContentView()
        listMenu()
        filterPref()
        addMenuHome()
        if (hasNetwork(context!!)) {

            presenter.getHomeData("token $tokenLogin")
            presenter.getDataIdeationPinned("token $tokenLogin", 1)
//        swipe_home.setOnRefreshListener {
//            presenter.getHomeData("token $tokenLogin") }

        } else {
            showErrorMessage(getString(R.string.no_internet))
        }

        imageIdePen.setOnClickListener {
            startActivity(Intent(activity, WriteIdeationActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun filterPref() {
        try {
            val dataToken = SavePrefTokenLogin().getTokenLogin()
            Log.e("tag","$dataToken")
            if (dataToken == null){
                prefs?.edit()?.remove("loginToken")?.apply()
                prefs?.edit()?.remove("login")?.apply()
                prefs?.edit()?.putBoolean("login",false)?.apply()
               activity?.finish()
                Hawk.deleteAll()
                startActivity(Intent(activity, DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                activity?.finish()
            }


        } catch (e : Exception){
            Log.e("TAG","error $e")
            prefs?.edit()?.remove("loginToken")?.apply()
            prefs?.edit()?.remove("login")?.apply()
            prefs?.edit()?.putBoolean("login",false)?.apply()
            activity?.finish()
            Hawk.deleteAll()
            startActivity(Intent(activity, DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            activity?.finish()
        }
    }


    private fun addMenuHome() {
        sample = ArrayList()
        pinned = ArrayList()



        menuHome = ArrayList()
        menuHome.add(HomeMenu(0, "Dari ide jadi usaha", 0, "", "", "", "", "", "", idea, pinned, sample, 1, false, false, false))
        menuHome.add(HomeMenu(0, "", 0, "", "", "", "", "", "", idea, pinned, sample, 2, false, false, false))

        //     menuHome.add(HomeMenu(0,"Label", 0, "","","","","","", idea, t, sample, 1,false,false))
        menuHome.add(HomeMenu(0, "", 0, "", "", "", "", "", "", idea, pinned, sample, 3, false, false, false))

        adapter = AdapterMenu(activity!!, menuHome)
        listHomeMenu.setHasFixedSize(true)
        listHomeMenu.layoutManager = LinearLayoutManager(activity!!)
        listHomeMenu.adapter = adapter
        listHomeMenu.smoothScrollToPosition(RecyclerView.VERTICAL)


    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun showLoad() {
        progressHome.visibility = View.VISIBLE

    }

    override fun hideLoad() {
        progressHome.visibility = View.GONE

    }

    //add event icon
    override fun onSuccessHome(response: HomeResponse) {
        if (response.result.events.size != 0)


            menuHome.add(HomeMenu(0, "Gerakan Eskalasi Diri", 0, "", "", "", "", "", "", idea, pinned, sample, 1, false, false, false))

        for (element in response.result.events.iterator()) {
            menuHome.add(HomeMenu(element.id,
                    element.description,
                    doa.ai.R.drawable.ic_logo_login,
                    element.image_url,
                    //    "https://s3-ap-southeast-1.amazonaws.com/pixelcluster.doain.ai/THE+OKRs.png",
                    element.description,
                    element.title,
                    element.start_date,
                    element.end_date,
                    element.condition,
                    idea, pinned, sample, 5, element.use_plan, element.use_document, element.im_registered_here
            ))

        }

        adapter = AdapterMenu(activity!!, menuHome)
        adapter.notifyDataSetChanged()
        listHomeMenu.setHasFixedSize(true)
        listHomeMenu.layoutManager = LinearLayoutManager(activity!!)
        listHomeMenu.adapter = adapter
        listHomeMenu.smoothScrollToPosition(RecyclerView.VERTICAL)


    }

    override fun onFailedHome(error: String) {
        //  swipe_home.isRefreshing = false
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu

        inflater?.inflate(doa.ai.R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAppBar() {
//        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
//            var isShow = false
//            var scrollRange = -1
//
//            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.totalScrollRange
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    isShow = true
//                    showOption(doa.ai.R.id.actions_pen)
////                    showOption(R.id.actions_notes)
////                    showOption(R.id.actions_voice)
////                    showOption(R.id.actions_camera)
//                    toolbarHome.visibility = View.VISIBLE
//                } else if (isShow) {
//                    isShow = false
//                    hideOption(doa.ai.R.id.actions_pen)
////                    hideOption(R.id.actions_notes)
////                    hideOption(R.id.actions_voice)
////                    hideOption(R.id.actions_camera)
//                    toolbarHome.visibility = View.GONE
//                }
//            }
//        })
    }

    private fun setContentView() {

        textHomeWriteIdeation.setOnClickListener {
            startActivity(Intent(activity!!, WriteIdeationActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        imageSettingUser.setOnClickListener {
            startActivity(Intent(activity!!, NotesActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("flags", "settings"))
        }

        imageProfileUser.setOnClickListener {
            startActivity(Intent(activity!!, NotesActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("flags", "profile"))
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
        listMenu()
    }

    private fun listMenu() {
        idea = ArrayList()
        idea.add(HomeIdea(1, "Ide", doa.ai.R.drawable.ic_home_ideation))
        idea.add(HomeIdea(2, "Rencana Usaha", doa.ai.R.drawable.ic_home_bplan))
        idea.add(HomeIdea(3, "Perizinan", doa.ai.R.drawable.ic_home_permit))
        idea.add(HomeIdea(4, "Shop", doa.ai.R.drawable.ic_home_themes))
    }

    override fun showDataIdeaPinned(data: MutableList<ResultsItem>?) {
        Log.e("Tag","data pinned ${Gson().toJson(data)}")
        data?.let { pinned.addAll(it) }


    }

    override fun showDataIdeaPinnedAfter(data: MutableList<ResultsItem>?) {

    }



}
