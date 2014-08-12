package com.zendaimoney.android.athena.parsejson;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmParse {
	/**
	 * 返回的json文本参数
	 */
	public static final String JSON_STATUS = "status";
	public static final String JSON_RESPDESC = "respDesc";
	
	public int status;
	public String respDesc;
	
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
