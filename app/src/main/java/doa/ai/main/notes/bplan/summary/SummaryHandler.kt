package doa.ai.main.notes.bplan.summary

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.evrencoskun.tableview.TableView
import doa.ai.BuildConfig
import doa.ai.R
import doa.ai.main.notes.bplan.finance.AdapterTable
import doa.ai.model.CashFlowTrX
import doa.ai.model.IncomeStatementTr
import kotlinx.android.synthetic.main.activity_bplan_summary.*
import android.app.DownloadManager
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.net.Uri
import android.os.Environment
import java.io.File
import android.content.Context.DOWNLOAD_SERVICE
import androidx.core.content.ContextCompat.getSystemService

import android.app.PendingIntent.getActivity
import android.content.Context
import java.util.*
import android.content.DialogInterface
import com.karumi.dexter.PermissionToken
import android.content.Intent
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.icu.util.UniversalTimeScale.toBigDecimal
import android.text.Html
import android.util.Log
import android.view.*
import com.karumi.dexter.Dexter
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.listener.PermissionRequest
import doa.ai.base.BaseActivity
import doa.ai.utils.formatStringCurrency
import doa.ai.utils.pref.SavePrefId
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.view_planing_business.*


class SummaryActivity : BaseActivity(), SummaryView {
    var titlePlan = ""
    var idPlanDownloadPdf = 0
    var planID: Int = 0
    var businessID: Int = 0
    var marketID: Int = 0
    var strategyID: Int = 0
    var financeID: Int = 0
    var ID: Int = 0
    private var tokenLogin: String? = ""
    private var mTableAdapter: AdapterTable? = null
    private lateinit var presenter: SummaryPresenter
    private var prefs: SharedPreferences? = null
    var urlDownload = ""
    private var downloadManager: DownloadManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(doa.ai.R.layout.activity_bplan_summary)
        setSupportActionBar(toolbarBplanSummary)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        checkPermission()
        presenter = SummaryPresenter()
        presenter.summaryContract()
        prefs = applicationContext.getSharedPreferences("MyPref", 0)
        planID = intent.getIntExtra("planID", 0)
        businessID = intent.getIntExtra("businessID", 0)
        marketID = intent.getIntExtra("marketID", 0)
        strategyID = intent.getIntExtra("strategyID", 0)
        financeID = intent.getIntExtra("financeID", 0)
        tokenLogin = prefs?.getString("loginToken", "")
        ID = intent.getIntExtra("ID", 0)


        handleUi()

        if (planID == 0) {

            presenter.detailSummary("token $tokenLogin", ID)
            idPlanDownloadPdf =intent.getIntExtra("IDPLAN", 0)
        } else {


            if (businessID != null || !businessID.equals(0)) {
                presenter.summary("token $tokenLogin",
                        planID, businessID, marketID,
                        strategyID, financeID, "summary for all segment", "")
            } else {
                val id = SavePrefId().getPlanID().toString()
                val businessId = SavePrefId().getBussinessId()
                val marketId = SavePrefId().getMarketId()
                val strategyId = SavePrefId().getStrategyId()
                val finnanceId = SavePrefId().getFinnanceId()

                idPlanDownloadPdf = id.toInt()
                planID = id.toInt()
                businessID = businessId.toInt()
                marketID = marketId.toInt()
                strategyID = strategyId.toInt()
                financeID = finnanceId.toInt()
                presenter.summary("token $tokenLogin",
                        planID, businessID, marketID,
                        strategyID, financeID, "summary for all segment", "")
            }

        }

