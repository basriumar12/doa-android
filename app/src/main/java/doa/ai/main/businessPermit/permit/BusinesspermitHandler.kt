package doa.ai.main.businessPermit.permit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ArrayAdapter
import doa.ai.R
import doa.ai.main.businessPermit.entity.BusinessEntityActivity
import kotlinx.android.synthetic.main.activity_business_permit.*

class BusinessPermitActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_permit)
        setSupportActionBar(toolbarBusinessPermit)
        setTitle("Business Permit")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        spinnerBusinessPermit()


        btnSearch.setOnClickListener{
            startActivity(Intent(this, BusinessEntityActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
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

    private fun spinnerBusinessPermit(){
        val parent = ArrayList<String>()
        parent.add("Usaha Dagang (UD)")
        parent.add("Commanditaire Vennootschap (CV)")
        parent.add("Firma (Fa)")
        parent.add("Perseroan Terbatas (PT)")
        parent.add("Koperasi")
        parent.add("Badan Usaha Milik Desa")

        val dataAdapter = ArrayAdapter(this,R.layout.textview_spinner,parent)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBusinessPermit.prompt = "Select"
        spinnerBusinessPermit.adapter = dataAdapter
    }
}