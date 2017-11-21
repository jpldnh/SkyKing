package com.sking.lib.res.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 谁说青春不能错 on 2016/6/22.
 */
public class SKRWebView extends WebView {
    public SKRWebView(Context context) {
        this(context, (AttributeSet)null);
    }

    public SKRWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.set();
    }

    public SKRWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.set();
    }

    private void set() {
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setPluginState(WebSettings.PluginState.ON);
        this.setWebViewClient(new SKRWebView.FrameWebViewClient());
        this.setWebChromeClient(new WebChromeClient());
    }

    private class FrameWebViewClient extends WebViewClient {
        private FrameWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                new URL(url);
                view.loadUrl(url);
                return true;
            } catch (MalformedURLException var4) {
                return false;
            }
        }
    }
}
