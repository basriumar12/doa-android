package doa.ai.main.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import doa.ai.R
import doa.ai.database.AppDatabase
import doa.ai.main.notes.NotesActivity
import doa.ai.main.writeIdeation.WriteIdeationActivity
import kotlinx.android.synthetic.main.activity_home.*
import com.crashlytics.android.Crashlytics
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import io.fabric.sdk.android.Fabric
import com.orhanobut.hawk.Hawk
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import doa.ai.App.Companion.context
import doa.ai.base.BaseActivity
import doa.ai.dashboard.DashboardActivity
import doa.ai.fcm.BodyNotif
import doa.ai.main.event.EventResponse
import doa.ai.main.notes.ideation.model.ResultsItem
import doa.ai.network.Service
import doa.ai.profile.profiles.ProfileResult
import doa.ai.utils.hasNetwork
import doa.ai.utils.pref.SavePrefTokenLogin
import kotlinx.android.synthetic.main.activity_home.drawerLayout
import kotlinx.android.synthetic.main.activity_home.navigatView
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.nav_bar_main.*
import kotlinx.android.synthetic.main.nav_bar_main.view.*
import retrofit2.Call
import retrofit2.Response
import kotlin.collections.ArrayList


class HomeActivity : BaseActivity(), HomeView {


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(doa.ai.R.layout.activity_home)
         setSupportActionBar(toolbarHome)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = HomePresenter()
        presenter.homeContract()
        prefs = applicationContext.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs.getString("loginToken", "")

        presenter.getProfile("token $tokenLogin")

        setSupportToolbar()
        setNavigationItem()

        progressHome.visibility = View.VISIBLE
        progressPinnedHome.visibility = View.VISIBLE

        mDB = AppDatabase.getInstance(this)
          setAppBar()
        setContentView()
        listMenu()
        filterPref()
        addMenuHome()
        if (hasNetwork(this)) {

            presenter.getHomeData("token $tokenLogin")
            presenter.getDataIdeationPinned("token $tokenLogin", 1)
//        swipe_home.setOnRefreshListener {
//            presenter.getHomeData("token $tokenLogin") }

        } else {
            showErrorMessage(getString(R.string.no_internet))
        }

