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
import com.zendaimoney.android.athena.data.BrithRemindData;
import com.zendaimoney.android.athena.httputil.HttpUtil;
import com.zendaimoney.android.athena.httputil.UrlStrings;
import com.zendaimoney.android.athena.parsejson.BrithListParse;

public class BrithRemindListActivity extends Activity{
	private ListView listView;
	private Button backBtn;
	private TextView remindHead;
	private List<BrithRemindData> listData = new ArrayList<BrithRemindData>();   //投资到期提醒列表数据
	private BrithRemindListAdapter remindListAdapter = null;
	
	private Dialog mProgressDlg;
	private String responseStr; //网络访问时的返回参数
	private JSONObject params;  //接口参数
	private BrithListParse brithListParse = new BrithListParse();	//		生日提醒解析类
	
	private String userId;
	
	private int mTotalPage;
	private int mCurrentPage = 1;
	private boolean isLoading = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remind_list);
		mTotalPage = getIntent().getIntExtra("totalPageNum", 0);
		initContentView();
		setViewHw();
		initAdapter();
		getListData();
		ExitApplication.getInstance().addActivity(this); 
	}
	
	/**
	 * 初始化控件
	 */
	public void initContentView(){
		remindHead = (TextView)findViewById(R.id.remind_head);
		remindHead.setText(getString(R.string.brith_remind_list));
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
	
	public void initAdapter(){
		if(listData.size() > 0){
			listData.clear();
		}
		remindListAdapter = new BrithRemindListAdapter(BrithRemindListActivity.this, listData, false, R.layout.salary_remind_list_adapter);
		listView.setAdapter(remindListAdapter);
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
			params = new JSONObject();
			params.put(Constants.ID, userId);
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
				responseStr = HttpUtil.getContent(BrithRemindListActivity.this, UrlStrings.birthList, params);
				AppLog.i("MainActivity", "res:" + responseStr);
				if(responseStr != null && !responseStr.equals("")){
					brithListParse.parseResponse(responseStr);
					if(brithListParse.status == 0){
						listData = brithListParse.remindLists;
						mHandler.sendEmptyMessage(Constants.GETBRITHREMINDSUC);
					}else{
						mHandler.sendEmptyMessage(Constants.GETBRITHREMINDFAIL);
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
			switch (msg.what)
			{
			case Constants.GETBRITHREMINDSUC:
				fetchListData();
				break;
			case Constants.GETBRITHREMINDFAIL:
				Toast.makeText(BrithRemindListActivity.this, brithListParse.respDesc, Toast.LENGTH_LONG).show();
				fetchListData();
				break;
			case Constants.NETFAIL:
				Toast.makeText(BrithRemindListActivity.this, getString(R.string.netfail), Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	
	/**
	 * 填充数据列表
	 */
	public void fetchListData(){
//		for(int i = 0; i<6; i++){
//			listData.get(i).setId("4561861548f641ds5a6g4d6a");
//			listData.get(i).setOverTime("生日日期");
//			listData.get(i).setTime("1892-41-52");
//			listData.get(i).setUserName("测试的");
//		}
		remindListAdapter.addBriefs(brithListParse.remindLists);
		isLoading = false;
	}
	
	private void showProgress(){
    	if(mProgressDlg == null){
    		mProgressDlg = Utils.createLoadingDialog(this, "请稍候。。。");
    		mProgressDlg.setCancelable(true);
    		mProgressDlg.setCanceledOnTouchOutside(false);
    		mProgressDlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
   			 	public void onCancel(DialogInterface dialog) {
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
