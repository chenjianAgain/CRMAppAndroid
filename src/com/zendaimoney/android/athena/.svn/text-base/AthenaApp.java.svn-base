package com.zendaimoney.android.athena;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.OfflineMsgManager;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.model.LoginConfig;
import com.zendaimoney.android.athena.im.service.AlarmScheduler;
import com.zendaimoney.android.athena.im.service.ConnectListenerService;
import com.zendaimoney.android.athena.im.service.IMChatService;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AthenaApp extends Application{
	public static String TAG = "Application";
	private LoginConfig loginConfig;
	protected SharedPreferences preferences;
	
	public int connectTime = 0; //连接次数
	private int logintime = 1000;
	private Timer tExit;
	public static boolean connectRun = false;
	private String username;
	private String password;
	public static boolean isServiceStart = false;
	
	static {
		try {
			AppLog.i(TAG, "添加重连接管理包");
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
//			Connection.addConnectionListener(org.jivesoftware.smack.ConnectionListener);
//			Connection.removeConnectionListener(org.jivesoftware.smack.ConnectionListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public void onCreate() {
	    super.onCreate();
	    SharedPreferences sp = getSharedPreferences(Constants.SHAREUSER,
				MODE_WORLD_WRITEABLE);
	    preferences = getSharedPreferences(Constant.LOGIN_SET, 0);
	    if(sp.getInt(Constants.LOGINSTATE, 0) == 1){
	    	isServiceStart = false;
	    	AppLog.i("AthenaApp", "出现非正常退出情况！");
			
	    	//启动心跳包
			AlarmScheduler scheduler = new AlarmScheduler(this);
			scheduler.stop();
			
	    	if(sp.getInt(Constants.SHUTDOWN, 0) == 1){
	    		AppLog.v(TAG, "检测到为关机重启!");
	    		return;
	    	}else{
	    		loginConfig = getLoginConfig();
	    		loginConfig.setXmppHost(Constant.XMPP_HOST);
	    		XmppConnectionManager.getInstance(AthenaApp.this).init(loginConfig);							
			
	    		connectTime = 0;
	    		connectRun = true;
	    		tExit = new Timer();
	    		tExit.schedule(new timetask(), logintime);
	    	}
	    }
	    AppLog.i("TAG", "初始化Application");
	}
	
	public LoginConfig getLoginConfig() {
		LoginConfig loginConfig = new LoginConfig();

		loginConfig
				.setXmppHost(preferences.getString(Constant.XMPP_HOST, null));
		loginConfig.setXmppPort(preferences.getInt(Constant.XMPP_PORT,
				getResources().getInteger(R.integer.xmpp_port)));
		loginConfig.setUsername(preferences.getString(Constant.USERNAME, null));
		loginConfig.setPassword(preferences.getString(Constant.PASSWORD, null));
		loginConfig.setXmppServiceName(preferences.getString(
				Constant.XMPP_SEIVICE_NAME, null));
		loginConfig.setAutoLogin(preferences.getBoolean(Constant.IS_AUTOLOGIN,
				getResources().getBoolean(R.bool.is_autologin)));
		loginConfig.setNovisible(preferences.getBoolean(Constant.IS_NOVISIBLE,
				getResources().getBoolean(R.bool.is_novisible)));
		loginConfig.setRemember(preferences.getBoolean(Constant.IS_REMEMBER,
				getResources().getBoolean(R.bool.is_remember)));
		loginConfig.setFirstStart(preferences.getBoolean(
				Constant.IS_FIRSTSTART, true));
		return loginConfig;
	}
	
	@Override
	public void onTerminate(){
		super.onTerminate();
		AppLog.v(TAG, "程序终止！====onTerminate");
//		super.onTrimMemory(level);
	}
	
	@Override
	public void onLowMemory(){
		super.onLowMemory();
		AppLog.v(TAG, "资源不足，释放后台程序！=====onLowMemory");
	}
	
	/**  
     * 用来判断服务是否运行.  
     * @param context  
     * @param className 判断的服务名字  
     * @return true 在运行 false 不在运行  
     */  
    public static boolean isServiceRunning(Context mContext,String className) {   
        boolean isRunning = false;   
        ActivityManager activityManager = (ActivityManager)   
        		mContext.getSystemService(Context.ACTIVITY_SERVICE);    
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);   
        if (!(serviceList.size()>0)) {   
            return false;   
        }   
        for (int i=0; i<serviceList.size(); i++) {   
            if (serviceList.get(i).service.getClassName().equals(className) == true) {   
                isRunning = true;   
                break;   
            }   
        }   
        return isRunning;   
    }
    
	/**
	 * 重新登录
	 * @author hpq
	 *
	 */
	class timetask extends TimerTask {
		private ConnectivityManager connectivityManager;
		private NetworkInfo info;
		@Override
		public void run() {
			
			connectivityManager = (ConnectivityManager) AthenaApp.this
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			info = connectivityManager.getActiveNetworkInfo();
			
			SharedPreferences sp = AthenaApp.this.getSharedPreferences(
					Constants.SHAREUSER, AthenaApp.this.MODE_WORLD_WRITEABLE);
			
			NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		    NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		    if(!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()){
		    	AppLog.v(TAG, "第" + connectTime + "次连接,网络已断开");
		    	
		    	Editor editor = sp.edit();
				editor.putInt(Constants.IFNEEDLOGIN, 1);
				editor.commit();
				
		    	Intent chatServer = new Intent(AthenaApp.this, IMChatService.class);
		    	stopService(chatServer);
				Intent connectListenerService = new Intent(AthenaApp.this, ConnectListenerService.class);
				stopService(connectListenerService);
				
		    	AlarmScheduler scheduler = new AlarmScheduler(AthenaApp.this);
				scheduler.rescheduleSyncLocation(60000);
		    	return;
		    }
		    
//			AppLog.v(TAG, "第" + connectTime + "次连接");
			connectTime ++;
			username = loginConfig.getUsername();
			password = loginConfig.getPassword();
			
//			AppLog.v(TAG, "user1:" + loginConfig.getUsername() + "\npwd1:" + loginConfig.getPassword());
			XMPPConnection connection = XmppConnectionManager
					.getInstance(AthenaApp.this).getConnection();
			if (!connection.isConnected()) {
				if (username != null && password != null) {
					// 连接服务器
					try {
						connection.connect();
						connection.login(username, password); // 登录
						// 处理离线消息
						OfflineMsgManager.getInstance(
								AthenaApp.this).dealOfflineMsg(
								connection);
						connection.sendPacket(new Presence(
								Presence.Type.available));
						loginConfig.setXmppServiceName(connection
								.getServiceName());
						connectRun = false;
						
						//启动心跳包
						AlarmScheduler scheduler = new AlarmScheduler(AthenaApp.this);
						scheduler.rescheduleSyncLocation(15000);
						
					} catch (IllegalThreadStateException e) {
						AppLog.e(TAG, "非法线程状态\n");
						e.printStackTrace();
					} catch (Exception e) {
						AppLog.e(TAG, "重新登录出现错误!\n");
						AppLog.e(TAG, e.toString());
						if (e.toString()
								.contains("Already logged in to server")) {
							return;
						}
						if (connectTime >= 50) {
							// 聊天服务
							Intent chatServer = new Intent(
									AthenaApp.this,
									IMChatService.class);
							AthenaApp.this.stopService(chatServer);
							XmppConnectionManager.getInstance(
									AthenaApp.this).disconnect();
							if (sp.getInt(Constants.LOGINSTATE, 0) == 1) {
								Editor editor = sp.edit();
								editor.putInt(Constants.LOGINSTATE, 0);
								editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
								editor.commit();
								// finish();
							}
							ExitApplication.getInstance().exit();
						} else {
							tExit.schedule(new timetask(), logintime);
						}
					}
				}
			}else{
				//启动心跳包
				AlarmScheduler scheduler = new AlarmScheduler(AthenaApp.this);
				scheduler.rescheduleSyncLocation(15000);
			}
		}
	}
}