        postNotif()
    }

    private fun postNotif() {
        val fcmId : ArrayList<String> = arrayListOf()

        fcmId.add("eMpq7UtmVgI:APA91bEgY872NKXPjmdt4E4M78RKggR4KGCru49k6hz1RgAy-RkF0_qvh3_NVREV4ylyWX0zuSETMTXxyxDhReRtqHjAxau_LLZ-yZRizhE8JPWv_b4JM_mqsvFV3mBSX3ES-GgVHWGJ")

        val body = BodyNotif(fcmId,"crosscheck-plan")
        val service = Service.Create.service()

        service.postNotif("token $tokenLogin", body)
                .enqueue(object : retrofit2.Callback<EventResponse>{
                    override fun onFailure(call: Call<EventResponse>, t: Throwable) {

                        Log.e("TAG","response notif error ${t.message}")
                    }

                    override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {

                        Log.e("TAG","response notif ${response.body()?.message}")
                    }
                })
    }

    override fun onPause() {
        super.onPause()
        filterPref()
    }


    private fun setSupportToolbar() {
        setSupportActionBar(toolbarHome)
        supportActionBar?.setTitle("")

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_ideation_menu_sandwich)
        }
        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_ideation_menu_sandwich)
    }

    private fun setNavigationItem() {
        navigatView.getHeaderView(0).linearProfile.setOnClickListener {
            title = ""
            drawerLayout.closeDrawers()
            startActivity(Intent(context, NotesActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("flags", "profile"))

        }
        navigatView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.navHome -> {
//
                }
                R.id.navIdeas -> {
                    startActivity(Intent(context, NotesActivity::class.java))
                }
                R.id.navBplan -> {
                    startActivity(Intent(context, NotesActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("flags", "bplan"))
                }
                R.id.navThemes -> {
                    startActivity(Intent(context, NotesActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("flags", ""))
                }
                R.id.navArchive -> {
                    startActivity(Intent(context, NotesActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("flags", ""))
                }
                R.id.navTrash -> {
                    startActivity(Intent(context, NotesActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("flags", ""))
                }
                R.id.navSettings -> {
                    startActivity(Intent(context, NotesActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("flags", "settings"))
                }

            }
            true
        }
    }

    private fun filterPref() {
        try {
            val dataToken = SavePrefTokenLogin().getTokenLogin()
            Log.e("tag", "$dataToken")
            if (dataToken == null) {
                prefs?.edit()?.remove("loginToken")?.apply()
                prefs?.edit()?.remove("login")?.apply()
                prefs?.edit()?.putBoolean("login", false)?.apply()
                finish()
                Hawk.deleteAll()
                startActivity(Intent(this, DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }


        } catch (e: Exception) {
            Log.e("TAG", "error $e")
            prefs?.edit()?.remove("loginToken")?.apply()
            prefs?.edit()?.remove("login")?.apply()
            prefs?.edit()?.putBoolean("login", false)?.apply()
            finish()
            Hawk.deleteAll()
            startActivity(Intent(this, DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
    }


    private fun addMenuHome() {
        sample = ArrayList()
        pinned = ArrayList()

//        pinned = mDB.appsDao().loadAllPinned()
//        pinned?.observe(this, Observer<MutableList<PinnedEntry>> { t ->
//            menuHome = ArrayList()
//            menuHome.add(HomeMenu(0, "Dari ide jadi usaha", 0, "", "", "", "", "", "", idea, t, sample, 1, false, false, false))
//            menuHome.add(HomeMenu(0, "", 0, "", "", "", "", "", "", idea, t, sample, 2, false, false, false))
//
//            //     menuHome.add(HomeMenu(0,"Label", 0, "","","","","","", idea, t, sample, 1,false,false))
//            menuHome.add(HomeMenu(0, "", 0, "", "", "", "", "", "", idea, t, sample, 3, false, false, false))
//
//            adapter = AdapterMenu(this@HomeActivity, menuHome)
//            listHomeMenu.setHasFixedSize(true)
//            listHomeMenu.layoutManager = LinearLayoutManager(this@HomeActivity)
//            listHomeMenu.adapter = adapter
//            listHomeMenu.smoothScrollToPosition(RecyclerView.VERTICAL)
//        })

        menuHome = ArrayList()
        menuHome.add(HomeMenu(0, "Dari ide jadi usaha", 0, "", "", "", "", "", "", idea, pinned, sample, 1, false, false, false))
        menuHome.add(HomeMenu(0, "", 0, "", "", "", "", "", "", idea, pinned, sample, 2, false, false, false))

        //     menuHome.add(HomeMenu(0,"Label", 0, "","","","","","", idea, t, sample, 1,false,false))
        menuHome.add(HomeMenu(0, "", 0, "", "", "", "", "", "", idea, pinned, sample, 3, false, false, false))

        adapter = AdapterMenu(this@HomeActivity, menuHome)
        listHomeMenu.setHasFixedSize(true)
        listHomeMenu.layoutManager = LinearLayoutManager(this@HomeActivity)
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
        progressPinnedHome.visibility = View.VISIBLE

    }

    override fun hideLoad() {
        progressHome.visibility = View.GONE
        progressPinnedHome.visibility = View.GONE

    }

    //add event icon
    override fun onSuccessHome(response: HomeResponse) {
        if (response.result.events.size != 0) {
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
            adapter = AdapterMenu(this@HomeActivity, menuHome)
            adapter.notifyDataSetChanged()
            listHomeMenu.setHasFixedSize(true)
            listHomeMenu.layoutManager = LinearLayoutManager(this@HomeActivity)
            listHomeMenu.adapter = adapter
            listHomeMenu.smoothScrollToPosition(RecyclerView.VERTICAL)

        } else{
            progressHome.visibility = View.GONE
            progressPinnedHome.visibility = View.GONE
        }



    }

    override fun onFailedHome(error: String) {
        //  swipe_home.isRefreshing = false
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        this.menu = menu
//        menuInflater.inflate(doa.ai.R.menu.menu, menu)
//        hideOption(doa.ai.R.id.actions_pen)
////        hideOption(R.id.actions_notes)
////        hideOption(R.id.actions_voice)
////        hideOption(R.id.actions_camera)
//
//        toolbarHome.visibility = View.GONE
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        return when (item?.itemId) {
//            android.R.id.home -> {
//                drawerLayout.openDrawer(GravityCompat.START)
//                true
//            }
//            R.id.actions_setting -> {
//               showInfoMessage("Segera Hadir")
//                true
//            }
//
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(doa.ai.R.menu.menu, menu)
        hideOption(doa.ai.R.id.actions_pen)
//        hideOption(R.id.actions_notes)
//        hideOption(R.id.actions_voice)
//        hideOption(R.id.actions_camera)
        //
        toolbarHome.visibility = View.GONE
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
//            doa.ai.R.id.actions_pen -> {
//                startActivity(Intent(this, WriteIdeationActivity::class.java)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                return true
//            }
//            R.id.actions_notes ->{
//                return true
//            }
//            R.id.actions_voice ->{
//                return true
//            }
//            R.id.actions_camera ->{
//                return true
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAppBar() {
        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    showOption(doa.ai.R.id.actions_pen)
//                    showOption(R.id.actions_notes)
//                    showOption(R.id.actions_voice)
//                    showOption(R.id.actions_camera)
                    toolbarHome.visibility = View.VISIBLE
                } else if (isShow) {
                    isShow = false
                    hideOption(doa.ai.R.id.actions_pen)
//                    hideOption(R.id.actions_notes)
//                    hideOption(R.id.actions_voice)
//                    hideOption(R.id.actions_camera)
                    toolbarHome.visibility = View.GONE
                }
            }
        })
    }

    private fun setContentView() {

        imageIdePen.setOnClickListener {
            startActivity(Intent(this, WriteIdeationActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }


        textHomeWriteIdeation.setOnClickListener {
            startActivity(Intent(this, WriteIdeationActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        imageSettingUser.setOnClickListener {
            startActivity(Intent(this, NotesActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("flags", "settings"))
        }

        imageProfileUser.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)

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
        Log.e("Tag", "data pinned ${Gson().toJson(data)}")
        data?.let { pinned.addAll(it) }


    }

    override fun showDataIdeaPinnedAfter(data: MutableList<ResultsItem>?) {

    }

    override fun onSuccessProfile(profile: ProfileResult) {
        navigatView.getHeaderView(0).textProfileNav.text = profile.fullname
        navigatView.getHeaderView(0).textProfileStatusNav.text = profile.profession
        Picasso.get().load(profile.photo_url).error(R.drawable.ic_menu_profiles).fit().into( navigatView.getHeaderView(0).imageNavigation)

    }


}
