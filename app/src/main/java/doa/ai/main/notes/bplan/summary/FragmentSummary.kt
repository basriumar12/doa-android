package doa.ai.main.notes.bplan.summary

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import doa.ai.BuildConfig
import doa.ai.R
import doa.ai.base.BaseFragment
import doa.ai.main.notes.bplan.finance.AdapterTable
import doa.ai.utils.pref.SavePrefId
import kotlinx.android.synthetic.main.activity_bplan_summary.*
import kotlinx.android.synthetic.main.view_planing_business.*
import java.io.File
import java.util.*

class FragmentSummary : BaseFragment(), SummaryView {


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        presenter = SummaryPresenter()
        presenter.summaryContract()
        prefs = context!!.getSharedPreferences("MyPref", 0)
        tokenLogin = prefs?.getString("loginToken", "")
        val intent = Intent()

        planID = intent.getIntExtra("planID", 0)
        businessID = intent.getIntExtra("businessID", 0)
        marketID = intent.getIntExtra("marketID", 0)
        strategyID = intent.getIntExtra("strategyID", 0)
        financeID = intent.getIntExtra("financeID", 0)

        ID = SavePrefId().getSummaryId().toInt()
        idPlanDownloadPdf = SavePrefId().getPlanID().toInt()
        if (!ID.equals(1019000)) {

            presenter.detailSummary("token $tokenLogin", ID)
            //idPlanDownloadPdf =intent.getIntExtra("IDPLAN", 0)
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



        urlDownload = BuildConfig.BASE_URL + "plan/export/$idPlanDownloadPdf/"
        downloadManager = Objects.requireNonNull(activity)?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?

        btn_download.setOnClickListener {
            getDownload()
        }

    }

    fun checkPermission() {
        Dexter.withActivity(activity)
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
                        context?.let {
                            AlertDialog.Builder(it)
                                    .setTitle("Storage Permission Needed")
                                    .setMessage("This app needs the Storage permission, please accept to use storage location for save the pdf")
                                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                                        //Prompt the user once explanation has been shown
                                        token.continuePermissionRequest()
                                    })
                                    .create()
                                    .show()
                        }
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
        Log.e("tag"," data url $urlDownload $tokenLogin")
        request.addRequestHeader("Accept-Language", "id");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setTitle("Download bisnis plan $titlePlan.pdf sukses")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setVisibleInDownloadsUi(true)
        request.setDestinationUri(downloadLocation)
        val refid = downloadManager?.enqueue(request)

    }


    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_planing_business,container,false)

    }

    override fun onSuccessSummary(response: SummaryResponse) {
        titlePlan = response.result.plan.title
        val summaryConc = response.result.short_summary
        editExecuitveSummary.setText(Html.fromHtml(summaryConc))

        val bussinesShortSummary = response.result.business.short_summary
        editSummaryBisnis.setText(Html.fromHtml(bussinesShortSummary))

        val marketShortSummary = response.result.market.short_summary
        editSummaryMarket.setText(Html.fromHtml(marketShortSummary))

        val strategyShortSummary = response.result.strategy.short_summary
        editSummaryStrategy.setText(Html.fromHtml(strategyShortSummary))

        val conculsionSummary = response.result.conclusion
        editSummaryConclusion.setText(Html.fromHtml(conculsionSummary))

        val attachment = response.result.attachment
        editSummaryAttachment.setText(Html.fromHtml(attachment))

        val finnanceShortSummary = response.result.finance.short_summary
        editSummaryFinance1.loadData(finnanceShortSummary, "text/html; charset=utf-8",null)




    }

    override fun onFailedSummary(error: String) {
       showErrorMessage("ada terjadi kesalahan")
    }
}