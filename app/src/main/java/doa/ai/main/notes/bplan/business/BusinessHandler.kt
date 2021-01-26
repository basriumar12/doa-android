package doa.ai.main.notes.bplan.business

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle

import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import doa.ai.base.BaseActivity
import doa.ai.extentions.Dialog
import doa.ai.main.notes.bplan.business.adapter.AdapterKeySuccess
import doa.ai.main.notes.bplan.business.adapter.AdapterOwnerAndPerson
import doa.ai.main.notes.bplan.business.adapter.OwnerAndPersonModel
import doa.ai.main.notes.bplan.business.edit.ResponseSubDistrict
import doa.ai.main.notes.bplan.market.MarketActivity
import doa.ai.model.CountryModel
import doa.ai.model.DistrictModel
import doa.ai.model.ProvinceModel
import doa.ai.model.SubDistrictModel
import doa.ai.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_bplan_business.*




class BusinessActivity: BaseActivity(), BusinessView {

    var subDistrict: Int = 0
    var planID: Int? = 0
    var tokenLogin: String? = ""
    private lateinit var presenter: BusinessPresenter
    private lateinit var prefs: SharedPreferences
    var keyResources: MutableList<String> = ArrayList()
    var ownerAndPersonModel: MutableList<OwnerAndPersonModel> = ArrayList()
    private  var namePersonal: MutableList<String> = ArrayList()
    private  var positionPersonal: MutableList<String> = ArrayList()
    private  var expertisPersonal: MutableList<String> =ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(doa.ai.R.layout.activity_bplan_business)
        setSupportActionBar(toolbarBplanBusiness)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        presenter = BusinessPresenter()
        presenter.businessContract()

        prefs = applicationContext.getSharedPreferences("MyPref",0)
        tokenLogin = prefs.getString("loginToken","")
        planID = intent.getIntExtra("planID",0)

