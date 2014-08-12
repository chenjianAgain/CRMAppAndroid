package com.zendaimoney.android.athena.receiver;

import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.ExitApplication;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.ActivitySupport;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.OfflineMsgManager;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.model.LoginConfig;
import com.zendaimoney.android.athena.im.service.IMChatService;
import com.zendaimoney.android.athena.im.util.XmppConnectionListener;
//import com.zendaimoney.android.athena.im.service.ReConnectService;

public class NetChangeReceiver extends BroadcastReceiver{

	private String TAG = "NetChangeReceiver";
	private Context mContext;
	public Timer tExit;
	public int logintime = 1000;
	public static boolean reConnectRun = false;
	public int connectTime = 0; //重连次数
	public ActivitySupport activitySupport = new ActivitySupport();
	private LoginConfig loginConfig;
	public SharedPreferences preferences1;
	public static boolean connectRun = false;
	
	private String username;
	private String password;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
//		try {
//			ConnectivityManager connectivityManager;
//			NetworkInfo info;
//			mContext = context;
//			preferences1 = mContext.getSharedPreferences(Constant.LOGIN_SET, 0);
//			
//			loginConfig = getLoginConfig();
//			// connectTime = 0;
//			if (intent.getAction().equals(
//					ConnectivityManager.CONNECTIVITY_ACTION)) {
//				Log.v("TAG", "网络状态改变");
//				try {
//					connectivityManager = (ConnectivityManager) context
//							.getSystemService(Context.CONNECTIVITY_SERVICE);
//					AppLog.v("TAG", "网络状态改变1");
//					final XMPPConnection connection = XmppConnectionManager
//							.getInstance(context).getConnection();
//					AppLog.v("TAG", "网络状态改变2");
//					info = connectivityManager.getActiveNetworkInfo();
//					Log.v(TAG, "infotype:" + info.getType());
//					Log.v(TAG, "infoisAvailable:" + info.isAvailable());
//					SharedPreferences preferences = mContext
//							.getSharedPreferences(Constant.LOGIN_SET, 0);
//					AppLog.v("TAG", "网络状态改变3");
//					final String username = preferences.getString(
//							Constant.USERNAME, null);
//					AppLog.v("TAG", "网络状态改变4");
//					final String password = preferences.getString(
//							Constant.PASSWORD, null);
//					AppLog.v("TAG", "网络状态改变5");
//
//					if (info != null && info.isAvailable()
//							&& !connection.isConnected()) {
//						try {
//							AppLog.v("TAG", "网络状态改变20");
//							new Thread(new Runnable() {
//								@Override
//								public void run() {
//									try {
////										AppLog.v("TAG", "网络状态改变6");
////										connection.connect();
////										AppLog.v("TAG", "网络状态改变7");
////										// OfflineMsgManager.getInstance(mContext).dealOfflineMsg(connection);
////										AppLog.v(TAG, "user:" + username);
////										AppLog.v(TAG, "password:" + password);
////										// connection.login(username, password);
//										if(!connectRun && !XmppConnectionManager
//												.getInstance(mContext).getConnection().isConnected()){
//											
////											XmppConnectionManager.getInstance(ConnectListenerService.this).disconnect();
//											//重新初始化配置
////											loginConfig.setXmppHost("192.16.10.149");
//											loginConfig.setXmppHost("172.16.220.102");
//											XmppConnectionManager.getInstance(mContext).init(loginConfig);							
//											
//											connectTime = 0;
//											connectRun = true;
//											tExit = new Timer();
//											tExit.schedule(new timetask(), logintime);
//										}
//									} catch (Exception e) {
//										// TODO Auto-generated catch block
//										AppLog.v("TAG", "网络状态改变异常1");
//										e.printStackTrace();
//										sendInentAndPre(Constant.RECONNECT_STATE_FAIL);
//									}
//									if (!connection.isConnected()) {
//										sendInentAndPre(Constant.RECONNECT_STATE_FAIL);
//									} else {
//										sendInentAndPre(Constant.RECONNECT_STATE_SUCCESS);
//										Presence presence = new Presence(
//												Presence.Type.available);
//										connection.sendPacket(presence);
//									}
//								}
//							}).start();
//						} catch (IllegalThreadStateException e) {
//							e.printStackTrace();
//						}
//					} else if (!connection.isConnected()) {
//						// sendInentAndPre(Constant.RECONNECT_STATE_FAIL);
//						AppLog.v("TAG", "socket连接断开");
//						// XmppConnectionManager.getInstance(context).getConnection().disconnect();
//					} else if (info == null || !info.isAvailable()) {
//						AppLog.v("TAG", "网络断开");
//					}
//				} catch (Exception e) {
//					AppLog.v(TAG, "捕捉到网络重连异常！");
//					e.printStackTrace();
//					SharedPreferences sp = mContext.getSharedPreferences(
//							Constants.SHAREUSER, mContext.MODE_WORLD_WRITEABLE);
//					if (sp.getInt(Constants.LOGINSTATE, 0) == 1) {
//						Editor editor = sp.edit();
//						editor.putInt(Constants.LOGINSTATE, 0);
//						// editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
//						editor.commit();
//						// finish();
//					}
//
//					// 好友联系人服务
//					Intent server = new Intent(mContext, IMContactService.class);
//					context.stopService(server);
//					// 聊天服务
//					Intent chatServer = new Intent(mContext,
//							IMChatService.class);
//					context.stopService(chatServer);
//
//					// 聊天服务
//					Intent connectListenerService = new Intent(mContext,
//							ConnectListenerService.class);
//					context.stopService(connectListenerService);
//
//					ExitApplication.getInstance().exit();
//				}
//			}
//		} catch (Exception e) {
//			AppLog.e(TAG, "网络切换过程出现异常");
//			AppLog.e(TAG, e.toString());
//			e.printStackTrace();
//		}
	}
	
	private void sendInentAndPre(boolean isSuccess) {
		Intent intent = new Intent();
		AppLog.e(TAG, "sendInentAndPre1");
		SharedPreferences preference = mContext.getSharedPreferences(Constant.LOGIN_SET,
				0);
		AppLog.e(TAG, "sendInentAndPre2");
		preference.edit().putBoolean(Constant.IS_ONLINE, isSuccess).commit();
		AppLog.e(TAG, "sendInentAndPre3");
		intent.setAction(Constant.ACTION_RECONNECT_STATE);
		intent.putExtra(Constant.RECONNECT_STATE, isSuccess);
		AppLog.e(TAG, "sendInentAndPre4");
		mContext.sendBroadcast(intent);
		AppLog.e(TAG, "sendInentAndPre5");
	}
	
	public LoginConfig getLoginConfig(){
		loginConfig
				.setXmppHost(preferences1.getString(Constant.XMPP_HOST, null));
		loginConfig.setXmppPort(preferences1.getInt(Constant.XMPP_PORT,
				mContext.getResources().getInteger(R.integer.xmpp_port)));
		loginConfig.setUsername(preferences1.getString(Constant.USERNAME, null));
		loginConfig.setPassword(preferences1.getString(Constant.PASSWORD, null));
		loginConfig.setXmppServiceName(preferences1.getString(
				Constant.XMPP_SEIVICE_NAME, null));
		loginConfig.setAutoLogin(preferences1.getBoolean(Constant.IS_AUTOLOGIN,
				mContext.getResources().getBoolean(R.bool.is_autologin)));
		loginConfig.setNovisible(preferences1.getBoolean(Constant.IS_NOVISIBLE,
				mContext.getResources().getBoolean(R.bool.is_novisible)));
		loginConfig.setRemember(preferences1.getBoolean(Constant.IS_REMEMBER,
				mContext.getResources().getBoolean(R.bool.is_remember)));
		loginConfig.setFirstStart(preferences1.getBoolean(
				Constant.IS_FIRSTSTART, true));
		return loginConfig;
	}
	
	/**
	 * 重新登录
	 * @author hpq
	 *
	 */
	class timetask extends TimerTask {
		@Override
		public void run() {
			AppLog.v(TAG, "第" + connectTime + "次连接");
			connectTime ++;
			SharedPreferences sp = mContext.getSharedPreferences(
					Constants.SHAREUSER, mContext.MODE_WORLD_WRITEABLE);
			username = loginConfig.getUsername();
			password = loginConfig.getPassword();
			
			AppLog.v(TAG, "user1:" + loginConfig.getUsername() + "\npwd1:" + loginConfig.getPassword());
			
			if (username != null && password != null) {
				// 连接服务器
				try {
					// 聊天服务
					Intent chatServer = new Intent(mContext,
							IMChatService.class);
					mContext.stopService(chatServer);
					
					// 监听服务
					Intent connectListenerServer = new Intent(mContext,
							XmppConnectionListener.class);
					mContext.stopService(connectListenerServer);
					
					/**
					 * 开始登录
					 */
					XMPPConnection connection = XmppConnectionManager
							.getInstance(mContext).getConnection();
//					connection.connect();
//					connection.login(username, password);
//					connection
//							.sendPacket(new Presence(Presence.Type.available));
					AppLog.v(TAG, "user:" + username + "\npwd:" + password);
					connection.connect();
					connection.login(username, password); // 登录
					//处理离线消息
					OfflineMsgManager.getInstance(mContext).dealOfflineMsg(connection);
					connection.sendPacket(new Presence(Presence.Type.available));
					loginConfig.setXmppServiceName(connection.getServiceName());
					
					//保存配置
					saveLoginConfig(loginConfig);
					
					/**
					 * 启动服务
					 */
					// 聊天服务
					mContext.startService(chatServer);
					// 监听服务
					mContext.startService(connectListenerServer);
					
					//添加监听
//					connection.addConnectionListener(connectionListener);
					connectRun = false;
				} catch(IllegalThreadStateException e){
					e.printStackTrace();
				}catch (Exception e) {
					AppLog.e(TAG, "重新登录出现错误!\n");
					AppLog.e(TAG, e.toString());
					if(connectTime >= 10){
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
							editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
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
	
	/**
	 * 保存配置信息
	 * @param loginConfig
	 */
	public void saveLoginConfig(LoginConfig loginConfig) {
		preferences1.edit()
				.putString(Constant.XMPP_HOST, loginConfig.getXmppHost())
				.commit();
		preferences1.edit()
				.putInt(Constant.XMPP_PORT, loginConfig.getXmppPort()).commit();
		preferences1
				.edit()
				.putString(Constant.XMPP_SEIVICE_NAME,
						loginConfig.getXmppServiceName()).commit();
		preferences1.edit()
				.putString(Constant.USERNAME, loginConfig.getUsername())
				.commit();
		preferences1.edit()
				.putString(Constant.PASSWORD, loginConfig.getPassword())
				.commit();
		preferences1.edit()
				.putBoolean(Constant.IS_AUTOLOGIN, loginConfig.isAutoLogin())
				.commit();
		preferences1.edit()
				.putBoolean(Constant.IS_NOVISIBLE, loginConfig.isNovisible())
				.commit();
		preferences1.edit()
				.putBoolean(Constant.IS_REMEMBER, loginConfig.isRemember())
				.commit();
		preferences1.edit()
				.putBoolean(Constant.IS_ONLINE, loginConfig.isOnline())
				.commit();
		preferences1.edit()
				.putBoolean(Constant.IS_FIRSTSTART, loginConfig.isFirstStart())
				.commit();
	}
}
