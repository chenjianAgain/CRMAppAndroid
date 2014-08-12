package com.zendaimoney.android.athena;

import java.util.LinkedList;
import java.util.List;


import android.app.Activity;

public class ExitApplication{
	private List<Activity> activityList = new LinkedList();
	private AppLog appLog = new AppLog();
	private static ExitApplication instance;
	
	private ExitApplication() {
	}

	// 单例模式中获取唯一的ExitApplication实例
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;

	}

	public AppLog getAppLog(){
		if(appLog == null){
			appLog = new AppLog();
		}
		return appLog;
	}
	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish

	public void exit() {

		for (Activity activity : activityList) {
			activity.finish();
		}

		System.exit(0);

	}
	
	public void exitInError(){
		for (Activity activity : activityList) {
			activity.finish();
		}
		
//		Intent i = new Intent(ExitApplication.this, ScannerActivity.class);
//		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(i);
//		
//		System.exit(0);
	}
}
