package com.zendaimoney.android.athena.parsejson;

import org.json.JSONException;
import org.json.JSONObject;

public class ValidateAccountParse {
	/**
	 * 返回的json文件中的参数名
	 */
	public final static String JSON_STATUS = "status";
	public final static String JSON_RESPDESC = "respDesc";
	
	/**
	 * 返回的结果
	 */
	public int status;	//登陆状态
	public String respDesc; 	//响应描述
	
	public void parseResponse(String content) {		
		try {
			JSONObject jsonObj = new JSONObject(content);
			
			this.status = jsonObj.getInt(JSON_STATUS);
			this.respDesc = jsonObj.getString(JSON_RESPDESC);	
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
