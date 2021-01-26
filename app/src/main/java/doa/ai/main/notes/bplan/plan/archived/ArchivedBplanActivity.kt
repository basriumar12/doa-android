package doa.ai.main.notes.bplan.plan.archived

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import doa.ai.R
import kotlinx.android.synthetic.main.activity_archive_bplan.*

class ArchivedBplanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archived_bplan)
        setSupportActionBar(toolbarBplanArchive)
        supportActionBar?.setTitle("Arsip")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportFragmentManager.beginTransaction()
                .replace(R.id.root_layout, ArchiveBplanFragment(), "").commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }
}
