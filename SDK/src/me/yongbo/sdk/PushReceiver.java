package me.yongbo.sdk;

import me.yongbo.sdk.service.PushService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver {
	PushManager pm = PushManager.getInstance();
	
	private Thread pushThread = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				pm.setLocalData(PushService.getPushs());
				Log.d("TAG", "push获取成功...共" + pm.getLocalData().size() + "条数据");
				pm.showPush();
			} catch (Exception e) {
				Log.d("TAG", "网络错误，获取push失败");
				pm.setTimeout(PushManager.PUSH_DELAY_TIME);
			}
		}
	});
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("TAG", "收到广播...正在处理...");
		if(!isNetworkOK(context)) {
			Log.d("TAG", "网络未开启...push获取失败");
			pm.setTimeout(PushManager.PUSH_DELAY_TIME);
			return;
		}
		if(pm.getLocalData() == null) {
			Log.d("TAG", "正在从服务器获取push");
			pushThread.start();
    	} else {
    		Log.d("TAG", "正在从本地获取push");
    		pm.showPush();
    	}
	}

	private boolean isNetworkOK(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
}
