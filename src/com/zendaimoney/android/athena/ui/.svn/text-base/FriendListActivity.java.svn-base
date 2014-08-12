package com.zendaimoney.android.athena.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.ExitApplication;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.data.FriendListData;
import com.zendaimoney.android.athena.hzpytran.PinYin;
import com.zendaimoney.android.athena.hzpytran.PinyinComparator;
import com.zendaimoney.android.athena.im.ActivitySupport;
import com.zendaimoney.android.athena.im.manager.ContacterManager;
import com.zendaimoney.android.athena.im.model.User;
import com.zendaimoney.android.athena.zxing.view.HorizontalListView;

public class FriendListActivity extends ActivitySupport{
	private TextView choiceGroup;
	private RelativeLayout title;
	private LinearLayout listPart;
	private LinearLayout bottomPart;
	private ListView listView;
	private HorizontalListView choiceList;
	
	private List<FriendListData> listData = new ArrayList<FriendListData>();
	private FriendListAdapter chatListAdapter = null;
	
	private FriendListHorizonAdapter friendListHorizonAdapter = null;
	
	private List<User> userList = new ArrayList<User>();
	private List<Integer> headImgList = new ArrayList<Integer>();
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator = new PinyinComparator();;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_list);
		
		initContentView();
		setViewHw();
		initAdapter();
		getFetchData();
		ExitApplication.getInstance().addActivity(this); 
	}
	
	public void initContentView(){
		choiceList = (HorizontalListView)findViewById(R.id.choicelist);
		listView = (ListView)findViewById(R.id.remindlist);
		choiceGroup = (TextView)findViewById(R.id.choicegroup);
		title = (RelativeLayout)findViewById(R.id.title);
		listPart = (LinearLayout)findViewById(R.id.list_part);
//		bottomPart = (LinearLayout)findViewById(R.id.bottom_part);
		
		listView.setOnItemClickListener(new OnItemClickListener() {   
            @Override  
            public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,   
                    long arg3) {  
            	AppLog.v("TAG", "点击第" + arg2 + "个朋友");
            	FriendListAdapter.ViewHolder viewHolder = (FriendListAdapter.ViewHolder) arg1.getTag();
            	if(viewHolder.selectView.isChecked()){
            		viewHolder.selectView.setChecked(false);
            	}else{
            		viewHolder.selectView.setChecked(true);
            	}
            } 
		});
	}
	
	public void setViewHw(){
		RelativeLayout title = (RelativeLayout)findViewById(R.id.title);
		RelativeLayout.LayoutParams params;
		//title高度
		params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
                (int) (Constants.displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(params);
		
		//选择群部分宽高度
		params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
                (int) (Constants.displayHeight * 0.08f + 0.5f));
		params.addRule(RelativeLayout.BELOW, title.getId());
		choiceGroup.setLayoutParams(params);
		
		//联系人列表部分宽高度
		
		params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
                (int) (Constants.displayHeight * 0.75f + 0.5f));
//		listView.setClickable(true);
		params.addRule(RelativeLayout.BELOW, choiceGroup.getId());
		listPart.setLayoutParams(params);	
		
		LinearLayout.LayoutParams lp;
		lp = new LinearLayout.LayoutParams((int) (Constants.displayWidth * 0.95f + 0.5f),
				LayoutParams.FILL_PARENT);
		listView.setLayoutParams(lp);
//		
//		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
//                (int) (Constants.displayHeight * 0.1f + 0.5f));
//		choiceList.setLayoutParams(params);
//		bottomPart.setBackgroundColor(this.getResources().getColor(R.color.grey));
	}
	
	public void initAdapter(){
		if(listData.size() > 0){
			listData.clear();
		}
		for(int i = 0; i < 20; i++){
			FriendListData simple = new FriendListData();
			simple.name = "证大爱赚钱";
			listData.add(simple);
		}
		chatListAdapter = new FriendListAdapter(FriendListActivity.this, userList, false, R.layout.friend_list_adapter);	
		listView.setAdapter(chatListAdapter);
		
		int i = R.drawable.headphoto;
		headImgList.add(i);
		friendListHorizonAdapter = new FriendListHorizonAdapter(FriendListActivity.this, headImgList, false, R.layout.horizon_list_adapter);
		choiceList.setAdapter(friendListHorizonAdapter);
	}
	
	/**
	 * 获取朋友列表数据
	 */
	public void getFetchData(){
		userList = ContacterManager.getContacterList();
		User addnewname = new User();
		addnewname.setName("测试姓名");
		userList.add(addnewname);
		User addnewname1 = new User();
		addnewname1.setName("阿妹");
		userList.add(addnewname1);
		User addnewname2 = new User();
		addnewname2.setName("单于");
		userList.add(addnewname2);
		User addnewname3 = new User();
		addnewname3.setName("黄培强");
		userList.add(addnewname3);
		User addnewname4 = new User();
		addnewname4.setName("~~打");
		userList.add(addnewname4);
		int listLeng = userList.size();
		User simpleUser;
		/**
		 * 设置每个姓名的首个拼音
		 */	
		for(int i = 0; i < listLeng; i++){
			simpleUser = userList.get(i);
			simpleUser.setSortLetters(PinYin.getPinYin(simpleUser.getName()));
//			simpleUser.setSortLetters(PinYin.getPinYin("单于"));
		}
		Collections.sort(userList, pinyinComparator);
		chatListAdapter.addBriefs(userList);
	}
}
