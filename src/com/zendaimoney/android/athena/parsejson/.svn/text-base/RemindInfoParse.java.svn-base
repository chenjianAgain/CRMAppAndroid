package com.zendaimoney.android.athena.parsejson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RemindInfoParse {
	private static final String JSON_STATUS = "status";
	private static final String JSON_RESP = "respDesc";
	private static final String JSON_INFOS = "infos";
	private static final String JSON_INFOTYPE = "infoType";
	private static final String JSON_COUNT = "count";
	
	public int status;
	public String respDesc;
	public int salaryRemind;
	public int brithRemind;
	
	public RemindInfoParse(){
		salaryRemind = 0;
		brithRemind = 0;
	}
	
	public void parseResponse(String content){
		try {
			JSONObject jsonMain = new JSONObject(content);
			
			this.status = jsonMain.getInt(JSON_STATUS);
			this.respDesc = jsonMain.getString(JSON_RESP);	
			
			JSONArray jsonArray = jsonMain.getJSONArray(JSON_INFOS);
			JSONObject jsonObj;
			
			for(int i = 0; i < jsonArray.length(); i++){
				jsonObj = jsonArray.getJSONObject(i);
				if(jsonObj.getInt(JSON_INFOTYPE) == 1){
					this.salaryRemind = jsonObj.getInt(JSON_COUNT);
				}else if(jsonObj.getInt(JSON_INFOTYPE) == 2){
					this.brithRemind = jsonObj.getInt(JSON_COUNT);
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
