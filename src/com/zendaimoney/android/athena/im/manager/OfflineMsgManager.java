package com.zendaimoney.android.athena.im.manager;

import java.util.Calendar;
import java.util.Iterator;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.OfflineMessageManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.ChatActivity;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.model.IMMessage;
import com.zendaimoney.android.athena.im.model.Notice;
import com.zendaimoney.android.athena.im.util.DateUtil;
import com.zendaimoney.android.athena.im.util.FileUtil;
import com.zendaimoney.android.athena.ui.ScannerActivity;

/**
 * 
 * 离线信息管理类.
 * 
 * @author shimiso
 */
public class OfflineMsgManager {
	private static OfflineMsgManager offlineMsgManager = null;
//	private IActivitySupport activitySupport;
//	private ActivitySupport activitySupport;
	private Context context;
	private String TAG = "offlineMsgManager";
	String nickName = "";	//提示消息的发送对象昵称
	private String noticemsg; // 提示消息
	private NotificationManager notificationManager;

	private OfflineMsgManager(Context activitySupport) {
//		this.activitySupport = activitySupport;
		this.context = activitySupport;
	}

	public static OfflineMsgManager getInstance(Context activitySupport) {
		if (offlineMsgManager == null) {
			offlineMsgManager = new OfflineMsgManager(activitySupport);
		}

		return offlineMsgManager;
	}

	
	private void setNotiType(int iconId, String contentTitle,
			String contentText, Class activity, String from, String nickName) {
//		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		/*
		 * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
		 */
		Intent notifyIntent = new Intent(context, ChatActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("to", from);
		bundle.putString("toname", nickName);
		AppLog.v("====", "from====" + from);
		AppLog.v("====", "nickName====" + nickName);
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		notifyIntent.putExtras(bundle);
//		notifyIntent.setFlags(Intent.Intent.FLAG_ACTIVITY_CLEAR_TOP);

		/* 创建PendingIntent作为设置递延运行的Activity */
		PendingIntent appIntent = PendingIntent.getActivity(context, 0,
				notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		/* 创建Notication，并设置相关参数 */
		Notification myNoti = new Notification();
		// 点击自动消失
		myNoti.flags = Notification.FLAG_AUTO_CANCEL;
		/* 设置statusbar显示的icon */
		myNoti.icon = iconId;
		/* 设置statusbar显示的文字信息 */
		myNoti.tickerText = contentTitle;
		/* 设置notification发生时同时发出默认声音 */
		myNoti.defaults = Notification.DEFAULT_SOUND;
		/* 设置Notification留言条的参数 */
		myNoti.setLatestEventInfo(context, contentTitle, contentText, appIntent);
//		myNoti.ledOffMS.
		/* 送出Notification */
		notificationManager.notify(0, myNoti);
	}
	/**
	 * 
	 * 处理离线消息.
	 * 
	 * @param connection
	 * @author shimiso
	 * @update 2012-7-9 下午5:45:32
	 */
	public void dealOfflineMsg(XMPPConnection connection) {
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		OfflineMessageManager offlineManager = new OfflineMessageManager(
				connection);
		try {
			Iterator<org.jivesoftware.smack.packet.Message> it = offlineManager
					.getMessages();
			
//			Log.i("离线消息数量: ", "" + offlineManager.getMessageCount());
			while (it.hasNext()) {
				org.jivesoftware.smack.packet.Message message = it.next();
//				Log.i("收到离线消息", "Received from 【" + message.getFrom()
//						+ "】 message: " + message.getBody());
				if (message != null && message.getBody() != null
						&& !message.getBody().equals("null")) {
//					IMMessage msg = new IMMessage();
//					String time = (String) message
//							.getProperty(IMMessage.KEY_TIME);
//					msg.setTime(time == null ? DateUtil.getCurDateStr() : time);
//					msg.setContent(message.getBody());
//					if (Message.Type.error == message.getType()) {
//						msg.setType(IMMessage.ERROR);
//					} else {
//						msg.setType(IMMessage.SUCCESS);
//					}
//					String from = message.getFrom().split("/")[0];
//					String nickName = "";
//					if(message.getBody().contains(Constant.NICKNAME)){
//						
//					}
//					msg.setFromSubJid(from);
//
//					// 生成通知
//					NoticeManager noticeManager = NoticeManager
//							.getInstance(context);
//					Notice notice = new Notice();
//					notice.setTitle("会话信息");
//					notice.setNoticeType(Notice.CHAT_MSG);
//					notice.setContent(message.getBody());
//					notice.setFrom(from);
//					notice.setStatus(Notice.UNREAD);
//					notice.setNoticeTime(time == null ? DateUtil
//							.getCurDateStr() : time);
//
//					// 历史记录
//					IMMessage newMessage = new IMMessage();
//					newMessage.setMsgType(0);
//					newMessage.setFromSubJid(from);
//					newMessage.setContent(message.getBody());
//					newMessage.setTime(time == null ? DateUtil.getCurDateStr()
//							: time);
//					MessageManager.getInstance(context).saveIMMessage(
//							newMessage);
//
//					long noticeId = noticeManager.saveNotice(notice);
//					if (noticeId != -1) {
//						Intent intent = new Intent(Constant.NEW_MESSAGE_ACTION);
//						intent.putExtra(IMMessage.IMMESSAGE_KEY, msg);
//						intent.putExtra("noticeId", noticeId);
//						context.sendBroadcast(intent);
//						activitySupport.setNotiType(
//								R.drawable.icon,
//								context.getResources().getString(
//										R.string.new_message),
//								notice.getContent(), ChatActivity.class, from, );
//					}
					
					IMMessage msg = new IMMessage();
					String msgcontent = message.getBody();
					String time = (String) message
							.getProperty(IMMessage.KEY_TIME);
					AppLog.v(TAG, "time:" + time);
					if(time == null || time.equals(null)){
						time = DateUtil.date2Str(Calendar.getInstance(),
								Constant.TIME_FORMART);
					}
					AppLog.v(TAG, "time1:" + time + "\n message:" + msgcontent);
					int type = IMMessage.MSGTYPE_TEXT;
		
					if(msgcontent.contains(Constant.audiotype)){
						type = IMMessage.MSGTYPE_VOICE;
						msgcontent = msgcontent.replace(Constant.audiotype, "");

					}else if(msgcontent.contains(Constant.imagetype)){
						type = IMMessage.MSGTYPE_IMG;
						msgcontent = msgcontent.replace(Constant.imagetype, "");
					}else{
						type = IMMessage.MSGTYPE_TEXT;
					}
					
					try {
						msg.setTime(time);
						msg.setType(type); // IMMessage.SUCCESS
						String nickNamePart = "";
						
						if(msgcontent.contains(Constant.NICKNAME)){
							nickNamePart = msgcontent.substring(msgcontent.indexOf(Constant.NICKNAME));
							nickName = msgcontent.substring(msgcontent.lastIndexOf("=") + 1, msgcontent.lastIndexOf("]"));
						}
						switch (type) {
						case IMMessage.MSGTYPE_TEXT:

							msg.setContent(msgcontent);
							if(msgcontent.contains(Constant.NICKNAME)){
								noticemsg = msgcontent.substring(0, msgcontent.indexOf(Constant.NICKNAME));
							}else{
								noticemsg = msgcontent;
							}
							break;

						case IMMessage.MSGTYPE_VOICE:

							try {
								String nametime = DateUtil.date2Str(Calendar.getInstance(),"yyyyMMddHHmmssSSS");  
						    	String mFileName = FileUtil.createFileOnSD("/Athena/chat/audio/", nametime+".amr");
						    	String base64 = "";
						    	if(msgcontent.contains(Constant.NICKNAME)){
						    		base64 = msgcontent.substring(0, msgcontent.indexOf(Constant.NICKNAME));
						    	}
						    	else{
						    		base64 = msgcontent;
						    	}
								FileUtil.decoderBase64File(base64, mFileName);
								
								msg.setContent(mFileName);
								
								msgcontent = mFileName + nickNamePart;							
								noticemsg = "[语音]";
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								AppLog.i(TAG, "-----e:" + e);
							}
							break;

						case IMMessage.MSGTYPE_IMG:

							try {
								noticemsg = "[图片]";
								AppLog.i(TAG,
										"-------------PacketListener-------------图片-------");
								String nametime = DateUtil.date2Str(Calendar.getInstance(), "yyyyMMddHHmmssSSS");  
						    	String mFileName = FileUtil.createFileOnSD("/Athena/file/img/", nametime+".jpg");

//								final String finalFileSavePath = fileSavePath
//										+ fileName;
						    	String base64 = "";
						    	if(msgcontent.contains(Constant.NICKNAME)){
						    		AppLog.v(TAG, "存在昵称！");
						    		base64 = msgcontent.substring(0, msgcontent.indexOf(Constant.NICKNAME));
						    	}
						    	else{
						    		base64 = msgcontent;
						    		AppLog.v(TAG, "不存在昵称！");
						    	}
								msg.setContent(FileUtil.decoderBase64File(
										base64, mFileName));
								msgcontent = mFileName + nickNamePart;
								
								noticemsg = "[图片]";
								msg.setContent(msgcontent);

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;

						default:
							msg.setContent(message.getBody());
							noticemsg = msgcontent;
							break;
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msg.setContent(message.getBody());
					}

					String from = message.getFrom().split("/")[0];
					msg.setFromSubJid(from);
					AppLog.v(TAG, "notice from:" + from);
					// 生成通知
					NoticeManager noticeManager = NoticeManager
							.getInstance(context);
					Notice notice = new Notice();
					notice.setTitle("会话信息");
					notice.setNoticeType(Notice.CHAT_MSG);
					notice.setContent(noticemsg);
					notice.setFrom(from);
					ScannerActivity.unreadSum ++;
					notice.setStatus(Notice.UNREAD);
					notice.setNoticeTime(time);

					// 历史记录
					IMMessage newMessage = new IMMessage();
					newMessage.setMsgType(0);
					newMessage.setFromSubJid(from);
					newMessage.setContent(msgcontent);
					newMessage.setTime(time);
					
					AppLog.v(TAG, "notice time:" + time);
					newMessage.setType(type);
					MessageManager.getInstance(context).saveIMMessage(newMessage);
					long noticeId = -1;

					noticeId = noticeManager.saveNotice(notice);

					if (noticeId != -1) {
						Intent intent = new Intent(Constant.NEW_MESSAGE_ACTION);
						intent.putExtra(IMMessage.IMMESSAGE_KEY, msg);
						intent.putExtra("notice", notice);
						context.sendBroadcast(intent);
						AppLog.v("from", "======" + from);
						setNotiType(R.drawable.icon_new,
								context.getResources().getString(R.string.new_message),
								notice.getContent(), ChatActivity.class, from, nickName);
					}
					AppLog.i(TAG, "----------------------------- message.getFrom()="
							+ from);				
				}
			}

//			Intent intent = new Intent(Constant.NEW_MESSAGE_ACTION);
//			intent.putExtra(IMMessage.IMMESSAGE_KEY, msg);
//			intent.putExtra("notice", notice);
//			context.sendBroadcast(intent);
//			Log.v("from", "======" + from);
//			setNotiType(R.drawable.icon_new,
//					context.getResources().getString(R.string.new_message),
//					notice.getContent(), ChatActivity.class, from, nickName);
			
			offlineManager.deleteMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
