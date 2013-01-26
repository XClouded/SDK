package me.yongbo.sdk.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpService {
	public final static String HOST = "http://27.54.218.155";
	public static String getPushs() throws Exception {
		String strURL = HOST + "/iapk/getPushs/1";  
	    URL url = new URL(strURL);  
	    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
	    InputStreamReader input = new InputStreamReader(httpConn.getInputStream());  
	    BufferedReader bufReader = new BufferedReader(input);  
	    String line = null;  
	    StringBuilder contentBuf = new StringBuilder();  
	    while ((line = bufReader.readLine()) != null) {  
	        contentBuf.append(line);  
	    }
	    return contentBuf.toString();
	}
}
