package com.zendaimoney.android.athena.ui;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.data.CustomerInfoData;
import com.zendaimoney.android.athena.im.ChatActivity;

public class CustomerListAdapter extends BaseAdapter{
	int currwidget;
	Context mContext;
	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<CustomerInfoData> mList;
	int mlayout;
	private int mScreentWidth;  

	public CustomerListAdapter(Context paramContext, List<CustomerInfoData> paramList, Boolean paramBoolean, int paramInt, int screenWidth)
	{
		mContext = paramContext;
		mInflater = LayoutInflater.from(mContext);
		mList = paramList;
		mInternetpic = paramBoolean;
		mlayout = paramInt;
		mScreentWidth = screenWidth;  
	}
	
	public void addBriefs(List<CustomerInfoData> mList){
		for(int i = 0; i<mList.size(); i++){
			this.mList.add(mList.get(i));
		}
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0L;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder localViewHolder;
		CustomerInfoData localSimpleString;
		if(convertView == null){
			convertView = mInflater.inflate(mlayout, parent, false);
			localViewHolder = new ViewHolder();
			
			localViewHolder.hSView = (HorizontalScrollView) convertView.findViewById(R.id.hsv);
			localViewHolder.action = convertView.findViewById(R.id.ll_action);
			
			localViewHolder.textName = (TextView) convertView.findViewById(R.id.custom_name);
			localViewHolder.textHope = (TextView)convertView.findViewById(R.id.cdhope);
			localViewHolder.textDtIdTitle = (TextView)convertView.findViewById(R.id.id_dt);
			localViewHolder.textDtId = (TextView)convertView.findViewById(R.id.id_dt_str);
			localViewHolder.contactBtn = (Button)convertView.findViewById(R.id.contact_btn);
			localViewHolder.peopleIcon = (ImageView)convertView.findViewById(R.id.peopelicon);
			localViewHolder.heartIcon = (ImageView)convertView.findViewById(R.id.hearticon);
			localViewHolder.textView = (LinearLayout)convertView.findViewById(R.id.list);
			localViewHolder.call = (LinearLayout)convertView.findViewById(R.id.call);
			localViewHolder.chat = (LinearLayout)convertView.findViewById(R.id.chat);
			
			//设置内容view的大小为屏幕宽度,这样按钮就正好被挤出屏幕外  
			localViewHolder.content = convertView.findViewById(R.id.content);  
            LayoutParams lp = (LayoutParams) localViewHolder.content.getLayoutParams();  
            lp.width = mScreentWidth;
            
//            int actionW = localViewHolder.action.getWidth();
//            Log.v("TAG", "actionW:" + actionW);
//            localViewHolder.hSView.smoothScrollTo(actionW, 0);
			convertView.setTag(localViewHolder);
		}
		else{
			localViewHolder = (ViewHolder) convertView.getTag();
			
			localViewHolder.hSView = (HorizontalScrollView) convertView.findViewById(R.id.hsv);
			localViewHolder.action = convertView.findViewById(R.id.ll_action);
			
			localViewHolder.textName = (TextView) convertView.findViewById(R.id.custom_name);
			localViewHolder.textHope = (TextView)convertView.findViewById(R.id.cdhope);
			localViewHolder.textDtIdTitle = (TextView)convertView.findViewById(R.id.id_dt);
			localViewHolder.textDtId = (TextView)convertView.findViewById(R.id.id_dt_str);
			localViewHolder.contactBtn = (Button)convertView.findViewById(R.id.contact_btn);
			localViewHolder.peopleIcon = (ImageView)convertView.findViewById(R.id.peopelicon);
			localViewHolder.heartIcon = (ImageView)convertView.findViewById(R.id.hearticon);
			localViewHolder.textView = (LinearLayout)convertView.findViewById(R.id.list);
			localViewHolder.call = (LinearLayout)convertView.findViewById(R.id.call);
			localViewHolder.chat = (LinearLayout)convertView.findViewById(R.id.chat);
			
			localViewHolder.content = convertView.findViewById(R.id.content);  
            LayoutParams lp = (LayoutParams) localViewHolder.content.getLayoutParams();  
            lp.width = mScreentWidth;
            
//            int actionW = localViewHolder.action.getWidth();
//            Log.v("TAG", "actionW:" + actionW);
//            localViewHolder.hSView.smoothScrollTo(actionW, 0);         
		}
		
		localSimpleString = (CustomerInfoData)mList.get(position);
		localViewHolder.textName.setText(localSimpleString.customerName);
		/**
		 * 设置显示内容
		 */
		if(localSimpleString.customerType.equals("0")){
			localViewHolder.textHope.setText(localSimpleString.cdHope);
			localViewHolder.textDtIdTitle.setText(mContext.getResources().getString(R.string.dt));
			localViewHolder.textDtId.setText(localSimpleString.dt);
		}else if(localSimpleString.customerType.equals("1")){
			localViewHolder.textHope.setVisibility(View.GONE);
			localViewHolder.heartIcon.setVisibility(View.GONE);
			localViewHolder.textDtIdTitle.setText(mContext.getResources().getString(R.string.id_num));
			int len = localSimpleString.idNum.length();
			if(len < 6){
				localViewHolder.textDtId.setText("****" + localSimpleString.idNum);
			}else{
				localViewHolder.textDtId.setText("****" + localSimpleString.idNum.substring(localSimpleString.idNum.length() - 6));
			}
		}
		setViewHw(localViewHolder);
		/**
		 * 将两个按钮挤出页面
		 */
//		Runnable mScrollView = new Runnable() {  
//            
//            @Override  
//            public void run() {  
//            	int actionW = localViewHolder.action.getWidth();
//            	localViewHolder.hSView.scrollTo(actionW, 0);//改变滚动条的位置  
//            }
//        };
//        Handler mHandler = new Handler();
//        mHandler.post(mScrollView); 
                
		final String mobile = localSimpleString.mobile;
		final String name = localSimpleString.customerName;
		/**
		 * 设置按钮
		 */
		localViewHolder.contactBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				localViewHolder.action.setVisibility(View.VISIBLE);
				localViewHolder.contactBtn.setVisibility(View.GONE);
				
				LayoutParams lp = (LayoutParams) localViewHolder.content.getLayoutParams();  
				int actionW = localViewHolder.action.getWidth(); 
				lp.width = mScreentWidth - actionW;
				//获得操作区域的长度               
				localViewHolder.hSView.smoothScrollTo(actionW, 0);
//				showCallDialog(mContext, mobile, name);
			}
		});
		
		localViewHolder.content.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				localViewHolder.action.setVisibility(View.GONE);
				localViewHolder.contactBtn.setVisibility(View.VISIBLE);
				LayoutParams lp = (LayoutParams) localViewHolder.content.getLayoutParams();
				lp.width = mScreentWidth;
			}
		});
		
		localViewHolder.call.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCallDialog(mContext, mobile, name);
				localViewHolder.hSView.smoothScrollTo(0, 0);
			}
		});
		localViewHolder.chat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, ChatActivity.class);
        		intent.putExtra("to", mobile + "@vm190");
        		intent.putExtra("toname", name);
        		mContext.startActivity(intent);
        		localViewHolder.hSView.smoothScrollTo(0, 0);
        		
        		localViewHolder.action.setVisibility(View.GONE);
				localViewHolder.contactBtn.setVisibility(View.VISIBLE);
				LayoutParams lp = (LayoutParams) localViewHolder.content.getLayoutParams();
				lp.width = mScreentWidth;
			}
		});
		
		return convertView;
	}

	public void setViewHw(ViewHolder localViewHolder){
		RelativeLayout.LayoutParams paramsIcon = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		if(Constants.displayHeight >= 1000){
			paramsIcon.setMargins((int) (Constants.displayWidth * 0.05f + 0.5f), (int) (Constants.displayWidth * 0.045f + 0.5f), 0, 0);
		}else{
			paramsIcon.setMargins((int) (Constants.displayWidth * 0.02f + 0.5f), (int) (Constants.displayWidth * 0.045f + 0.5f), 0, 0);
		}
		localViewHolder.peopleIcon.setLayoutParams(paramsIcon);
		//联系按钮
		if(Constants.displayHeight >= 1000){
			paramsIcon = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			paramsIcon.setMargins(0, 0, (int) (Constants.displayWidth * 0.05f + 0.5f), 0);
		}else{
			paramsIcon = new RelativeLayout.LayoutParams((int) (Constants.displayWidth * 0.2f + 0.5f),
					(int) (((Constants.displayWidth * 0.2f + 0.5f)/110)*52));
			paramsIcon.setMargins(0, 0, (int) (Constants.displayWidth * 0.02f + 0.5f), 0);
		}
		
		paramsIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsIcon.addRule(RelativeLayout.CENTER_VERTICAL);
		localViewHolder.contactBtn.setLayoutParams(paramsIcon);
		
		//文本部分
		paramsIcon = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		paramsIcon.setMargins(0, (int) (Constants.displayWidth * 0.04f + 0.5f), 0, 0);
		paramsIcon.addRule(RelativeLayout.RIGHT_OF, R.id.peopelicon);
		localViewHolder.textView.setLayoutParams(paramsIcon);
	}
	public static void showCallDialog(final Context context, final String phone, final String name){
		String message = context.getResources().getString(R.string.ifcall) + name 
				+ context.getResources().getString(R.string.phone);
		new AlertDialog.Builder(context)
		.setTitle(context.getResources().getString(R.string.remind))
		.setMessage(message + phone)
		.setPositiveButton(context.getResources().getString(R.string.surecall), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				try{
					Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
					context.startActivity(phoneIntent);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		})
		.setNegativeButton(context.getResources().getString(R.string.cancelcall), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
	}
	static class ViewHolder{
		public HorizontalScrollView hSView;
		TextView textName;
		TextView textHope;
		TextView textDtIdTitle;
		TextView textDtId;
		Button contactBtn;
		ImageView peopleIcon;
		ImageView heartIcon;
		LinearLayout textView;
		public View content;    
		public View action;  
		LinearLayout call;
		LinearLayout chat;
	}
}
