package com.zendaimoney.android.athena.im.service;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.IBinder;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.Constants;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.ChatActivity;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.manager.MessageManager;
import com.zendaimoney.android.athena.im.manager.NoticeManager;
import com.zendaimoney.android.athena.im.manager.XmppConnectionManager;
import com.zendaimoney.android.athena.im.model.IMMessage;
import com.zendaimoney.android.athena.im.model.Notice;
import com.zendaimoney.android.athena.im.util.DateUtil;
import com.zendaimoney.android.athena.im.util.FileUtil;
import com.zendaimoney.android.athena.ui.ScannerActivity;

/**
 * 
 * 聊天服务.
 * 
 * @author shimiso
 */
public class IMChatService extends Service {

	private String TAG = "IMChatService";
	private Context context;
	private NotificationManager notificationManager;

	private String noticemsg; // 提示消息
	String nickName = "";	//提示消息的发送对象昵称

	@Override
	public void onCreate() {
		context = this;
		super.onCreate();
		initSoundPool();
		initChatManager();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		SharedPreferences sp = getSharedPreferences(
				Constants.SHAREUSER,
				MODE_WORLD_WRITEABLE);
//		if (sp.getInt(Constants.LOGINSTATE, 0) == 1) {
//			Editor editor = sp.edit();
//			editor.putInt(Constants.LOGINSTATE, 0);
//			editor.commit();
//		}
//		XmppConnectionManager.getInstance(context).disconnect();   //下线
		super.onDestroy();
	}

	private void initChatManager() {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		final XMPPConnection conn = XmppConnectionManager.getInstance(context)
				.getConnection();

//		// 监听文本消息
//		conn.addPacketListener(pListener, new MessageTypeFilter(
//				Message.Type.chat));
		
		ChatManager chatManager = conn.getChatManager();
		chatManager.addChatListener(new MyChatManagerListener());
	}

