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
import android.util.DisplayMetrics;
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

import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.ExitApplication;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.Utils;
import com.zendaimoney.android.athena.data.CustomerInfoData;
import com.zendaimoney.android.athena.httputil.HttpUtil;
import com.zendaimoney.android.athena.httputil.UrlStrings;
import com.zendaimoney.android.athena.parsejson.CustomerInfoParse;

public class CustomerListActivity extends Activity{
	private ListView listView;
	private Button backBtn;
	private TextView customerHead;
	private List<CustomerInfoData> listData = new ArrayList<CustomerInfoData>();   //投资到期提醒列表数据
	private CustomerListAdapter customerListAdapter = null;
	
	private Dialog mProgressDlg;
	private String responseStr; //网络访问时的返回参数
	private JSONObject params;  //接口参数
	private CustomerInfoParse customerInfoParse = new CustomerInfoParse();	//		生日提醒解析类
	
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
		customerHead = (TextView)findViewById(R.id.remind_head);
		customerHead.setText(getString(R.string.customer_num_head));
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
		DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
		customerListAdapter = new CustomerListAdapter(CustomerListActivity.this, listData, false, R.layout.customer_list_adapter, dm.widthPixels);
	
		listView.setAdapter(customerListAdapter);
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
				responseStr = HttpUtil.getContent(CustomerListActivity.this, UrlStrings.getCustomers, params);
				if(responseStr != null && !responseStr.equals("")){
					customerInfoParse.parseResponse(responseStr);
					if(customerInfoParse.status == 0){
						listData = customerInfoParse.infoLists;
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
				Toast.makeText(CustomerListActivity.this, customerInfoParse.respDesc, Toast.LENGTH_LONG).show();
				fetchListData();
				break;
			case Constants.NETFAIL:
				Toast.makeText(CustomerListActivity.this, getString(R.string.netfail), Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	
	/**
	 * 填充数据列表
	 */
	public void fetchListData(){
		customerListAdapter.addBriefs(customerInfoParse.infoLists);
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
			
			boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount);
			if(loadMore && !isLoading){
				if(mCurrentPage + 1 <= mTotalPage){
					mCurrentPage++;
					getListData();
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
