package com.zendaimoney.android.athena.im.manager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.content.Context;
import android.content.SharedPreferences;

import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.comm.Constant;
import com.zendaimoney.android.athena.im.model.LoginConfig;
import com.zendaimoney.android.athena.im.util.XmppConnectionListener;

/**
 * 
 * XMPP服务器连接工具类.
 * 
 * @author shimiso
 */
public class XmppConnectionManager{
	private XMPPConnection connection;
	private static ConnectionConfiguration connectionConfig;
	private static XmppConnectionManager xmppConnectionManager;
	public static Context context;
	public static XmppConnectionListener xmppConnectionListener;
	
	private XmppConnectionManager(Context context) {
		xmppConnectionListener = new XmppConnectionListener(context);
	}

	public static XmppConnectionManager getInstance(Context mcontext) {
		if (xmppConnectionManager == null) {
			xmppConnectionManager = new XmppConnectionManager(mcontext);
		}
		context = mcontext;
		return xmppConnectionManager;
	}
	// init
	public XMPPConnection init(LoginConfig loginConfig) {
		Connection.DEBUG_ENABLED = false;
		ProviderManager pm = ProviderManager.getInstance();
		configure(pm);

		connectionConfig = new ConnectionConfiguration(
				loginConfig.getXmppHost(), loginConfig.getXmppPort(),
				loginConfig.getXmppServiceName());
		connectionConfig.setSASLAuthenticationEnabled(false);// 不使用SASL验证，设置为false
		connectionConfig
				.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
		// 允许自动连接
		connectionConfig.setReconnectionAllowed(true);
		// 允许登陆成功后更新在线状态
		connectionConfig.setSendPresence(false);
		// 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
		Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
		connection = new XMPPConnection(connectionConfig); 
		return connection;
	}

	/**
	 * 返回一个有效的xmpp连接,如果无效则返回空.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-4 下午6:54:31
	 */
	public XMPPConnection getConnection() {
		if (connection == null) {
//			throw new RuntimeException("请先初始化XMPPConnection连接");
			Connection.DEBUG_ENABLED = false;
			ProviderManager pm = ProviderManager.getInstance();
			configure(pm);
			
			LoginConfig loginConfig = new LoginConfig();

			SharedPreferences preferences = context.getSharedPreferences(Constant.LOGIN_SET, 0);
			loginConfig
					.setXmppHost(preferences.getString(Constant.XMPP_HOST, null));
			loginConfig.setXmppPort(preferences.getInt(Constant.XMPP_PORT,
					context.getResources().getInteger(R.integer.xmpp_port)));
			loginConfig.setUsername(preferences.getString(Constant.USERNAME, null));
			loginConfig.setPassword(preferences.getString(Constant.PASSWORD, null));
			loginConfig.setXmppServiceName(preferences.getString(
					Constant.XMPP_SEIVICE_NAME, null));
			loginConfig.setAutoLogin(preferences.getBoolean(Constant.IS_AUTOLOGIN,
					context.getResources().getBoolean(R.bool.is_autologin)));
			loginConfig.setNovisible(preferences.getBoolean(Constant.IS_NOVISIBLE,
					context.getResources().getBoolean(R.bool.is_novisible)));
			loginConfig.setRemember(preferences.getBoolean(Constant.IS_REMEMBER,
					context.getResources().getBoolean(R.bool.is_remember)));
			loginConfig.setFirstStart(preferences.getBoolean(
					Constant.IS_FIRSTSTART, true));
			
			connectionConfig = new ConnectionConfiguration(
					loginConfig.getXmppHost(), loginConfig.getXmppPort(),
					loginConfig.getXmppServiceName());
			connectionConfig.setSASLAuthenticationEnabled(false);// 不使用SASL验证，设置为false
			connectionConfig
					.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
			// 允许自动连接
			connectionConfig.setReconnectionAllowed(true);
			// 允许登陆成功后更新在线状态
			connectionConfig.setSendPresence(false);
			// 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
			Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
			connection = new XMPPConnection(connectionConfig); 
//			XmppConnectionManager.getInstance().init(getL);
		}
		return connection;
	}
	/**
	 * 
	 * 返回一个有效的xmpp连接,如果无效则返回空.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-4 下午6:54:31
	 */
//	public synchronized XMPPConnection getConnection() {
//		if (connection == null) {
////			throw new RuntimeException("请先初始化XMPPConnection连接");
//			
//			
////			LoginConfig loginConfig = new LoginConfig();
////
////			loginConfig.setXmppHost(loginConfig.getXmppHost()); //null   192.16.220.190 "192.16.10.149"
////			
////			loginConfig.setXmppPort(5222);  //getResources().getInteger(R.integer.xmpp_port)
////			loginConfig.setRemember(true);
////			loginConfig.setAutoLogin(true);
////			loginConfig.setNovisible(false);
//			
//			// 初始化xmpp配置
//			LoginConfig loginConfig = new LoginConfig();
//			loginConfig.setXmppHost(Constant.XMPP_HOST); //null //192.16.220.190		
//			loginConfig.setXmppPort(Integer.parseInt(Constant.XMPP_PORT));
//			init(loginConfig);
//		}
//		return connection;
//	}

	/**
	 * 
	 * 销毁xmpp连接.
	 * 
	 * @author shimiso
	 * @update 2012-7-4 下午6:55:03
	 */
	public void disconnect() {
		if (connection != null) {
			connection.disconnect();
		}
	}

