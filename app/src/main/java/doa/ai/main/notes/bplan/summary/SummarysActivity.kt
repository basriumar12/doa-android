package doa.ai.main.notes.bplan.summary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.main.notes.NotesActivity
import doa.ai.main.notes.bplan.business.BusinessFragment
import doa.ai.main.notes.bplan.business.edit.BusinessFragmentEdit
import doa.ai.main.notes.bplan.finance.FinnanceFragment
import doa.ai.main.notes.bplan.finance.edit.FinnanceFragmentEdit
import doa.ai.main.notes.bplan.market.MarketFragment
import doa.ai.main.notes.bplan.market.edit.MarketFragmentEdit
import doa.ai.main.notes.bplan.strategy.StrategyFragment
import doa.ai.main.notes.bplan.strategy.edit.StrategyFragmentEdit
import kotlinx.android.synthetic.main.activity_bplan_segment.*
import kotlinx.android.synthetic.main.activity_summarys.*
import kotlinx.android.synthetic.main.activity_summarys.view.*

class SummarysActivity : BaseActivity(),BusinessFragmentEdit.FragmentCallback,
        MarketFragmentEdit.FragmentCallback ,
        StrategyFragmentEdit.FragmentCallback

{
    override fun saveStrategyId(id: String) {

    }

    override fun saveIdMarket(id: String) {

    }

    override fun saveIdBusiness(id: String) {

    }

    private var savedInstanceState: Bundle? = null
    private lateinit var segment: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summarys)
        setSupportActionBar(toolbarSummary)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        segment = ArrayList()
        setupSegment()
        // openFragment(BusinessFragment())

        img_idea_at_summary.setOnClickListener { startActivity(Intent(this, NotesActivity::class.java)) }


    }


        private fun setupSegment() {

            segment.add(getString(R.string.bisniss))
            segment.add(getString(R.string.pasar))
            segment.add(getString(R.string.strategi))
            segment.add(getString(R.string.finnance))
            segment.add(getString(R.string.summary))
            segment.add(getString(R.string.bisnis_model))

            val dataAdapter = ArrayAdapter<String>(this, R.layout.textview_spinner, segment)
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_summary.prompt = "Select"
            spinner_summary.adapter = dataAdapter
            spinner_summary.setSelection(4)
            spinner_summary.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    if (position.equals(0)) {
                        openFragment(BusinessFragmentEdit())
                    }

                    if (position.equals(1)) {
                        openFragment(MarketFragmentEdit())
                    }
                    if (position.equals(2)) {

                        openFragment(StrategyFragmentEdit())
                    }


                    if (position.equals(3)) {
                        openFragment(FinnanceFragmentEdit())
                    }


                    if (position.equals(4)) {
                       openFragment(FragmentSummary())
                    }
                    if (position.equals(5)) {
                       openFragment(FragmentBusinessModel())
                    }
                }

            }


        }

    fun openFragment(fragment: Fragment) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.root_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_segmen, menu)
        return super.onCreateOptionsMenu(menu)

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
