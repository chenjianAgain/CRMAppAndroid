package com.zendaimoney.android.athena.im.util;

import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.ExitApplication;
import com.zendaimoney.android.athena.MainActivity;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.service.IMChatService;
//import com.zendaimoney.android.athena.im.service.ReConnectService;

public class XmppConnectionListener implements ConnectionListener {

	private String TAG = "XmppConnectionListener";
	public Context mContext;
	private Timer tExit;
	private String username;
	private String password;
	private int logintime = 2000;
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	private SharedPreferences sp;
	private Editor editor;
	public static boolean connectRun = false;
	public int connectTime = 0; //连接次数

	public XmppConnectionListener(Context context) {
		this.mContext = context;
		sp = context.getSharedPreferences(Constants.SHAREUSER,
				context.MODE_WORLD_WRITEABLE);
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		Log.v(TAG, "xmpp连接正常关闭!");
//		/**
//		 * 修改OPENFIRE服务器登陆状态
//		 */
//		if (sp.getInt(Constants.LOGINSTATEOPENFIRE, 0) == 1) {
//			editor = sp.edit();
//			editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
//			editor.commit();
//		}
		if(!connectRun && !XmppConnectionManager
				.getInstance(mContext).getConnection().isConnected()){
			connectTime = 0;
			connectRun = true;
			tExit = new Timer();
			tExit.schedule(new timetask(), logintime);
		}
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		// TODO Auto-generated method stub
		AppLog.v(TAG, "xmpp连接异常关闭!\n");
		AppLog.v(TAG, e.toString() + "\n");
//		/**
//		 * 修改OPENFIRE服务器登陆状态
//		 */
//		if (sp.getInt(Constants.LOGINSTATEOPENFIRE, 0) == 1) {
//			editor = sp.edit();
//			editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
//			editor.commit();
//		}
		connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		info = connectivityManager.getActiveNetworkInfo();
		boolean error = e.getMessage().equals("stream:error (conflict)");
		if (!error) {
			if (info != null && info.isAvailable() && !XmppConnectionManager
					.getInstance(mContext).getConnection().isConnected()) {
				AppLog.v(TAG, "其他原因导致，开始重连！\n");
				if(!connectRun){
					connectRun = true;
					tExit = new Timer();
					tExit.schedule(new timetask(), logintime);
				}
			} else {
				AppLog.v(TAG, "网络切换导致！\n");
			}
		} else {
			AppLog.v(TAG, "其他设备登陆本账号！\n");
//			showExitDialog();
//			Toast.makeText(mContext, "您的账号在另一台设备上登录,如非本人操作,请确认账号是否安全！", Toast.LENGTH_LONG);
			/**
			 * 修改CRM服务器登陆状态
			 */
			SharedPreferences sp1 = mContext.getSharedPreferences(
					Constants.SHAREUSER,
					mContext.MODE_WORLD_WRITEABLE);
			if (sp1.getInt(Constants.LOGINSTATE, 0) == 1) {
				Editor editor1 = sp1.edit();
				editor1.putInt(Constants.LOGINSTATE, 0);
				editor1.putInt(Constants.LOGINSTATEOPENFIRE, 0);
				editor1.commit();
				// finish();
			}
			// 聊天服务
			Intent chatServer = new Intent(mContext, IMChatService.class);
			mContext.stopService(chatServer);

//			// 自动恢复连接服务
//			Intent reConnectService = new Intent(mContext,
//					ReConnectService.class);
//			mContext.stopService(reConnectService);
			XmppConnectionManager.getInstance(mContext).disconnect();
			
			/*
			 * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
			 */
			NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
			Intent notifyIntent = new Intent(mContext, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("notiflag", "1");
			notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			notifyIntent.putExtras(bundle);
//			notifyIntent.setFlags(Intent.Intent.FLAG_ACTIVITY_CLEAR_TOP);

			/* 创建PendingIntent作为设置递延运行的Activity */
			PendingIntent appIntent = PendingIntent.getActivity(mContext, 0,
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
			myNoti.setLatestEventInfo(mContext, "下线通知", "您的账号在另一台设备上登录,如非本人操作,请确认账号是否安全！", appIntent);
//			myNoti.ledOffMS.
			/* 送出Notification */
			notificationManager.notify(0, myNoti);
			
			ExitApplication.getInstance().exit(); 
//			System.exit(0);
			// mContext.stopService(service)
		}
		// tExit = new Timer();
		// tExit.schedule(new timetask(), logintime);
	}

	@Override
	public void reconnectingIn(int arg0) {
		// TODO Auto-generated method stub
		Log.v(TAG, "xmpp reconnectingIn!");
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		// TODO Auto-generated method stub
		Log.v(TAG, "xmpp重连失败!");
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		Log.v(TAG, "xmpp重连成功!");
	}
	class timetask extends TimerTask {
		@Override
		public void run() {
			connectTime ++;
			SharedPreferences sp = mContext.getSharedPreferences(
					Constants.SHAREUSER, mContext.MODE_WORLD_WRITEABLE);
			username = sp.getString(Constants.UERNAME, "");
			password = sp.getString(Constants.PASSWD, "");
			if (username != null && password != null) {
				// 连接服务器
				try {
					// 聊天服务
					Intent chatServer = new Intent(mContext,
							IMChatService.class);
					mContext.stopService(chatServer);

					XmppConnectionManager.getInstance(mContext).disconnect();
					XMPPConnection connection = XmppConnectionManager
							.getInstance(mContext).getConnection();
					connection.connect();
					connection.login(username, password);
					connection
							.sendPacket(new Presence(Presence.Type.available));
					// 聊天服务
					mContext.startService(chatServer);
					connectRun = false;
				} catch(IllegalThreadStateException e){
					// 聊天服务
					Intent chatServer = new Intent(mContext,
							IMChatService.class);
					mContext.stopService(chatServer);
					XmppConnectionManager.getInstance(mContext).disconnect();
					
//					SharedPreferences sp = mContext.getSharedPreferences(
//							Constants.SHAREUSER,
//							mContext.MODE_WORLD_WRITEABLE);
					if (sp.getInt(Constants.LOGINSTATE, 0) == 1) {
						Editor editor = sp.edit();
						editor.putInt(Constants.LOGINSTATE, 0);
						editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
						editor.commit();
//						finish();
					}
					ExitApplication.getInstance().exit();
				}catch (Exception e) {
					AppLog.e(TAG, "重新登录出现错误!\n");
					AppLog.e(TAG, e.toString());
					if(connectTime >= 60){
						// 聊天服务
						Intent chatServer = new Intent(mContext,
								IMChatService.class);
						mContext.stopService(chatServer);
						XmppConnectionManager.getInstance(mContext).disconnect();
						
//						SharedPreferences sp = mContext.getSharedPreferences(
//								Constants.SHAREUSER,
//								mContext.MODE_WORLD_WRITEABLE);
						if (sp.getInt(Constants.LOGINSTATE, 0) == 1) {
							Editor editor = sp.edit();
							editor.putInt(Constants.LOGINSTATE, 0);
//							editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
							editor.commit();
//							finish();
						}
						ExitApplication.getInstance().exit();
					}else{
						tExit.schedule(new timetask(), logintime);
					}
				}
			}
		}
	}
}
