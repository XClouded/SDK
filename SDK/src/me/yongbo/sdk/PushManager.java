package me.yongbo.sdk;
import java.util.List;

import me.yongbo.sdk.modle.App;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushManager {
	public static final long PUSH_DELAY_TIME = 2 * 1000 * 3600; //����push��ʱ������������10s��
	public static final long BEFORE_PUSH_DELAY_TIME = 30 * 1000;//��һ��pushǰ���ӳٵ�ʱ�䣨������5s��
	private static final int MAX_PUSH_SIZE = 2;
	public List<App> localData = null;
	
	private static PushManager pm = null;
	private Context mContext;
	private NotificationManager mNotificationManager;
	private AlarmManager alarmManager;
	private Notification notification;
	private PendingIntent pIntent;
	private PendingIntent alermPendingIntent;
	public PushManager(Context mContext) {
		this.mContext = mContext;
		this.mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        this.alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, PushReceiver.class);  
        alermPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
	}
	public static PushManager init(Context mContext){
		if(pm == null){
			pm = new PushManager(mContext);
		}
		return pm;
	}
	public static PushManager getInstance(){
		return pm;
	}
	public void push() {
		if(alermPendingIntent != null){
			alarmManager.cancel(alermPendingIntent);
		}
		setTimeout(PushManager.BEFORE_PUSH_DELAY_TIME);
	}
	public void setTimeout(long time){
		Log.d("TAG", "�����¶�ʱ...�㲥�ڶ�ʱ��������...");
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, alermPendingIntent);
	}
	public void showPush() {
		if(localData.size() == 0) {
			localData = null;
			Log.d("TAG", "ľ�������ˣ�����push����");
			return;
		}
		App app = localData.get(0);
        Intent intent = new Intent(mContext, SDKWebView.class);
        intent.putExtra("Url", app.getAppDetailUrl());
        pIntent = PendingIntent.getActivity(mContext, app.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        notification = new Notification(android.R.drawable.ic_menu_share, app.getName(), System.currentTimeMillis());
        notification.setLatestEventInfo(mContext, app.getName(), app.getShortDesc(), pIntent); 
        //��֪ͨ����ʾ
        Log.d("TAG", "֪ͨ����ʾ����");
        mNotificationManager.notify(app.getId(), notification);
        localData.remove(0);
        
        Log.d("TAG", "push���...");
        setTimeout(PUSH_DELAY_TIME);
	}
	public List<App> getLocalData() {
		return localData;
	}
	public PushManager setLocalData(List<App> localData) {
		this.localData = localData;
		return this;
	}
	
}
