package com.skingapp.commen.activitys;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRBaseResult;
import com.skingapp.commen.R;
import com.skingapp.commen.bases.BaseActivity;

public class WebViewActivity extends BaseActivity {

    private TextView titleText;
    private TextView titleLeft;
    private TextView titleRight;
    private String titleName;
    private String urlPath;
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.skr_layout_activity_webview);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getExras() {
        titleName = getIntent().getStringExtra("TITLE_NAME");
        urlPath = getIntent().getStringExtra("URL_PATH");
    }

    @Override
    protected void findView() {
        titleText = (TextView) findViewById(R.id.top_title_text);
        titleLeft = (TextView) findViewById(R.id.top_title_left);
        titleRight = (TextView) findViewById(R.id.top_title_right);
        webView = (WebView) findViewById(R.id.webview);
    }

    @Override
    protected void setListener() {
        titleText.setText(titleName);
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        titleRight.setVisibility(View.GONE);
        if (urlPath.contains("https://") || urlPath.contains("https://"))
            webView.loadUrl(urlPath);
        else
            webView.loadUrl(getApplicationContext().getSysInitInfo().getSys_web_service() + urlPath);
    }


    @Override
    protected void networkRequestBefore(SKRNetTask netTask) {

    }

    @Override
    protected void networkRequestAfter(SKRNetTask netTask) {

    }

    @Override
    protected void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult baseResult) {

    }

    @Override
    protected void networkRequestParseFailed(SKRNetTask netTask, SKRBaseResult baseResult) {

    }

    @Override
    protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {

    }
}
