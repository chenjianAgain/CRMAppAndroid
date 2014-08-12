package com.zendaimoney.android.athena.parsejson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zendaimoney.android.athena.data.SalaryRemindData;

public class SalaryListParse {
	private static final String JSON_STATUS = "status";
	private static final String JSON_RESP = "respDesc";
	private static final String JSON_INFOS = "infos";
	private static final String JSON_NAME = "customerName";
	private static final String JSON_PARTTERN = "pattern";
	private static final String JSON_INVESTAMT = "investAmt";
	private static final String JSON_ENDDATE = "endDate";
	
	public List<SalaryRemindData> remindLists;
	public int status;
	public String respDesc;
	
	public SalaryListParse(){
		remindLists = new ArrayList<SalaryRemindData>();
	}
	
	public void parseResponse(String content){
		try {
			JSONObject jsonMain = new JSONObject(content);
			
			this.status = jsonMain.getInt(JSON_STATUS);
			this.respDesc = jsonMain.getString(JSON_RESP);	
			
			JSONArray jsonArray = jsonMain.getJSONArray(JSON_INFOS);
			JSONObject jsonObj;
			SalaryRemindData simpleData;
			if(remindLists.size() > 0){
				remindLists.clear();
			}
			for(int i = 0; i < jsonArray.length(); i++){
				jsonObj = jsonArray.getJSONObject(i);
				simpleData = new SalaryRemindData();
				simpleData.setUserName(jsonObj.getString(JSON_NAME));
				simpleData.setCategory(jsonObj.getString(JSON_PARTTERN));
				simpleData.setMoney(jsonObj.getString(JSON_INVESTAMT));
				simpleData.setTime(jsonObj.getString(JSON_ENDDATE));
				simpleData.setOverTime("到期时间");
				remindLists.add(simpleData);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
