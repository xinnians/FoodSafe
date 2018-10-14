package com.ufistudio.ianlin.foodsafe.utils.views

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.google.firebase.perf.metrics.AddTrace
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.pages.base.InteractionView
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.pages.main.news.NewsFragment
import kotlinx.android.synthetic.main.fragment_webview.*

class WebViewFragment : InteractionView<OnPageInteractionListener.WebView>() {

    companion object {
        fun newInstance(): WebViewFragment = WebViewFragment()
        private val TAG = NewsFragment::class.simpleName

        const val URL = "com.ufistudio.ianlin.foodsafe.utils.views.url"
    }

    @AddTrace(name = "onCreateTrace", enabled = true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUrl()
    }

    private fun loadUrl() {

        webView.webChromeClient = object : VideoEnabledWebChromeClient(nonVideoView, hasVideoView) {
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url?.toString())
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        webView.settings.javaScriptEnabled = true
        webView.loadUrl(arguments?.getString(URL)?.toString())
    }
}