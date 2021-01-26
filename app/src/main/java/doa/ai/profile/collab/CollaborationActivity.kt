package doa.ai.profile.collab

import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import doa.ai.R
import doa.ai.base.BaseActivity
import kotlinx.android.synthetic.main.activity_collaboration.*
import kotlinx.android.synthetic.main.activity_summarys.*
import kotlinx.android.synthetic.main.view_error_layout.*

class CollaborationActivity : BaseActivity() {

    var flag =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collaboration)
        setSupportActionBar(toolbarCollab)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewpager_collabolator.adapter = CollabolatorTabAdapter(supportFragmentManager)
        tabs_collabolator.setupWithViewPager(viewpager_collabolator)

        flag =intent.getStringExtra("flags")

        if (flag.toString().equals("suggest")){
            viewpager_collabolator.currentItem = 1
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_collab, menu)
        return super.onCreateOptionsMenu(menu)

    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.actions_search ->{

                startActivity(Intent(this,SearchConnectionActivity::class.java))
                true

            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
