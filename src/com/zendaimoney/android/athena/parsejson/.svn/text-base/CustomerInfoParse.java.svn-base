package com.zendaimoney.android.athena.parsejson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zendaimoney.android.athena.data.CustomerInfoData;

public class CustomerInfoParse {
	private static final String JSON_STATUS = "status";
	private static final String JSON_RESP = "respDesc";
	private static final String JSON_INFOS = "infos";
	private static final String JSON_CUSTOMID = "customerId";
	private static final String JSON_CUSTOMNAME = "customerName";
	private static final String JSON_IDNUM = "idNum";
	private static final String JSON_MOBILE = "mobile";
	private static final String JSON_CDHOPE = "cdHope";
	private static final String JSON_DT = "dt";
	private static final String JSON_TYPE = "customerType";
	private static final String JSON_COUNT = "count";
	
	public List<CustomerInfoData> infoLists;
	public int status;
	public String respDesc;
	public int count;
	
	public CustomerInfoParse(){
		infoLists = new ArrayList<CustomerInfoData>();
	}
	
	public void parseResponse(String content){
		try {
			JSONObject jsonMain = new JSONObject(content);
			
			this.status = jsonMain.getInt(JSON_STATUS);
			this.respDesc = jsonMain.getString(JSON_RESP);
			this.count = jsonMain.getInt(JSON_COUNT);
			
			JSONArray jsonArray = jsonMain.getJSONArray(JSON_INFOS);
			JSONObject jsonObj;
			CustomerInfoData simpleData;
			if(infoLists.size() > 0){
				infoLists.clear();
			}
			for(int i = 0; i < jsonArray.length(); i++){
				jsonObj = jsonArray.getJSONObject(i);
				simpleData = new CustomerInfoData();
				simpleData.customerId = jsonObj.getString(JSON_CUSTOMID);
				simpleData.customerName = jsonObj.getString(JSON_CUSTOMNAME);
				simpleData.cdHope = jsonObj.getString(JSON_CDHOPE);
				simpleData.idNum = jsonObj.getString(JSON_IDNUM);
				simpleData.mobile = jsonObj.getString(JSON_MOBILE);
				String dtStr = jsonObj.getString(JSON_DT);
				if(dtStr.length() > 10){
					simpleData.dt = dtStr.substring(0, 10);
				}else{
					simpleData.dt = dtStr;
				}
				simpleData.customerType = jsonObj.getString(JSON_TYPE);
				infoLists.add(simpleData);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
