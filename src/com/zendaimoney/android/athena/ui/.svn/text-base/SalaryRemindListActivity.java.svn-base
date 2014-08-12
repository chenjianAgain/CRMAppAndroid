package com.zendaimoney.android.athena.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.ExitApplication;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.Utils;
import com.zendaimoney.android.athena.data.SalaryRemindData;
import com.zendaimoney.android.athena.httputil.HttpUtil;
import com.zendaimoney.android.athena.httputil.UrlStrings;
import com.zendaimoney.android.athena.parsejson.SalaryListParse;

public class SalaryRemindListActivity extends Activity{
	private ListView listView;
	private Button backBtn;
	private TextView remindHead;
	private List<SalaryRemindData> listData = new ArrayList<SalaryRemindData>();   //投资到期提醒列表数据
	private SalaryRemindListAdapter remindListAdapter = null;
	
	private Dialog mProgressDlg;
	private String responseStr; //网络访问时的返回参数
	private JSONObject params;  //接口参数
	private SalaryListParse salaryListParse = new SalaryListParse();//投资到期提醒解析类
	
	private int mTotalPage;
	private int mCurrentPage = 1;
	private boolean isLoading = true;
	private String userId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remind_list);
		mTotalPage = getIntent().getIntExtra("totalPageNum", 0);
		AppLog.i("totalpage", "totalPage:" + mTotalPage);
		initContentView();
		setViewHw();
		initAdapter();
		getListData();
		ExitApplication.getInstance().addActivity(this); 
	}
	
	public void initAdapter(){
		if(listData.size() > 0){
			listData.clear();
		}
		remindListAdapter = new SalaryRemindListAdapter(SalaryRemindListActivity.this, listData, false, R.layout.salary_remind_list_adapter);
		listView.setAdapter(remindListAdapter);
	}
	/**
	 * 初始化控件
	 */
	public void initContentView(){
		remindHead = (TextView)findViewById(R.id.remind_head);
		remindHead.setText(getString(R.string.salary_remind_list));
		listView = (ListView)findViewById(R.id.remindlist);
		listView.setOnScrollListener(new MyScrollListener());
		backBtn = (Button)findViewById(R.id.backbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	public void setViewHw(){
		RelativeLayout title = (RelativeLayout)findViewById(R.id.title);
		LinearLayout.LayoutParams params;
		//title高度
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                (int) (Constants.displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(params);
	}
	
	/**
	 * 获取列表数据（网络访问）
	 */
	public void getListData(){
		isLoading = true;
		/**
		 * 获取用户ID
		 */
		SharedPreferences sp = getSharedPreferences(Constants.SHAREUSER, MODE_WORLD_WRITEABLE);
		userId = sp.getString(Constants.USERID, "");
		try {
			listData = new ArrayList<SalaryRemindData>();
			params = new JSONObject();
			params.put(Constants.ID, userId);
//			params.put(Constants.INFOTYPE, 2);
			params.put(Constants.PAGENO, mCurrentPage);
			params.put(Constants.PAGESIZE, 10);
			AppLog.i("TAG", "userId:" + userId);
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
				responseStr = HttpUtil.getContent(SalaryRemindListActivity.this, UrlStrings.creditList, params);
				AppLog.i("MainActivity", "res:" + responseStr);
				if(responseStr != null && !responseStr.equals("")){
					salaryListParse.parseResponse(responseStr);
					if(salaryListParse.status == 0){
						listData = salaryListParse.remindLists;
						mHandler.sendEmptyMessage(Constants.GETSALARYREMINDSUC);
					}else{
						mHandler.sendEmptyMessage(Constants.GETSALARYREMINDFAIL);
					}
				}else{
					mHandler.sendEmptyMessage(Constants.NETFAIL);
				}
				dismissProgress();
			}
		}).start();
	}
	
	/**
	 * 线程通信handler
	 */
	public Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
//			if()
			switch (msg.what)
			{
			case Constants.GETSALARYREMINDSUC:
				fetchListData();
				break;
			case Constants.GETSALARYREMINDFAIL:
				Toast.makeText(SalaryRemindListActivity.this, salaryListParse.respDesc, Toast.LENGTH_LONG).show();
				fetchListData();
				break;
			case Constants.NETFAIL:
				Toast.makeText(SalaryRemindListActivity.this, getString(R.string.netfail), Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	/**
	 * 填充数据列表
	 */
	public void fetchListData(){
//		for(int i = 0; i<6; i++){
//			SalaryRemindData simple = new SalaryRemindData();
//			simple.setCategory("证大财富");
//			simple.setMoney("10000000");
//			simple.setOverTime("到期时间");
//			simple.setTime("1892-41-52 000000000");
//			simple.setUserName("测试的");
//			listData.add(simple);
//		}
//		remindListAdapter = new SalaryRemindListAdapter(SalaryRemindListActivity.this, listData, false, R.layout.salary_remind_list_adapter);
//		listView.setAdapter(remindListAdapter);
//		remindListAdapter.notifyDataSetChanged();
		remindListAdapter.addBriefs(salaryListParse.remindLists);
		isLoading = false;
	}
	
	private void showProgress(){
    	if(mProgressDlg == null){
    		mProgressDlg = Utils.createLoadingDialog(this, "请稍候。。。");
    		mProgressDlg.setCancelable(true);
    		mProgressDlg.setCanceledOnTouchOutside(false);
    		mProgressDlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
   			 	public void onCancel(DialogInterface dialog) {
//   			 		Constants.ifCancelProgress++;
   			 		finish();
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
    
    private class MyScrollListener implements OnScrollListener{
		
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			
			//Log.d(TAG, "onScroll " + firstVisibleItem + " "  + visibleItemCount + " " + totalItemCount);
			//Log.d(TAG, "Current page " + mCurrentPage);
			
			boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount);
			if(loadMore && !isLoading){
				if(mCurrentPage + 1 <= mTotalPage){
					mCurrentPage++;
					getListData();
//					fetchOrderList();
				}
			}
			
		}

		public void onScrollStateChanged(AbsListView view, int scrollState) {
					
		}
		
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}
}
