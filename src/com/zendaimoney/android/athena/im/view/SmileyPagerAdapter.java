package com.zendaimoney.android.athena.im.view;



import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.ChatActivity;

public class SmileyPagerAdapter extends PagerAdapter{
	public List<View> mListViews;  
	public Context context;
	public View curView;
	private  Handler mhandler;

	
//	private int [] smileyimgid = {R.drawable.bq_001,R.drawable.bq_002,R.drawable.bq_003,R.drawable.bq_004,R.drawable.bq_005,R.drawable.bq_006,R.drawable.bq_007,R.drawable.bq_008,R.drawable.bq_009,R.drawable.bq_010,
//			R.drawable.bq_011,R.drawable.bq_012,R.drawable.bq_013,R.drawable.bq_014,R.drawable.bq_015,R.drawable.bq_016,R.drawable.bq_017,R.drawable.bq_018,R.drawable.bq_019,R.drawable.bq_020,
//			R.drawable.bq_021,R.drawable.bq_022,R.drawable.bq_023,R.drawable.bq_024,R.drawable.bq_025,R.drawable.bq_026,R.drawable.bq_027,R.drawable.bq_028,R.drawable.bq_029,R.drawable.bq_030,
//			R.drawable.bq_031,R.drawable.bq_032,R.drawable.bq_033,R.drawable.bq_034,R.drawable.bq_035,R.drawable.bq_036,R.drawable.bq_037,R.drawable.bq_038,R.drawable.bq_039,R.drawable.bq_040,
//			R.drawable.bq_041,R.drawable.bq_042,R.drawable.bq_043,R.drawable.bq_044,R.drawable.bq_045,R.drawable.bq_046,R.drawable.bq_047,R.drawable.bq_048,R.drawable.bq_049,R.drawable.bq_050,
//			R.drawable.bq_051,R.drawable.bq_052,R.drawable.bq_053,R.drawable.bq_054,R.drawable.bq_055,R.drawable.bq_056,R.drawable.bq_057,R.drawable.bq_058,R.drawable.bq_059,R.drawable.bq_060,
//			R.drawable.bq_061,R.drawable.bq_062,R.drawable.bq_063,R.drawable.bq_064,R.drawable.bq_065,R.drawable.bq_066,R.drawable.bq_067,R.drawable.bq_068,R.drawable.bq_069,R.drawable.bq_070,
//			R.drawable.bq_071,R.drawable.bq_072,R.drawable.bq_073,R.drawable.bq_074,R.drawable.bq_075,R.drawable.bq_076,R.drawable.bq_077,R.drawable.bq_078,R.drawable.bq_079,R.drawable.bq_080,
//			R.drawable.bq_081,R.drawable.bq_082,R.drawable.bq_083,R.drawable.bq_084,R.drawable.bq_085};
	private int [] smileyimgid_01 = {R.drawable.bq_001,R.drawable.bq_002,R.drawable.bq_003,R.drawable.bq_004,R.drawable.bq_005,R.drawable.bq_006,R.drawable.bq_007,R.drawable.bq_008,R.drawable.bq_009,R.drawable.bq_010,
			R.drawable.bq_011,R.drawable.bq_012,R.drawable.bq_013,R.drawable.bq_014,R.drawable.bq_015,R.drawable.bq_016,R.drawable.bq_017,R.drawable.bq_018,R.drawable.bq_019,R.drawable.bq_020
			, R.drawable.bq_022, R.drawable.bq_023, R.drawable.bq_024, R.drawable.bq_025, R.drawable.bq_026, R.drawable.bq_027, R.drawable.bq_028, R.drawable.bq_029, R.drawable.bq_030, R.drawable.bq_031, R.drawable.bq_021}; 
//	private int [] smileyimgid_02 = {R.drawable.bq_022,R.drawable.bq_023,R.drawable.bq_024,R.drawable.bq_025,R.drawable.bq_026,R.drawable.bq_027,R.drawable.bq_028,R.drawable.bq_029,R.drawable.bq_030,
//			R.drawable.bq_031,R.drawable.bq_032,R.drawable.bq_033,R.drawable.bq_034,R.drawable.bq_035,R.drawable.bq_036,R.drawable.bq_037,R.drawable.bq_038,R.drawable.bq_039,R.drawable.bq_040,
//			R.drawable.bq_041,R.drawable.bq_042};
//	private int [] smileyimgid_03 = {R.drawable.bq_043,R.drawable.bq_044,R.drawable.bq_045,R.drawable.bq_046,R.drawable.bq_047,R.drawable.bq_048,R.drawable.bq_049,R.drawable.bq_050,
//			R.drawable.bq_051,R.drawable.bq_052,R.drawable.bq_053,R.drawable.bq_054,R.drawable.bq_055,R.drawable.bq_056,R.drawable.bq_057,R.drawable.bq_058,R.drawable.bq_059,R.drawable.bq_060,
//			R.drawable.bq_061,R.drawable.bq_062,R.drawable.bq_063};
//	private int [] smileyimgid_04 = {R.drawable.bq_064,R.drawable.bq_065,R.drawable.bq_066,R.drawable.bq_067,R.drawable.bq_068,R.drawable.bq_069,R.drawable.bq_070,
//			R.drawable.bq_071,R.drawable.bq_072,R.drawable.bq_073,R.drawable.bq_074,R.drawable.bq_075,R.drawable.bq_076,R.drawable.bq_077,R.drawable.bq_078,R.drawable.bq_079,R.drawable.bq_080,
//			R.drawable.bq_081,R.drawable.bq_082,R.drawable.bq_083,R.drawable.bq_084};
//	private int [] smileyimgid_05 = {R.drawable.bq_085};
	
	
	public SmileyPagerAdapter(List<View> mListViews, Context mContext, Handler mhandler) {  
		this.mListViews = mListViews;
		this.context = mContext;
		this.mhandler = mhandler;
	}  
	
