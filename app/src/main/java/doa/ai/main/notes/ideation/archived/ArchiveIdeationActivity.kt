package doa.ai.main.notes.ideation.archived

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import doa.ai.R
import doa.ai.main.notes.ideation.trashed.IdeationTrashFragment
import kotlinx.android.synthetic.main.activity_trash_ideation.*

class ArchiveIdeationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_ideation)
        setSupportActionBar(toolbarIdeationArchive)
        supportActionBar?.setTitle("Arsip")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportFragmentManager.beginTransaction()
                .replace(R.id.root_layout, IdeationArchiveFragment(), "").commit()
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
