package me.yongbo.sdk;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ApkDownloadManager {

	private static final int DOWN_NOSDCARD = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
	
	private static ApkDownloadManager downloadManager;
	
	private Context mContext;
	//֪ͨ�Ի���
	private Dialog noticeDialog;
	//���ضԻ���
	private Dialog downloadDialog;
    //������
    private ProgressBar mProgress;
    //��ʾ������ֵ
    private TextView mProgressText;
    //��ѯ����
    private ProgressDialog mProDialog;
    //����ֵ
    private int progress;
    //�����߳�
    private Thread downLoadThread;
    //��ֹ���
    private boolean interceptFlag;
	//��ʾ��
	private String updateMsg = "";
	//���صİ�װ��url
	private String apkUrl = "";
	//���ذ�����·��
    private String savePath = "";
	//apk��������·��
	private String apkFilePath = "";
	//��ʱ�����ļ�·��
	private String tmpFilePath = "";
	//�����ļ���С
	private String apkFileSize;
	//�������ļ���С
	private String tmpFileSize;
	
	private List<String> urls = new ArrayList<String>();
	
	private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				Toast.makeText(mContext, "�޷����ذ�װ�ļ�������SD���Ƿ����", 3000).show();
				break;
			}
    	};
    };
	public ApkDownloadManager(Context context) {
		this.mContext = context;
	}

	public static ApkDownloadManager getInstance(Context context) {
		if(downloadManager == null){
			downloadManager = new ApkDownloadManager(context);
		}
		downloadManager.interceptFlag = false;
		return downloadManager;
	}
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				String apkName = "OSChinaApp_.apk";
				String tmpApk = "OSChinaApp_.tmp";
				//�ж��Ƿ������SD��
				if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//û�й���SD�����޷������ļ�
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}
				String dateNow = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
				savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + dateNow + "/";
				File file = new File(savePath);
				//�����ļ����Ƿ����
				if(!file.exists()){
					file.mkdirs();
				}
				apkFilePath = savePath + apkName;
				tmpFilePath = savePath + tmpApk;
				File ApkFile = new File(apkFilePath);
				
				//�Ƿ������ظ����ļ�
				if(ApkFile.exists()){
					downloadDialog.dismiss();
					installApk();
					return;
				}
				
				//�����ʱ�����ļ�
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);
				
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				
				//��ʾ�ļ���С��ʽ��2��С������ʾ
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	//������������ʾ�����ļ���С
		    	apkFileSize = df.format((float) length / 1024 / 1024) + "MB";
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do {   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    		//������������ʾ�ĵ�ǰ�����ļ���С
		    		tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
		    		//��ǰ����ֵ
		    	    progress =(int)(((float)count / length) * 100);
		    	    //���½���
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){	
		    			//������� - ����ʱ�����ļ�ת��APK�ļ�
						if(tmpFile.renameTo(ApkFile)){
							//֪ͨ��װ
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	} while(!interceptFlag);//���ȡ����ֹͣ����
				
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
			
		}
	};
	/**
	 * ��ʾ���ضԻ���
	 */
	private void showDownloadDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("��������...");
		
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
		
		builder.setView(v);
		builder.setNegativeButton("ȡ��", new OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();
	}
	
	/**
	* ����apk
	* @param url
	*/	
	public void doDown(String url) {
		this.apkUrl = url;
		showDownloadDialog();
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	/**
    * ��װapk
    * @param url
    */
	private void installApk(){
		File apkfile = new File(apkFilePath);
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        mContext.startActivity(i);
	}
}
