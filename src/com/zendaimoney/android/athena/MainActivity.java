package com.zendaimoney.android.athena;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.zendaimoney.android.athena.httputil.HttpUtil;
import com.zendaimoney.android.athena.httputil.UrlStrings;
import com.zendaimoney.android.athena.im.ActivitySupport;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.model.LoginConfig;
import com.zendaimoney.android.athena.parsejson.LoginParse;
import com.zendaimoney.android.athena.ui.ScannerActivity;

public class MainActivity extends ActivitySupport {
	private EditText account;
	private EditText passwd;
	private Button loginBtn;

	private JSONObject params; // 接口参数
	private String responseStr; // 网络访问时的返回参数
	private String userStr; // 输入的账号
	private String pwdStr; // 输入的密码

	private int loginState = 0;
	private int openfireLoginState = 0;

	private Dialog mProgressDlg;
	private LoginParse loginParse = new LoginParse(); // 登陆返回结果解析类

	public static String userId = null;

	private LoginConfig loginConfig;
	private String notiFlag = ""; // 是否为下线通知的跳转

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppLog.v("ahga", "main onCreate");
		setContentView(R.layout.activity_main);

//		loginConfig = getLoginConfig();
//		XMPPConnection connection = XmppConnectionManager.getInstance(MainActivity.this)
//				.getConnection();
//		loginConfig.setXmppHost(Constant.XMPP_HOST);
//		XmppConnectionManager.getInstance(context).init(loginConfig);
//		AppLog.e("TAG", "connect:" + connection);
//		AppLog.e("TAG", "connect:" + connection.isConnected());
//		AppLog.e("TAG", "isAnonymous:" + connection.isAnonymous());
//		AppLog.e("TAG", "isAuthenticated:" + connection.isAuthenticated());
//		AppLog.e("TAG", "isSecureConnection:" + connection.isSecureConnection());
//		AppLog.e("TAG", "isSendPresence:" + connection.isSendPresence());
//		AppLog.e("TAG", "isSocketClosed:" + connection.isSocketClosed());
//		AppLog.e("TAG", "isUsingCompression:" + connection.isUsingCompression());
		
//		stopService();
		SharedPreferences sp = getSharedPreferences(
				Constants.SHAREUSER,
				MODE_WORLD_WRITEABLE);
		Editor editor = sp.edit();
		editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
		editor.commit();
		
		try {
			notiFlag = getIntent().getStringExtra("notiflag");
			if (notiFlag.equals("1")) {
				showOfflineDialog();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		getDisHw();
		// checkIfLogin();
		ExitApplication.getInstance().addActivity(this);
		loginConfig = getLoginConfig();
		
		SharedPreferences sp1 = getSharedPreferences(Constants.SHAREUSER,
				MODE_WORLD_WRITEABLE);
	    preferences = getSharedPreferences(Constant.LOGIN_SET, 0);
	    if(sp.getInt(Constants.LOGINSTATE, 0) != 1){
	    	loginConfig.setXmppHost(Constant.XMPP_HOST);
	    	XmppConnectionManager.getInstance(context).init(loginConfig);
	    }
	}

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
//		Log.v("ahga", "main resume");

		if (Constants.isMainReturn) {
			moveTaskToBack(true);
			Constants.isMainReturn = false;
//			finish();
		} else {
			// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			// .detectDiskReads().detectDiskWrites().detectNetwork()
			// .penaltyLog().build());
			// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			// .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
			// .penaltyLog().penaltyDeath().build());

			// 校验SD卡
//			checkMemoryCard();
			// 检测网络和版本
//			validateInternet();
			// 初始化xmpp配置
			// XmppConnectionManager.getInstance(context).init(loginConfig);
			// loginConfig.setXmppHost("192.16.220.190");
//			loginConfig.setXmppHost(Constant.XMPP_HOST);
			// loginConfig.setXmppHost("192.16.10.247");
			// loginConfig.setXmppHost("192.16.10.149");
//			XmppConnectionManager.getInstance(context).init(loginConfig);
			// MainActivity.this.saveLoginConfig(loginConfig);
			initContentView();
			setViewHw();
			checkIfLogin();

			if (loginState == 1 /* && openfireLoginState == 1 */) {
				Intent i = new Intent(MainActivity.this, ScannerActivity.class);
				startActivity(i);
				// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivityForResult(i, 20000);
			}
		}

		super.onResume();
	}

