package com.falcofemoralis.hdrezkaapp.clients

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.falcofemoralis.hdrezkaapp.interfaces.IConnection
import com.falcofemoralis.hdrezkaapp.objects.Film
import android.webkit.ValueCallback
import com.falcofemoralis.hdrezkaapp.objects.SettingsData
import com.falcofemoralis.hdrezkaapp.utils.FileManager
import com.google.android.exoplayer2.metadata.icy.IcyHeaders


class PlayerWebViewClient(val context: Context, val mainView: IConnection, val film: Film, val callback: () -> Unit) : WebViewClient() {
    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        if (!checkUrl(request.url.toString())) {
            view.loadUrl(request.url.toString())
        }
        val map = HashMap<String, String>()
        map[SettingsData.APP_HEADER,] = IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE
        view.loadUrl(request.url.toString(), map)
        return true
    }

    // Для старых устройств
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (!checkUrl(url)) {
            view.loadUrl(url)
        }
        view.loadUrl(url, mapOf(Pair(SettingsData.APP_HEADER, IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE)))
        return true
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        /*      if (error?.errorCode == ERROR_TIMEOUT) {
                  mainView.showConnectionError(IConnection.ErrorType.TIMEOUT, error.toString())
              }*/
        super.onReceivedError(view, request, error)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        // block advert
        view?.evaluateJavascript(FileManager.readFromAsset("advert.js", context), null)
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        view?.evaluateJavascript(FileManager.readFromAsset("script.js", context), null)

        if (film.autoswitch != null && film.autoswitch!!.isNotEmpty()) {
            view?.evaluateJavascript(film.autoswitch!!, null)
        }

        callback()
        super.onPageFinished(view, url)
    }

    private fun checkUrl(url: String): Boolean {
        return if (url == "https://t.me/hdrezka") {
            val linkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(linkIntent)
            true
        } else {
            false
        }
    }
}