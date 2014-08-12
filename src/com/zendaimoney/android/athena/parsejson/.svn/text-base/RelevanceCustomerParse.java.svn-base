package com.zendaimoney.android.athena.parsejson;

import org.json.JSONException;
import org.json.JSONObject;

public class RelevanceCustomerParse {
	/**
	 * json参数
	 */
	private final static String JSON_STATUS = "status";
	private final static String JSON_RESPDESC = "respDesc";
	/**
	 * 返回结果
	 */
	public int status;
	public String respDesc;
	
	public void parseResponse(String content){
		try {
			JSONObject jsonObj = new JSONObject(content);
			
			this.status = jsonObj.getInt(JSON_STATUS);
			this.respDesc = jsonObj.getString(JSON_RESPDESC);	
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
