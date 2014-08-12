package com.zendaimoney.android.athena.parsejson;

import org.json.JSONException;
import org.json.JSONObject;

import com.zendaimoney.android.athena.AppLog;

public class CustomerNumParse {
	public static final String JSON_STATUS = "status";
	public static final String JSON_RESPDESC = "respDesc";
	public static final String JSON_TOTALNUM = "count";
	
	public int count;
	public int status;
	public String respDesc;
	public void parseResponse(String content) {	
		try {
			JSONObject jsonObj = new JSONObject(content);
			
			this.status = jsonObj.getInt(JSON_STATUS);
			this.respDesc = jsonObj.getString(JSON_RESPDESC);
			this.count = jsonObj.getInt(JSON_TOTALNUM);	
			AppLog.i("TAG", "parse count:" + this.count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
