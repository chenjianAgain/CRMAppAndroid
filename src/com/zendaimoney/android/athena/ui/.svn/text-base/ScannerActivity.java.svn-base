package com.zendaimoney.android.athena.ui;

import org.jivesoftware.smack.XMPPConnection;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
//import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.ExitApplication;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.Utils;
import com.zendaimoney.android.athena.httputil.HttpUtil;
import com.zendaimoney.android.athena.httputil.UrlStrings;
import com.zendaimoney.android.athena.im.ActivitySupport;
import com.zendaimoney.android.athena.im.LoginTask;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.model.LoginConfig;
import com.zendaimoney.android.athena.im.service.AlarmScheduler;
import com.zendaimoney.android.athena.im.service.ConnectListenerService;
import com.zendaimoney.android.athena.parsejson.ConfirmParse;
import com.zendaimoney.android.athena.parsejson.CustomerNumParse;
import com.zendaimoney.android.athena.parsejson.RelevanceCustomerParse;
import com.zendaimoney.android.athena.parsejson.RemindInfoParse;
import com.zendaimoney.android.athena.parsejson.ValidateAccountParse;
import com.zendaimoney.android.athena.update.UpdateApp;

public class ScannerActivity extends ActivitySupport {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private TextView salaryWarnNum;
	private TextView brithWarnNum;
	private TextView userNum; // 客户总数
	private Button scanBtn;
	private Button exitBtn;
	private RelativeLayout salary;
	private RelativeLayout brith;
	private RelativeLayout usernum;
	private RelativeLayout zChat;
	private ImageView unreadIcon;
	private TextView chatNum;

	private int salaryRemindNum = 0; // 投资到期提醒信息数量
	private int brithRemindNum = 0; // 生日提醒信息数量
	private String customerNum = "0"; // 客户总数
	private String responseStr; // 网络访问时的返回参数
	private JSONObject params; // 接口参数
	private int totalPageNum; // 提醒信息总页数

	private String qrCodeStr; // 扫描到的二维码结果
	private Dialog mProgressDlg;

	private CustomerNumParse customNumParse = new CustomerNumParse();
	private RemindInfoParse remindInfoParse = new RemindInfoParse(); // 提醒信息解析类
	private RelevanceCustomerParse revlevancCustomerParse = new RelevanceCustomerParse(); // 关联客户解析类
	private ValidateAccountParse validateAccountParse = new ValidateAccountParse(); // 扫描登陆二维码解析类
	private ConfirmParse confirmParse = new ConfirmParse();

	UpdateApp updateApp;
	
	public static int unreadSum = 0;
	
	private LoginConfig loginConfig;
	
	public XMPPConnection connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.scanner_main);

		updateApp = new UpdateApp(ScannerActivity.this);
		updateApp.checkVersion();

