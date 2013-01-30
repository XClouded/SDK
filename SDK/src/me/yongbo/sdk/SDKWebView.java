package me.yongbo.sdk;

import me.yongbo.sdk.modle.Download;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class SDKWebView extends Activity {

	private WebView detail;
	//private ImageView loading;
	private RelativeLayout fLayout;
	private LinearLayout.LayoutParams params;
	private RelativeLayout.LayoutParams params2;
	
	private ProgressBar loading;
	
	private String url = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		url = getIntent().getStringExtra("Url");
		
		fLayout = new RelativeLayout(this);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		
		params2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.CENTER_IN_PARENT);
		loading = new ProgressBar(this);
		loading.setLayoutParams(params2);
		detail = new WebView(this);
		detail.setBackgroundColor(Color.GRAY);
		detail.loadUrl(url);
		detail.getSettings().setJavaScriptEnabled(true);
		detail.setWebViewClient(new WebViewClient() {  
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {  
		    	view.loadUrl(url);  
		        return true; 
		    }  
		  
		    @Override  
		    public void onPageStarted(WebView view, String url, Bitmap favicon) {  
		        super.onPageStarted(view, url, favicon);
		        Log.d("TAG", "ÕýÔÚ¼ÓÔØ...");
		        loading.setVisibility(View.VISIBLE);
		    }
		  
		    @Override  
		    public void onPageFinished(WebView view, String url) {  
		        super.onPageFinished(view, url);  
		        loading.setVisibility(View.GONE);
		    }

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				detail.loadUrl("file:///android_asset/page/error.html");
			}
		    
		});
		
		detail.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				/*
				Uri uri = Uri.parse(url);  
	            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
	            startActivity(intent);*/
				//Download d = new Download(url);
				ApkDownloadManager.getInstance(SDKWebView.this).doDown(url);
				Log.d("TAG", url);
			}
		});
		fLayout.removeAllViews();
		fLayout.setLayoutParams(params);
		fLayout.addView(detail, params);
		fLayout.addView(loading);
		setContentView(fLayout, params);
	}
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		url = intent.getStringExtra("Url");
		detail.loadUrl(url);
	}
	
	
}

