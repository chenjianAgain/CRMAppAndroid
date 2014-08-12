package com.zendaimoney.android.athena.receiver;

import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.AthenaApp;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.ExitApplication;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.OfflineMsgManager;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.model.LoginConfig;
import com.zendaimoney.android.athena.im.service.AlarmScheduler;
import com.zendaimoney.android.athena.im.service.ConnectListenerService;
import com.zendaimoney.android.athena.im.service.IMChatService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BootBroadcastReceiver extends BroadcastReceiver{
	private String TAG = "BootBroadcastReceiver";
	
	private LoginConfig loginConfig;
	protected SharedPreferences preferences;
	
	public int connectTime = 0; //连接次数
	private int logintime = 1000;
	private Timer tExit;
	public static boolean connectRun = false;
	private String username;
	private String password;
	
	private Context mContext;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			AppLog.v(TAG, "receive the boot broadcast!");
			this.mContext = context;
			SharedPreferences sp = mContext.getSharedPreferences(Constants.SHAREUSER,
					mContext.MODE_WORLD_WRITEABLE);
		    preferences = mContext.getSharedPreferences(Constant.LOGIN_SET, 0);
		    if(sp.getInt(Constants.LOGINSTATE, 0) == 1){
		    	AppLog.i(TAG, "收到开机广播，客户端已经登陆，开始连接XMPP");
		    	
		    	Editor editor = sp.edit();
				editor.putInt(Constants.SHUTDOWN, 0);
				editor.commit();
				
		    	loginConfig = getLoginConfig();
		    	loginConfig.setXmppHost(Constant.XMPP_HOST);
				XmppConnectionManager.getInstance(context).init(loginConfig);							
				
				connectTime = 0;
				connectRun = true;
				tExit = new Timer();
				tExit.schedule(new timetask(), logintime);
		    }else{
		    	AppLog.i(TAG, "收到开机广播，客户端未登陆！");
		    }
		}
	}
	
	public LoginConfig getLoginConfig() {
		LoginConfig loginConfig = new LoginConfig();

		loginConfig
				.setXmppHost(preferences.getString(Constant.XMPP_HOST, null));
		loginConfig.setXmppPort(preferences.getInt(Constant.XMPP_PORT,
				mContext.getResources().getInteger(R.integer.xmpp_port)));
		loginConfig.setUsername(preferences.getString(Constant.USERNAME, null));
		loginConfig.setPassword(preferences.getString(Constant.PASSWORD, null));
		loginConfig.setXmppServiceName(preferences.getString(
				Constant.XMPP_SEIVICE_NAME, null));
		loginConfig.setAutoLogin(preferences.getBoolean(Constant.IS_AUTOLOGIN,
				mContext.getResources().getBoolean(R.bool.is_autologin)));
		loginConfig.setNovisible(preferences.getBoolean(Constant.IS_NOVISIBLE,
				mContext.getResources().getBoolean(R.bool.is_novisible)));
		loginConfig.setRemember(preferences.getBoolean(Constant.IS_REMEMBER,
				mContext.getResources().getBoolean(R.bool.is_remember)));
		loginConfig.setFirstStart(preferences.getBoolean(
				Constant.IS_FIRSTSTART, true));
		return loginConfig;
	}
	
	/**
	 * 重新登录
	 * @author hpq
	 *
	 */
	class timetask extends TimerTask {
		private ConnectivityManager connectivityManager;
		@Override
		public void run() {
			
//			while(true){
//				if(AthenaApp.isServiceStart){
//					AppLog.v(TAG, "服务启动，开始登陆!");
//					break;
//				}else{
//					try {
//						AppLog.v(TAG, "服务未启动，延迟登陆时间!");
//						Thread.sleep(500);
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				}
//			}
			
			connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			SharedPreferences sp = mContext.getSharedPreferences(
					Constants.SHAREUSER, mContext.MODE_WORLD_WRITEABLE);
			
			NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		    NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		    if(!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()){
		    	AppLog.v(TAG, "第" + connectTime + "次连接,网络已断开");
		    	
		    	Editor editor = sp.edit();
				editor.putInt(Constants.IFNEEDLOGIN, 1);
				editor.commit();
				
		    	Intent chatServer = new Intent(mContext, IMChatService.class);
				mContext.stopService(chatServer);
				Intent connectListenerService = new Intent(mContext, ConnectListenerService.class);
				mContext.stopService(connectListenerService);
				
		    	//启动心跳包
				AlarmScheduler scheduler = new AlarmScheduler(mContext);
				scheduler.rescheduleSyncLocation(60000);
		    	return;
		    }
		    
			AppLog.v(TAG, "第" + connectTime + "次连接");
			connectTime ++;
		
			username = loginConfig.getUsername();
			password = loginConfig.getPassword();
			
//			AppLog.v(TAG, "user1:" + loginConfig.getUsername() + "\npwd1:" + loginConfig.getPassword());
			XMPPConnection connection = XmppConnectionManager
					.getInstance(mContext).getConnection();
			if (!connection.isConnected()) {
				if (username != null && password != null) {
					// 连接服务器
					try {
						connection.connect();
						connection.login(username, password); // 登录
						// 处理离线消息
						OfflineMsgManager.getInstance(
								mContext).dealOfflineMsg(
								connection);
						connection.sendPacket(new Presence(
								Presence.Type.available));
						loginConfig.setXmppServiceName(connection
								.getServiceName());
						connectRun = false;
						
						/**
						 * 判断是否为开启重启
						 */
						AppLog.v(TAG, "restart suc and modify the flag!");
						Intent chatServer = new Intent(mContext, IMChatService.class);
						mContext.startService(chatServer);
						Intent connectListenerService = new Intent(mContext, ConnectListenerService.class);
						mContext.startService(connectListenerService);
						
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
						if (connectTime >= 10) {
							// 聊天服务
							Intent chatServer = new Intent(
									mContext,
									IMChatService.class);
							mContext.stopService(chatServer);
							XmppConnectionManager.getInstance(
									mContext).disconnect();
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
			}
		}
	}
}
