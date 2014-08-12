package com.zendaimoney.android.athena.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class NetworkTool {
	public static VersionInfo getContent(String url) throws Exception{
		VersionInfo versionInfo = new VersionInfo();	//保存解析到的版本信息
	    StringBuilder sb = new StringBuilder();
	    HttpClient client = new DefaultHttpClient();
	    HttpParams httpParams = client.getParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
	    HttpConnectionParams.setSoTimeout(httpParams, 5000);
	    HttpResponse response = client.execute(new HttpGet(url));
	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);
	        
	        String line = null;
	        StringBuilder functionStr = new StringBuilder();
	        StringBuilder verName = new StringBuilder();
	        boolean functionFlag = false;
	        while ((line = reader.readLine())!= null){
	        	if(line.contains("Changes")){
	        		//进入版本名称行
	        		verName.append(line.substring(line.indexOf("version") + 8, line.indexOf("(") - 1));
	        		if(versionInfo.getVerName() == ""){
	        			versionInfo.setVerName(verName.toString());
	        		}
	        		
	        		if(line.contains("force")){
	        			if(!versionInfo.force){
	        				versionInfo.force = true;
	        			}
	        		}
	        	}else if(line.contains("---------")){
	        		//开始解析到新增功能行
	        		functionFlag = !functionFlag;
	        		functionStr.append(line + "\n");
	        	}else if(functionFlag){
	        		functionStr.append(line + "\n");
	        	}
	            sb.append(line + "\n");            
	        }
	        
	        versionInfo.setUpdateFunction(functionStr.toString());
	        reader.close();
	    }
	    
//	    Log.v("res", "resStr:" + sb.toString());
//	    Log.v("verName", "verName:" + versionInfo.getVerName());
//	    Log.v("updateFunc", "updateFunc:" + versionInfo.getUpdateFunction());
//	    return sb.toString();
	    return versionInfo;
	}
}
