package com.zendaimoney.android.athena.im;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.MessageManager;
import com.zendaimoney.android.athena.im.manager.NoticeManager;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.model.IMMessage;
import com.zendaimoney.android.athena.im.model.Notice;
import com.zendaimoney.android.athena.im.util.DateUtil;
import com.zendaimoney.android.athena.im.util.FileUtil;

/**
 * 
 * 聊天对话.
 * 
 * @author shimiso
 */
public abstract class AChatActivity extends ActivitySupport {

	private static final String TAG = "AChatActivity";
	private List<IMMessage> message_pool = null;
	protected String to;// 聊天人
	protected String toname; //聊天人昵称
	private static int pageSize = 10;
	private List<Notice> noticeList;
	
	protected XMPPConnection connection;
	private Chat chat = null;
	private Bundle bundle = new Bundle();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle = getIntent().getExtras();
		to = bundle.getString("to");
		AppLog.v(TAG, "message from======:" + to);
		toname = bundle.getString("toname");
		if (to == null)
			return;
		
//		try{
			connection = XmppConnectionManager.getInstance(context).getConnection();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		chat = connection.getChatManager().createChat(to, null);
		
	}

	@Override
	protected void onPause() {
		Constant.CHATINGRUN = false;
		Constant.CHATJID = "";
		unregisterReceiver(receiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		to = getIntent().getStringExtra("to");
		AppLog.v(TAG, "message from======:" + to);
		toname = getIntent().getStringExtra("toname");
		Constant.CHATINGRUN = true;
		Constant.CHATJID = to;
		//进入聊天界面时取消存在的notification
		NotificationManager manger = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
		manger.cancel(0);
		
		pageSize = MessageManager.getInstance(context).getChatCountWithSb(to);
		// 第一次查询
		message_pool = MessageManager.getInstance(context)
				.getMessageListByFrom(to, 1, pageSize);
		if (null != message_pool && message_pool.size() > 0)
			Collections.sort(message_pool);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.NEW_MESSAGE_ACTION);
		registerReceiver(receiver, filter);

		// 更新某人所有通知
		NoticeManager.getInstance(context).updateStatusByFrom(to, Notice.READ);
		super.onResume();

	}

	/**
	 * 收到消息的广播
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constant.NEW_MESSAGE_ACTION.equals(action)) {
				IMMessage message = intent
						.getParcelableExtra(IMMessage.IMMESSAGE_KEY);
				if(Constant.CHATJID.equals(message.getFromSubJid()))
				{
					message_pool.add(message);
					receiveNewMessage(message);
					refreshMessage(message_pool);
				}
			}
		}

	};

	protected abstract void receiveNewMessage(IMMessage message);

	protected abstract void refreshMessage(List<IMMessage> messages);

	protected List<IMMessage> getMessages() {
		return message_pool;
	}

	/**
	 * 发送消息
	 * messageContent 内容
	 * type 类型
	 */
	protected void sendMessage(String messageContent, int type) throws Exception {

		String time = DateUtil.date2Str(Calendar.getInstance(),
				Constant.TIME_FORMART);
		AppLog.v(TAG, "send time:" + time);
		final Message message = new Message();
//		message.setProperty(IMMessage.KEY_TIME, time);		
//		message.setProperty(IMMessage.MSGTYPE,  type);

		switch(type){
		case IMMessage.MSGTYPE_TEXT:
			
			message.setProperty(IMMessage.KEY_TIME, time);
			message.setBody(messageContent);
			break;
			
		case IMMessage.MSGTYPE_VOICE:
			
			String path  = messageContent;
			messageContent = Constant.audiotype + FileUtil.encodeBase64File(messageContent);
			message.setBody(messageContent);
			message.setProperty(IMMessage.KEY_TIME, time);
			messageContent = path; //消息中显示路径
			
//			String strs [] = messageContent.split("/");
//			String filename = strs[strs.length-1];
//			message.setProperty(IMMessage.FILENAME,  filename);
			break;
			
		case IMMessage.MSGTYPE_IMG:

//            message.setBody(FileUtil.encodeBase64File(messageContent));
			String msg = FileUtil.img2Base64(messageContent);
			messageContent = Constant.imagetype +msg;
			message.setBody(messageContent );
			message.setProperty(IMMessage.KEY_TIME, time);
			
//			String fileName = time+".jpg";
//			String fileSavePath = Environment
//					.getExternalStorageDirectory()
//					.getAbsolutePath()
//					+ "/Prometheus/file/img/";
//			FileUtil.createFileOnSD(fileSavePath, fileName);
			String nametime = DateUtil.date2Str(Calendar.getInstance(),"yyyyMMddHHmmss");  
	    	String mFileName = FileUtil.createFileOnSD("/Athena/file/img/", nametime+".jpg");
	    	
//			final String finalFileSavePath = fileSavePath
//					+ fileName;

			FileUtil.decoderBase64File(msg, mFileName);
			messageContent = mFileName;
			
//			messageContent =msg;
			
//			String strs02 [] = messageContent.split("/");
//			String filename02 = strs02[strs02.length-1];
//			message.setProperty(IMMessage.FILENAME,  filename02);
			break;
		}
		
//		chat.sendMessage(message); 
		AppLog.v(TAG, "to:" + message.getTo());
		AppLog.v(TAG, "from:" + message.getFrom());
		
		final IMMessage newMessage = new IMMessage();
		newMessage.setMsgType(1);
		newMessage.setFromSubJid(chat.getParticipant());
		newMessage.setContent(messageContent + Constant.NICKNAME + toname + "]");      
		newMessage.setTime(time);
		newMessage.setType(type);
		message_pool.add(newMessage);
		
		// 刷新视图
		refreshMessage(message_pool);
		
		connection = XmppConnectionManager.getInstance(context).getConnection();
		chat = connection.getChatManager().createChat(to, null);
		
		new Thread(new  Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					chat.sendMessage(message); 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
//					mHandler.sendEmptyMessage(0);				
					newMessage.setMsg_status( IMMessage.msg_status_senderro);
					message_pool.remove(message_pool.size()-1);
					message_pool.add(newMessage);
					((Activity) context).runOnUiThread(new Runnable() {
						public void run() {

							Toast.makeText(context, "发送失败，请检查网络后重试！", 1).show();

							refreshMessage(message_pool);
							//ChatUtils.loginIM(context);
						}
					});
				}
				MessageManager.getInstance(context).saveIMMessage(newMessage);
			}
			
		}).start();
