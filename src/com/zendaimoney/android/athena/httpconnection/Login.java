package com.zendaimoney.android.athena.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends BaseCommand {
	private static final String LOGIN_CMD = "manageraccount/login";
	/**
	 * 接口访问的参数
	 */
	private static final String PARAM_USER = "userName";
	private static final String PARAM_PASSWD = "password";
	
	/**
	 * 返回的json文本参数
	 */
	public static final String JSON_STATUS = "status";
	public static final String JSON_RESPDESC = "respDesc";
	public static final String JSON_ID = "id";
	
	private String mUser;
	private String mPasswd;
	
	public static class Response extends BaseResponse {

		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;
		public int status;	//登陆状态
		public String respDesc; 	//响应描述
		public String id;	//账号id
	}
		
	public Login() {
		super();
	}
	
	public void setUser(String user){
		mUser = user;
	}
	
	public void setPasswd(String passwd){
		mPasswd = passwd;
	}


	@Override
	protected List<NameValuePair> getParameters() {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		nvps.add( new BasicNameValuePair(PARAM_USER, mUser));
		nvps.add( new BasicNameValuePair(PARAM_PASSWD, mPasswd));
	
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		Response login = new Response();
		
		try {
			JSONObject jsonObj = new JSONObject(content);
			login.okey = true;
			
			login.status = jsonObj.getInt(JSON_STATUS);
			login.respDesc = jsonObj.getString(JSON_RESPDESC);
			login.id = jsonObj.getString(JSON_ID);		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return login;
	}

	@Override
	protected String getComand() {
		return LOGIN_CMD;
	}
}