	@Override 
	public void destroyItem(View arg0, int arg1, Object arg2) {  
		((ViewPager) arg0).removeView(mListViews.get(arg1)); 
	}  
	@Override 
	public void finishUpdate(View arg0) {  
	}  
	@Override 
	public int getCount() {  
		return mListViews.size();  
	}  
	@Override 
	public Object instantiateItem(View convertView, int position) {  
		((ViewPager) convertView).addView(mListViews.get(position), 0); 		
		fetchListData(convertView, position);
		curView = convertView;
		return mListViews.get(position);  
	}  
	@Override 
	public boolean isViewFromObject(View arg0, Object arg1) {  
		return arg0 == (arg1);  
	}  
	@Override 
	public void restoreState(Parcelable arg0, ClassLoader arg1) {  
	}  
	@Override 
	public Parcelable saveState() {  
		return null;  
	}  
	@Override 
	public void startUpdate(View arg0) {  
	} 
	
	/**
	 * 数据改变，重新更新列表
	 */
	public void listDataChange(List<Integer> listDatas){
//		fetchListData(curView);
	}
	/**
	 * 填充数据
	 */
	public void fetchListData(View convertView, int index){
		
		GridView mGridView = (GridView)convertView.findViewById(R.id.gridview);

		mAdapter mmmAdapter = null;
		switch(index){
		case 0:
			mmmAdapter = new mAdapter(context, smileyimgid_01, index);
			break;
//		case 1:
//			mmmAdapter = new mAdapter(context, smileyimgid_02, index);
//			break;
//		case 2:
//			mmmAdapter = new mAdapter(context, smileyimgid_03, index);
//			break;
//		case 3:
//			mmmAdapter = new mAdapter(context, smileyimgid_04, index);
//			break;
//		case 4:
//			mmmAdapter = new mAdapter(context, smileyimgid_05, index);
//			break;
		}
			
		mGridView.setAdapter(mmmAdapter);

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AppLog.i("setOnItemClickListener", "------------------------------------position="+position);

				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				Message message = mhandler.obtainMessage(); 
				message.setData(bundle);
				message.what = ChatActivity.SMILEY_PISITION;
				mhandler.sendMessage(message);
			}
		});
	}

	class mAdapter extends BaseAdapter {

		private Context context;
		private int position = 0;
		private int smileyimgid[];
		

		public mAdapter(Context context,  int[] smileyimgid, int position) {  //, List<String> listItems
			this.context = context;		
			this.position = position;	
			this.smileyimgid = smileyimgid;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return smileyimgid.length; //
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			return super.getDropDownView(position, convertView, parent);
		}


		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

//			ImageView mImageView = new ImageView(context);
//			mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//					LayoutParams.WRAP_CONTENT));
//		
//			LinearLayout layout = new LinearLayout(context);
//			layout.setOrientation(LinearLayout.VERTICAL);
//			layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//					LayoutParams.WRAP_CONTENT));
//			layout.addView(mImageView);
			

				LayoutInflater layinflate = LayoutInflater.from(context);
				convertView = layinflate.inflate(R.layout.smileyview_item, null);

								
				ImageView mImageView = (ImageView) convertView.findViewById(R.id.img);

			mImageView.setBackgroundResource(smileyimgid[position]);
//			listItemView.lendingNo.setText("test");

     		
            

			return convertView;
		}

	}
}
