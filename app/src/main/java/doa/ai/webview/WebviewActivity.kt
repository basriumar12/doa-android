package doa.ai.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import doa.ai.R
import kotlinx.android.synthetic.main.activity_webview.*

class WebviewActivity : AppCompatActivity() {

    var url: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        setSupportActionBar(toolbarWebview)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        url = intent.getStringExtra("url")
        Log.e("tag"," url bro ini$url")

        webviewID.webViewClient = WebViewClient()
        webviewID.settings.javaScriptEnabled = true
        webviewID.settings.builtInZoomControls = false
        webviewID.settings.setSupportZoom(false)
        webviewID.settings.javaScriptCanOpenWindowsAutomatically = true
        webviewID.settings.allowFileAccess = true
        webviewID.settings.domStorageEnabled = true
        webviewID.canGoBack()
        webviewID.goBack()
        webviewID.loadUrl(url)
        webviewID.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                if (webviewProgress != null){
                    webviewProgress.visibility = View.GONE
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home ->{
                finish()
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }
}
