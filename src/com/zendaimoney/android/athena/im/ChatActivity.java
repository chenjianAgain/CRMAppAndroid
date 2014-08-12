package com.zendaimoney.android.athena.im;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.text.format.DateFormat;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.ContacterManager;
import com.zendaimoney.android.athena.im.manager.MessageManager;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.model.IMMessage;
import com.zendaimoney.android.athena.im.model.User;
import com.zendaimoney.android.athena.im.util.DateUtil;
import com.zendaimoney.android.athena.im.util.StringUtil;
import com.zendaimoney.android.athena.im.view.SmileyPagerAdapter;
import com.zendaimoney.android.athena.ui.ScannerActivity;

/**
 * 聊天Activity
 * 
 * @author Administrator
 * 
 */
public class ChatActivity extends AChatActivity {

	private final static String TAG = "ChatActivity";
	private Button titleBack;
	private MessageListAdapter adapter = null;
	private EditText messageInput = null;
	private Button messageSendBtn = null;
	private ImageButton userInfo;
	private ListView listView;
	private GridView gridView;

	private Button voiceBtn;
	private int recordCount;
	// private View listHead;
	// private Button listHeadButton;
	private User user;// 聊天人
	private TextView tvChatTitle;
	private String to_name;
	private ImageView iv_status;
	private Button sendfilebtn;
	private String filename = null;

	private static final int PHOTO_RESULT = 0x12;
	private static final int CARME_RESULT = 0x13;

	private static String[] smileys;
	private static String[] smileyidnames;
	private ViewPager smileyPager;
	private List<View> listViews; // Tab页面列
	private int currsmileyPagerIndex = 0;//
	public static final int SMILEY_PISITION = 10;

	private Button sendvoicebtn; // 按住发送语音
	private Button voicebtn; // 切换语音
	private View moreview;

	private int[] gridViewImg = new int[] { R.drawable.emoticon,
			R.drawable.picture, R.drawable.photo };
	private String[] gridViewName = new String[] { "表情", "图片", "拍照" };

	private int lasty;
	private boolean cancelAudio = false;
	private TimeCount mTimeCount;

	private View mtipview;
	private View tipview01;
	private View tipview02;
	private ImageView tipimg;

	private String lastMessageTime = "0"; // 上一条记录时间
	
	private String takePhotoPath = Environment.getExternalStorageDirectory() + "/Athena/"; //拍照时照片的存储路径
	private String takePhotoName = "";		//拍照时生成照片的名称
	
