package doa.ai.main.notes.settings.Redeem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import doa.ai.R
import kotlinx.android.synthetic.main.activity_redeem_voucher.*
import kotlinx.android.synthetic.main.dialog_congratulation.*
import kotlinx.android.synthetic.main.dialog_congratulation.view.*

class RedeemVoucherActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redeem_voucher)
        setSupportActionBar(toolbarRedeemVoucher)
        title = getString(R.string.enter_voucher_code)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        setRedeem()
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

    private fun setRedeem(){

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.dialog_congratulation,LinerDialogCongratulation)
        builder.setView(dialogLayout)
        builder.setCancelable(false)
        val congratulationDialog: AlertDialog = builder.create()

        btnRedeemVoucher.setOnClickListener {
            congratulationDialog.show()
        }

        dialogLayout.btnCloseCongratulation.setOnClickListener {
            if(congratulationDialog.isShowing) congratulationDialog.dismiss()
        }

    }


}