	class MyChatManagerListener implements ChatManagerListener {   
        
        
        public void chatCreated(Chat chat, boolean arg1) {    
            chat.addMessageListener(new MessageListener(){    
                public void processMessage(Chat arg0, Message message) {    
                    /**通过handler转发消息*/    
                    AppLog.v(TAG, "收到服务器群发信息");    
                    if(message.getFrom().equals("vm190")){
                    	return;
                    }
                    if (message != null && message.getBody() != null
        					&& !message.getBody().equals("null")) {

        				IMMessage msg = new IMMessage();
        				String msgcontent = message.getBody();
//        				message.getProperty(arg0);
        				
        				String time = DateUtil.date2Str(Calendar.getInstance(),
        						Constant.TIME_FORMART);
        				AppLog.v(TAG, "time:" + time);
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

//        					try {
//        						type = (Integer) message.getProperty(IMMessage.MSGTYPE);
//        					} catch (Exception e) {
//        						type = IMMessage.MSGTYPE_TEXT; // 默认为文本
//        					}
        //
//        					try {
//        						time = (String) message.getProperty(IMMessage.KEY_TIME);
//        					} catch (Exception e) {
//        						time = DateUtil.date2Str(Calendar.getInstance(),
//        								Constant.MS_FORMART);// 默认为收到的时间
//        					}
//        					if(time == null){
//        						time = DateUtil.date2Str(Calendar.getInstance(),
//        								Constant.MS_FORMART);
//        					}
        					msg.setTime(time);

        					// if (Message.Type.error == message.getType()) {
        					// msg.setType(IMMessage.ERROR);
        					// } else {

        					msg.setType(type); // IMMessage.SUCCESS
        					// }

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
//        							String fileName = (String) message
//        									.getProperty(IMMessage.FILENAME);
//        							String fileSavePath = Environment
//        									.getExternalStorageDirectory()
//        									.getAbsolutePath()
//        									+ "/Athena/file/";
//        							createFileOnSD(fileSavePath, fileName);
        //
//        							final String finalFileSavePath = fileSavePath
//        									+ fileName;
//        							FileUtil.decoderBase64File(msgcontent,
//        									finalFileSavePath);
        //
//        							msg.setContent(finalFileSavePath);
        //
//        							msgcontent = finalFileSavePath;
//        							noticemsg = "[语音]";

        							String nametime = DateUtil.date2Str(Calendar.getInstance(),"yyyyMMddHHmmss");  
        					    	String mFileName = FileUtil.createFileOnSD("/Athena/chat/audio/", nametime+".amr");

//        							final String finalFileSavePath = fileSavePath + fileName;
//        					    	String base64 = msgcontent.substring(0, msgcontent.indexOf(Constant.NICKNAME));
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
        							String nametime = DateUtil.date2Str(Calendar.getInstance(),"yyyyMMddHHmmss");  
        					    	String mFileName = FileUtil.createFileOnSD("/Athena/file/img/", nametime+".jpg");

//        							final String finalFileSavePath = fileSavePath
//        									+ fileName;
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
//        							Log.i(TAG	, "-------------PacketListener-------------图片-------msgcontent="+msgcontent );
        							msg.setContent(msgcontent);

        						} catch (Exception e) {
        							// TODO Auto-generated catch block
        							e.printStackTrace();
        							AppLog.i(TAG, "-----e:" + e);
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
        					AppLog.i(TAG, "-----e:" + e);
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
        				if(Constant.CHATINGRUN && Constant.CHATJID.equals(from)){
//        				if(Constant.CHATINGRUN){
        					ScannerActivity.unreadSum = 0;
        					notice.setStatus(Notice.READ);
        				}else{
        					ScannerActivity.unreadSum ++;
        					notice.setStatus(Notice.UNREAD);
        				}
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
        					sendBroadcast(intent);
        					if(!Constant.CHATINGRUN || !Constant.CHATJID.equals(from)){
//        					if(!Constant.CHATINGRUN || ){	
        						AppLog.v("from", "======" + from);
        						setNotiType(R.drawable.icon_new,
        							getResources().getString(R.string.new_message),
        							notice.getContent(), ChatActivity.class, from, nickName);
        					}else{
        						playSound(Notification.DEFAULT_SOUND, 1);
        					}
        				}

        				AppLog.i(TAG, "----------------------------- message.getFrom()="
        						+ from);
        			}
                }    
            });    
        }   
    }
	
	PacketListener pListener = new PacketListener() {
		@Override
		public void processPacket(Packet arg0) {

			Message message = (Message) arg0;
			AppLog.i(TAG, "-------------PacketListener---------------------"
					+ message.getBody());
			AppLog.i(TAG, "-------------PacketListener---------------------"
					+ message.getType());
			if (message != null && message.getBody() != null
					&& !message.getBody().equals("null")) {

				IMMessage msg = new IMMessage();
				String msgcontent = message.getBody();
//				message.getProperty(arg0);
				
				String time = DateUtil.date2Str(Calendar.getInstance(),
						Constant.TIME_FORMART);
				AppLog.v(TAG, "time:" + time);
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

//					try {
//						type = (Integer) message.getProperty(IMMessage.MSGTYPE);
//					} catch (Exception e) {
//						type = IMMessage.MSGTYPE_TEXT; // 默认为文本
//					}
//
//					try {
//						time = (String) message.getProperty(IMMessage.KEY_TIME);
//					} catch (Exception e) {
//						time = DateUtil.date2Str(Calendar.getInstance(),
//								Constant.MS_FORMART);// 默认为收到的时间
//					}
//					if(time == null){
//						time = DateUtil.date2Str(Calendar.getInstance(),
//								Constant.MS_FORMART);
//					}
					msg.setTime(time);

					// if (Message.Type.error == message.getType()) {
					// msg.setType(IMMessage.ERROR);
					// } else {

					msg.setType(type); // IMMessage.SUCCESS
					// }

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
//							String fileName = (String) message
//									.getProperty(IMMessage.FILENAME);
//							String fileSavePath = Environment
//									.getExternalStorageDirectory()
//									.getAbsolutePath()
//									+ "/Athena/file/";
//							createFileOnSD(fileSavePath, fileName);
//
//							final String finalFileSavePath = fileSavePath
//									+ fileName;
//							FileUtil.decoderBase64File(msgcontent,
//									finalFileSavePath);
//
//							msg.setContent(finalFileSavePath);
//
//							msgcontent = finalFileSavePath;
//							noticemsg = "[语音]";

							String nametime = DateUtil.date2Str(Calendar.getInstance(),"yyyyMMddHHmmss");  
					    	String mFileName = FileUtil.createFileOnSD("/Athena/chat/audio/", nametime+".amr");

//							final String finalFileSavePath = fileSavePath + fileName;
//					    	String base64 = msgcontent.substring(0, msgcontent.indexOf(Constant.NICKNAME));
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
							String nametime = DateUtil.date2Str(Calendar.getInstance(),"yyyyMMddHHmmss");  
					    	String mFileName = FileUtil.createFileOnSD("/Athena/file/img/", nametime+".jpg");

//							final String finalFileSavePath = fileSavePath
//									+ fileName;
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
//							Log.i(TAG	, "-------------PacketListener-------------图片-------msgcontent="+msgcontent );
							msg.setContent(msgcontent);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							AppLog.i(TAG, "-----e:" + e);
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
					AppLog.i(TAG, "-----e:" + e);
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
				if(Constant.CHATINGRUN && Constant.CHATJID.equals(from)){
//				if(Constant.CHATINGRUN){
					ScannerActivity.unreadSum = 0;
					notice.setStatus(Notice.READ);
				}else{
					ScannerActivity.unreadSum ++;
					notice.setStatus(Notice.UNREAD);
				}
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
					sendBroadcast(intent);
					if(!Constant.CHATINGRUN || !Constant.CHATJID.equals(from)){
//					if(!Constant.CHATINGRUN || ){	
						AppLog.v("from", "======" + from);
						setNotiType(R.drawable.icon_new,
							getResources().getString(R.string.new_message),
							notice.getContent(), ChatActivity.class, from, nickName);
					}else{
						playSound(Notification.DEFAULT_SOUND, 1);
					}
				}

				AppLog.i(TAG, "----------------------------- message.getFrom()="
						+ from);
			}
		}
	};

	//
	// /**
	// * 仅监听文件传输请求
	// * @param conn
	// */
	// private void listenFileTransfer(XMPPConnection conn){ //Handler _handler
	// // fileTransferHandler = _handler;
	// ServiceDiscoveryManager sdm = ServiceDiscoveryManager
	// .getInstanceFor(conn);
	// if (sdm == null) {
	// sdm = new ServiceDiscoveryManager(conn);
	// }
	// sdm.addFeature("http://jabber.org/protocol/disco#info");
	// sdm.addFeature("jabber:iq:privacy");
	//
	// FileTransferNegotiator.setServiceEnabled(conn, true);
	//
	// FileTransferManager filemanger = new FileTransferManager( conn);
	// filemanger.addFileTransferListener(filetransferListenr);
	// Log.i(TAG, "----------------------------- conn.getUser()="+
	// conn.getUser());
	// }
	//
	//
	// private FileTransferRequest request;
	// private File file;
	// FileTransferListener filetransferListenr = new FileTransferListener() {
	// @Override
	// public void fileTransferRequest(final FileTransferRequest _request) {
	//
	// request = _request;
	// Log.i(TAG,
	// "--------------filetransferListenr----------------"+_request.getFileName());
	// Log.i(TAG,
	// "--------------filetransferListenr----------------"+_request.getFileSize());
	// Log.i(TAG,
	// "--------------filetransferListenr----------------getRequestor="+_request.getRequestor());
	//
	// Log.i(TAG,
	// "--------------filetransferListenr----------------request.getMimeType()="+request.getMimeType());
	//
	//
	//
	//
	// String fileName = _request.getFileName();
	// String fileSavePath =
	// Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/save/";
	// createFileOnSD(fileSavePath, fileName);
	//
	// final String finalFileSavePath = fileSavePath + fileName;
	// file = new File(finalFileSavePath);
	//
	// acceptfilehandler.sendEmptyMessage(0);
	// //////////////////
	//
	//
	// Message message =new Message();
	// message.setFrom(_request.getRequestor());
	// message.setBody(finalFileSavePath);
	//
	// if (message != null && message.getBody() != null
	// && !message.getBody().equals("null")) {
	// IMMessage msg = new IMMessage();
	// // String time = (String)
	// // message.getProperty(IMMessage.KEY_TIME);
	// String time = DateUtil.date2Str(Calendar.getInstance(),
	// Constant.MS_FORMART);
	// msg.setTime(time);
	// msg.setContent(message.getBody());
	// // if (Message.Type.error == message.getType()) {
	// // msg.setType(IMMessage.ERROR);
	// // } else {
	// // msg.setType(IMMessage.SUCCESS);
	// // }
	// if("voice".equals(_request.getDescription())){
	// msg.setType(IMMessage.MSGTYPE_VOICE);
	// }
	//
	// String from = message.getFrom().split("/")[0];
	// msg.setFromSubJid(from);
	//
	// // 生成通知
	// NoticeManager noticeManager = NoticeManager
	// .getInstance(context);
	// Notice notice = new Notice();
	// notice.setTitle("会话信息");
	// notice.setNoticeType(Notice.CHAT_MSG);
	// notice.setContent(message.getBody());
	// notice.setFrom(from);
	// notice.setStatus(Notice.UNREAD);
	// notice.setNoticeTime(time);
	//
	// // 历史记录
	// IMMessage newMessage = new IMMessage();
	// newMessage.setMsgType(0);
	// newMessage.setFromSubJid(from);
	// newMessage.setContent(message.getBody());
	// newMessage.setTime(time);
	// MessageManager.getInstance(context).saveIMMessage(newMessage);
	// long noticeId = -1;
	//
	// noticeId = noticeManager.saveNotice(notice);
	//
	// if (noticeId != -1) {
	// Intent intent = new Intent(Constant.NEW_MESSAGE_ACTION);
	// intent.putExtra(IMMessage.IMMESSAGE_KEY, msg);
	// intent.putExtra("notice", notice);
	// sendBroadcast(intent);
	// setNotiType(R.drawable.icon,
	// getResources().getString(R.string.new_message),
	// notice.getContent(), ChatActivity.class, from);
	//
	// }
	// }
	//
	// }
	// };
	//
	//
	//
	// private Handler acceptfilehandler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// switch (msg.what) {
	//
	// case 0:
	// Log.i(TAG, "------------------handler-------------");
	//
	// Toast.makeText(context, "收到一个文件", 0).show();
	//
	// saveReciveFile(true, request, null);
	// break;
	// default:
	// break;
	// }
	// };
	// };
	//
	//
	//
	//
	// public void saveReciveFile(boolean _isRecive, FileTransferRequest
	// _request, final Handler _handler){
	// android.os.Message msg = android.os.Message.obtain();
	// if (_isRecive) {
	// final IncomingFileTransfer transfer = _request.accept();
	// try {
	//
	// String fileName = _request.getFileName();
	//
	// final String finalFileSavePath =
	// AudioRecord.createFileOnSD("/Prometheus/file/", fileName); //fileSavePath
	// + fileName;
	//
	// Log.i(TAG,
	// "-------saveReciveFile------------------------transfer.getFileSize()="+transfer.getFileSize());
	// Log.i(TAG,
	// "-------saveReciveFile------------------------transfer.getFileName()="+transfer.getFileName());
	// Log.i(TAG,
	// "-------saveReciveFile------------------------transfer.getStatus()="+transfer.getStatus());
	//
	// transfer.recieveFile(new File(finalFileSavePath));
	//
	//
	//
	//
	// while (!transfer.isDone()){
	// System.out.println("Status:" + transfer.getStatus());
	// System.out.println("Progress:" + transfer.getProgress());
	// Thread.sleep(1000);
	// }
	// if (transfer.isDone()){
	// msg.obj = "COMPLITE_RECIVE_FILE"; //ReturnCodeBean.COMPLITE_RECIVE_FILE;
	// // _handler.sendMessage(msg);
	// Log.i(TAG,
	// "-------saveReciveFile---------transfer.isDone()----------------");
	// }
	// }
	//
	// catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// Log.i(TAG, "---------saveReciveFile-------erro----------------e:"+e);
	// }
	// }else {
	// _request.reject();
	// }
	// }
	//

	/**
	 * * 在SD卡上创建文件 * @param _filepath 文件名称 * @param _folder 文件夹名称
	 */
	public static File createFileOnSD(String _folder, String _file) {
		File file = new File(_folder + _file);
		File fileFolder = new File(_folder);
		if (!fileFolder.exists())
			fileFolder.mkdirs();
		// 这里不做文件是否存在的判断
		try {
			file.createNewFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 
	 * 发出Notification的method.
	 * 
	 * @param iconId
	 *            图标
	 * @param contentTitle
	 *            标题
	 * @param contentText
	 *            你内容
	 * @param activity
	 * @author shimiso
	 * @update 2012-5-14 下午12:01:55
	 */
	private void setNotiType(int iconId, String contentTitle,
			String contentText, Class activity, String from, String nickName) {

		/*
		 * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
		 */
		Intent notifyIntent = new Intent(IMChatService.this, ChatActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("to", from);
		bundle.putString("toname", nickName);
		AppLog.v("====", "from====" + from);
		AppLog.v("====", "nickName====" + nickName);
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		notifyIntent.putExtras(bundle);
//		notifyIntent.setFlags(Intent.Intent.FLAG_ACTIVITY_CLEAR_TOP);

		/* 创建PendingIntent作为设置递延运行的Activity */
		PendingIntent appIntent = PendingIntent.getActivity(this, 0,
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
		myNoti.setLatestEventInfo(this, contentTitle, contentText, appIntent);
//		myNoti.ledOffMS.
		/* 送出Notification */
		notificationManager.notify(0, myNoti);
	}

	// 有空看看
	// /////////////////////////////////////
	SoundPool sp; // 声明SoundPool的引用
	HashMap<Integer, Integer> hm; // 声明一个HashMap来存放声音文件
	int currStreamId;// 当前正播放的streamId

	// 初始化声音池的方法
	public void initSoundPool() {
		sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 0); // 创建SoundPool对象
		hm = new HashMap<Integer, Integer>(); // 创建HashMap对象
		// hm.put(1, sp.load(this, R.raw.musictest, 1)); //
		// 加载声音文件musictest并且设置为1号声音放入hm中
	}

	// 播放声音的方法
	public void playSound(int sound, int loop) { // 获取AudioManager引用
		AudioManager am = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		// 获取当前音量
		float streamVolumeCurrent = am
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		// 获取系统最大音量
		float streamVolumeMax = am
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// 计算得到播放音量
		float volume = streamVolumeCurrent / streamVolumeMax;
		// 调用SoundPool的play方法来播放声音文件
		currStreamId = sp.play(hm.get(sound), volume, volume, 1, loop, 1.0f);
	}
}
