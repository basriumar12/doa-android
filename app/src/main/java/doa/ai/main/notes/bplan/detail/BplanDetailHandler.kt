package doa.ai.main.notes.bplan.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import doa.ai.R
import doa.ai.main.notes.bplan.business.BusinessActivity
import doa.ai.main.notes.bplan.finance.FinanceActivity
import doa.ai.main.notes.bplan.market.MarketActivity
import doa.ai.main.notes.bplan.strategy.StrategyActivity
import doa.ai.main.notes.bplan.summary.SummaryActivity
import kotlinx.android.synthetic.main.activity_bplan_detail.*

class BplanDetailActivity: AppCompatActivity() {

    var planID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bplan_detail)
        setSupportActionBar(toolbarBplanDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        planID = intent.getIntExtra("planID",0)
        setView()
    }

    private fun setView() {
        cardViewBusiness.setOnClickListener {
            startActivity(Intent(this,BusinessActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("planID",planID))
        }

        cardViewMarket.setOnClickListener {
            startActivity(Intent(this, MarketActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("planID",planID))
        }

        cardViewStrategy.setOnClickListener {
            startActivity(Intent(this,StrategyActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("planID",planID))
        }

        cardViewFinance.setOnClickListener {
            startActivity(Intent(this,FinanceActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("planID",planID))
        }

        cardViewSummary.setOnClickListener {
            startActivity(Intent(this,SummaryActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("planID",planID))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        return when(id){
            android.R.id.home -> {
                finish()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }
}