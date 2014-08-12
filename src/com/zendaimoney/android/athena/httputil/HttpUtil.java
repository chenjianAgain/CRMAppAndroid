package com.zendaimoney.android.athena.httputil;

import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.R;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration.Protocol;
//import android.util.Log;
import android.widget.Toast;


public class HttpUtil {
	
	private static String TAG = "HttpUtil";
	private static Context mcontext;
	public static final String BaseUrl = "https://192.16.220.190:8443/hera/";    // https://192.16.220.190:8443/hera/      http://192.16.220.190:7070/hera/
	
	private static HttpClient mHttpClient;
	
	

	/**
	 * 通过URL和参数进行HTTP请求
	 * @param interfaceUrl
	 * @param params
	 * @return
	 */
	public static String getContent(Context context, String interfaceUrl, JSONObject jsonparam) {   //Map<String, String> params  JSONObject param
		
		if(interfaceUrl == null){
			return "";
		}
		
		mcontext = context;
		
		String BaseUrl = null;
		Properties props = new Properties();  
		try {  
			InputStream in = context.getResources().openRawResource(R.raw.prometheus);  //new FileInputStream(getSettingFile());  
			props.load(in);  
			BaseUrl= props.getProperty("BaseUrl");
			AppLog.i(TAG, "---------- getProperty= "+BaseUrl);
			} catch (Exception e1) {   // TODO Auto-generated catch block   e1.printStackTrace();  }
		}
		interfaceUrl= BaseUrl + interfaceUrl;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("json", jsonparam.toString()); //
		
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(); //参数
		parameters.add(new BasicNameValuePair("par", "request-post"));
		if (null != params) {
			//添加参数
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
				
		mHttpClient = CustomerHttpClient.getHttpClient(mcontext);
		enableSSL(mHttpClient);
		
		try {

			HttpPost request = new HttpPost(interfaceUrl);

			HttpEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");		//
			AppLog.i(TAG, "------params="+parameters.toString());
			
			request.setEntity(entity);
			
			
			HttpResponse response =mHttpClient .execute(request);
			
//			Log.i(TAG, "-------------interfaceUrl = "+interfaceUrl+"------------------getStatusCode ="+response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());
//				Log.i(TAG, "---------------  result= "+result);
				return result;
			}
		}catch(SocketTimeoutException e){
//			Log.d(TAG, "=============e="+e);
//			Log.d(TAG, "读取数据失败1" + interfaceUrl);
			showTimeOut();
		}
		catch(ConnectTimeoutException e){
//			Log.d(TAG, "=============e="+e);
//			Log.d(TAG, "读取数据失败2" + interfaceUrl);
			showTimeOut();
		}
		catch(SSLHandshakeException e)	{
//			Log.d(TAG, "=============e="+e);
//			Log.d(TAG, "读取数据失败3" + interfaceUrl);
			showServiceErro();
		}
		catch(HttpHostConnectException e){
//			Log.d(TAG, "=============e="+e);
//			Log.d(TAG, "读取数据失败4" + interfaceUrl);
			showServiceErro();
		}
		catch (Exception e) {
//			Log.d(TAG, "=============e="+e);
//			Log.d(TAG, "读取数据失败5" + interfaceUrl);
			showNetErro();			
		}
		return "";
	}
	
	private static void showTimeOut(){
		if(Constants.ifCancelProgress <= 0){
			((Activity) mcontext).runOnUiThread(new Runnable() {
				public void run() {
//					Toast.makeText(mcontext, "服务提交超时，请再次提交", 0).show();
				}
			});
		}
	}
	private static void showNetErro(){
		if(Constants.ifCancelProgress <= 0){
		((Activity) mcontext).runOnUiThread(new Runnable() {
			public void run() {
//				Toast.makeText(mcontext, "网络故障，请确认网络已经打开或者稍后再试", 0).show();
			}
		});
		}
	}
	private static void showServiceErro(){
		if(Constants.ifCancelProgress <= 0){
		((Activity) mcontext).runOnUiThread(new Runnable() {
			public void run() {
//				Toast.makeText(mcontext, "服务故障，请拨打电话：400-821-6888", 0).show();
			}
		});
		}
	}
	
	private static void enableSSL(HttpClient httpclient) {
		
		try {

			Scheme sche = new Scheme("https",new EasySSLSocketFactory(mcontext), 8443);
			httpclient.getConnectionManager().getSchemeRegistry().register(sche);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
	}
}