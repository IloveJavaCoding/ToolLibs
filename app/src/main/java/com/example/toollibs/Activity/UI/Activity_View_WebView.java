package com.example.toollibs.Activity.UI;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.example.toollibs.R;

import java.security.Key;

public class Activity_View_WebView extends AppCompatActivity {
    private WebView webView;
    private EditText etInput;
    private Button bSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__view__web_view);

        init();
        setData();
        setListener();
    }

    private void init() {
        webView = findViewById(R.id.webView);
        etInput = findViewById(R.id.etInput);
        bSearch = findViewById(R.id.bSearch);
    }

    private void setData() {

    }

    private void setListener() {
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Url = etInput.getText().toString();
                Url = Url.toLowerCase();
                if(Url.equals("")){
                    Url="sohu";
                }

                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new MyChromeClient());
                webView.setWebViewClient(new MyViewClient());
                webView.loadUrl("https://www."+Url+".com");
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode== KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
         }
    }

    class MyChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            etInput.setText(title);
        }
    }

    //load only in local page
    class MyViewClient extends WebViewClient{
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.evaluateJavascript("javascript;alert('Hello')",null);
        }
    }
}