        handleView()
        handlePostBusinessSegment()
        presenter.getProvinceList("token $tokenLogin","indo")
    }

    private fun handleView() {
      //add key success
        keyResources = ArrayList()
        keyResources.add("")
        rv_key_succsess.layoutManager = LinearLayoutManager(this)
        val adapterKeySuccess = AdapterKeySuccess(keyResources as ArrayList<String>, this)
        rv_key_succsess.adapter = adapterKeySuccess

        tv_tambah_kunci_suksess.setOnClickListener {
            keyResources.add("")
            rv_key_succsess.adapter = adapterKeySuccess
            adapterKeySuccess.notifyDataSetChanged()

        }
        //delete item
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_key_succsess.adapter as AdapterKeySuccess
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv_key_succsess)



        //add owner data
        ownerAndPersonModel = ArrayList()
        rv_owner_and_person.layoutManager = LinearLayoutManager(this)
        val addOne = OwnerAndPersonModel("","","")
        ownerAndPersonModel.add(addOne)
        val adapterOwnerAndPerson  = AdapterOwnerAndPerson(ownerAndPersonModel as ArrayList<OwnerAndPersonModel>, this)
        rv_owner_and_person.adapter = adapterOwnerAndPerson

        tv_add_owner_and_person.setOnClickListener {
            val data = OwnerAndPersonModel("","","")
            ownerAndPersonModel.add(data)
            rv_owner_and_person.adapter = adapterOwnerAndPerson
            adapterOwnerAndPerson.notifyDataSetChanged()

        }
        //delete item
        val swipeHandlerPerson = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_owner_and_person.adapter as AdapterOwnerAndPerson
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelperPerson = ItemTouchHelper(swipeHandlerPerson)
        itemTouchHelperPerson.attachToRecyclerView(rv_owner_and_person)







        cardviewStructure.setOnClickListener { v ->
            val popMenu = PopupMenu(this,v)
            popMenu.menu.add("Personal")
            popMenu.menu.add("Commanditaire Vennootschap (CV)")
            popMenu.menu.add("Perseroan Terbatas (PT)")
            popMenu.menu.add("Firma (Fa)")
            popMenu.menu.add("Koperasi")
            popMenu.menu.add("Badan Layanan Umum Daerah")
            popMenu.menu.add("Badan Usaha Milik Desa")
            popMenu.setOnMenuItemClickListener { item ->
                textStructure.text = item.title
                false
            }
            popMenu.show()
        }

        imageInfoBusiness.setOnClickListener {
            Dialog.progressDialog(this,getString(doa.ai.R.string.info_business))
        }

        imageInfoLocation.setOnClickListener {
            Dialog.progressDialog(this,getString(doa.ai.R.string.info_location))
        }

        imageInfoResources.setOnClickListener {
            Dialog.progressDialog(this, getString(doa.ai.R.string.info_resources))
        }

        imageInfoPersonal.setOnClickListener {
            Dialog.progressDialog(this, getString(doa.ai.R.string.info_personal))
        }
    }

    private fun handlePostBusinessSegment() {



        businessCreate.setOnClickListener {
            planID?.let { it1 ->

                if ( editRTLocation.text.toString().isEmpty()){
                    showErrorMessage("RT  belum di isi")
                }
               else if(keyResources.isNullOrEmpty()){
                    showErrorMessage("Kunci sukses belum di isi")
                }
                else if(ownerAndPersonModel.isNullOrEmpty()) {
                    showErrorMessage("nama personal / pendiri belum di isi")
                }


               else if ( editRWLocation.text.toString().isEmpty()){
                    showErrorMessage("RW belum di isi")
                }



              else  if (editLocation.text.toString().isEmpty()){
                    showErrorMessage("jalan belum di isi")
                }

               else if (textSubDistrict.text.toString().isEmpty()){
                    showErrorMessage("Kelurahan belum di pilih")
                }
                else if (keyResources.get(0).isNullOrEmpty()){
                    showErrorMessage("data belum lengkap")
                }
               else if (ownerAndPersonModel.get(0).name.equals("") || ownerAndPersonModel.get(0).name.isNullOrEmpty()){
                    showErrorMessage("data belum lengkap")
                }
                else if (ownerAndPersonModel.get(0).position.equals("") || ownerAndPersonModel.get(0).position.isNullOrEmpty()){
                    showErrorMessage("data belum lengkap")
                }
                else if (ownerAndPersonModel.get(0).skill.equals("") || ownerAndPersonModel.get(0).skill.isNullOrEmpty()){
                    showErrorMessage("data belum lengkap")
                }

                else {
                    for (i in 0 until ownerAndPersonModel.size) {
                        val dataName = ownerAndPersonModel.get(i).name
                        val dataPosition = ownerAndPersonModel.get(i).position
                        val dataSkils = ownerAndPersonModel.get(i).skill
                        namePersonal.add(dataName)
                        positionPersonal.add(dataPosition)
                        expertisPersonal.add(dataSkils)
                    }

                    val datas = textSubDistrict.text.toString()
                    val subDistrictsRemovePostalCode = datas.substring(0, datas.length - 7)

//                    presenter.postBusinessSegment("token $tokenLogin",
//                            it1,
//                            textStructure.text.toString(),
//                            editLocation.text.toString(),
//                            subDistrict,
//                            subDistrictsRemovePostalCode,
//                            keyResources,
//                            0,
//                            editRTLocation.text.toString().toInt(),
//                            editRWLocation.text.toString().toInt(),
//                            namePersonal,
//                            positionPersonal,
//                            expertisPersonal)

                }
           }
        }
    }



    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onSuccessProvince(data: CountryModel) {
        cardviewProvince.setOnClickListener { v ->
            val subMenu = PopupMenu(this, v)
            for (province in data.results[0].provinces) {
                subMenu.menu.add(province.id, province.id,0,province.name)
                subMenu.setOnMenuItemClickListener { item ->
                    textProvince.text = item.title
                    presenter.getDistrictList("token $tokenLogin", item.title.toString())
                    false
                }
                subMenu.show()
            }
        }
    }

    override fun onSuccessDistrict(data: ProvinceModel) {
        cardviewDistrict.setOnClickListener { v ->
            val subMenu = PopupMenu(this, v)
            for (district in data.results[0].districts) {
                subMenu.menu.add(district.id, district.id,0,district.name)
                subMenu.setOnMenuItemClickListener { item ->
                    textDistrict.text = item.title
                    presenter.getSubDistrict("token $tokenLogin", item.title.toString())
                    false
                }
                subMenu.show()
            }
        }
    }

    override fun onSuccessSubDistrict(data: DistrictModel) {
        cardviewSubDistrict.setOnClickListener { v ->
            val subMenu = PopupMenu(this, v)
            for (subdistrict in data.results[0].sub_districts) {
                subMenu.menu.add(subdistrict.id, subdistrict.id,0,subdistrict.name_label)
                subMenu.setOnMenuItemClickListener { item ->
                    subDistrict = item.itemId
                    textSubDistrict.text = item.title

                    val postalCode = item.title.takeLast(5)
                    textPostalCode.text = postalCode


                    //  presenter.getPostalCode("token $tokenLogin", item.title.toString())
                    false
                }
                subMenu.show()
            }
        }
    }

    override fun onSuccessPostalCode(data: SubDistrictModel) {
        cardviewPostalCode.setOnClickListener { v ->
            val subMenu = PopupMenu(this, v)
            for (postalCode in data.results) {
                subMenu.menu.add(postalCode.id, postalCode.id,0,postalCode.postal_code)
                subMenu.setOnMenuItemClickListener { item ->
                    textPostalCode.text = item.title
                    false
                }
                subMenu.show()
            }
        }
    }

    override fun onSuccessGetID(id: Int) {
        startActivity(Intent(this, MarketActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra("planID",planID)
                .putExtra("businessID", id))
        this.finish()
    }

    override fun onFailedGetID(error: String) {
        val toast = Toast.makeText(this,"pastikan data yang input anda benar", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
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

    override fun onSuccessgetDataBusiness(data: Result) {

    }

    override fun onSubDistrictDetail(data: ResponseSubDistrict) {

    }
}