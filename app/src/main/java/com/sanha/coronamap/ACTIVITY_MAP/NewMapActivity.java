package com.sanha.coronamap.ACTIVITY_MAP;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sanha.coronamap.MODULES.IDManger;
import com.sanha.coronamap.R;

public class NewMapActivity extends AppCompatActivity {

    String url = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = "https://terms.naver.com/entry.nhn?docId=5912275&cid=43667&categoryId=43667#TABLE_OF_CONTENT5";
        setContentView(R.layout.activity_new_map);
        IDManger.SetBannerAd(this,findViewById(R.id.newmap_adview));
        WebView wb = (WebView) findViewById(R.id.newmap_webview);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.loadUrl(url);
        wb.setWebChromeClient(new WebChromeClient());
        wb.setWebViewClient(new WebViewClientClass());

    }
    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
