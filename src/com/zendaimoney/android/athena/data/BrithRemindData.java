package com.zendaimoney.android.athena.data;

public class BrithRemindData {
	private String userName;
	private String overTime;
	private String time;
	private String id;
	
	public void setUserName(String username){
		this.userName = username;
	}
	public String getUserName(){
		return this.userName;
	}
	
	public void setOverTime(String overtime){
		this.overTime = overtime;
	}
	public String getOverTime(){
		return this.overTime;
	}
	
	public void setTime(String time){
		this.time = time;
	}
	public String getTime(){
		return this.time;
	}
	
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return this.id;
	}
}
