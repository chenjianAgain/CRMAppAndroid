package com.zendaimoney.android.athena.data;

public class SalaryRemindData {
	private String userName;
	private String overTime;
	private String time;
	private String category;
	private String money;
	
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
		if(time.length() > 10)
			this.time = time.substring(0, 10);
		else 
			this.time = time;
	}
	public String getTime(){
		return this.time;
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	public String getCategory(){
		return this.category;
	}
	
	public void setMoney(String money){
		this.money = money;
	}
	public String getMoney(){
		return this.money;
	}
}
