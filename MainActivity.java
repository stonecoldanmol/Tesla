package com.example.tesla;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import javax.sql.ConnectionEventListener;

public class MainActivity extends AppCompatActivity
{

    WebView webView;
    private String webURL="https://www.tesla.com";
    ProgressBar progressBarWeb;
    ProgressDialog progressDialog;

    RelativeLayout relaiveLayout;
    Button btnNoInternetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window =getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);


        webView=findViewById(R.id.myWebView);

        progressBarWeb=findViewById(R.id.progressBar);

        btnNoInternetConnection=findViewById(R.id.btnNoConnection);
        relaiveLayout=findViewById(R.id.relativeLayout);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait...");


        webView.getSettings().setJavaScriptEnabled(true);

        checkConnections();




        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                progressBarWeb.setVisibility(View.VISIBLE);
                progressBarWeb.setProgress(newProgress);
                setTitle("Loading...");
                progressDialog.show();


                if (newProgress==100)
                {
                    progressBarWeb.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                    progressDialog.dismiss();
                }


                super.onProgressChanged(view, newProgress);
            }
        });


        btnNoInternetConnection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkConnections();
            }
        });



    }


    @Override
    public void onBackPressed() {

        if (webView.canGoBack())
        {
            webView.goBack();
        }
        else
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to Exit?")
                   .setNegativeButton("No",null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i)
                        {
                            finishAffinity();
                        }
                    }).show();
        }

    }

    public void checkConnections()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.isConnected())
        {
            webView.loadUrl(webURL);
            webView.setVisibility(View.VISIBLE);
            relaiveLayout.setVisibility(View.GONE);
        }
        else if(mobileNetwork.isConnected())
        {
            webView.loadUrl(webURL);
            webView.setVisibility(View.VISIBLE);
            relaiveLayout.setVisibility(View.GONE);
        }
        else
        {
            webView.setVisibility(View.GONE);
            relaiveLayout.setVisibility(View.VISIBLE);
        }

    }


}