	/**
	 * 获取屏幕的宽高
	 */
	public void getDisHw() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		Constants.displayWidth = displayMetrics.widthPixels;
		Constants.displayHeight = displayMetrics.heightPixels;
	}

	/**
	 * 设置屏幕中各个控件的宽高度
	 */
	public void setViewHw() {
		RelativeLayout title = (RelativeLayout) findViewById(R.id.title);
		LinearLayout logo = (LinearLayout) findViewById(R.id.logo);
		TableLayout input = (TableLayout) findViewById(R.id.input);
		TableRow userInput = (TableRow) findViewById(R.id.user_input);
		TableRow pwdInput = (TableRow) findViewById(R.id.pwd_input);
		TextView userImg = (TextView) findViewById(R.id.user_img);
		TextView pwdImg = (TextView) findViewById(R.id.pwd_img);

		LinearLayout.LayoutParams params;
		// title高度
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(params);
		// logo高度
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.3f + 0.5f));
		logo.setLayoutParams(params);
		// 输入框高度
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.3f + 0.5f));
		input.setLayoutParams(params);
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.15f + 0.5f));
		params.setMargins((int) (Constants.displayWidth * 0.05f + 0.5f),
				(int) (Constants.displayWidth * 0.02f + 0.5f), 0,
				(int) (Constants.displayWidth * 0.02f + 0.5f));
		userInput.setLayoutParams(params);
		pwdInput.setLayoutParams(params);

		// 设置输入框图标位置
		// params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// params.setMargins((int)(Constants.displayHeight * 0.1f + 0.5f), 0,
		// (int)(Constants.displayHeight * 0.05f + 0.5f), 0);
		// userImg.setLayoutParams(params);
		// pwdImg.setLayoutParams(params);
		// userInput.setPadding(0, (int)(Constants.displayHeight * 0.01f +
		// 0.5f), 0, (int)(Constants.displayHeight * 0.01f + 0.5f));
		// pwdInput.setPadding(0, (int)(Constants.displayHeight * 0.01f + 0.5f),
		// 0, (int)(Constants.displayHeight * 0.01f + 0.5f));

		// button高度和宽度
		params = new LinearLayout.LayoutParams(
				(int) (Constants.displayWidth * 0.8f + 0.5f),
				(int) (Constants.displayHeight * 0.1f + 0.5f));
		params.setMargins(0, (int) (Constants.displayHeight * 0.05f + 0.5f), 0,
				0);
		loginBtn.setLayoutParams(params);
	}

	/**
	 * 检查是否已经登陆
	 */
	public void checkIfLogin() {
		SharedPreferences sp = getSharedPreferences(Constants.SHAREUSER,
				MODE_WORLD_WRITEABLE);
		loginState = sp.getInt(Constants.LOGINSTATE, 0);
		// openfireLoginState = sp.getInt(Constants.LOGINSTATEOPENFIRE, 0);
	}

	/**
	 * 初始化控件
	 */
	public void initContentView() {
		account = (EditText) findViewById(R.id.account);
		passwd = (EditText) findViewById(R.id.passwd);
		loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userStr = account.getText().toString();
				pwdStr = passwd.getText().toString();
				pwdStr = MD5.MD5(pwdStr);
//				Log.v("TAG", "passwordMd5:" + pwdStr);

				if (userStr == null || "".equals(userStr)) {
					Toast.makeText(MainActivity.this,
							getString(R.string.usernull), Toast.LENGTH_LONG)
							.show();
				}
				if (pwdStr == null || "".equals(pwdStr)) {
					Toast.makeText(MainActivity.this,
							getString(R.string.pwdnull), Toast.LENGTH_LONG)
							.show();
				} else {
					loginAccount(pwdStr, userStr);
					// pwdStr = "0000";
					// userStr = "test05";
					// loginConfig.setPassword(pwdStr);
					// loginConfig.setUsername(userStr);
					// LoginTask loginTask = new LoginTask(MainActivity.this,
					// loginConfig);
					// loginTask.execute();
				}
				// Intent i = new Intent(MainActivity.this,
				// LoginActivity.class);
				// startActivity(i);
			}
		});
	}

	/**
	 * 登陆系统
	 * 
	 * @param passwd
	 *            密码
	 * @param account
	 *            账号
	 */
	public void loginAccount(String passwd, String account) {
		params = new JSONObject();
		try {

			params.put(Constants.UERNAME, account);
			params.put(Constants.PWD, passwd);
//			Log.i("TAg", "用户名密码：" + params);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 开始进入网络访问
		 */
		showProgress();
		new Thread(new Runnable() {
			public void run() {
				responseStr = HttpUtil.getContent(MainActivity.this,
						UrlStrings.login, params);
				// Thread.this.
//				Log.i("MainActivity", "res:" + responseStr);
				if (responseStr != null && !responseStr.equals("")) {
					loginParse.parseResponse(responseStr);
					if (loginParse.status == 0) {
						mHandler.sendEmptyMessage(Constants.LOGINSUC);
					} else {
						mHandler.sendEmptyMessage(Constants.CONFIRMFAIL);
					}
				} else {
					mHandler.sendEmptyMessage(Constants.NETFAIL);
				}
			}
		}).start();
		// Intent i = new Intent(MainActivity.this, ScannerActivity.class);
		// startActivity(i);
	}

	/**
	 * 线程通信handler
	 */
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Intent i = new Intent(MainActivity.this, ScannerActivity.class);
			// startActivity(i);
			// finish();
			if (Constants.ifCancelProgress <= 0) {
				switch (msg.what) {
				case Constants.LOGINSUC:
					userId = loginParse.id;
					SharedPreferences sp = getSharedPreferences(
							Constants.SHAREUSER, MODE_WORLD_WRITEABLE);
					Editor editor = sp.edit();
					editor.putString(Constants.UERNAME, userStr);
					editor.putString(Constants.PASSWD, pwdStr);
					editor.putString(Constants.USERID, userId);
					editor.putInt(Constants.LOGINSTATE, 1);
					editor.commit();
					Intent i = new Intent(MainActivity.this,
							ScannerActivity.class);
					startActivity(i);
					// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// startActivityForResult(i, 20000);
					// finish();
					// loginConfig.setPassword(pwdStr);
					// loginConfig.setUsername(userStr);
					// LoginTask loginTask = new LoginTask(MainActivity.this,
					// loginConfig);
					// loginTask.execute();
					break;
				case Constants.CONFIRMFAIL:
					Toast.makeText(MainActivity.this, loginParse.respDesc,
							Toast.LENGTH_LONG).show();
					break;
				case Constants.NETFAIL:
					Toast.makeText(MainActivity.this,
							getString(R.string.netfail), Toast.LENGTH_LONG)
							.show();
					break;
				}

				dismissProgress();
			} else {
				Constants.ifCancelProgress--;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		// Log.i(TAG, "--------onKey-------------keyCode="+event.getKeyCode());
		int keyCode = event.getKeyCode();
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// SharedPreferences sp = getSharedPreferences(
			// Constants.SHAREUSER,
			// MODE_WORLD_WRITEABLE);
			// if (sp.getInt(Constants.LOGINSTATE, 0) == 1) {
			// Editor editor = sp.edit();
			// editor.putInt(Constants.LOGINSTATE, 0);
			// editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
			// editor.commit();
			// // finish();
			// }
			// System.exit(0);

			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
		}
		return super.dispatchKeyEvent(event);
	}

	private void showProgress() {
		if (mProgressDlg == null) {
			mProgressDlg = Utils.createLoadingDialog(this, "请稍候。。。");
			mProgressDlg.setCancelable(true);
			mProgressDlg.setCanceledOnTouchOutside(false);
			mProgressDlg
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
							Constants.ifCancelProgress++;
							mProgressDlg.dismiss();
							mProgressDlg = null;
						}
					});
			mProgressDlg.show();
		}
	}

	private void dismissProgress() {
		if (mProgressDlg != null) {
			mProgressDlg.dismiss();
			mProgressDlg = null;
		}
	}

	public void showOfflineDialog() {
		new AlertDialog.Builder(MainActivity.this)
				.setTitle("警告")
				.setMessage("您的账号在另一台设备上登录,如非本人操作,请确认账号是否安全！")
				.setPositiveButton("重新登录",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.dismiss();
							}
						})
				.setNegativeButton("知道了",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								MainActivity.this.finish();
								System.exit(0);
							}
						}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 20000:
//			Log.v("GAT", "退出");
			// Intent intent = new Intent();
			// intent.setAction(Intent.ACTION_MAIN);
			// intent.addCategory(Intent.CATEGORY_HOME);
			// startActivity(intent);
			moveTaskToBack(true);
			break;
		}
	}

}
