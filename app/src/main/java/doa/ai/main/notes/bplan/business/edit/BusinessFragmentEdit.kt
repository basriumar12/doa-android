package doa.ai.main.notes.bplan.business.edit

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.system.ErrnoException
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import doa.ai.R
import doa.ai.base.BaseFragment
import doa.ai.extentions.Dialog
import doa.ai.main.notes.bplan.business.BusinessPresenter
import doa.ai.main.notes.bplan.business.BusinessView
import doa.ai.main.notes.bplan.business.Result
import doa.ai.main.notes.bplan.business.adapter.*
import doa.ai.model.CountryModel
import doa.ai.model.DistrictModel
import doa.ai.model.ProvinceModel
import doa.ai.model.SubDistrictModel
import doa.ai.utils.SwipeToDeleteCallback
import doa.ai.utils.pref.SavePrefId
import doa.ai.utils.pref.SavePrefSegmentBusiness
import doa.ai.utils.pref.SavePrefTokenLogin
import kotlinx.android.synthetic.main.activity_bplan_business.*
import kotlinx.android.synthetic.main.activity_bplan_business.view.*
import kotlinx.android.synthetic.main.view_owner_person.view.*

class BusinessFragmentEdit : BaseFragment(), BusinessView {


    private var savedInstanceState: Bundle? = null
    var subDistrict: Int = 0
    var planID: Int = 0
    var businessID: Int = 0
    var tokenLogin: String? = ""
    private lateinit var presenter: BusinessPresenter
    private lateinit var prefs: SharedPreferences
    var keyResources: MutableList<String> = ArrayList()
    var businessActivities: MutableList<String> = ArrayList()
    var businesspartners: MutableList<String> = ArrayList()
    var operatingExpenses: MutableList<String> = ArrayList()
    var ownerAndPersonModel: MutableList<OwnerAndPersonModel> = ArrayList()
    private var namePersonal: MutableList<String> = ArrayList()
    private var positionPersonal: MutableList<String> = ArrayList()
    private var expertisPersonal: MutableList<String> = ArrayList()
    lateinit var cardviewProvince: CardView
    lateinit var cardviewDistrict: CardView
    lateinit var cardviewSubDistrict: CardView
    lateinit var adapterKeySuccess: AdapterKeySuccess
    lateinit var adapterBusinessPartnerts: AdapterBusinessPartnerts
    lateinit var adapterBusinessActivities: AdapterBusinessActivities
    lateinit var adapterOperatingExpanses: AdapterOperatingExpanses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("tag", "0 $subDistrict")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_bplan_business, container, false)
        Log.e("tag", "1 $subDistrict")
        cardviewDistrict = view.cardviewDistrict
        cardviewProvince = view.cardviewProvince
        cardviewSubDistrict = view.cardviewSubDistrict
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = SavePrefId().getPlanID()
        planID = id.toInt()
        businessID = SavePrefId().getBussinessId().toInt()
        tokenLogin = SavePrefTokenLogin().getTokenLogin().toString()
        presenter = BusinessPresenter()
        presenter.businessContract()
        presenter.getProvinceList("token $tokenLogin", "indo")
        presenter.getDetailBusiness("token $tokenLogin", businessID)


        handleView()
        handlePostBusinessSegment()
        handleSave()

    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }


    private fun handleSave() {
        editRTLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                SavePrefSegmentBusiness().setBusinessRT(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        })
        editRWLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentBusiness().setBusinessRW(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        })
        editLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                SavePrefSegmentBusiness().setBusinessStreet(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        })


