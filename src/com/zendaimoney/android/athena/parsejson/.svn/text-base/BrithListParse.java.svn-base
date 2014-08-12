package com.zendaimoney.android.athena.parsejson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zendaimoney.android.athena.data.BrithRemindData;

public class BrithListParse {
	private static final String JSON_STATUS = "status";
	private static final String JSON_RESP = "respDesc";
	private static final String JSON_INFOS = "infos";
	private static final String JSON_CUSTOMID = "customerId";
	private static final String JSON_NAME = "customerName";
	private static final String JSON_DATE = "dataOfBirth";
	
	public List<BrithRemindData> remindLists;
	public int status;
	public String respDesc;
	
	public BrithListParse(){
		remindLists = new ArrayList<BrithRemindData>();
	}
	
	public void parseResponse(String content){
		try {
			JSONObject jsonMain = new JSONObject(content);
			
			this.status = jsonMain.getInt(JSON_STATUS);
			this.respDesc = jsonMain.getString(JSON_RESP);	
			
			JSONArray jsonArray = jsonMain.getJSONArray(JSON_INFOS);
			JSONObject jsonObj;
			BrithRemindData simpleData;
			if(remindLists.size() > 0){
				remindLists.clear();
			}
			for(int i = 0; i < jsonArray.length(); i++){
				jsonObj = jsonArray.getJSONObject(i);
				simpleData = new BrithRemindData();
				simpleData.setUserName(jsonObj.getString(JSON_NAME));
				simpleData.setId(jsonObj.getString(JSON_CUSTOMID));
				simpleData.setTime(jsonObj.getString(JSON_DATE));
				simpleData.setOverTime("出生日期");
				remindLists.add(simpleData);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
