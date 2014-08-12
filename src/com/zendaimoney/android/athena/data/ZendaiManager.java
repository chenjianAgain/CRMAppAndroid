package com.zendaimoney.android.athena.data;

public class ZendaiManager {
	
	public ZendaiManager() {
		super();
		// TODO Auto-generated constructor stub
	}
	private int userid;
	private String userName,userPass,gesturepass;
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getGesturepass() {
		return gesturepass;
	}
	public void setGesturepass(String gesturepass) {
		this.gesturepass = gesturepass;
	}
	public ZendaiManager(int userid, String userName, String userPass,
			String gesturepass) {
		super();
		this.userid = userid;
		this.userName = userName;
		this.userPass = userPass;
		this.gesturepass = gesturepass;
	}
	
}
