package doa.ai.main.businessPermit.entity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.MenuItem
import doa.ai.R
import kotlinx.android.synthetic.main.activity_business_entity.*

class BusinessEntityActivity : AppCompatActivity(){

    lateinit var listPermit : MutableList<Permit>
    lateinit var adapter : AdapterBusinessPermit
    lateinit var subtitlePermit : MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_entity)
        setSupportActionBar(toolbarBusinessEntity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        subtitlePermit = ArrayList()
        subtitlePermit.add("1. Akta \n Document Require : \n a) Copy KTP Founders "
                + "\n b) Copy NPWP Founders \n Procedure : \n a) Go to notary public"
                + "\n add as much as field of  business\n that corelate on your current plan")
        subtitlePermit.add("2. NPWP \n Document Require : \n a) Copy KTP Founders"
                + "\n b) Copy NPWP Founders \n Procedure : \n a) Go to KPP "
                + "\n add as much as field of  business\n that corelate on your current plan")
        subtitlePermit.add("1. Akta \n Document Require : \n a) Copy KTP Founders "
                + "\n b) Copy NPWP Founders \n Procedure : \n a) Go to notary public"
                + "\n add as much as field of  business\n that corelate on your current plan")

        listPermit = ArrayList()
        listPermit.add(Permit("A. General Permission", subtitlePermit))
        listPermit.add(Permit("B. Accounting (Special Permission)", subtitlePermit))
        listPermit.add(Permit("C. Advertising (Special Permission)", subtitlePermit))

        adapter = AdapterBusinessPermit(this, listPermit)
        listPermission.layoutManager = LinearLayoutManager(this)
        listPermission.adapter = adapter

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