//        textProvince.text = SavePrefSegmentBusiness().getBusinessProvince().toString()
//        textDistrict.text = SavePrefSegmentBusiness().getBusinesscity().toString()
//        textSubDistrict.text = SavePrefSegmentBusiness().getBusinessSubDistric().toString()
//        textPostalCode.text = SavePrefSegmentBusiness().getBusinessPostalCode()
//        editLocation.setText(SavePrefSegmentBusiness().getBusinessStreet().toString())
//        editRTLocation.setText(SavePrefSegmentBusiness().getBusinessRT().toString())
//        editRWLocation.setText(SavePrefSegmentBusiness().getBusinessRW().toString())


    }

    private fun handlePostBusinessSegment() {


        businessCreate.setOnClickListener {
            planID?.let { it1 ->

                if (editRTLocation.text.toString().isEmpty()) {
                    showErrorMessage("RT  belum di isi")
                } else if (keyResources.isNullOrEmpty()) {
                    showErrorMessage("Kunci sukses belum di isi")
                } else if (ownerAndPersonModel.isNullOrEmpty()) {
                    showErrorMessage("nama personal / pendiri belum di isi")
                } else if (editRWLocation.text.toString().isEmpty()) {
                    showErrorMessage("RW belum di isi")
                } else if (editLocation.text.toString().isEmpty()) {
                    showErrorMessage("jalan belum di isi")
                } else if (textSubDistrict.text.toString().isEmpty()) {
                    showErrorMessage("Kelurahan belum di pilih")
                } else if (keyResources.isNullOrEmpty()) {
                    showErrorMessage("data belum lengkap")
                } else if (operatingExpenses.isNullOrEmpty()) {
                    showErrorMessage("data belum lengkap")
                } else if (businessActivities.isNullOrEmpty()) {
                    showErrorMessage("data belum lengkap")
                } else if (businesspartners.isNullOrEmpty()) {
                    showErrorMessage("data belum lengkap")
                } else if (keyResources.get(0).equals("")) {
                    showErrorMessage("data belum lengkap")
                } else if (operatingExpenses.get(0).equals("")) {
                    showErrorMessage("data belum lengkap")
                } else if (businessActivities.get(0).equals("")) {
                    showErrorMessage("data belum lengkap")
                } else if (businesspartners.get(0).equals("")) {
                    showErrorMessage("data belum lengkap")
                } else if (ownerAndPersonModel.get(0).name.equals("") || ownerAndPersonModel.get(0).name.isNullOrEmpty()) {
                    showErrorMessage("data belum lengkap")
                } else if (ownerAndPersonModel.get(0).position.equals("") || ownerAndPersonModel.get(0).position.isNullOrEmpty()) {
                    showErrorMessage("data belum lengkap")
                } else if (ownerAndPersonModel.get(0).skill.equals("") || ownerAndPersonModel.get(0).skill.isNullOrEmpty()) {
                    showErrorMessage("data belum lengkap")
                } else {
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
                    subDistrict = SavePrefSegmentBusiness().getBusinessProvinceId().toInt()
                    presenter.postBusinessEditSegment("token $tokenLogin",
                            it1,
                            textStructure.text.toString(),
                            editLocation.text.toString(),
                            subDistrict,
                            subDistrictsRemovePostalCode,
                            keyResources,
                            0,
                            editRTLocation.text.toString().toInt(),
                            editRWLocation.text.toString().toInt(),
                            namePersonal,
                            positionPersonal,
                            expertisPersonal,
                            businesspartners,
                            businessActivities,
                            operatingExpenses
                    )

                }
            }
        }
    }


    private fun handleView() {

        //add key success
        keyResources = ArrayList()

        rv_key_succsess.layoutManager = LinearLayoutManager(activity!!)
        adapterKeySuccess = AdapterKeySuccess(keyResources as ArrayList<String>, activity!!)
        rv_key_succsess.adapter = adapterKeySuccess

        tv_tambah_kunci_suksess.setOnClickListener {
            keyResources.add("")
            rv_key_succsess.adapter = adapterKeySuccess
            adapterKeySuccess.notifyDataSetChanged()

        }
        //delete item
        val swipeHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_key_succsess.adapter as AdapterKeySuccess
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv_key_succsess)

        //add Business Partnerts
        businesspartners = ArrayList()

        rv_business_partnerts.layoutManager = LinearLayoutManager(activity!!)
        adapterBusinessPartnerts = AdapterBusinessPartnerts(businesspartners as ArrayList<String>, activity!!)
        rv_business_partnerts.adapter = adapterBusinessPartnerts

        tv_bussiness_partnerts.setOnClickListener {
            businesspartners.add("")
            rv_business_partnerts.adapter = adapterBusinessPartnerts
            adapterBusinessPartnerts.notifyDataSetChanged()

        }
        //add Business Activities
        businessActivities = ArrayList()

        rv_business_activities.layoutManager = LinearLayoutManager(activity!!)
        adapterBusinessActivities = AdapterBusinessActivities(businessActivities as ArrayList<String>, activity!!)
        rv_business_activities.adapter = adapterBusinessActivities

        tv_bussiness_activities.setOnClickListener {
            businessActivities.add("")
            rv_business_activities.adapter = adapterBusinessActivities
            adapterBusinessActivities.notifyDataSetChanged()

        }

        //add Operating Expanses
        operatingExpenses = ArrayList()

        rv_operating_expanses.layoutManager = LinearLayoutManager(activity!!)
        adapterOperatingExpanses = AdapterOperatingExpanses(operatingExpenses as ArrayList<String>, activity!!)
        rv_operating_expanses.adapter = adapterOperatingExpanses

        tv_operating_expanses.setOnClickListener {
            operatingExpenses.add("")
            rv_operating_expanses.adapter = adapterOperatingExpanses
            adapterOperatingExpanses.notifyDataSetChanged()

        }


        //add owner data
        ownerAndPersonModel = ArrayList()
        rv_owner_and_person.layoutManager = LinearLayoutManager(activity!!)

        //from pref
        val nameOwner =    SavePrefSegmentBusiness().getBusinessNameFounder()
        val positionOwner =  SavePrefSegmentBusiness().getBusinessPositionFounder()
        val expertisOwner =SavePrefSegmentBusiness().getBusinessExpertisFounder()
        val addOneFromPref = OwnerAndPersonModel(nameOwner.toString(), positionOwner.toString(), expertisOwner.toString())
        val addOne = OwnerAndPersonModel("", "", "")
        ownerAndPersonModel.add(addOneFromPref)


        //ownerAndPersonModel.add(addOne)

        val adapterOwnerAndPerson = AdapterOwnerAndPerson(ownerAndPersonModel as ArrayList<OwnerAndPersonModel>, activity!!)
        rv_owner_and_person.adapter = adapterOwnerAndPerson

        tv_add_owner_and_person.setOnClickListener {
            val data = OwnerAndPersonModel("", "", "")
            ownerAndPersonModel.add(data)
            rv_owner_and_person.adapter = adapterOwnerAndPerson
            adapterOwnerAndPerson.notifyDataSetChanged()

        }
        //delete item
        val swipeHandlerPerson = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_owner_and_person.adapter as AdapterOwnerAndPerson
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelperPerson = ItemTouchHelper(swipeHandlerPerson)
        itemTouchHelperPerson.attachToRecyclerView(rv_owner_and_person)



        cardviewStructure.setOnClickListener { v ->
            val popMenu = PopupMenu(activity!!, v)
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


            imageInfoBusiness.setOnClickListener {
                Dialog.progressDialog(activity!!, getString(doa.ai.R.string.info_business))
            }

            imageInfoLocation.setOnClickListener {
                Dialog.progressDialog(activity!!, getString(doa.ai.R.string.info_location))
            }

            imageInfoResources.setOnClickListener {
                Dialog.progressDialog(activity!!, getString(doa.ai.R.string.info_resources))
            }

            imageInfoPersonal.setOnClickListener {
                Dialog.progressDialog(activity!!, getString(doa.ai.R.string.info_personal))
            }

        }


    }


    override fun onSuccessProvince(data: CountryModel) {


        cardviewProvince.setOnClickListener { v ->
            val subMenu = PopupMenu(context!!, v)
            for (province in data.results[0].provinces) {
                subMenu.menu.add(province.id, province.id, 0, province.name)
                subMenu.setOnMenuItemClickListener { item ->

                    SavePrefSegmentBusiness().setBusinessProvince(item.title.toString())
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
            val subMenu = PopupMenu(context!!, v)
            for (district in data.results[0].districts) {
                subMenu.menu.add(district.id, district.id, 0, district.name)
                subMenu.setOnMenuItemClickListener { item ->
                    SavePrefSegmentBusiness().setBusinessCity(item.title.toString())
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
            val subMenu = PopupMenu(context!!, v)
            for (subdistrict in data.results[0].sub_districts) {
                subMenu.menu.add(subdistrict.id, subdistrict.id, 0, subdistrict.name_label)
                subMenu.setOnMenuItemClickListener { item ->
                    SavePrefSegmentBusiness().setBusinessSubDistric(item.title.toString())
                    SavePrefSegmentBusiness().setBusinessProvinceId(item.itemId.toString())

                    subDistrict = item.itemId
                    textSubDistrict.text = item.title

                    val postalCode = item.title.takeLast(5)
                    SavePrefSegmentBusiness().setBusinessPostalCode(postalCode.toString())
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
            val subMenu = PopupMenu(activity!!, v)
            for (postalCode in data.results) {
                subMenu.menu.add(postalCode.id, postalCode.id, 0, postalCode.postal_code)
                subMenu.setOnMenuItemClickListener { item ->
                    SavePrefSegmentBusiness().setBusinessPostalCode(item.title.toString())
                    textPostalCode.text = item.title
                    false
                }
                subMenu.show()
            }
        }
    }

    interface FragmentCallback {
        fun saveIdBusiness(id: String)
    }

    private var fragmentCallback: FragmentCallback? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentCallback = context as FragmentCallback
    }

    override fun onSuccessGetID(id: Int) {

        showInfoMessage("Sukses ")
        fragmentCallback?.saveIdBusiness(id.toString())


    }

    public fun openFragment(fragment: Fragment) {
        childFragmentManager
                .beginTransaction()
                .replace(R.id.root_fragments, fragment, fragment.javaClass.simpleName)
                .commit()

    }

    override fun onSuccessgetDataBusiness(data: Result) {
        textStructure.text = data.structure

        subDistrict = data.sub_district
        editLocation.setText(data.address.toString())
        editRTLocation.setText(data.rt.toString())
        editRWLocation.setText(data.rw.toString())

        data.key_resources.forEach {
            keyResources.addAll(listOf(it.toString()))
        }
        data.business_activities.forEach {
            businessActivities.addAll(listOf(it))
        }

        data.business_partners.forEach {
            businesspartners.addAll(listOf(it))
        }

        data.operating_expenses.forEach {
            operatingExpenses.addAll(listOf(it))
        }

        adapterBusinessActivities.notifyDataSetChanged()
        adapterBusinessPartnerts.notifyDataSetChanged()
        adapterKeySuccess.notifyDataSetChanged()
        adapterOperatingExpanses.notifyDataSetChanged()

        presenter.getDetailSubDistrict("token $tokenLogin", subDistrict)


    }

    override fun onSubDistrictDetail(data: ResponseSubDistrict) {
        textProvince.text = data.result?.province?.name
        textDistrict.text = data.result?.district?.name
        textPostalCode.text = data.result?.postalCode?.toString()
        textSubDistrict.text = data.result?.name

    }

    override fun onFailedGetID(error: String) {
        Log.e("tag", "error bisnis $error")
        val toast = Toast.makeText(activity, "pastikan data yang input anda benar, error : $error", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

}