package doa.ai.profile.collab

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import doa.ai.R
import doa.ai.base.BaseActivity
import doa.ai.profile.collab.find_out.AdapterSuggestionList
import doa.ai.profile.collab.find_out.SuggestionPresenter
import doa.ai.profile.collab.find_out.SuggestionView
import doa.ai.register.AdapterRegister
import kotlinx.android.synthetic.main.activity_collaboration.*
import kotlinx.android.synthetic.main.activity_register_second.*
import kotlinx.android.synthetic.main.activity_search_connection.*
import kotlinx.android.synthetic.main.activity_search_connection.progress_suggestion_collab
import kotlinx.android.synthetic.main.activity_search_connection.progress_suggestion_collab_bottom
import kotlinx.android.synthetic.main.activity_search_connection.rv_suggestion_collab
import kotlinx.android.synthetic.main.fragment_collabolator_suggestion.*
import kotlinx.android.synthetic.main.view_search_collab.*

class SearchConnectionActivity : BaseActivity(), SuggestionView {


    lateinit var adapterSuggestionList: AdapterSuggestionList

    var currentPage = 1
    private var allDataLoaded = false
    private var prefs: SharedPreferences? = null
    private var tokenLogin: String? = ""
    private var dataSuggestionList: MutableList<ResultsItem> = mutableListOf()
    private lateinit var presenter: SuggestionPresenter
    var sector = ""
    var sortir = "asc"
    private lateinit var interest: MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_connection)
        setSupportActionBar(toolbarSearchCollab)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        onClickAction()
        spinnerInterest()

        adapterSuggestionList = AdapterSuggestionList(dataSuggestionList, this,
                { dataId: ResultsItem -> addCollab(dataId) })
        presenter = SuggestionPresenter()
        presenter.suggestionContract()
        prefs = applicationContext?.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs?.getString("loginToken", "")


        setUpRecylerview()


    }

    private fun onClickAction() {
        img_filter.setOnClickListener {
            dialogSearch()
        }


        search_collab.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.toString().length > 5)

                presenter.getlistSuggestionSearch("token $tokenLogin", 1, newText.toString(),
                        sector, sortir
                )
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                presenter.getlistSuggestionSearch("token $tokenLogin", 1, query.toString(),
                        sector, sortir
                        )
                return true
            }

        })


    }

    private fun spinnerInterest() {
        interest = ArrayList()

        interest.add("Alas Kaki")
        interest.add("Advertising, Printing & Media")
        interest.add("Aplikasi dan pengembangan permainan")
        interest.add("Arsitektur")
        interest.add("Asuransi")
        interest.add("Batu-Batuan")
        interest.add("Batubara")
        interest.add("Desain produk")
        interest.add("Desain interior")
        interest.add("Elektronik")
        interest.add("Energy")
        interest.add("Farmasi")
        interest.add("Fesyen")
        interest.add("Film, Animasi & Video")
        interest.add("Fotografi")
        interest.add("Jasa Computer & Perangkatnya")
        interest.add("Jalan Tol, Pelabuhan, Bandara")
        interest.add("Kabel")
        interest.add("Kayu & Pengolahannya")
        interest.add("Keramik, Porselen & Kaca")
        interest.add("Kehutanan")
        interest.add("Kimia")
        interest.add("Komunikasi Visual")
        interest.add("Kriya")
        interest.add("Kuliner")
        interest.add("Kontruksi Bangunan")
        interest.add("Kosmetik & Keperluan Rumah Tangga")
        interest.add("Lembaga Pembiayaan")
        interest.add("Logam & Sejenisnya")
        interest.add("Logam & Mineral Lainnya")
        interest.add("Makanan & Minuman")
        interest.add("Mesin & Alat Berat")
        interest.add("Minyak & Gas Bumi")
        interest.add("Musik")
        interest.add("Otomotif & Komponennya")
        interest.add("Pakan Ternak")
        interest.add("Perkebunan")
        interest.add("Peternakan")
        interest.add("Perikanan")
        interest.add("Perdagangan Besar (Barang Produksi & Konsumsi)")
        interest.add("Perdagangan Eceran")
        interest.add("Peralatan Rumah Tanga")
        interest.add("Penerbitan")
        interest.add("Periklanan")
        interest.add("Perusahaan Investasi")
        interest.add("Perusahaan Efek")
        interest.add("Plastik & Kemasan")
        interest.add("Pulp & Kertas")
        interest.add("Properti & Real Estate")

        interest.add("Reksa Dana")
        interest.add("Restoran, Hotel & Pariwisata")
        interest.add("Rokok")
        interest.add("Semen")
        interest.add("Seni Pertunjukan")
        interest.add("Senirupa")
        interest.add("Tanaman Pangan")
        interest.add("Telekomunikasi")
        interest.add("Televisi & Radio")
        interest.add("Tekstil & Garmen")
        interest.add("Transportasi")
        interest.add("Lainnya")


    }

    fun dialogSearch() {

        val dialog = Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar)

        // Setting dialogview
        val window = dialog.getWindow()
        window.setGravity(Gravity.TOP)

        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT)
        dialog.setTitle(null)
        dialog.setContentView(doa.ai.R.layout.view_search_collab)
        dialog.img_left.setOnClickListener { view ->
            val data: List<String>
            data = interest

            val subMenu = PopupMenu(this, view)
            for (data in data) {

                subMenu.menu.add(0, 0, 0, data.toString())
                subMenu.setOnMenuItemClickListener {
                    dialog.tv_sector.text = it.title
                    sector = it.title.toString()
                    false
                }
                subMenu.show()
            }



        }

        dialog.radiogroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                if (p1 == R.id.radio_az) {
                    sortir = "asc"
                }
                if (p1 == R.id.radio_za) {
                    sortir = "desc"
                }
            }

        })

        dialog.tv_cancel.setOnClickListener {
            dialog.dismiss()
            sortir =""
            sector =""
        }

        dialog.tv_apply.setOnClickListener {
            dialog.dismiss()
            Log.e("Tag","data popmenu $sortir $sector")
        }


        dialog.setCancelable(true)

        dialog.show()
    }

    private fun addCollab(dataId: ResultsItem) {
        val receivers: MutableList<Int> = arrayListOf()
        dataId.user?.id?.let { receivers.add(it) }
        val body = BodySendCollab(receivers, "requested")
        presenter.postSendCollab("token $tokenLogin", body)


    }

    private fun setUpRecylerview() {
        rv_suggestion_collab.layoutManager = GridLayoutManager(this, 1)
        rv_suggestion_collab.adapter = adapterSuggestionList
        rv_suggestion_collab.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var directiorDown: Boolean = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                directiorDown = dy > 0
            }


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (recyclerView.canScrollVertically(1).not()
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && directiorDown
                ) {
                    currentPage += 1
                    //presenter.getlistSuggestionAfter("token $tokenLogin", currentPage, "")

                    if (allDataLoaded == true) {
                        showInfoMessage("Semua data di load")
                    }

                }
            }
        })

    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }


    override fun onSuccessSuggestion(data: MutableList<ResultsItem>) {
        dataSuggestionList.clear()
        dataSuggestionList.addAll(data)
        adapterSuggestionList.notifyDataSetChanged()
        if (data.size == 0) {

            tv_empty_data.visibility = View.VISIBLE
        }

    }

    override fun onSuccessSuggestionAfter(data: MutableList<ResultsItem>) {
        Log.e("Tag", "data suggestion list after ${Gson().toJson(data)}")
        if (data.size != 0) {
            allDataLoaded = false
            dataSuggestionList.addAll(data)
            adapterSuggestionList.notifyDataSetChanged()
        } else {
            allDataLoaded = true
        }
    }

    override fun onFailed(error: String) {

    }

    override fun onSucces(message: String) {
        showInfoMessage(message)
        //adapterSuggestionList.removeAt()
    }

    override fun showLoad() {
        progress_suggestion_collab.visibility = View.VISIBLE

    }

    override fun hideLoad() {
       progress_suggestion_collab.visibility = View.GONE

    }

    override fun showLoadBottom() {
        progress_suggestion_collab_bottom.visibility = View.VISIBLE

    }

    override fun hideLoadBottom() {
        progress_suggestion_collab_bottom.visibility = View.GONE

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
