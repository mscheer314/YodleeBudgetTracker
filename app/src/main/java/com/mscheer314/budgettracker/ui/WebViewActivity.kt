package com.mscheer314.budgettracker.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.android.databinding.ActivityWebViewBinding
import com.mscheer314.budgettracker.api.LoginInfo
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        val token = intent.getStringExtra("token")

        binding.webview.webViewClient = webClient

        //Allows debugging WebView in Chrome
        //To debug the WebView, open Chrome and type the 'chrome://inspect' url in the browser.
        WebView.setWebContentsDebuggingEnabled(true)
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.addJavascriptInterface(JSInterfaceHandler(this), LoginInfo.FASTLINK_WEBVIEW_HANDLER_NAME)
        try {
            val postData = "accessToken=Bearer " + token + "&extraParams=" + URLEncoder.encode("configName=aggregation", "UTF-8")

            binding.webview.postUrl(LoginInfo.FASTLINK_URL, postData.toByteArray())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    private var webClient: WebViewClient = object : WebViewClient() {
        //To ByPass the SSL certificate error you can enable the following code.
        //This should not be enabled in the production. Production will have the valid certificate.
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            val bypassSSLError = true
            if (bypassSSLError) {
                // Ignore SSL certificate errors
                handler.proceed()
            }
        }
    }
}

internal class JSInterfaceHandler(var context: Context) {
    var eventsInfo = ArrayList<String>()

    @JavascriptInterface
    fun postMessage(data: String) {
        Log.d("FL:MESSAGE", data)
        eventsInfo.add(data)
        try {
            val userData = JSONObject(data)
            val type = userData.getString("type")
            val fastlinkdata = userData.getJSONObject("data")
            if (type == "POST_MESSAGE") {
                if (fastlinkdata.has("action") && fastlinkdata.getString("action") == "exit") {
                   val intent = Intent(context.applicationContext, BudgetOverview::class.java)
                   intent.putExtra("eventsInfo", eventsInfo)
                   context.startActivity(intent)
                }
            }
            if (type == "OPEN_EXTERNAL_URL") {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fastlinkdata.getString("url")))
                context.startActivity(intent)
            }
        } catch (e: JSONException) {
            Log.d("FL:ERROR", e.message)
        }
    }
}