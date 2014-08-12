package com.zendaimoney.android.athena.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class ValidateAccount extends BaseCommand{
	public final static String VALIDATE_CMD = "manageraccount/validateAccount";
	
	/**
	 * 验证身份的接口访问参数名
	 */
	public final static String PARAM_USER = "userName";
	public final static String PARAM_CODE = "dimeCode";
	/**
	 * 返回的json文件中的参数名
	 */
	public final static String JSON_STATUS = "status";
	public final static String JSON_RESPDESC = "respDesc";
	
	private String userName;
	private String dimeCode;
	
	public static class Response extends BaseResponse {

		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;
		public int status;	//登陆状态
		public String respDesc; 	//响应描述
	}
		
	public ValidateAccount() {
		super();
	}
	
	public void setUser(String user){
		userName = user;
	}
	
	public void setPasswd(String code){
		dimeCode = code;
	}


	@Override
	protected List<NameValuePair> getParameters() {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		nvps.add( new BasicNameValuePair(PARAM_USER, userName));
		nvps.add( new BasicNameValuePair(PARAM_CODE, dimeCode));
	
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		Response validateAccount = new Response();
		
		try {
			JSONObject jsonObj = new JSONObject(content);
			validateAccount.okey = true;
			
			validateAccount.status = jsonObj.getInt(JSON_STATUS);
			validateAccount.respDesc = jsonObj.getString(JSON_RESPDESC);	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return validateAccount;
	}

	@Override
	protected String getComand() {
		return VALIDATE_CMD;
	}
}
