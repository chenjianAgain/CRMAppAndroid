package com.zendaimoney.android.athena.im.service;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.ExitApplication;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.OfflineMsgManager;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.model.LoginConfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyAlarmReceiver extends BroadcastReceiver {

	private static final String TAG = MyAlarmReceiver.class.getSimpleName();
	
	public int connectTime = 0; //连接次数
	private int logintime = 1000;
	private Timer tExit;
	public static boolean connectRun = false;
	private String username;
	private String password;
	private LoginConfig loginConfig;
	protected SharedPreferences preferences;
	private Context mContext;

	@Override
	public void onReceive(final Context context, Intent intent) {

		// LocalPreference localPref = new LocalPreference(context);
		this.mContext = context;
		preferences = mContext.getSharedPreferences(Constant.LOGIN_SET, 0);
		final XMPPConnection connection = XmppConnectionManager.getInstance(context)
				.getConnection();
		if (connection.isConnected()) {
			AlarmAlertWakeLock.aquire(context);

//			Intent service = new Intent(context, LocationService.class);
//			context.startService(service);

			new Thread(new Runnable() {
				public void run() {
					try {
						connection.getPacketWriter().writer.write(" ");
						connection.getPacketWriter().writer.flush();
						AlarmScheduler scheduler = new AlarmScheduler(context);
						scheduler.rescheduleSyncLocation(15000);
						AppLog.v(TAG, "send packet heartbeat");
						AlarmAlertWakeLock.release();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						AlarmAlertWakeLock.release();
					}
				}
			}).start();
		}else{			
//			AppLog.v(TAG, "reconnect xmpp!!");
			SharedPreferences sp = mContext.getSharedPreferences(
					Constants.SHAREUSER, mContext.MODE_WORLD_WRITEABLE);
			if(sp.getInt(Constants.IFNEEDLOGIN, 0) == 1){//是否需要重新登录
				AppLog.v(TAG, "reconnect xmpp!!");
				loginConfig = getLoginConfig();
		    	loginConfig.setXmppHost(Constant.XMPP_HOST);
				XmppConnectionManager.getInstance(context).init(loginConfig);							
				
				connectTime = 0;
				connectRun = true;
				tExit = new Timer();
				tExit.schedule(new timetask(), logintime);
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
			connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		    NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		    if(!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()){
		    	AppLog.v(TAG, "第" + connectTime + "次连接,网络已断开");
		    	//启动心跳包
				AlarmScheduler scheduler = new AlarmScheduler(mContext);
				scheduler.rescheduleSyncLocation(60000);
		    	return;
		    }
		    
			AppLog.v(TAG, "第" + connectTime + "次连接");
			connectTime ++;
			SharedPreferences sp = mContext.getSharedPreferences(
					Constants.SHAREUSER, mContext.MODE_WORLD_WRITEABLE);
			username = loginConfig.getUsername();
			password = loginConfig.getPassword();

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
						
						Intent chatServer = new Intent(mContext, IMChatService.class);
						mContext.startService(chatServer);
						Intent connectListenerService = new Intent(mContext, ConnectListenerService.class);
						mContext.startService(connectListenerService);
						
						AlarmScheduler scheduler = new AlarmScheduler(mContext);
						scheduler.rescheduleSyncLocation(15000);
						
						Editor editor = sp.edit();
						editor.putInt(Constants.SHUTDOWN, 0);
						editor.putInt(Constants.IFNEEDLOGIN, 0);
						editor.commit();
						
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
