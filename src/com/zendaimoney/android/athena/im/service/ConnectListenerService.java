package com.zendaimoney.android.athena.im.service;

import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.OfflineMessageManager;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.AthenaApp;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.ExitApplication;
import com.zendaimoney.android.athena.MainActivity;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.OfflineMsgManager;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.model.LoginConfig;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;

public class ConnectListenerService extends Service {

	private OfflineMessageManager offlineManager;

	public static XMPPConnection con = null;
	public static String TAG = "ConnectListenerService";
	
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	
	public int connectTime = 0; //连接次数
	private String username;
	private String password;
	private int logintime = 1000;
	private Timer tExit;
	public static boolean connectRun = false;
	
	public SharedPreferences preferences;
	
	private LoginConfig loginConfig;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
//		unregisterReceiver(reConnectionBroadcastReceiver);
		AppLog.v(TAG, "服务被kill");
		con.removeConnectionListener(connectionListener);
//		SharedPreferences sp = getSharedPreferences(
//				Constants.SHAREUSER,
//				MODE_WORLD_WRITEABLE);
//		if (sp.getInt(Constants.LOGINSTATE, 0) == 1) {
//			Editor editor = sp.edit();
//			editor.putInt(Constants.LOGINSTATE, 0);
//			editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
//			editor.commit();
//		}
//		// 聊天服务
//		Intent chatServer = new Intent(ConnectListenerService.this, IMChatService.class);
//		stopService(chatServer);
//		ExitApplication.getInstance().exit();
		super.onDestroy();
	}
	
	@Override
	public void onCreate() {
	
		super.onCreate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		AthenaApp.isServiceStart = true;
		
		AppLog.e(TAG, "服务启动");
		//注册网络切换广播监听
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mFilter.addAction(Constant.RECONNECT_ACTION);
//		registerReceiver(reConnectionBroadcastReceiver, mFilter);
		//获取连接配置
		preferences = getSharedPreferences(Constant.LOGIN_SET, 0);
		loginConfig = getLoginConfig();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try{
					con = XmppConnectionManager.getInstance(
							ConnectListenerService.this).getConnection();
					con.addConnectionListener(connectionListener);
				}catch(Exception e){
					if(con != null){
						AppLog.e(TAG, con.getConnectionID() + con.getHost());
					}
					AppLog.e(TAG, "监听服务出现异常!");
					ConnectListenerService.this.stopSelf();
				}
			}

		}).start();
	};
	/**
	 * 重新登录
	 * @author hpq
	 *
	 */
	class timetask extends TimerTask {
		@Override
		public void run() {
			connectivityManager = (ConnectivityManager) ConnectListenerService.this
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			info = connectivityManager.getActiveNetworkInfo();
			
			NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		    NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		    if(!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()){
		    	AppLog.v(TAG, "第" + connectTime + "次连接,无网络连接！");
		    	return;
		    }
		    
			AppLog.v(TAG, "第" + connectTime + "次连接");
			connectTime ++;
			SharedPreferences sp = ConnectListenerService.this.getSharedPreferences(
					Constants.SHAREUSER, ConnectListenerService.this.MODE_WORLD_WRITEABLE);
			username = loginConfig.getUsername();
			password = loginConfig.getPassword();
			
//			AppLog.v(TAG, "user1:" + loginConfig.getUsername() + "\npwd1:" + loginConfig.getPassword());
			XMPPConnection connection = XmppConnectionManager
					.getInstance(ConnectListenerService.this).getConnection();
			if (!connection.isConnected()) {
				if (username != null && password != null) {
					// 连接服务器
					try {
						// 聊天服务
						Intent chatServer = new Intent(
								ConnectListenerService.this,
								IMChatService.class);
						ConnectListenerService.this.stopService(chatServer);

						connection.connect();
						connection.login(username, password); // 登录
						// 处理离线消息
						OfflineMsgManager.getInstance(
								ConnectListenerService.this).dealOfflineMsg(
								connection);
						connection.sendPacket(new Presence(
								Presence.Type.available));
						loginConfig.setXmppServiceName(connection
								.getServiceName());

						// 保存配置
						saveLoginConfig(loginConfig);

						/**
						 * 启动服务
						 */
						// 聊天服务
						ConnectListenerService.this.startService(chatServer);

						// 添加监听
						connection.addConnectionListener(connectionListener);
						connectRun = false;
						
						//启动心跳包
						AlarmScheduler scheduler = new AlarmScheduler(ConnectListenerService.this);
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
									ConnectListenerService.this,
									IMChatService.class);
							ConnectListenerService.this.stopService(chatServer);
							XmppConnectionManager.getInstance(
									ConnectListenerService.this).disconnect();
							if (sp.getInt(Constants.LOGINSTATE, 0) == 1) {
								Editor editor = sp.edit();
								editor.putInt(Constants.LOGINSTATE, 0);
								editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
								editor.commit();
								// finish();
							}
							ExitApplication.getInstance().exit();
							ConnectListenerService.this.stopSelf();
						} else {
							tExit.schedule(new timetask(), logintime);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 连接监听
	 */
	ConnectionListener connectionListener = new ConnectionListener(){

		@Override
		public void connectionClosed() {
			AppLog.e(TAG, "来自连接监听,conn正常关闭");
			//停止心跳包定时器
			AlarmScheduler scheduler = new AlarmScheduler(ConnectListenerService.this);
			scheduler.stop();

			if(!connectRun){
				loginConfig.setXmppHost(Constant.XMPP_HOST);
				XmppConnectionManager.getInstance(ConnectListenerService.this).init(loginConfig);							
				
				connectTime = 0;
				connectRun = true;
				tExit = new Timer();
				tExit.schedule(new timetask(), logintime);
			}
		}

		@Override
		public void connectionClosedOnError(Exception e) {
			boolean error = e.getMessage().equals(
					"stream:error (conflict)");
			AppLog.e(TAG, "来自连接监听,conn异常关闭");
			
			//停止心跳包
			AlarmScheduler scheduler = new AlarmScheduler(ConnectListenerService.this);
			scheduler.stop();
			
			// 这里就是网络不正常或者被挤掉断线激发的事件
			if (error) { // 被挤掉线
				/*
				 * log.e("来自连接监听,conn非正常关闭");
				 * log.e("非正常关闭异常:"+arg0.getMessage());
				 * log.e(con.isConnected());
				 */
				AppLog.e(TAG, "来自连接监听,账号被登录");
				
				/**
				 * 修改CRM服务器登陆状态
				 */
				SharedPreferences sp1 = ConnectListenerService.this.getSharedPreferences(
						Constants.SHAREUSER,
						ConnectListenerService.this.MODE_WORLD_WRITEABLE);
				if (sp1.getInt(Constants.LOGINSTATE, 0) == 1) {
					Editor editor1 = sp1.edit();
					editor1.putInt(Constants.LOGINSTATE, 0);
					editor1.putInt(Constants.LOGINSTATEOPENFIRE, 0);
					editor1.commit();
					// finish();
				}

				NotificationManager notificationManager = (NotificationManager)ConnectListenerService.this.getSystemService(ConnectListenerService.this.NOTIFICATION_SERVICE);
				Intent notifyIntent = new Intent(ConnectListenerService.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("notiflag", "1");
				notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				notifyIntent.putExtras(bundle);
//				notifyIntent.setFlags(Intent.Intent.FLAG_ACTIVITY_CLEAR_TOP);

				/* 创建PendingIntent作为设置递延运行的Activity */
				PendingIntent appIntent = PendingIntent.getActivity(ConnectListenerService.this, 0,
						notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

				/* 创建Notication，并设置相关参数 */
				Notification myNoti = new Notification();
				// 点击自动消失
				myNoti.flags = Notification.FLAG_AUTO_CANCEL;
				/* 设置statusbar显示的icon */
				myNoti.icon = R.drawable.icon_new;
				/* 设置statusbar显示的文字信息 */
				myNoti.tickerText = "下线通知";
				/* 设置notification发生时同时发出默认声音 */
				myNoti.defaults = Notification.DEFAULT_SOUND;
				/* 设置Notification留言条的参数 */
				myNoti.setLatestEventInfo(ConnectListenerService.this, "下线通知", "您的账号在另一台设备上登录,如非本人操作,请确认账号是否安全！", appIntent);
//				myNoti.ledOffMS.
				/* 送出Notification */
				notificationManager.notify(0, myNoti); 
//				System.exit(0);
//				mContext.stopService(service);
			
				// 关闭连接，由于是被人挤下线，可能是用户自己，所以关闭连接，让用户重新登录是一个比较好的选择
				XmppConnectionManager.getInstance(ConnectListenerService.this).disconnect();
				Intent chatServer = new Intent(ConnectListenerService.this, IMChatService.class);
				stopService(chatServer);
				ExitApplication.getInstance().exit();
				ConnectListenerService.this.stopSelf();
				ExitApplication.getInstance().exit();
				// 接下来你可以通过发送一个广播，提示用户被挤下线，重连很简单，就是重新登录
			} 
//			else if (e.getMessage().contains(
//					"Connection timed out")) {// 连接超时
//				// 不做任何操作，会实现自动重连
//				AppLog.e(TAG, "来自连接监听,连接超时");
////				XmppConnectionManager.getInstance(
////						ConnectListenerService.this).disconnect();
//			}
			else{
//				AppLog.e(TAG, "来自连接监听,连接断开");
				
//				reConnect();
//				XmppConnectionManager.getInstance(
//						ConnectListenerService.this).disconnect();
			}
		}

		@Override
		public void reconnectingIn(int arg0) {
			// 重新连接的动作正在进行的动作，里面的参数arg0是一个倒计时的数字，如果连接失败的次数增多，数字会越来越大，开始的时候是9
			AppLog.e(TAG, "来自连接监听,conn重连中..." + arg0);
		}

		@Override
		public void reconnectionFailed(Exception arg0) {
			// 重新连接失败
			AppLog.e(TAG, "来自连接监听,conn失败：" + arg0.getMessage());
//			reConnect();
		}

		@Override
		public void reconnectionSuccessful() {
			// 当网络断线了，重新连接上服务器触发的事件
			AppLog.e(TAG, "来自连接监听,conn重连成功");
			XMPPConnection connection = XmppConnectionManager
					.getInstance(ConnectListenerService.this).getConnection();
			connection.sendPacket(new Presence(Presence.Type.available));
			
			//启动心跳包
			AlarmScheduler scheduler = new AlarmScheduler(ConnectListenerService.this);
			scheduler.rescheduleSyncLocation(15000);
		}

	};

	/**
	 * 获取配置信息
	 * @return
	 */
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
	/**
	 * 保存配置信息
	 * @param loginConfig
	 */
	public void saveLoginConfig(LoginConfig loginConfig) {
		preferences.edit()
				.putString(Constant.XMPP_HOST, loginConfig.getXmppHost())
				.commit();
		preferences.edit()
				.putInt(Constant.XMPP_PORT, loginConfig.getXmppPort()).commit();
		preferences
				.edit()
				.putString(Constant.XMPP_SEIVICE_NAME,
						loginConfig.getXmppServiceName()).commit();
		preferences.edit()
				.putString(Constant.USERNAME, loginConfig.getUsername())
				.commit();
		preferences.edit()
				.putString(Constant.PASSWORD, loginConfig.getPassword())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_AUTOLOGIN, loginConfig.isAutoLogin())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_NOVISIBLE, loginConfig.isNovisible())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_REMEMBER, loginConfig.isRemember())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_ONLINE, loginConfig.isOnline())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_FIRSTSTART, loginConfig.isFirstStart())
				.commit();
	}
	
	private void sendInentAndPre(boolean isSuccess) {
		Intent intent = new Intent();
		SharedPreferences preference = getSharedPreferences(Constant.LOGIN_SET,
				0);
		preference.edit().putBoolean(Constant.IS_ONLINE, isSuccess).commit();
		intent.setAction(Constant.ACTION_RECONNECT_STATE);
		intent.putExtra(Constant.RECONNECT_STATE, isSuccess);
		sendBroadcast(intent);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

}
