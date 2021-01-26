package doa.ai.main.notes.bplan.tab_segment

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import doa.ai.R
import doa.ai.main.notes.NotesActivity
import doa.ai.main.notes.bplan.business.BusinessFragment
import doa.ai.main.notes.bplan.business.edit.BusinessFragmentEdit
import doa.ai.main.notes.bplan.finance.FinnanceFragment
import doa.ai.main.notes.bplan.market.MarketFragment
import doa.ai.main.notes.bplan.strategy.StrategyFragment
import doa.ai.utils.pref.SavePrefId
import doa.ai.utils.pref.SavePrefTokenLogin
import kotlinx.android.synthetic.main.activity_bplan_segment.*

class SegmentBplanActivity : doa.ai.base.BaseActivity(),
        BusinessFragment.FragmentCallback, MarketFragment.FragmentCallback,
        StrategyFragment.FragmentCallback {

    override fun saveStrategyId(id: String) {

        SavePrefId().setStrategyId(id.toString())
        openFragment(FinnanceFragment())
        spinner_segment.setSelection(3)
    }

    override fun saveIdMarket(id: String) {
        SavePrefId().setMarketId(id.toString())
        openFragment(StrategyFragment())
        spinner_segment.setSelection(2)
    }

    override fun saveIdBusiness(id: String) {
        SavePrefId().setBusinessId(id.toString())
        openFragment(MarketFragment())
        spinner_segment.setSelection(1)

    }


    private var positionFlagFragment = ""
    private var savedInstanceState: Bundle? = null
    private lateinit var segment: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bplan_segment)
        setSupportActionBar(toolbarBplanSegment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        SavePrefTokenLogin().setTagSegmentSpinner("0")
        segment = ArrayList()
        setupSegment()
        // openFragment(BusinessFragment())

        img_idea_at_segment.setOnClickListener { startActivity(Intent(this, NotesActivity::class.java)) }


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
        spinner_segment.prompt = "Select"
        spinner_segment.adapter = dataAdapter
        spinner_segment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position.equals(0)) {
                    openFragment(BusinessFragment())
                }

                if (position.equals(1)) {
                    openFragment(MarketFragment())
                }
                if (position.equals(2)) {
                    openFragment(StrategyFragment())
                }


                if (position.equals(3)) {
                    openFragment(FinnanceFragment())
                }


                if (position.equals(4)) {
                    showInfoMessage("Data belum lengkap, silahkan lengkapi data")
                }
                if (position.equals(5)) {
                    showInfoMessage("Data belum lengkap, silahkan lengkapi data")
                }
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_segmen, menu)
        return super.onCreateOptionsMenu(menu)

    }


    fun openFragment(fragment: Fragment) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.root_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
        }


    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_setting_segmen -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}