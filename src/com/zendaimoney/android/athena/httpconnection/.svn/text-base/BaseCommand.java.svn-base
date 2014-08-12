package com.zendaimoney.android.athena.httpconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

public abstract class BaseCommand {

	protected static final String DEFAULT_BACKEND_URL = "https://192.16.220.190:7070/hera/";

	private static final String TAG = "BaseCommand";

	// The timeout in milliseconds until a connection is established.
	private static final int CONNECTION_TIMOUT = 15*1000;

	// The default socket timeout (SO_TIMEOUT)
	// in milliseconds which is the timeout for waiting for data.
	private static final int SO_TIMEOUT = 15*1000;
	
	private CookieStore mCookieStore;
	private String mBackEndServerURL;
	private boolean mIsGetMethod;
		

	protected abstract String getComand();
	protected abstract List<NameValuePair> getParameters();
	protected abstract BaseResponse parseResponse(String content);

	public BaseCommand() {
		mCookieStore = null;
		mIsGetMethod = false;
		mBackEndServerURL = DEFAULT_BACKEND_URL;
	}
	
	public void setCookieStore(CookieStore cookieStore){
		mCookieStore = cookieStore;
	}
	
	protected void setServerUrl(String url){
		mBackEndServerURL = url;
	}
	
	protected void setGetMethod(){
		mIsGetMethod = true;
	}

	public BaseResponse execute() throws IOException {
		return openHttpConnection(mBackEndServerURL + getComand());		
	}

	private BaseResponse openHttpConnection(String url) throws IOException {

		DefaultHttpClient client = new DefaultHttpClient();
		
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMOUT);
		HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
		client.setParams(params);
		
		HttpResponse httpResp;
		
		if(mIsGetMethod){
			
			List<NameValuePair> nvps = getParameters();
			if(nvps != null){
				for( NameValuePair p : nvps){
					Log.d(TAG, p.getName() + "=" + p.getValue());
					url = url +  "&" + p.getName() + "=" + URLEncoder.encode(p.getValue());
				}
			}
			
			Log.d(TAG, "connect to: " + url);
			
			// http get
			HttpGet method = new HttpGet(url);
			
			if(mCookieStore != null){
				HttpContext localContext = new BasicHttpContext();
				localContext.setAttribute(ClientContext.COOKIE_STORE , mCookieStore);
				httpResp = client.execute(method, localContext);
			}
			else{
				httpResp = client.execute(method);
			}
			
		}
		else{
			
			Log.d(TAG, "connect to: " + url);
			
			// http post
			HttpPost method = new HttpPost(url);
			
			// attach parameters
			List<NameValuePair> nvps = getParameters();
			if(nvps != null){
				method.setEntity( new UrlEncodedFormEntity(nvps, HTTP.UTF_8) );
			}
					
			if(mCookieStore != null){
				HttpContext localContext = new BasicHttpContext();
				localContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
				httpResp = client.execute(method, localContext);
			}
			else{
				httpResp = client.execute(method);
			}
		}
						
		String response = handleResponse(httpResp);
		if(response == null){
			BaseResponse resp = new BaseResponse();
			resp.okey = false;
			resp.errno = httpResp.getStatusLine().getStatusCode();
			return resp;
		}
		
		Log.d(TAG, "response: " + response);
		
		BaseResponse baseRsp =  parseResponse(response);
		
		
		baseRsp.cookies = client.getCookieStore().getCookies();
		if(baseRsp.cookies.size() == 0){
			Log.d(TAG, "no cookie received");
		}
		else{
			for(Cookie c : baseRsp.cookies){
				Log.d(TAG, "cookie received - " + c.toString());
			}
		}

		return baseRsp;
	}

	private String handleResponse(HttpResponse response) throws IOException {

		StatusLine status = response.getStatusLine();

		if (status.getStatusCode() != HttpStatus.SC_OK) {
			Log.e(TAG,"http connection failed! status= " + status.getStatusCode());
			return null;
		}
		

		HttpEntity entity = response.getEntity();
		StringBuilder builder = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				entity.getContent()));

		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}

		reader.close();

		return builder.toString();
	}

}