	public Bitmap bitmapDecodeImage = null;	//加载缩略图的bitmap
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat);
		// to = getIntent().getStringExtra("to");
		// Log.v(TAG, "message from======:" + to);
		// toname = getIntent().getStringExtra("toname");
		init();
		initGridView();
	}

	public void initGridView() {
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i <= 2; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", gridViewImg[i]);
			map.put("ItemText", "" + gridViewName[i]);
			meumList.add(map);
		}

		SimpleAdapter saItem = new SimpleAdapter(this, meumList, // 数据源
				R.layout.gridview_item, // xml实现
				new String[] { "ItemImage", "ItemText" }, // 对应map的Key
				new int[] { R.id.ItemImage, R.id.ItemText }); // 对应R的Id

		// 添加Item到网格中
		gridView.setAdapter(saItem);
		// 添加点击事件
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					gridView.setVisibility(View.GONE);
					smileyPager.setVisibility(View.VISIBLE);
					break;
				case 1:
					try {
						Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
						getAlbum.setType("image/*");
						startActivityForResult(getAlbum, PHOTO_RESULT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 2:// 拍照
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);	
					
					takePhotoName = DateFormat.format("yyyyMMdd_hhmmss",  
			                Calendar.getInstance(Locale.CHINA))  
			                + ".jpg";  
					
			        Uri imageUri = Uri.fromFile(new File(takePhotoPath, takePhotoName));
			        
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, CARME_RESULT);
					break;
				default:
					break;
				}
				// Toast用于向用户显示一些帮助/提示
			}
		});
	}

	private void init() {

		smileys = getResources().getStringArray(R.array.smileys);
		smileyidnames = getResources().getStringArray(R.array.smileysidname);
		initSmileyView();

		gridView = (GridView) findViewById(R.id.add_view);
		voiceBtn = (Button) findViewById(R.id.voice_btn);
		titleBack = (Button) findViewById(R.id.title_back);
		titleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// iv_status=findViewById(R.id.)
		// 与谁聊天
		tvChatTitle = (TextView) findViewById(R.id.to_chat_name);
		AppLog.v(TAG, "chat to jid:" + to);
		user = ContacterManager.getByUserJid(to, XmppConnectionManager
				.getInstance(context).getConnection());
		if (null == user) {
			to_name = StringUtil.getUserNameByJid(to);
		} else {
			to_name = user.getName() == null ? user.getJID() : user.getName();

		}
		tvChatTitle.setText(toname);

		// userInfo = (ImageButton) findViewById(R.id.user_info);

		listView = (ListView) findViewById(R.id.chat_list);
		listView.setCacheColorHint(0);
		listView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (gridView.getVisibility() == View.VISIBLE) {
					gridView.setVisibility(View.GONE);
				}
				if (smileyPager.getVisibility() == View.VISIBLE) {
					smileyPager.setVisibility(View.GONE);
				}
				return false;
			}
		});
		adapter = new MessageListAdapter(ChatActivity.this, getMessages(),
				listView);

		// 头

		LayoutInflater mynflater = LayoutInflater.from(context);
		// listHead = mynflater.inflate(R.layout.chatlistheader, null);
		// listHeadButton = (Button)
		// listHead.findViewById(R.id.buttonChatHistory);
		// listHeadButton.setOnClickListener(chatHistoryCk);
		// listView.addHeaderView(listHead);
		listView.setAdapter(adapter);

		messageInput = (EditText) findViewById(R.id.chat_content);
		messageSendBtn = (Button) findViewById(R.id.chat_sendbtn);
		messageSendBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String message = messageInput.getText().toString(); // Html.toHtml(
																	// (Spanned)
																	// messageInput.getText()
																	// )
																	// .toString();
																	// //

				// sendmessage = sendmessage.replace("<p dir='ltr'>",
				// "").replace("</p>", "");

				if ("".equals(message)) {
					Toast.makeText(ChatActivity.this, "不能为空",
							Toast.LENGTH_SHORT).show();
				} else {

					try {

						sendMessage(message, IMMessage.MSGTYPE_TEXT);
						messageInput.setText("");

					} catch (Exception e) {
						showToast("信息发送失败");
						messageInput.setText(message);
					}
					closeInput();
				}
			}
		});

		sendfilebtn = (Button) findViewById(R.id.addbtn);
		sendfilebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				gridView.setVisibility(View.VISIBLE);
			}
		});

		messageInput.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// moreview.setVisibility(View.GONE);
				gridView.setVisibility(View.GONE);
				smileyPager.setVisibility(View.GONE);
			}
		});

		sendvoicebtn = (Button) findViewById(R.id.voice_btn);
		sendvoicebtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:

					initAudioTipView();

					filename = AudioRecord.startRecording(); // 开始录音

					lasty = (int) event.getY();
					cancelAudio = false;

					mTimeCount = new TimeCount(60000, 500);
					mTimeCount.start();

					sendvoicebtn.setText("松开结束");

					return true;

				case MotionEvent.ACTION_MOVE:

					int moveY = (int) event.getY();

					moveY = Math.abs(moveY);
					if (moveY - lasty >= 70) {

						tipview01.setVisibility(View.GONE);
						tipview02.setVisibility(View.VISIBLE);

						cancelAudio = true; // 取消发送语音
					}

					break;
				case MotionEvent.ACTION_UP:

					// Log.i(TAG,
					// "-------onTouch----------ACTION_UP------------filename="+filename);
					mtipview.setVisibility(View.GONE);
					mTimeCount.cancel();

					AudioRecord.stopRecording();

					if (!cancelAudio) {

						try {
							sendMessage(filename, IMMessage.MSGTYPE_VOICE);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							showToast("信息发送失败");
						}
					}

					sendvoicebtn.setText("按住说话");
					return true;

				default:
					break;
				}

				return false;
			}
		});

		Button sendvoicebtn = (Button) findViewById(R.id.sendvoicebtn);
		sendvoicebtn.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (messageInput.getVisibility() == View.VISIBLE) {
						messageInput.setVisibility(View.GONE);
						voiceBtn.setVisibility(View.VISIBLE);
						v.setBackground(getResources().getDrawable(
								R.drawable.keyboard));
					} else {
						messageInput.setVisibility(View.VISIBLE);
						voiceBtn.setVisibility(View.GONE);
						v.setBackground(getResources().getDrawable(
								R.drawable.chatting_setmode_voice_btn_normal));
					}
					return true;
				}
				return false;
			}
		});

	}

	// protected File getFile(){
	//
	// String mFileName =
	// Environment.getExternalStorageDirectory().getAbsolutePath();
	// mFileName += "/Download/audiorecordtest.amr"; //audiorecordtest.amr m.png
	//
	// File file = new File(mFileName);
	// if (file.exists()) {
	// Log.i(TAG, "--------file.exists-------file.length="+file.length());
	// }else{
	// Log.i(TAG, "--------file.exists-----bucunzai--");
	// }
	// return file;
	// }

	@Override
	protected void receiveNewMessage(IMMessage message) {

	}

	@Override
	protected void refreshMessage(List<IMMessage> messages) {

		adapter.refreshList(messages);

		AppLog.i(TAG, "-----------------refreshMessage----------------getTime="
				+ messages.get(messages.size() - 1).getTime() + "---getType="
				+ messages.get(messages.size() - 1).getType());
	}

	@Override
	protected void onResume() {
		super.onResume();
		ScannerActivity.unreadSum = 0;
		recordCount = MessageManager.getInstance(context)
				.getChatCountWithSb(to);
		// if (recordCount <= 0) {
		// listHead.setVisibility(View.GONE);
		// } else {
		// listHead.setVisibility(View.VISIBLE);
		// }
		adapter.refreshList(getMessages());
	}

	private class MessageListAdapter extends BaseAdapter {

		private List<IMMessage> items;
		private Context context;
		private ListView adapterList;
		private LayoutInflater inflater;

		public MessageListAdapter(Context context, List<IMMessage> items,
				ListView adapterList) {
			this.context = context;
			this.items = items;
			this.adapterList = adapterList;
		}

		public void refreshList(List<IMMessage> items) {
			this.items = items;
			this.notifyDataSetChanged();
			adapterList.setSelection(items.size() - 1);
		}

		@Override
		public int getCount() {
			return items == null ? 0 : items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final IMMessage message = items.get(position);
			IMMessage lastMessage = new IMMessage();
			if (position != 0) {
				lastMessage = items.get(position - 1);
			}
			if (message.getMsgType() == 0) {
				convertView = this.inflater.inflate(
						R.layout.chatting_item_msg_text_left, null); // formclient_chat_in
			} else {
				convertView = this.inflater.inflate(
						R.layout.chatting_item_msg_text_right, null); // formclient_chat_out
			}
			// TextView useridView = (TextView) convertView
			// .findViewById(R.id.formclient_row_userid);
			TextView dateView = (TextView) convertView
					.findViewById(R.id.tv_sendtime); // formclient_row_date
			TextView msgView = (TextView) convertView
					.findViewById(R.id.tv_chatcontent); // formclient_row_msg
			ImageView timeIcon = (ImageView) convertView
					.findViewById(R.id.time_icon);

			String time = message.getTime();
			AppLog.v(TAG, "message:" + message.getContent());
			AppLog.v(TAG, "lastMessageTime:" + lastMessage.getTime());
			AppLog.v(
					TAG,
					"时间差:"
							+ Math.abs(DateUtil.calendarDiff(time,
									lastMessage.getTime())));
			AppLog.v(TAG, "time:" + message.getTime());
			if (Math.abs(DateUtil.calendarDiff(time, lastMessage.getTime())) < 2) {
				dateView.setVisibility(View.GONE);
				timeIcon.setVisibility(View.GONE);
			}

			lastMessageTime = time;
			/**
			 * 比较信息时间差
			 */
			long diffMin = DateUtil.calendarDiff(time); // 相差分钟数
			long hours = diffMin / 60; // 相差小时数
			if (hours > 0 && hours < 24) {
				dateView.setText(hours + "小时前");
			} else if (hours >= 24) {
				String timeDis = DateUtil.date2Str(DateUtil.str2Calendar(time));
				dateView.setText(timeDis);
			} else if (hours == 0) {
				dateView.setText(diffMin + "分钟前");
			}
			// dateView.setText(time);

			if (message.getContent().contains(Constant.NICKNAME)) {
				message.setContent(message.getContent().substring(0,
						message.getContent().indexOf(Constant.NICKNAME)));
			}

			switch (message.getType()) {

			case IMMessage.MSGTYPE_TEXT:
				msgView.setText(FormatText(message.getContent()));
				// msgView.setText( (message.getContent()));
				break;

			case IMMessage.MSGTYPE_VOICE:
				// msgView.setText(message.getContent());
				// break;
				String duration = AudioRecord.getDuration(message.getContent())
						+ "''";
				if (message.getMsgType() == 0) {
					msgView.setText(FormatText("<img src='im_audio_from'/>"));
				} else {
					msgView.setText(FormatText("<img src='im_audio_to'/>"));
				}

				TextView audiotime = (TextView) convertView
						.findViewById(R.id.audiotime);
				audiotime.setVisibility(View.VISIBLE);
				audiotime.setText(duration);

				AppLog.i(TAG, "--------duration=" + duration);
				break;

			case IMMessage.MSGTYPE_IMG:

				String messageContent = message.getContent();

				// Log.i(TAG,
				// "-----------------MSGTYPE_IMG------------------messageContent="+messageContent);
				msgView.append(Html.fromHtml("<img src='" + messageContent
						+ "'/>", imageGetter, null));
				break;
			}

			if (message.getMsg_status() == IMMessage.msg_status_senderro) {
				ImageView erro_img = (ImageView) convertView
						.findViewById(R.id.send_fail);
				erro_img.setVisibility(View.VISIBLE);
			}

			msgView.setOnClickListener(new onMsgClickListener(message));

			return convertView;
		}

	}

	/**
	 * 点击进入聊天记录
	 */
	private OnClickListener chatHistoryCk = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// Intent in = new Intent(context, ChatHistoryActivity.class);
			// in.putExtra("to", to);
			// startActivity(in);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			AppLog.e(TAG, "-------ActivityResult resultCode error");
			return;
		}
		// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
		ContentResolver resolver = getContentResolver();

		// 此处的用于判断接收的Activity是不是你想要的那个
		switch (requestCode) {
		case PHOTO_RESULT:
			try {
				Uri originalUri = data.getData(); // 获得图片的uri
				// bm = MediaStore.Images.Media.getBitmap(resolver,
				// originalUri); // 显得到bitmap图片 貌似没用 先注释掉
				String[] proj = { MediaStore.Images.Media.DATA };

				Cursor cursor = managedQuery(originalUri, proj, null, null,
						null);

				// 这个是获得用户选择的图片的索引值
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

				// 将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();

				// 最后根据索引值获取图片路径
				String path = cursor.getString(column_index);

				sendIMG(path);

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case CARME_RESULT:
			try {
				AppLog.v(TAG, "send image:" + takePhotoPath + takePhotoName);
				Toast.makeText(this, takePhotoPath + takePhotoName, Toast.LENGTH_LONG).show();
				sendIMG(takePhotoPath + takePhotoName);

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	/**
	 * 发送图片
	 * 
	 * @param path
	 */
	public void sendIMG(final String path) {

		if (TextUtils.isEmpty(path)) {
			return;
		}

		try {
			sendMessage(path, IMMessage.MSGTYPE_IMG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	ImageGetter imageGetter = new Html.ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			AppLog.i("TAG", "------imageGetter--------path source:" + source);
			File imgFile = new File(source);
			if (!imgFile.exists()) {
				return null;
			}	
			
			BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
	        newOpts.inJustDecodeBounds = true;  
	        Bitmap bitmap = BitmapFactory.decodeFile(source,newOpts);//此时返回bm为空  
	          
	        newOpts.inJustDecodeBounds = false;  
	        int w = newOpts.outWidth;  
	        int h = newOpts.outHeight;  
	        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
	        float hh = 100f;//这里设置高度为800f  
	        float ww = 100f;//这里设置宽度为480f  
	        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
	        int be = 1;//be=1表示不缩放  
	        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
	            be = (int) (newOpts.outWidth / ww);  
	        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
	            be = (int) (newOpts.outHeight / hh);  
	        }  
	        if (be <= 0)  
	            be = 1;  
	        newOpts.inSampleSize = be;//设置缩放比例  
	        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
	        bitmap = BitmapFactory.decodeFile(source, newOpts); 
	        
			drawable = new BitmapDrawable(bitmap);
			drawable.setBounds(0, 0, newOpts.outWidth, newOpts.outHeight);
			return drawable;
		}
	};

	/**
	 * 将文本转化 使文字和图片可以同时显示
	 * 
	 * @param htmlstr
	 * @return
	 */
	public static CharSequence FormatText(String string) {

		String htmlstr = null;
		StringBuffer sb = new StringBuffer();
		String s = "\\[";
		String smileystr[] = string.split(s);
		for (int i = 0; i < smileystr.length; i++) {
			// Log.i(TAG,
			// "---------FormatText--------smileys[i]="+smileystr[i]);
			if (smileystr[i].length() > 2) {
				String smileyname = smileystr[i].substring(0, 3);
				// Log.i(TAG,
				// "---------FormatText--------smileyname="+smileyname);
				if (smileyname.endsWith("]")) {
					String name = "[" + smileyname;
					for (int j = 0; j < smileyidnames.length; j++) {
						if (name.equalsIgnoreCase(smileys[j])) { // 发送的名称
							name = smileyidnames[j]; // 资源图片的名称
						}
					}

					// Log.i(TAG, "---------FormatText--------name="+name);

					smileystr[i] = smileystr[i].replace(smileyname,
							"<img src='" + name + "'/>");
				}
			}

			sb.append(smileystr[i]);
		}

		htmlstr = sb.toString();

		// 项目资源文件 图片名称 htmlstr = "测试图片信息：<img src='ic_launcher'/>;
		ImageGetter imageGetter_name = new ImageGetter() {

			@Override
			public Drawable getDrawable(String source) {
				// TODO Auto-generated method stub
				// Log.i(TAG , "---------getDrawable--------source="+source);

				try {
					// 获得系统资源的信息，比如图片信息
					Drawable drawable = context.getResources().getDrawable(
							getResourceId(source));

					drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight());

					return drawable;

				} catch (Exception e) {
					AppLog.e(TAG, "e:" + e);
				}
				return null;
			}
		};

		CharSequence charSequence = Html.fromHtml(htmlstr, imageGetter_name,
				null);

		return charSequence;
	}

	private static int getResourceId(String name) {
		try {
			// 根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
			java.lang.reflect.Field field = R.drawable.class.getField(name);
			// 取得并返回资源的id的字段(静态变量)的值，使用反射机制
			return Integer.parseInt(field.get(null).toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	class onMsgClickListener implements OnClickListener {

		IMMessage message;

		public onMsgClickListener(IMMessage message) {
			this.message = message;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AppLog.i(TAG, "-------------setOnClickListener----------------type="
					+ message.getType());

			switch (message.getType()) {

			case IMMessage.MSGTYPE_TEXT:

				break;

			case IMMessage.MSGTYPE_VOICE:
				AudioRecord.startPlaying(message.getContent());
				break;

			case IMMessage.MSGTYPE_IMG:
				// final AlertDialog dialog = new
				// AlertDialog.Builder(ChatActivity.this).create();
				// ImageView imgView = getView(message.getContent());
				// dialog.setView(imgView);
				// dialog.show();

				// 全屏显示的方法
				final Dialog dialog = new Dialog(ChatActivity.this,
						android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				ImageView imgView = getView(message.getContent());
				dialog.setContentView(imgView);
				dialog.show();

				// 点击图片消失
				imgView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				break;
			}
		}

	}

	private ImageView getView(String picPath) {
		ImageView imgView = new ImageView(this);
		imgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		// BitmapFactory.Options options = new BitmapFactory.Options();
		// // options.inSampleSize = 2;//图片大小，设置越大，图片越不清晰，占用空间越小
		// Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);

		Drawable drawable = BitmapDrawable.createFromPath(picPath);
		imgView.setImageDrawable(drawable);

		return imgView;
	}

	/**
	 * 表情
	 */
	private void initSmileyView() {

		smileyPager = (ViewPager) findViewById(R.id.vpager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.smileyview, null));
		listViews.add(mInflater.inflate(R.layout.smileyview, null));
		listViews.add(mInflater.inflate(R.layout.smileyview, null));
		listViews.add(mInflater.inflate(R.layout.smileyview, null));
		listViews.add(mInflater.inflate(R.layout.smileyview, null));

		SmileyPagerAdapter mSmileyPagerAdapter = new SmileyPagerAdapter(
				listViews, context, mhandler);
		smileyPager.setAdapter(mSmileyPagerAdapter);
		smileyPager.setCurrentItem(0);

		smileyPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				AppLog.i(TAG,
						"----------------setOnPageChangeListener--------------------arg0="
								+ arg0);
				currsmileyPagerIndex = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public Handler mhandler = new Handler() {
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		@SuppressLint("NewApi")
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SMILEY_PISITION:

				if (null != msg.getData()) {

					int position = (Integer) msg.getData().get("position");
					position = currsmileyPagerIndex * 31 + position;

					String sendmessage = messageInput.getText().toString(); // Html.toHtml(
																			// (Spanned)
																			// messageInput.getText()
																			// );

					// Log.i(TAG,
					// "------------mhandler---------------------sendmessage.length()="+sendmessage.length());
					if (position == 30 && sendmessage.length() >= 4
							&& sendmessage.endsWith("]")) { // 表情删除按钮
						sendmessage = sendmessage.substring(0,
								sendmessage.length() - 4);
					} else if (position == 30 && sendmessage.length() >= 1) {
						sendmessage = sendmessage.substring(0,
								sendmessage.length() - 1); // 只删一个字符
					} else if (position != 30) {
						sendmessage = sendmessage + smileys[position];
					}
					messageInput.setText((sendmessage));
				}

				break;

			default:
				break;
			}
		};
	};

	/**
	 * 初始化语音提示界面
	 */
	private void initAudioTipView() {

		mtipview = findViewById(R.id.audiotipview);
		tipview01 = mtipview.findViewById(R.id.audiotip01);
		tipview02 = mtipview.findViewById(R.id.audiotip02);

		tipimg = (ImageView) tipview01.findViewById(R.id.audiotipimg);

		mtipview.setVisibility(View.VISIBLE);
		tipview01.setVisibility(View.VISIBLE);
		tipview02.setVisibility(View.GONE);

	}

	class TimeCount extends CountDownTimer {

		/**
		 * 计时器 语音播放完后停止
		 * 
		 * @param millisInFuture
		 *            总时长
		 * @param countDownInterval
		 *            时间间隔
		 */
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		int position = 0;
		int imgid[] = { R.drawable.im_audio02, R.drawable.im_audio04,
				R.drawable.im_audio06 };

		@Override
		public void onFinish() {// 计时完毕时触发

			AudioRecord.stopRecording();
			Toast.makeText(context, "语音最长时间为60秒", 1).show();

		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示

			position++;
			position = position % 3;
			tipimg.setBackgroundResource(imgid[position]);
		}
	}
}