        urlDownload = BuildConfig.BASE_URL + "plan/export/$idPlanDownloadPdf/"
        downloadManager = Objects.requireNonNull(this).getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?

    }

    private fun handleUi() {
        linear_planing_business.setOnClickListener {
            view_rencana_usaha.visibility = View.VISIBLE
            view_business_model.visibility = View.GONE
            layout_business_planing.visibility = View.VISIBLE
            layout_business_model.visibility = View.GONE


        }

        linear_business_model.setOnClickListener {
            view_rencana_usaha.visibility = View.GONE
            view_business_model.visibility = View.VISIBLE
            layout_business_planing.visibility = View.GONE
            layout_business_model.visibility = View.GONE
            showInfoMessage("Bisnis model kosong")


        }
    }

    fun checkPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {}
                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.addCategory(Intent.CATEGORY_HOME)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)

                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        AlertDialog.Builder(this@SummaryActivity)
                                .setTitle("Storage Permission Needed")
                                .setMessage("This app needs the Storage permission, please accept to use storage location for save the pdf")
                                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                                    //Prompt the user once explanation has been shown
                                    token.continuePermissionRequest()
                                })
                                .create()
                                .show()
                    }
                }).check()
    }

    private fun getDownload() {
        Log.e("tag", "download uri $urlDownload")
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val futureStudioIconFile = File(folder, "doa_summary_bisnis_plan_$titlePlan.pdf")
        val downloadLocation = Uri.fromFile(futureStudioIconFile)
        val download_Uri = Uri.parse(urlDownload)
        val request = DownloadManager.Request(download_Uri)
        request.addRequestHeader("Authorization", "token $tokenLogin")
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setTitle("Download bisnis plan $titlePlan.pdf sukses")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setVisibleInDownloadsUi(true)
        request.setDestinationUri(downloadLocation)
        val refid = downloadManager?.enqueue(request)

    }

    private fun initializeTableView(tableView: TableView, incomeStatementTr: MutableList<IncomeStatementTr>, flowTrX: MutableList<CashFlowTrX>) {

        mTableAdapter = AdapterTable(this, incomeStatementTr, flowTrX)
        tableView.adapter = mTableAdapter

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_summary, menu)
        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_download_pdf -> {
                getDownload()
                true
            }
            else -> super.onOptionsItemSelected(item)
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

    override fun onSuccessSummary(response: SummaryResponse) {
        titlePlan = response.result.plan.title
        val summaryConc = response.result.short_summary
        val executiveSummary = "${response.result.plan.title} menawarkan ${response.result.market.product} kepada ${response.result.market.target_customer} di pasar ${response.result.market.geographic_market}. Bisnis ini memiliki posisi pasar yang kuat dan strategi yang koheren dan telah menetapkan langkah yang jelas untuk mencapai tujuannya ${response.result.strategy.year_objective} dalam 5 tahun kedepan. ${response.result.plan.title} telah menetapkan target yang ingin dicapai. Tahun depan bisnis ini akan mencapai penjualan ${response.result.finance.currency}  ${response.result.finance.analysis.tables.income_statement.tr[1].td[0].toString().formatStringCurrency()}. Penjualan akan tumbuh sebesar ${response.result.finance.sales_growth_year} persen setiap tahun untuk mencapai angka ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[1].td[4].toString().formatStringCurrency()} pada akhir tahun ke 5. Pada tahun terakhir dari rencana ini, bisnis akan mencapai laba bersih ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[5].td[4].toString().formatStringCurrency()}. Ini akan mencerminkan pengembalian yang baik dan memberikan laba ditahan yang cukup untuk rencana pengembangan masa depan."
        editExecuitveSummary.setText(Html.fromHtml(summaryConc))

        val bussinesShortSummary = response.result.business.short_summary

        val businessSummary = "${response.result.plan.title} didirikan sebagai ${response.result.business.structure}. Ini adalah badan hukum yang sesuai untuk jenis bisnis ${response.result.plan.title} dan akan cocok dengan tujuannya. Tim manajemen akan meninjau struktur ini ketika bisnis berkembang.\n" +
                "\n" +
                "${response.result.plan.title} berada di posisi yang tepat untuk menawarkan ${response.result.market.product} di pasar ${response.result.market.target_customer}. Untuk melakukan hal tersebut Tim manajemen kami memiliki pengalaman dan kompetensi untuk mencapai target yang telah kami tetapkan, berikut ini adalah personil dari tim manajemen ${response.result.plan.title} :\n" +
                "\n" +
                "${response.result.business.key_personals[2].value[0]}, ${response.result.business.key_personals[1].value[0]}, memiliki keahlian dan pengalaman ${response.result.business.key_personals[0].value[0]}.\n" +
                "\n" +
                "Tim manajemen berkomitmen untuk mengembangkan bisnis dengan memberikan layanan terbaik kepada pelanggan dan membangun brand yang kuat dan kredibel. Hal tersebut dapat dilihat melalui hubungan yang baik antara ${response.result.plan.title}, pelanggan, dan pemangku kepentingan lainnya. Kami juga menganggap serius tanggung jawab dan berkomitmen pada pendekatan etis terhadap bisnis. \n" +
                "\n" +
                "Tim manajemen memiliki keahlian dalam memanfaatkan sumber daya bisnis yang ${response.result.plan.title} miliki untuk mencapai tujuan. Berikut ini adalah sumber daya utama tersebut:\n" +
                "\n" +
                "${response.result.business.key_resources[0]}.\n" +
                "\n" +
                "Sumber daya tersebut secara sinergis membawa nilai tambah dan menjadi keunggulan yang sulit ditiru pesaing sehingga memberikan keuntungan yang signifikan dan berkelanjutan. \n" +
                "\n" +
                "Kami mengakui pentingnya sumber daya ini untuk itu kami memastikan setiap sumber daya tersebut dikembangkan lebih lanjut dan dikapitalisasi.\n" +
                "\n" +
                "Singkatnya, ${response.result.plan.title} memiliki bentuk hukum, sumber daya, nilai, dan tim manajemen yang tepat untuk mencapai keberhasilan. Efek gabungan dari faktor-faktor ini memberikan fondasi yang kuat untuk mencapai bisnis yang kuat dan menguntungkan."
        editSummaryBisnis.setText(Html.fromHtml(bussinesShortSummary))


        val marketShortSummary = response.result.market.short_summary
        val marketSummary = "${response.result.plan.title} menawarkan ${response.result.market.product} kepada ${response.result.market.target_customer}  di pasar ${response.result.market.geographic_market}. Pasar ini membuka peluang yang besar bagi ${response.result.plan.title}, dengan keunggulan kompetitifnya seperti, ${response.result.market.competitive_advantage}. Dengan begitu ${response.result.plan.title} dapat mengambil posisi pasar yang kuat dalam mengembangkan bisnisnya.\n" +
                "\n" +
                "${response.result.plan.title} menghadapi persaingan, seperti yang diharapkan di pasar yang menarik ini. ${response.result.market.key_competitors[0].value[0]}. adalah pesaing utama. Ia memiliki beberapa kekuatan, tetapi kelemahan. Kelemahan tersebut akan membatasi kapasitasnya untuk bersaing.\n" +
                "\n" +
                "${response.result.plan.title} akan dapat memanfaatkan kelemahan para pesaingnya untuk mendapatkan posisi pasar.\n" +
                "\n" +
                "Singkatnya, permintaan tinggi untuk apa yang bisa ${response.result.plan.title} tawarkan dan kelemahan yang diidentifikasi dalam pesaing utamanya memberikan peluang pasar yang menarik."
        editSummaryMarket.setText(Html.fromHtml(marketShortSummary))

        val strategyShortSummary = response.result.strategy.short_summary
        val strategySummary = "${response.result.plan.title} memiliki tujuan ${response.result.strategy.year_objective}. Tim manajemen juga telah mengidentifikasi peluang strategis yang akan membantu mencapai tujuan, sebagai berikut: \n" +
                "\n" +
                "${response.result.strategy.strategic_opportunities[0]}.\n" +
                "\n" +
                "Peluang-peluang ini selaras dengan tujuan 5 tahun dan memberikan jalur manajemen yang jelas kepada tim manajemen. Tim manajemen berkomitmen untuk mengejar peluang-peluang ini dan juga mengidentifikasi peluang-peluang baru yang muncul. Kami melihat peluang sebagai bagian penting dari peran kami dan masa depan bisnis. Tim manajemen telah mengidentifikasi langkah-langkah kunci untuk mencapai tujuan lima tahun dan memanfaatkan peluang yang diidentifikasi. Ini akan diterapkan secara sistematis selama periode tersebut.\n" +
                "\n" +
                "Langkah 1: ${response.result.strategy.strategic_steps[0]}.\n" +
                "\n" +
                "Pemilihan dan urutan tindakan ini telah dipilih dengan cermat untuk menghasilkan potensi penuh dari peluang bisnis dan memastikan bahwa target tercapai. Tim manajemen akan terus memantau kinerja bisnis terhadap target dan melakukan penyesuaian yang diperlukan. Setiap saat fokusnya adalah mencapai tujuan-tujuan utama.\n" +
                "\n" +
                "Singkatnya, ${response.result.plan.title} telah dengan jelas mengidentifikasi peluang dan rencana sistematis dengan tahapan yang diartikulasikan dengan jelas untuk mencapai tujuan lima tahun."
        editSummaryStrategy.setText(Html.fromHtml(strategyShortSummary))

        val conculsionSummary = response.result.conclusion
        val conclSummary = "${response.result.plan.title} memiliki rencana yang koheren untuk kesuksesan di masa depan (lihat ringkasan dalam lampiran B). Ini adalah posisi yang tepat untuk menargetkan ${response.result.market.target_customer}. Tim manajemen telah memberikan tujuan yang jelas untuk ${response.result.strategy.year_objective} dalam periode lima tahun. Target keuangan telah terbukti realistis mengingat kekuatan bisnis dan posisi strategisnya. Tim manajemen berkomitmen untuk mencapai tujuan strategis. Mereka memiliki rencana tahapan strategis yang jelas untuk mewujudkan potensi bisnis dan menghasilkan pengembalian yang sehat bagi semua pemangku kepentingan."

        editSummaryConclusion.setText(Html.fromHtml(conculsionSummary))

        val attachment = response.result.attachment
        val appendixA = "A. Asumsi\n" +
                "\n" +
                "Asumsi berikut dibuat dalam mengembangkan rencana ini.\n" +
                "\n" +
                "1. ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[1].td[0].toString().formatStringCurrency()}. penjualan di tahun pertama. Tingkat penjualan ini realistis mengingat pengalaman sebelumnya dan omset perusahaan-perusahaan serupa di sektor ini.\n" +
                "\n" +
                "2. Pertumbuhan penjualan ${response.result.finance.sales_growth_year} persen per tahun. Tingkat pertumbuhan penjualan ini dapat dicapai mengingat daya tarik pasar dan rencana strategis yang koheren dari tim manajemen.\n" +
                "\n" +
                "3. Biaya penjualan ${response.result.finance.cash_of_sale} persen. Persentase biaya penjualan ini sejalan dengan rata-rata sektor. Persentase biaya penjualan diperkirakan akan tetap stabil selama periode rencana.\n" +
                "\n" +
                "4. Pengeluaran ${response.result.finance.currency} ${response.result.finance.expanses_first_year.toString().formatStringCurrency()} di tahun pertama. Tingkat biaya overhead ini sejalan dengan bisnis dengan ukuran yang sama di sektor ini.\n" +
                "\n" +
                "5. Pertumbuhan biaya tahunan ${response.result.finance.expanses_growth_year} persen. Pertumbuhan biaya ini memperhitungkan perubahan-perubahan dalam overhead ketika perusahaan berkembang.\n" +
                "\n" +
                "6. ${response.result.finance.customer_day_credit} hari kredit pelanggan. Ini sejalan dengan ketentuan perdagangan industri dan harus kompetitif.\n" +
                "\n" +
                "7. ${response.result.finance.supplier_day_credit} hari kredit pemasok. Ini realistis mengingat persyaratan yang biasanya ditawarkan oleh pemasok.\n" +
                "\n" +
                "8. Perkiraan arus kas mengasumsikan bahwa piutang dan hutang diselesaikan sebelum periode berikutnya.\n" +
                "\n" +
                "9. Arus kas tidak memperhitungkan perolehan aset tetap dari kas yang dihasilkan.\n" +
                "\n" +
                "10. ${response.result.finance.currency} ${response.result.finance.cash_invested.toString().formatStringCurrency()} injeksi uang tunai ekuitas. Ekuitas ini dipastikan tersedia dan siap untuk diinvestasikan dalam bisnis.\n" +
                "\n" +
                "11. Injeksi tunai ekuitas diasumsikan dilakukan pada awal rencana.\n" +
                "\n" +
                "12. Angka-angka ini memperhitungkan inflasi.\n" +
                "\n" +
                "13. Angka-angka ini realistis mengingat situasi pasar saat ini dan tren yang diharapkan.\n" +
                "\n" +
                "14. Sementara pendekatan konservatif telah diambil untuk proyeksi dan upaya untuk memperhitungkan risiko, seperti semua prediksi ada potensi faktor yang tidak terduga.\n"
        editSummaryAttachment.setText(Html.fromHtml(attachment))

        val appendixB = "B. ANALISA RINGKASAN SOAR (Kekuatan, Peluang, Aspirasi, Hasil)\n" +
                "\n" +
                "Lampiran ini memberikan ringkasan rencana dalam bentuk analisis SOAR (Stavros dan Hinrichs, 2009). Ini menyoroti kekuatan, peluang, aspirasi, dan hasil utama. Bisnis ini terbukti memiliki dasar yang kuat untuk mencapai aspirasinya.\n" +
                "\n" +
                "KEKUATAN\n" +
                "\n" +
                "${response.result.business.key_resources[0]}.\n" +
                "Investasi ekuitas tunai sebesar ${response.result.finance.currency} ${response.result.finance.cash_invested.toString().formatStringCurrency()}\n" +
                "Personel Utama kami memiliki keterampilan berikut:\n" +
                "• ${response.result.business.key_personals[0].value[0]}\n" +
                "\n" +
                "PELUANG\n" +
                "\n" +
                "${response.result.strategy.strategic_opportunities[0]}.\n" +
                "\n" +
                "\n" +
                "ASPIRASI\n" +
                "\n" +
                "Bisnis ini memiliki tujuan lima tahun untuk ${response.result.strategy.year_objective}. \n" +
                "\n" +
                "HASIL\n" +
                "\n" +
                "Penjualan dalam lima tahun = ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[1].td[4].toString().formatStringCurrency()}\n" +
                "Laba Bersih dalam lima tahun = ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[5].td[4].toString().formatStringCurrency()}\n"
        editSummaryAttachment2.setText(appendixB)

        val finnanceShortSummary = response.result.finance.short_summary

        val finance1 = "Tabel 1 di bawah ini menunjukkan laporan laba rugi yang diproyeksikan untuk lima tahun dari rencana. Ini menunjukkan bagaimana target akan dicapai. Proyeksi telah dibangun dengan hati-hati dan didasarkan pada asumsi yang realistis (lihat lampiran Asumsi). \n" +
                "\n" +
                "Pada tahun pertama dari rencana ${response.result.plan.title} akan mencapai penjualan ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[1].td[0]} , laba kotor ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[3].td[0]} dan laba bersih ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[5].td[0]}. di tengah-tengah rencana, bisnis akan mencapai penjualan ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[1].td[2]}, laba kotor ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[1].td[4]} dan laba bersih ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[5].td[4]}. Pada akhir rencana bisnis akan mencapai penjualan ${response.result.finance.currency}  ${response.result.finance.analysis.tables.income_statement.tr[1].td[4]}, laba kotor ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[3].td[4]} dan laba bersih ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[5].td[4]}.\n" +
                "\n" +
                "Angka-angka menunjukkan pertumbuhan yang stabil dalam penjualan dan laba kotor selama periode rencana. Bisnis menjadi menguntungkan pada tahun pertama dari rencana, yang lebih awal dari banyak pesaing yang sama dan menunjukkan kekuatan model bisnis. Ini akan memberikan dasar keuangan yang kuat untuk mengembangkan rencananya. Pertumbuhan penjualan rata-rata 98 \u200B\u200Bpersen sepanjang periode adalah sehat dan menunjukkan potensi bisnis. Penjualan dan pertumbuhan yang diprediksi realistis karena kekuatan bisnis dan daya tarik pasar.\n"
      //  editSummaryFinance1.setText(Html.fromHtml(finnanceShortSummary))
        editSummaryFinance1.loadData(finnanceShortSummary, "text/html; charset=utf-8",null)

        initializeTableView(tableViewSummaryStatment, response.result.finance.analysis.tables.income_statement.tr,
                response.result.finance.analysis.tables.cash_flow.tr)
        mTableAdapter?.setStatement()

        val finance2 = "${response.result.finance.analysis.tables.income_statement.tr[0].td[0]}-${response.result.finance.analysis.tables.income_statement.tr[0].td[4]} ( ${response.result.finance.currency} ).\n" +
                "\n" +
                "\n" +
                "Tabel 2 di bawah ini menunjukkan arus kas yang diproyeksikan untuk lima tahun dari rencana. Ini menunjukkan likuiditas bisnis selama periode tersebut.\n" +
                "\n" +
                "${response.result.plan.title} mendasarkan arus kas pada asumsi bahwa mereka akan menawarkan pelanggan kredit ${response.result.finance.customer_day_credit} hari. Ini sejalan dengan norma industri dan harus kompetitif di pasar. Pemasok diasumsikan menawarkan kredit ${response.result.finance.supplier_day_credit} hari. Ini juga realistis mengingat praktik saat ini dalam industri. Bisnis ini mencapai posisi cash forward (kas yang dibawa ke periode selanjutnya) positif di tahun pertama. Pada akhir lima tahun bisnis memiliki posisi kas positif ${response.result.finance.currency} ${response.result.finance.analysis.tables.cash_flow.tr[6].td[4]}. Ini menunjukkan kekuatan bisnis dan kemampuannya menghasilkan uang tunai untuk mendukung rencana masa depan."
        editSummaryFinance2.setText(finance2)
        initializeTableView(tableViewSummaryCash, response.result.finance.analysis.tables.income_statement.tr,
                response.result.finance.analysis.tables.cash_flow.tr)
        mTableAdapter?.setCashFlow()

        val finance3 = "Singkatnya, ${response.result.plan.title} telah membangun perkiraan keuangan metodis yang menunjukkan aliran kas tangguh dan laba menarik di tahun terakhir. Indikator kinerja utama untuk periode lima tahun adalah:\n" +
                "\n" +
                "Pertumbuhan penjualan per tahun =  ${response.result.finance.expanses_growth_year} persen\n" +
                "\n" +
                "Laba Bersih pada tahun ke lima = ${response.result.finance.currency} ${response.result.finance.analysis.tables.income_statement.tr[5].td[0]}\n" +
                "\n" +
                "Uang tunai yang dihasilkan dalam tahun kelima = ${response.result.finance.currency} ${response.result.finance.analysis.tables.cash_flow.tr[6].td[4]}\n" +
                "\n" +
                "Akan ada analisis varian bulanan yang dilakukan atas kinerja aktual terhadap metrik kritis ini. Tim manajemen akan bereaksi cepat terhadap setiap masalah yang diidentifikasi dalam mencapai target ini.\n"

        editSummaryFinance3.setText(finance3)
    }

    override fun onFailedSummary(error: String) {
        val toast = Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}