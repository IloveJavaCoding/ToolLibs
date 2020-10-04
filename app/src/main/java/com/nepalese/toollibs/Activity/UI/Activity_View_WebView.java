package com.nepalese.toollibs.Activity.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.nepalese.toollibs.Activity.Config.Constant;
import com.nepalese.toollibs.Activity.Config.SettingData;
import com.nepalese.toollibs.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_View_WebView extends AppCompatActivity {
    private WebView webView;
    private AutoCompleteTextView input;
    private Button bSearch;
    private List<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__view__web_view);

        getData();;
        init();
        setData();
        setListener();
    }

    private void getData() {
        list = new ArrayList<>();
        String cache = SettingData.getUrl(this);
        if(!cache.equals("")){
            list.add(cache);
        }
    }

    private void init() {
        input = findViewById(R.id.atvInput);
        webView = findViewById(R.id.webView);
        bSearch = findViewById(R.id.bSearch);
    }

    private void setData() {
        input.setAdapter(new AutoAdapter(this, list));
        input.setThreshold(2);
    }

    private void setListener() {
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = input.getText().toString();
                url = url.toLowerCase();
                if(url.equals("")){
                    url=Constant.URL_DEFAULT;
                }

                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new MyChromeClient());
                webView.setWebViewClient(new MyViewClient());
                webView.loadUrl(url);
                SettingData.saveUrl(getApplicationContext(), url);
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
            //etInput.setText(title);
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

    class AutoAdapter extends BaseAdapter implements Filterable{
        Context context;
        LayoutInflater inflater;
        List<String> list;
        List<String> data;

        public AutoAdapter(Context context, List<String> list){
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.list = list;
        }
        @Override
        public int getCount() {
            return list==null? 0:list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.layout_auto_text,null);
            TextView tvAuto = view.findViewById(R.id.tvText);
            ImageView ivClose = view.findViewById(R.id.imgDel);
            tvAuto.setText(list.get(position));
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
            return view;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    List<String> aList = new ArrayList<>();
                    if(constraint != null){
                        for (String s: list) {
                            aList.add(s);
                        }
                    }
                    results.count = aList.size();
                    results.values = aList;
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    data = (List<String>) results.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }
    }
}
