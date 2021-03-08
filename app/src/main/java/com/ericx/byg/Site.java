package com.ericx.byg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Site extends AppCompatActivity {

    Toolbar siteToolbar;
    WebView webView;
    ImageView errorImg;
    TextView errorTxt;
    Button tryButton;
    ProgressBar progressBar;
    SwipeRefreshLayout refresh;
    private boolean networkConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        siteToolbar = findViewById(R.id.siteToolbar);
        webView = findViewById(R.id.webView);
        errorImg = findViewById(R.id.notConnectedImage);
        errorTxt = findViewById(R.id.notConnectedText);
        tryButton = findViewById(R.id.tryAgain);
        progressBar = findViewById(R.id.progress);
        refresh = findViewById(R.id.refresh);


        setSupportActionBar(siteToolbar);
        getSupportActionBar().setTitle("Blog");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Site.super.onBackPressed();
            }
        });


        checkNetwork();

        tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetwork();
            }
        });

        refresh.setColorSchemeColors(getResources().getColor(R.color.mainColor), getResources().getColor(R.color.mainColorOrange));



    }

    private void checkNetwork() {


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //you are connected to a network
            networkConnected = true;
        }
        else {
            networkConnected = false;
        }

        if (networkConnected == false){
            errorTxt.setVisibility(View.VISIBLE);
            errorImg.setVisibility(View.VISIBLE);
            tryButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        } else{
            errorTxt.setVisibility(View.GONE);
            errorImg.setVisibility(View.GONE);
            tryButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            proceed();
        }
    }

    private void proceed() {

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient( new webClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://beautifulyetgodly.com/");

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });



        //to allow scrolling in webview
        webView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (webView.getScrollY() == 0){
                    refresh.setEnabled(true);
                }
                else {
                    refresh.setEnabled(false);
                }
            }
        });



    }

    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
        }
    }

    public class webClient extends WebViewClient {
        public boolean  shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            refresh.setRefreshing(false);
        }
    }

    @Override

    public void onBackPressed (){
        if (webView.canGoBack()){
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }


    }

}