//		initContentView();
//		setViewHw();
//		getRemindInfoNum();
//		Log.d("TAG", "Scanner oncreate!");
	}

	/**
	 * 设置布局中每个空间宽高度
	 */
	public void setViewHw() {
		RelativeLayout title = (RelativeLayout) findViewById(R.id.title);
		RelativeLayout salaryRemind = (RelativeLayout) findViewById(R.id.salary);
		RelativeLayout brithRemind = (RelativeLayout) findViewById(R.id.brith);
		LinearLayout scanLogo = (LinearLayout) findViewById(R.id.scanlogo);
		Button scanBtn = (Button) findViewById(R.id.scanbtn);
		TextView salaryTitle = (TextView) findViewById(R.id.salary_title);
		TextView brithTitle = (TextView) findViewById(R.id.brith_title);
		TextView usenumTitle = (TextView) findViewById(R.id.usenum_title);
		TextView chatTitle = (TextView)findViewById(R.id.chat_title);
	
		LinearLayout.LayoutParams params;
		// title高度
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(params);
		// 投资提醒宽高度
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (Constants.displayHeight * 0.04f + 0.5f), 0,
				(int) (Constants.displayHeight * 0.04f + 0.5f), 0);
		salaryRemind.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (Constants.displayHeight * 0.04f + 0.5f), 0,
				(int) (Constants.displayHeight * 0.04f + 0.5f), 0);
		brithRemind.setLayoutParams(params);
		usernum.setLayoutParams(params);
		zChat.setLayoutParams(params);

		// 退出按钮设置
		RelativeLayout.LayoutParams paramsTitle = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		paramsTitle.setMargins(0, 0,
				(int) (Constants.displayHeight * 0.02f + 0.5f), 0);
		paramsTitle.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsTitle.addRule(RelativeLayout.CENTER_VERTICAL);
		exitBtn.setLayoutParams(paramsTitle);

		paramsTitle = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsTitle.setMargins((int) (Constants.displayWidth * 0.1f + 0.5f), 0,
				0, 0);
		paramsTitle.addRule(RelativeLayout.CENTER_VERTICAL);
		salaryTitle.setLayoutParams(paramsTitle);
		paramsTitle = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsTitle.setMargins((int) (Constants.displayWidth * 0.1f + 0.5f), 0,
				0, 0);
		paramsTitle.addRule(RelativeLayout.CENTER_VERTICAL);
		brithTitle.setLayoutParams(paramsTitle);
		paramsTitle = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsTitle.setMargins((int) (Constants.displayWidth * 0.1f + 0.5f), 0,
				0, 0);
		paramsTitle.addRule(RelativeLayout.CENTER_VERTICAL);
		usenumTitle.setLayoutParams(paramsTitle);
		paramsTitle = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsTitle.setMargins((int) (Constants.displayWidth * 0.1f + 0.5f), 0,
				0, 0);
		paramsTitle.addRule(RelativeLayout.CENTER_VERTICAL);
		chatTitle.setLayoutParams(paramsTitle);

		// logo款高度
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.3f + 0.5f));
		params.setMargins(0, (int) (Constants.displayHeight * 0.02f + 0.5f), 0,
				(int) (Constants.displayHeight * 0.02f + 0.5f));
		scanLogo.setLayoutParams(params);

		// button宽高度
		params = new LinearLayout.LayoutParams(
				(int) (Constants.displayWidth * 0.8f + 0.5f),
				(int) (Constants.displayHeight * 0.1f + 0.5f));
		scanBtn.setLayoutParams(params);
	}

	/**
	 * 初始化控件
	 */
	public void initContentView() {
		salaryWarnNum = (TextView) findViewById(R.id.salarywran_num);
		brithWarnNum = (TextView) findViewById(R.id.brithwran_num);
		userNum = (TextView) findViewById(R.id.usenum);
		exitBtn = (Button) findViewById(R.id.exitbtn);
		usernum = (RelativeLayout) findViewById(R.id.usernum);
		zChat = (RelativeLayout) findViewById(R.id.chat);
		unreadIcon = (ImageView)findViewById(R.id.unread_icom);
		chatNum = (TextView)findViewById(R.id.chat_num);

		exitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showExitDialog();
			}
		});
		scanBtn = (Button) findViewById(R.id.scanbtn);
		scanBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent();
				 intent.setClass(ScannerActivity.this,
				 MipcaActivityCapture.class);
				 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//				Intent mIntent = new Intent(ScannerActivity.this,
//						LoginActivity.class);
//				startActivity(mIntent);
			}
		});
		salary = (RelativeLayout) findViewById(R.id.salary); // 投资到期提醒的点击事件处理
		brith = (RelativeLayout) findViewById(R.id.brith); // 客户生日提醒的点击事件处理
		salary.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ScannerActivity.this,
						SalaryRemindListActivity.class);
				if (salaryRemindNum % 10 > 0) {
					totalPageNum = salaryRemindNum / 10 + 1;
				} else {
					totalPageNum = salaryRemindNum / 10;
				}
				i.putExtra("totalPageNum", totalPageNum);
