package doa.ai.main.notes.settings.setting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import doa.ai.R
import doa.ai.dashboard.DashboardActivity
import doa.ai.main.notes.settings.Redeem.RedeemVoucherActivity
import doa.ai.main.notes.settings.purchasePremium.PurchasePremiumrActivity
import kotlinx.android.synthetic.main.fragment_settings.*
import android.widget.Toast
import android.content.ActivityNotFoundException
import android.net.Uri
import com.orhanobut.hawk.Hawk
import doa.ai.base.BaseFragment
import doa.ai.webview.WebviewActivity


class SettingsFragment: BaseFragment(){

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    private var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        prefs = context?.applicationContext?.getSharedPreferences("MyPref",0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerSettingCurrency()
        spinnerSettingLanguage()
        setLogout()
        setRedeemVoucher()
        setPurchasePremium()
        setHelpView()
        launchMarket()
        launchMedsos()
        setHelpView()
    }

    private fun spinnerSettingLanguage(){
        val language = ArrayList<String>()
        language.add("Indonesia")
        language.add("English")

        val dataAdapter = ArrayAdapter<String>(context as Context,android.R.layout.simple_spinner_item,language)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = dataAdapter
    }

    private fun spinnerSettingCurrency(){
        val currency = ArrayList<String>()
        currency.add("Rupiah (Rp)")
        currency.add("USD ($)")

        val dataAdapter = ArrayAdapter(context as Context,android.R.layout.simple_spinner_item,currency)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCurrency.adapter = dataAdapter
    }

    private fun setRedeemVoucher(){
        linerVoucher.setOnClickListener{
            showInfoMessage(getString(R.string.fitur_belum_ready))
            //  startActivity(Intent(context, RedeemVoucherActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    private fun setPurchasePremium(){
        linerPurchase.setOnClickListener{
            startActivity(Intent(context, PurchasePremiumrActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    private fun setHelpView() {
        linerHelp.setOnClickListener {
            startActivity(Intent(context, WebviewActivity::class.java)
                    .putExtra("url","https://doain.ai/#/bantuan")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        linerTermService.setOnClickListener {
            startActivity(Intent(context, WebviewActivity::class.java)
                    .putExtra("url","https://doain.ai/#/ketentuan-layanan")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        linerPrivacyPolicy.setOnClickListener {
            startActivity(Intent(context, WebviewActivity::class.java)
                    .putExtra("url","https://doain.ai/#/kebijakan-privasi")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    private fun launchMedsos() {
        likeFacebook.setOnClickListener {
            val uri = Uri.parse("https://web.facebook.com/doainajadulu/" )
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context as Context, " unable to find facebook", Toast.LENGTH_LONG).show()
            }
        }

        likeInstagram.setOnClickListener {
            val uri = Uri.parse("https://www.instagram.com/doaindulu/" )
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context as Context, " unable to find instagram", Toast.LENGTH_LONG).show()
            }
        }

        likeLinkedIn.setOnClickListener {
            val uri = Uri.parse("https://www.linkedin.com/company/14636177" )
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context as Context, " unable to find linkedin", Toast.LENGTH_LONG).show()
            }
        }

        likeYoutube.setOnClickListener {
            val uri = Uri.parse("https://www.youtube.com/channel/UCjnvotn5C5-fL0aBWUqyn4g?view_as=subscriber" )
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context as Context, " unable to find youtube", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun launchMarket() {
        linearRate.setOnClickListener {
            val uri = Uri.parse("market://details?id=${context?.packageName}" )
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context as Context, " unable to find market app", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setLogout() {
        linearLogout.setOnClickListener {
            prefs?.edit()?.remove("loginToken")?.apply()
            prefs?.edit()?.remove("login")?.apply()
            prefs?.edit()?.putBoolean("login",false)?.apply()
            activity?.finish()
            Hawk.deleteAll()
            startActivity(Intent(context as Context, DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            activity?.finish()
        }
    }
}