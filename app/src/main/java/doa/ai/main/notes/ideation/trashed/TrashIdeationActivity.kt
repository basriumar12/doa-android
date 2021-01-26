package doa.ai.main.notes.ideation.trashed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.main.notes.bplan.plan.archived.ArchiveBplanFragment
import kotlinx.android.synthetic.main.activity_archive_bplan.*
import kotlinx.android.synthetic.main.activity_trash_ideation.*

class TrashIdeationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash_ideation)
        setSupportActionBar(toolbarIdeationArchive)
        supportActionBar?.setTitle("Sampah")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportFragmentManager.beginTransaction()
                .replace(R.id.root_layout, IdeationTrashFragment(), "").commit()
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
