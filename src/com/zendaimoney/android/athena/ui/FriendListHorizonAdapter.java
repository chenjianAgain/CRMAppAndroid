package com.zendaimoney.android.athena.ui;

import java.util.List;
import com.zendaimoney.android.athena.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FriendListHorizonAdapter extends BaseAdapter{
	Context mContext;
	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<Integer> mList;
	int mlayout;
	
	public FriendListHorizonAdapter(Context paramContext, List<Integer> paramList, Boolean paramBoolean, int paramInt) {
		// TODO Auto-generated constructor stub
		mContext = paramContext;
		mInflater = LayoutInflater.from(mContext);
		mList = paramList;
		mInternetpic = paramBoolean;
		mlayout = paramInt;
	}
	
	public void addBriefs(List<Integer> mList){
		for(int i = 0; i<mList.size(); i++){
			this.mList.add(mList.get(i));
		}
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0L;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder localViewHolder;
		int localSimpleString;
		if(convertView == null){
			convertView = mInflater.inflate(mlayout, parent, false);
			localViewHolder = new ViewHolder();
			
			localViewHolder.headImg = (ImageView)convertView.findViewById(R.id.image);
			convertView.setTag(localViewHolder);
		}else{
			localViewHolder = (ViewHolder) convertView.getTag();
		}
		/**
		 * 设置显示内容
		 */
		localSimpleString = (int)mList.get(position);
		localViewHolder.headImg.setBackgroundResource(localSimpleString);

		return convertView;
	}
	
	static class ViewHolder{
		ImageView headImg;
	}
}
