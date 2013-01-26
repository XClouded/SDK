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
				Log.d("TAG", "push��ȡ�ɹ�...��" + pm.getLocalData().size() + "������");
				pm.showPush();
			} catch (Exception e) {
				Log.d("TAG", "������󣬻�ȡpushʧ��");
				pm.setTimeout(PushManager.PUSH_DELAY_TIME);
			}
		}
	});
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("TAG", "�յ��㲥...���ڴ���...");
		if(!isNetworkOK(context)) {
			Log.d("TAG", "����δ����...push��ȡʧ��");
			pm.setTimeout(PushManager.PUSH_DELAY_TIME);
			return;
		}
		if(pm.getLocalData() == null) {
			Log.d("TAG", "���ڴӷ�������ȡpush");
			pushThread.start();
    	} else {
    		Log.d("TAG", "���ڴӱ��ػ�ȡpush");
    		pm.showPush();
    	}
	}

	private boolean isNetworkOK(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
}
