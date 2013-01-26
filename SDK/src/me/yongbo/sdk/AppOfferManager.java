package me.yongbo.sdk;

import android.content.Context;
import android.content.Intent;

public class AppOfferManager {
	public final static String HOST = "http://27.54.218.155";
	private final static String WALL_PATH = "/iapk/offerList/1";
	private Context mContext;
	
	private static AppOfferManager aom = null;
	public AppOfferManager(Context context) {
		this.mContext = context;
	}
	public static AppOfferManager init(Context mContext){
		if(aom == null){
			aom = new AppOfferManager(mContext);
		}
		return aom;
	}
	public void showWall() {
		Intent intent = new Intent(mContext, me.yongbo.sdk.SDKWebView.class);
		intent.putExtra("Url", HOST + WALL_PATH);
		mContext.startActivity(intent);
	}
}