//				Log.i("总页数", totalPageNum + "");
				startActivity(i);
			}
		});
		brith.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ScannerActivity.this,
						BrithRemindListActivity.class);
				if (brithRemindNum % 10 > 0) {
					totalPageNum = brithRemindNum / 10 + 1;
				} else {
					totalPageNum = brithRemindNum / 10;
				}
				i.putExtra("totalPageNum", totalPageNum);
//				Log.v("总页数", totalPageNum + "");
				startActivity(i);
			}
		});
		usernum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ScannerActivity.this,
						CustomerListActivity.class);
				if (Integer.parseInt(customerNum) % 10 > 0) {
					totalPageNum = Integer.parseInt(customerNum) / 10 + 1;
				} else {
					totalPageNum = Integer.parseInt(customerNum) / 10;
				}
				i.putExtra("totalPageNum", totalPageNum);
				startActivity(i);
			}
		});

		zChat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				connection = XmppConnectionManager.getInstance(ScannerActivity.this)
						.getConnection();
//				if(connection == null || !connection.isConnected()){
//					Toast.makeText(ScannerActivity.this, "聊天服务器未连接", Toast.LENGTH_LONG).show();
//				}else{
					Intent i = new Intent(ScannerActivity.this,
							ChatListActivity.class);
					if (Integer.parseInt(customerNum) % 10 > 0) {
						totalPageNum = Integer.parseInt(customerNum) / 10 + 1;
					} else {
						totalPageNum = Integer.parseInt(customerNum) / 10;
					}
					i.putExtra("totalPageNum", totalPageNum);
					startActivity(i);
