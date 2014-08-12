package com.zendaimoney.android.athena.im;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.Utils;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.OfflineMsgManager;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.model.LoginConfig;
import com.zendaimoney.android.athena.im.service.AlarmScheduler;


/**
 * 
 * 登录异步任务.
 * 
 * @author shimiso
 */
public class LoginTask extends AsyncTask<String, Integer, Integer> {
	
	private static final String TAG = "LoginTask";
	private ProgressDialog pd;
	private Context context;
//	private IActivitySupport activitySupport;
	private ActivitySupport activitySupport;
	private LoginConfig loginConfig;
	
	private Dialog mProgressDlg;

	public LoginTask(ActivitySupport activitySupport, LoginConfig loginConfig) {
		this.activitySupport = activitySupport;
		this.loginConfig = loginConfig;
		this.pd = activitySupport.getProgressDialog();
		this.context = activitySupport.getContext();
	}

	@Override
	protected void onPreExecute() {
//		pd.setTitle("请稍等");
//		pd.setMessage("正在登录...");
//		pd.show();
//		showProgress();
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(String... params) {
		return login();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}

	@Override
	protected void onPostExecute(Integer result) {
//		dismissProgress();
		switch (result) {
		case Constant.LOGIN_SECCESS: // 登录成功
//			Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
			SharedPreferences sp = context.getSharedPreferences(Constants.SHAREUSER, context.MODE_WORLD_WRITEABLE);
			Editor editor = sp.edit();
			editor.putInt(Constants.LOGINSTATEOPENFIRE, 1);
			editor.commit();
//			Intent intent = new Intent();
//			if (loginConfig.isFirstStart()) {// 如果是首次启动
//				intent.setClass(context, GuideViewActivity.class);
//				loginConfig.setFirstStart(false);
//			} else {
//				intent.setClass(context, MainActivity.class);
//				intent.setClass(context, ContacterMainActivity.class);
//			intent.setClass(context, ScannerActivity.class);	
//			}
				
			activitySupport.saveLoginConfig(loginConfig);// 保存用户配置信息
			activitySupport.startService(); // 初始化各项服务
			//启动心跳包发送
			AlarmScheduler scheduler = new AlarmScheduler(context);
			scheduler.rescheduleSyncLocation(15000);
//			context.startActivity(intent);
//			
//			((Activity) context).finish();
			break;
		case Constant.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.message_invalid_username_password),
					Toast.LENGTH_SHORT).show();
			break;
		case Constant.SERVER_UNAVAILABLE:// 服务器连接失败
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.message_server_unavailable),
					Toast.LENGTH_SHORT).show();
			break;
		case Constant.LOGIN_ERROR:// 未知异常
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.unrecoverable_error), Toast.LENGTH_SHORT)
					.show();
			break;
		}
		super.onPostExecute(result);
	}
	// 登录
	private Integer login() {
		String username = loginConfig.getUsername();
		String password = loginConfig.getPassword();		
		AppLog.i(TAG, "-------login---------------username="+username+"-----password="+password);
		try {
			activitySupport.saveLoginConfig(loginConfig);
			AppLog.v(TAG, "post:" + loginConfig.getXmppHost());
			XMPPConnection connection = XmppConnectionManager.getInstance(context)
					.getConnection();
			connection.connect();
			connection.login(username, password); // 登录
			OfflineMsgManager.getInstance(activitySupport).dealOfflineMsg(connection);//处理离线消息
			connection.sendPacket(new Presence(Presence.Type.available));
//			if (loginConfig.isNovisible()) {// 隐身登录
//				Presence presence = new Presence(Presence.Type.unavailable);
//				Collection<RosterEntry> rosters = connection.getRoster()
//						.getEntries();
//				for (RosterEntry rosterEntry : rosters) {
//					presence.setTo(rosterEntry.getUser());
//					connection.sendPacket(presence);
//				}
//			}
			loginConfig.setUsername(username);
			if (loginConfig.isRemember()) {// 保存密码
				loginConfig.setPassword(password);
			} else {
				loginConfig.setPassword("");
			}
//			loginConfig.setOnline(true);
			loginConfig.setXmppServiceName(connection.getServiceName());
//			添加连接监听
//			connection.addConnectionListener(XmppConnectionManager.xmppConnectionListener);
			return Constant.LOGIN_SECCESS;
		} catch (Exception xee) {
			AppLog.v(TAG, "登陆异常！");
			SharedPreferences sp = context.getSharedPreferences(Constants.SHAREUSER,
					context.MODE_WORLD_WRITEABLE);
		    Editor editor = sp.edit();
			editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
			editor.commit();
			if (xee instanceof XMPPException) {
				XMPPException xe = (XMPPException) xee;
				final XMPPError error = xe.getXMPPError();
				int errorCode = 0;
				if (error != null) {
					errorCode = error.getCode();
				}
				if (errorCode == 401) {
					return Constant.LOGIN_ERROR_ACCOUNT_PASS;
				}else if (errorCode == 403) {
					return Constant.LOGIN_ERROR_ACCOUNT_PASS;
				} else {
					return Constant.SERVER_UNAVAILABLE;
				}
			} else {
				return Constant.LOGIN_ERROR;
			}
		}
	}
	
	private void showProgress(){
    	if(mProgressDlg == null){
    		mProgressDlg = Utils.createLoadingDialog(context, "请稍候。。。");
    		mProgressDlg.setCancelable(true);
    		mProgressDlg.setCanceledOnTouchOutside(false);
    		mProgressDlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
   			 	public void onCancel(DialogInterface dialog) {
   			 		Constants.ifCancelProgress++;
   			 		mProgressDlg.dismiss();
   			 		mProgressDlg = null;
   			 	}
    		});
    		mProgressDlg.show();
    	}
    }
    
    private void dismissProgress(){
    	if(mProgressDlg != null){
			mProgressDlg.dismiss();
			mProgressDlg = null;
		}
    }
}
