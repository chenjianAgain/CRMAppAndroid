package com.zendaimoney.android.athena.ui;

import java.util.List;

import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.model.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter{

	Context mContext;
	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<User> mList;
	int mlayout;
	private String lastLetter = "null";
	
	public FriendListAdapter(Context paramContext, List<User> paramList, Boolean paramBoolean, int paramInt){
		mContext = paramContext;
		mInflater = LayoutInflater.from(mContext);
		mList = paramList;
		mInternetpic = paramBoolean;
		mlayout = paramInt;
	}
	
	public void addBriefs(List<User> mList){
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
		User localSimpleString;
		if(convertView == null){
			convertView = mInflater.inflate(mlayout, parent, false);
			localViewHolder = new ViewHolder();
			
			localViewHolder.name = (TextView)convertView.findViewById(R.id.name);
			localViewHolder.alpha = (TextView)convertView.findViewById(R.id.alpha);
			localViewHolder.selectView = (CheckBox)convertView.findViewById(R.id.select_view);
			convertView.setTag(localViewHolder);
		}else{
			localViewHolder = (ViewHolder) convertView.getTag();
		}
		/**
		 * 设置显示内容
		 */
		localSimpleString = (User)mList.get(position);
		localViewHolder.name.setText(localSimpleString.getName());
		String curLetter = localSimpleString.getSortLetters();
		if(!Character.isLetter(curLetter.charAt(0))){
			curLetter = "#";
		}
		if(!lastLetter.equals(curLetter)){
			localViewHolder.alpha.setText(curLetter);
			localViewHolder.alpha.setVisibility(View.VISIBLE);
			lastLetter = curLetter;
		}	
		return convertView;
	}
	
	static class ViewHolder{
		TextView name;
		TextView alpha;
		CheckBox selectView;
	}
}
