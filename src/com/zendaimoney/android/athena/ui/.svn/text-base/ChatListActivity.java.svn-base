package com.zendaimoney.android.athena.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.ExitApplication;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.ActivitySupport;
import com.zendaimoney.android.athena.im.ChatActivity;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.MessageManager;
import com.zendaimoney.android.athena.im.model.ChartHisBean;

public class ChatListActivity extends ActivitySupport{
	private TextView chatHead;
	private ListView listView;
	private Button backBtn;
	private Button addBtn;
	private ChatListAdapter chatListAdapter = null;
	private List<ChartHisBean> chatHisBeanList = new ArrayList<ChartHisBean>();
	private String TAG = "ChatListActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_list);
		
		initContentView();
		setViewHw();
		initAdapter();
//		fetchData();
		ExitApplication.getInstance().addActivity(this); 
	}
	
	@Override
	protected void onPause() {
		unregisterReceiver(receiver);
		super.onPause();
	}
	public void initContentView(){
		chatHead = (TextView)findViewById(R.id.remind_head);
		chatHead.setText(getString(R.string.chathead));
		listView = (ListView)findViewById(R.id.remindlist);
		backBtn = (Button)findViewById(R.id.backbtn);
		addBtn = (Button)findViewById(R.id.addbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		addBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ChatListActivity.this, FriendListActivity.class);
				startActivity(i);
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {   
            @Override  
            public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,   
                    long arg3) {   
            	Intent intent = new Intent(context, ChatActivity.class);
        		intent.putExtra("to", chatHisBeanList.get(arg2).getFrom());
        		intent.putExtra("toname", chatHisBeanList.get(arg2).getName());
        		startActivity(intent);
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
		if(chatHisBeanList.size() > 0){
			chatHisBeanList.clear();
		}
		chatListAdapter = new ChatListAdapter(ChatListActivity.this, chatHisBeanList, false, R.layout.chat_list_adapter);	
		listView.setAdapter(chatListAdapter);
	}
	
	/**
	 * 获取朋友列表数据
	 */
	public void fetchData(){
//		userList = ContacterManager.getContacterList();
		if(chatHisBeanList.size() > 0){
			chatHisBeanList.clear();
		}
		chatHisBeanList = MessageManager.getInstance(context).getRecentContactsWithLastMsg();

		String name = "";
		for(ChartHisBean chartHisBean : chatHisBeanList){
			AppLog.v(TAG, "jid" + chartHisBean.getFrom());
			if(chartHisBean.getFrom().equals("vm190")){
				chatHisBeanList.remove(chartHisBean);
				break;
			}
		}
		for(ChartHisBean chartHisBean : chatHisBeanList){
			AppLog.v(TAG, "jid" + chartHisBean.getFrom());
			if(!chartHisBean.getFrom().equals("vm190")){
				if(chartHisBean.getContent().contains(Constant.NICKNAME))
					name = chartHisBean.getContent().substring(chartHisBean.getContent().lastIndexOf("=") + 1, chartHisBean.getContent().lastIndexOf("]"));
				chartHisBean.setName(name);
			}else{
				chartHisBean.setName("推送消息");
//				chatHisBeanList.remove(chartHisBean);
			}
		}
		chatListAdapter.changeBriefs(chatHisBeanList);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		Log.d(TAG, "run resume");
		/**
		 * 注册广播信息
		 */
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.NEW_MESSAGE_ACTION);
		registerReceiver(receiver, filter);

		fetchData();
	}
	
	/**
	 * 收到消息的广播
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constant.NEW_MESSAGE_ACTION.equals(action)) {
				fetchData();
			}
		}

	};
}
