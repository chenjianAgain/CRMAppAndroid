package com.zendaimoney.android.athena.parsejson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginParse{
	/**
	 * 返回的json文本参数
	 */
	public static final String JSON_STATUS = "status";
	public static final String JSON_RESPDESC = "respDesc";
	public static final String JSON_ID = "id";
	
	public static int ISSUC_FAILED = 0;
	public static int ISSUC_SUCC = 1;
	/**
	 * 返回结果
	 */
	public int status;	//登陆状态
	public String respDesc; 	//响应描述
	public String id;	//账号id

	public void parseResponse(String content) {	
		try {
			JSONObject jsonObj = new JSONObject(content);
			
			this.status = jsonObj.getInt(JSON_STATUS);
			this.respDesc = jsonObj.getString(JSON_RESPDESC);
			this.id = jsonObj.getString(JSON_ID);		
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
