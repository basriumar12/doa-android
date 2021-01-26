package doa.ai.main.notes.settings.purchasePremium

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import doa.ai.R
import doa.ai.payment.PaymentActivity
import doa.ai.payment.PaymentGoogleActivity
import kotlinx.android.synthetic.main.activity_purchase_premium.*

class PurchasePremiumrActivity : AppCompatActivity(){

    private lateinit var adapter : AdapterPurchaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_premium)
        setSupportActionBar(toolbarPurchasePremium)
        title = "Purchase Premium"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        val purchse: List<PurchasePremium> = listOf(
                PurchasePremium("Rp 15.000 / 6 bulan"),
                PurchasePremium("Rp 25.000 / tahun")
        )

        adapter = AdapterPurchaseHandler(this, purchse)
        viewPageDetailPurchase.adapter = adapter
        indicatorPurchase.setupWithViewPager(viewPageDetailPurchase,true)

        btnPurchasePremium.setOnClickListener {
            startActivity(Intent(this, PaymentGoogleActivity::class.java))
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        return when(id){
            android.R.id.home ->{
                finish()
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }
}