	public void configure(ProviderManager pm) {

		// Private Data Storage
//		pm.addIQProvider("query", "jabber:iq:private",
//				new PrivateDataManager.PrivateDataIQProvider());
//
//		// Time
//		try {
//			pm.addIQProvider("query", "jabber:iq:time",
//					Class.forName("org.jivesoftware.smackx.packet.Time"));
//		} catch (ClassNotFoundException e) {
//		}
//
//		// XHTML
//		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
//				new XHTMLExtensionProvider());
//
//		// Roster Exchange
//		pm.addExtensionProvider("x", "jabber:x:roster",
//				new RosterExchangeProvider());
//		// Message Events
//		pm.addExtensionProvider("x", "jabber:x:event",
//				new MessageEventProvider());
//		// Chat State
//		pm.addExtensionProvider("active",
//				"http://jabber.org/protocol/chatstates",
//				new ChatStateExtension.Provider());
//		pm.addExtensionProvider("composing",
//				"http://jabber.org/protocol/chatstates",
//				new ChatStateExtension.Provider());
//		pm.addExtensionProvider("paused",
//				"http://jabber.org/protocol/chatstates",
//				new ChatStateExtension.Provider());
//		pm.addExtensionProvider("inactive",
//				"http://jabber.org/protocol/chatstates",
//				new ChatStateExtension.Provider());
//		pm.addExtensionProvider("gone",
//				"http://jabber.org/protocol/chatstates",
//				new ChatStateExtension.Provider());
//
//		// FileTransfer
//		pm.addIQProvider("si", "http://jabber.org/protocol/si",
//				new StreamInitiationProvider());
//
//		// Group Chat Invitations
//		pm.addExtensionProvider("x", "jabber:x:conference",
//				new GroupChatInvitation.Provider());
//		// Service Discovery # Items
//		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
//				new DiscoverItemsProvider());
//		// Service Discovery # Info
//		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
//				new DiscoverInfoProvider());
//		// Data Forms
//		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
//		// MUC User
//		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
//				new MUCUserProvider());
//		// MUC Admin
//		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
//				new MUCAdminProvider());
//		// MUC Owner
//		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
//				new MUCOwnerProvider());
//		// Delayed Delivery
//		pm.addExtensionProvider("x", "jabber:x:delay",
//				new DelayInformationProvider());
//		// Version
//		try {
//			pm.addIQProvider("query", "jabber:iq:version",
//					Class.forName("org.jivesoftware.smackx.packet.Version"));
//		} catch (ClassNotFoundException e) {
//		}
//		// VCard
//		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
//		// Offline Message Requests
//		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
//				new OfflineMessageRequest.Provider());
//		// Offline Message Indicator
//		pm.addExtensionProvider("offline",
//				"http://jabber.org/protocol/offline",
//				new OfflineMessageInfo.Provider());
//		// Last Activity
//		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
//		// User Search
//		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
//		// SharedGroupsInfo
//		pm.addIQProvider("sharedgroup",
//				"http://www.jivesoftware.org/protocol/sharedgroup",
//				new SharedGroupsInfo.Provider());
//		// JEP-33: Extended Stanza Addressing
//		pm.addExtensionProvider("addresses",
//				"http://jabber.org/protocol/address",
//				new MultipleAddressesProvider());
		pm.addIQProvider("query", "jabber:iq:private",new PrivateDataManager.PrivateDataIQProvider());
        // Time
        try {
            pm.addIQProvider("query", "jabber:iq:time",Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Roster Exchange
        pm.addExtensionProvider("x", "jabberroster",new RosterExchangeProvider());
        // Message Events
        pm.addExtensionProvider("x", "jabberevent",new MessageEventProvider());
        // Chat State
        pm.addExtensionProvider("active","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
        // XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",new XHTMLExtensionProvider());
        // Group Chat Invitations
        pm.addExtensionProvider("x", "jabberconference",new GroupChatInvitation.Provider());
        // Service Discovery # Items //解析房间列表
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",new DiscoverItemsProvider());
        // Service Discovery # Info //某一个房间的信息
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",new DiscoverInfoProvider());
        // Data Forms
        pm.addExtensionProvider("x", "jabberdata", new DataFormProvider());
        // MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",new MUCUserProvider());
        // MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",new MUCAdminProvider());
        // MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",new MUCOwnerProvider());
        // Delayed Delivery
        pm.addExtensionProvider("x", "jabberdelay",new DelayInformationProvider());
        // Version
        try {
            pm.addIQProvider("query", "jabber:iq:version",Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
            // Not sure what's happening here.
        }
        // VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
        // Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline",new OfflineMessageRequest.Provider());
        // Offline Message Indicator
        pm.addExtensionProvider("offline","http://jabber.org/protocol/offline",new OfflineMessageInfo.Provider());
        // Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
        // User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
        // SharedGroupsInfo
        pm.addIQProvider("sharedgroup","http://www.jivesoftware.org/protocol/sharedgroup",new SharedGroupsInfo.Provider());
        // JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses","http://jabber.org/protocol/address",new MultipleAddressesProvider());
        pm.addIQProvider("si", "http://jabber.org/protocol/si",new StreamInitiationProvider());
        pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
        pm.addIQProvider("command", "http://jabber.org/protocol/commands",new AdHocCommandDataProvider());
        pm.addExtensionProvider("malformed-action","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.MalformedActionError());
        pm.addExtensionProvider("bad-locale","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadLocaleError());
        pm.addExtensionProvider("bad-payload","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadPayloadError());
        pm.addExtensionProvider("bad-sessionid","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadSessionIDError());
        pm.addExtensionProvider("session-expired","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.SessionExpiredError());

	}
}
