package doa.ai.main.notes

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import com.squareup.picasso.Picasso
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.main.home.HomeActivity
import doa.ai.main.home.HomeFragment
import doa.ai.main.notes.archive.ArchiveFragment
import doa.ai.main.notes.bplan.plan.BPlanFragment
import doa.ai.main.notes.ideation.IdeationFragment
import doa.ai.main.notes.settings.setting.SettingsFragment
import doa.ai.main.notes.themes.theme.ThemesFragment
import doa.ai.main.notes.trash.TrashFragment
import doa.ai.profile.profiles.ProfileFragment
import doa.ai.profile.profiles.ProfilePresenter
import doa.ai.profile.profiles.ProfileResult
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.nav_bar_main.view.*

class NotesActivity: BaseActivity(), NoteView {

    private lateinit var presenter: NotePresenter
    private var prefs: SharedPreferences? = null

    private var tokenLogin: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        setSupportToolbar()

        presenter = NotePresenter()
        presenter.notesContract()
        prefs = applicationContext?.getSharedPreferences("MyPref",0)
        tokenLogin = prefs?.getString("loginToken","")

        presenter.getProfile("token $tokenLogin")
        setNavigationItem()
        handlerView()
    }

    private fun handlerView() {
        when (intent.getStringExtra("flags")) {
            "settings" -> {
                title = getString(R.string.settings)
                replaceFragment(SettingsFragment.newInstance(),"")
            }
            "themes" -> {
                title = getString(R.string.themes)
                replaceFragment(ThemesFragment.newInstance(),"")
            }
            "bplan" -> {
                title = getString(R.string.business_plan)
                replaceFragment(BPlanFragment.newInstance(),"")
            }
            "profile" -> {
                title = ""
                replaceFragment(ProfileFragment.newInstance(),"")
            }
             else -> {
            title = "Idea"
            replaceFragment(IdeationFragment.newInstance(),"")

        }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==1 && resultCode== Activity.RESULT_OK){
            replaceFragment(IdeationFragment.newInstance(),"")
        }
    }
    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onSuccessProfile(profile: ProfileResult) {
        navigatView.getHeaderView(0).textProfileNav.text = profile.fullname
        navigatView.getHeaderView(0).textProfileStatusNav.text = profile.profession
        Picasso.get().load(profile.photo_url).error(R.drawable.ic_menu_profiles).fit().into(navigatView.getHeaderView(0).imageNavigation)

    }

    override fun onFailedProfile(error: String) {
        navigatView.getHeaderView(0).textProfileNav.text = ""
        navigatView.getHeaderView(0).textProfileStatusNav.text = ""
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        return when(id){
            android.R.id.home ->{
                drawerLayout.openDrawer(GravityCompat.START)
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setSupportToolbar(){
        setSupportActionBar(toolbarNotes)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_ideation_menu_sandwich)
        }
        val toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_ideation_menu_sandwich)
    }

    private fun setNavigationItem(){
        navigatView.getHeaderView(0).linearProfile.setOnClickListener {
            title = ""
            drawerLayout.closeDrawers()
            replaceFragment(ProfileFragment.newInstance(),"")
        }
        navigatView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId){
                R.id.navHome -> {
//                    title = ""
//                    drawerLayout.closeDrawers()
//                    replaceFragment(HomeFragment.newInstance(),"")

                    startActivity(Intent(this,HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }
                R.id.navIdeas ->
                {
                    title = getString(R.string.ideation)
                    drawerLayout.closeDrawers()
                    replaceFragment(IdeationFragment.newInstance(),"")
                }
                R.id.navBplan -> {
                    title = getString(R.string.business_plan)
                    drawerLayout.closeDrawers()
                    replaceFragment(BPlanFragment.newInstance(),"")
                }
                R.id.navThemes -> {
                    title = getString(R.string.themes)
                    drawerLayout.closeDrawers()
                    replaceFragment(ThemesFragment.newInstance(),"")
                }
                R.id.navArchive -> {
                    title = getString(R.string.archive)
                    drawerLayout.closeDrawers()
                    replaceFragment(ArchiveFragment.newInstance(),"")
                }
                R.id.navTrash -> {
                    title = getString(R.string.trash)
                    drawerLayout.closeDrawers()
                    replaceFragment(TrashFragment.newInstance(),"")
                }
                R.id.navSettings -> {
                    title = getString(R.string.settings)
                    drawerLayout.closeDrawers()
                    replaceFragment(SettingsFragment.newInstance(),"")
                }

            }
            true
        }
    }

    private fun replaceFragment(newFragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameNotes, newFragment, tag).commit()
    }
}