//				}
			}
		});
	}

	public void getRemindInfoNum() {
		try {
			SharedPreferences sp = getSharedPreferences(Constants.SHAREUSER,
					MODE_WORLD_WRITEABLE);
			params = new JSONObject();
			params.put(Constants.ID, sp.getString(Constants.USERID, ""));
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
				responseStr = HttpUtil.getContent(ScannerActivity.this,
						UrlStrings.getRemindInfos, params);
				if (responseStr != null && !responseStr.equals("")) {
					remindInfoParse.parseResponse(responseStr);
					if (remindInfoParse.status == 0) {
						mHandler.sendEmptyMessage(Constants.GETREMINDSUC);
						salaryRemindNum = remindInfoParse.salaryRemind;
						brithRemindNum = remindInfoParse.brithRemind;
					} else {
						mHandler.sendEmptyMessage(Constants.GETREMINDFAIL);
					}
				} else {
					mHandler.sendEmptyMessage(Constants.NETFAIL);
				}

				responseStr = HttpUtil.getContent(ScannerActivity.this,
						UrlStrings.getUserNumInfos, params);
//				Log.i("MainActivity", "res:" + responseStr);
				if (responseStr != null && !responseStr.equals("")) {
					customNumParse.parseResponse(responseStr);
					if (customNumParse.status == 0) {
						customerNum = Integer.toString(customNumParse.count);
						mHandler.sendEmptyMessage(Constants.GETCUSTOMNUNSUC);
					} else {
						mHandler.sendEmptyMessage(Constants.GETCUSTOMNUNFAIL);
					}
				}
				dismissProgress();
			}
		}).start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				// salaryWarnNum.setText(bundle.getString("result"));
				// brithWarnNum.setImageBitmap((Bitmap)
				// data.getParcelableExtra("bitmap"));
				qrCodeStr = bundle.getString("result");
				/**
				 * 扫描登陆二维码
				 */
				if (qrCodeStr.contains("LOGIN")) {
					qrCodeStr = qrCodeStr.substring(5, qrCodeStr.length());
					validateAccount();
				}
				/**
				 * 关联客户二维码
				 */
				else if (qrCodeStr.contains("CUSTOMER")) {
					qrCodeStr = qrCodeStr.substring(8, qrCodeStr.length());
					relevanceCustomer();
				} else {
					new AlertDialog.Builder(ScannerActivity.this)
							.setTitle(getString(R.string.remind))
							.setMessage("当前扫描的二维码不正确，将不做任何处理")
							.setPositiveButton(getString(R.string.ok),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											arg0.dismiss();
										}
									}).show();
				}
			}
			break;
		}
	}

	/**
	 * 关联客户
	 */
	public void relevanceCustomer() {
		try {
			SharedPreferences sp = getSharedPreferences(Constants.SHAREUSER,
					MODE_WORLD_WRITEABLE);
			params = new JSONObject();
			params.put(Constants.CARDCODE, qrCodeStr);
			params.put(Constants.ID, sp.getString(Constants.USERID, ""));
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
				responseStr = HttpUtil.getContent(ScannerActivity.this,
						UrlStrings.relevanceCustomer, params);
//				Log.i("MainActivity", "res:" + responseStr);
				if (responseStr != null && !responseStr.equals("")) {
					revlevancCustomerParse.parseResponse(responseStr);
					if (revlevancCustomerParse.status == 0) {
						mHandler.sendEmptyMessage(Constants.RELEVANCESUC);
					} else {
						mHandler.sendEmptyMessage(Constants.RELEVANCEFAIL);
					}
				} else {
					mHandler.sendEmptyMessage(Constants.NETFAIL);
				}
				dismissProgress();
			}
		}).start();
	}

	/**
	 * 扫描登陆
	 */
	public void validateAccount() {
		try {
			SharedPreferences sp = getSharedPreferences(Constants.SHAREUSER,
					MODE_WORLD_WRITEABLE);
			params = new JSONObject();
			params.put(Constants.UERNAME, sp.getString(Constants.UERNAME, ""));
			params.put(Constants.DIMECODE, qrCodeStr);
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
				responseStr = HttpUtil.getContent(ScannerActivity.this,
						UrlStrings.validateAccount, params);
				AppLog.v("MainActivity", "res:" + responseStr);
//				Log.v("MainActivity", "res:" + responseStr);
				if (responseStr != null && !responseStr.equals("")) {
					validateAccountParse.parseResponse(responseStr);
					if (validateAccountParse.status == 0) {
						mHandler.sendEmptyMessage(Constants.VALIDATEACCOUNTSUC);
					} else {
						mHandler.sendEmptyMessage(Constants.VALIDATEACCOUNTFAIL);
					}
				} else {
					mHandler.sendEmptyMessage(Constants.NETFAIL);
				}
				dismissProgress();
			}
		}).start();
	}

	/**
	 * 确认扫描二维码登陆
	 */
	private void confirmLogin() {
		try {
			params = new JSONObject();
			params.put(Constants.DIMECODE, qrCodeStr);
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
				responseStr = HttpUtil.getContent(ScannerActivity.this,
						UrlStrings.confirm, params);
//				Log.i("MainActivity", "res:" + responseStr);
				if (responseStr != null && !responseStr.equals("")) {
					confirmParse.parseResponse(responseStr);
					if (confirmParse.status == 0) {
						mHandler.sendEmptyMessage(Constants.CONFIRMSUC);

					} else {
						mHandler.sendEmptyMessage(Constants.CONFIRMFAIL);
					}
				} else {
					mHandler.sendEmptyMessage(Constants.NETFAIL);
				}
				dismissProgress();
			}
		}).start();
	}

	private void cancelLogin() {
		try {
			params = new JSONObject();
			params.put(Constants.DIMECODE, qrCodeStr);
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
				responseStr = HttpUtil.getContent(ScannerActivity.this,
						UrlStrings.cancelLogin, params);
//				Log.i("MainActivity", "res:" + responseStr);
				if (responseStr != null && !responseStr.equals("")) {
					confirmParse.parseResponse(responseStr);
					if (confirmParse.status == 0) {
						// mHandler.sendEmptyMessage(Constants.CANCELLOGINSUC);

					} else {
						mHandler.sendEmptyMessage(Constants.CANCELLOGINFAIL);
					}
				} else {
					mHandler.sendEmptyMessage(Constants.NETFAIL);
				}
				dismissProgress();
			}
		}).start();
	}

	private void showSucDialog(int flag) {
		String message = null;
		switch (flag) {
		case Constants.RELEVANCESUC:
			message = getString(R.string.relevancesuc);
			break;
		case Constants.RELEVANCEFAIL:
			message = revlevancCustomerParse.respDesc
					+ getString(R.string.relevancefail);
			break;
		case Constants.VALIDATEACCOUNTSUC:
			message = getString(R.string.confirmval);
			break;
		case Constants.VALIDATEACCOUNTFAIL:
			message = validateAccountParse.respDesc
					+ getString(R.string.valfail);
			break;
		case Constants.CONFIRMSUC:
			message = getString(R.string.login_suc_pc);
			break;
		case Constants.CONFIRMFAIL:
			message = getString(R.string.login_pc_fail);
			break;
		case Constants.CANCELLOGINFAIL:
			message = getString(R.string.cancel_login_fail);
			break;
		}
		if (flag == Constants.VALIDATEACCOUNTSUC) {
			new AlertDialog.Builder(ScannerActivity.this)
					.setTitle(getString(R.string.remind))
					.setMessage(message)
					.setPositiveButton(getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									confirmLogin();
									arg0.dismiss();
								}
							})
					.setNegativeButton(getString(R.string.cancal),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									cancelLogin();
									dialog.dismiss();
								}
							}).show();
		} else {
			new AlertDialog.Builder(ScannerActivity.this)
					.setTitle(getString(R.string.remind))
					.setMessage(message)
					.setPositiveButton(getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
								}
							}).show();
		}
	}

	public void showExitDialog() {
		new AlertDialog.Builder(ScannerActivity.this)
				.setTitle(getString(R.string.remind))
				.setMessage("是否确认安全退出？")
				.setPositiveButton(getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								SharedPreferences sp = getSharedPreferences(
										Constants.SHAREUSER,
										MODE_WORLD_WRITEABLE);
								if (sp.getInt(Constants.LOGINSTATE, 0) == 1) {
									Editor editor = sp.edit();
									editor.putInt(Constants.LOGINSTATE, 0);
									editor.putInt(Constants.LOGINSTATEOPENFIRE, 0);
									editor.commit();
//									finish();
								}
								//停止心跳包定时器
								AlarmScheduler scheduler = new AlarmScheduler(ScannerActivity.this);
								scheduler.stop();
								
								stopService();
								ExitApplication.getInstance().exit();
//								System.exit(0);
							}
						})
				.setNegativeButton(getString(R.string.no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();
	}

	/**
	 * 线程通信handler
	 */
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (Constants.ifCancelProgress <= 0) {
				switch (msg.what) {
				case Constants.GETCUSTOMNUNSUC:
//					Log.v("TAG", "客户数量" + customerNum);
					userNum.setText(customerNum);
					break;
				case Constants.GETCUSTOMNUNFAIL:
					Toast.makeText(ScannerActivity.this,
							getString(R.string.getcustomfail),
							Toast.LENGTH_LONG).show();
					break;
				case Constants.RELEVANCESUC:
					showSucDialog(Constants.RELEVANCESUC);
					break;
				case Constants.RELEVANCEFAIL:
					showSucDialog(Constants.RELEVANCEFAIL);
					break;
				case Constants.GETREMINDSUC:
					/**
					 * 设置提醒信息条数
					 */
					salaryWarnNum.setText(remindInfoParse.salaryRemind + "");
					brithWarnNum.setText(remindInfoParse.brithRemind + "");
					break;
				case Constants.GETREMINDFAIL:
					Toast.makeText(ScannerActivity.this,
							remindInfoParse.respDesc, Toast.LENGTH_LONG).show();
					break;
				case Constants.CONFIRMSUC:
					showSucDialog(Constants.CONFIRMSUC);
					break;
				case Constants.CONFIRMFAIL:
					showSucDialog(Constants.CONFIRMFAIL);
					break;
				case Constants.VALIDATEACCOUNTSUC:
					showSucDialog(Constants.VALIDATEACCOUNTSUC);
					break;
				case Constants.VALIDATEACCOUNTFAIL:
					showSucDialog(Constants.VALIDATEACCOUNTFAIL);
					break;
				case Constants.CANCELLOGINFAIL:
					showSucDialog(Constants.CANCELLOGINFAIL);
					break;
				case Constants.NETFAIL:
					Toast.makeText(ScannerActivity.this,
							getString(R.string.netfail), Toast.LENGTH_LONG)
							.show();
					break;
				}
			} else {
				Constants.ifCancelProgress--;
			}
		}
	};

	private void showProgress() {
		if (mProgressDlg == null) {
			mProgressDlg = Utils.createLoadingDialog(ScannerActivity.this,
					"请稍候。。。");
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		// Log.i(TAG,
		// "------------------onCreateOptionsMenu------------------------");
		return true;
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		int keyCode = event.getKeyCode();
		if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){			
			Constants.isMainReturn = true;
			finish();
		}
		return super.dispatchKeyEvent(event);
	}
	
	private void startActivitySafely(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
//			Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
		} catch (SecurityException e) {
			e.printStackTrace();
//			Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onPause() {
		unregisterReceiver(receiver);
//		Log.d("TAG", "Scanner pause!");
		super.onPause();
	}
	
	@Override
	public void onDestroy(){
//		Log.d("TAG", "Scanner destroy!");
		super.onDestroy();
	}
	
	@Override
	public void onStop(){
//		Log.d("TAG", "Scanner stop!");
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		AppLog.d("TAG", "Scanner onresume!");
//		setContentView(R.layout.scanner_main);
		
		LayoutInflater inflater = LayoutInflater.from(ScannerActivity.this);
		View view = inflater.inflate(R.layout.scanner_main, null);
		setContentView(view);
		
		connection = XmppConnectionManager.getInstance(ScannerActivity.this)
				.getConnection();
		loginConfig = getLoginConfig();
		
		AppLog.e("TAG", "connect:" + connection);
		AppLog.e("TAG", "connect:" + connection.isConnected());
		AppLog.e("TAG", "isAnonymous:" + connection.isAnonymous());
		AppLog.e("TAG", "isAuthenticated:" + connection.isAuthenticated());
		AppLog.e("TAG", "isSecureConnection:" + connection.isSecureConnection());
		AppLog.e("TAG", "isSendPresence:" + connection.isSendPresence());
		AppLog.e("TAG", "isSocketClosed:" + connection.isSocketClosed());
		AppLog.e("TAG", "isUsingCompression:" + connection.isUsingCompression());
		
		if(connection == null || !connection.isConnected()){
			SharedPreferences sp = getSharedPreferences(Constants.SHAREUSER,
					MODE_WORLD_WRITEABLE);
			
			if(sp.getInt(Constants.LOGINSTATEOPENFIRE, 0) == 1){
//				Log.v("TAG", "发送重连广播");
//				Intent intent = new Intent(Constant.RECONNECT_ACTION);
//				ScannerActivity.this.sendBroadcast(intent);
			}
			else{
				if(ConnectListenerService.connectRun){
					return;
				}else{
					loginConfig.setXmppHost(Constant.XMPP_HOST);
					XmppConnectionManager.getInstance(context).init(loginConfig);
			
					loginConfig.setPassword(sp.getString(Constants.PASSWD, ""));
					loginConfig.setUsername(sp.getString(Constants.UERNAME, ""));
					LoginTask loginTask = new LoginTask(ScannerActivity.this,
							loginConfig);
					loginTask.execute();
				}
			}
		}
		
		initContentView();
		setViewHw();
		getRemindInfoNum();
		
		if(unreadSum > 0){
			unreadIcon.setVisibility(View.VISIBLE);
		}else{
			unreadIcon.setVisibility(View.GONE);
		}
		chatNum.setText(unreadSum + "");
		/**
		 * 注册广播信息
		 */
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.NEW_MESSAGE_ACTION);
		registerReceiver(receiver, filter);
	}
	/**
	 * 收到消息的广播
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constant.NEW_MESSAGE_ACTION.equals(action)) {
				unreadIcon.setVisibility(View.VISIBLE);
				chatNum.setText(unreadSum + "");
			}
		}
	};
}
