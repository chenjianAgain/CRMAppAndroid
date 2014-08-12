package com.zendaimoney.android.athena.ui;

import java.util.List;

import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.model.ChartHisBean;
import com.zendaimoney.android.athena.im.util.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatListAdapter extends BaseAdapter{

	Context mContext;
	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<ChartHisBean> mList;
	int mlayout;
	
	public ChatListAdapter(Context paramContext, List<ChartHisBean> paramList, Boolean paramBoolean, int paramInt){
		mContext = paramContext;
		mInflater = LayoutInflater.from(mContext);
		mList = paramList;
		mInternetpic = paramBoolean;
		mlayout = paramInt;
	}
	
	public void addBriefs(List<ChartHisBean> mList){
		for(int i = 0; i<mList.size(); i++){
			this.mList.add(mList.get(i));
		}
		notifyDataSetChanged();
	}
	
	public void changeBriefs(List<ChartHisBean> mList){
		this.mList = mList;
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
		ChartHisBean localSimpleString;
		if(convertView == null){
			convertView = mInflater.inflate(mlayout, parent, false);
			localViewHolder = new ViewHolder();
			
			localViewHolder.friendName = (TextView)convertView.findViewById(R.id.friendname);
			localViewHolder.messageContent = (TextView)convertView.findViewById(R.id.messagecontent);
			localViewHolder.messageNum = (TextView)convertView.findViewById(R.id.messagenum);
			localViewHolder.messageTime = (TextView)convertView.findViewById(R.id.messagetime);
			convertView.setTag(localViewHolder);
		}else{
			localViewHolder = (ViewHolder) convertView.getTag();
		}
		/**
		 * 设置显示内容
		 */
		localSimpleString = (ChartHisBean)mList.get(position);
		localViewHolder.friendName.setText(localSimpleString.getName());
		String content = localSimpleString.getContent();
		if(content.contains(Constant.NICKNAME)){
			content = content.substring(0, content.indexOf(Constant.NICKNAME));
		}
		if(content.contains("/chat/")){
			content = "[语音]";
		}else if(content.contains("/img/")){
			content = "[图片]";
		}
		localViewHolder.messageContent.setText(content);
		if(localSimpleString.getNoticeSum() <= 0){
			localViewHolder.messageNum.setVisibility(View.GONE);
		}else{
			localViewHolder.messageNum.setVisibility(View.VISIBLE);
			localViewHolder.messageNum.setText(Integer.toString(localSimpleString.getNoticeSum()));
		}
		String time = localSimpleString.getNoticeTime();
		time = DateUtil.date2Str(DateUtil.str2Calendar(time), Constant.TIME_FORMART);
		localViewHolder.messageTime.setText(time);
		return convertView;
	}
	
	static class ViewHolder{
		TextView messageNum;
		TextView friendName;
		TextView messageTime;
		TextView messageContent;
	}
}