//		Log.i(TAG	, "-------########------sendMessage---------------------message="+message.getBody());
		
//		Log.i(TAG, "----------------sendMessage----IMMessage --type="+type);

//		IMMessage newMessage = new IMMessage();
//		newMessage.setMsgType(1);
//		newMessage.setFromSubJid(chat.getParticipant());
//		newMessage.setContent(messageContent + Constant.NICKNAME + toname + "]");      
//		newMessage.setTime(time);
//		newMessage.setType(type);
//		message_pool.add(newMessage);
//		MessageManager.getInstance(context).saveIMMessage(newMessage);
		// MChatManager.message_pool.add(newMessage);
		AppLog.i(TAG, "----------------sendMessage----refreshMessage --type="+type);
		
		
		// 刷新视图
//		refreshMessage(message_pool);

	}

	/**
	 * 下滑加载信息,true 返回成功，false 数据已经全部加载，全部查完了，
	 * 
	 * @param message
	 */
	protected Boolean addNewMessage() {
		List<IMMessage> newMsgList = MessageManager.getInstance(context)
				.getMessageListByFrom(to, message_pool.size(), pageSize);
		if (newMsgList != null && newMsgList.size() > 0) {
			message_pool.addAll(newMsgList);
			Collections.sort(message_pool);
			return true;
		}
		return false;
	}

	protected void resh() {
		// 刷新视图
		refreshMessage(message_pool);
	}	
}
