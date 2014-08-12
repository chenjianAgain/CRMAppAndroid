package com.zendaimoney.android.athena;

public class Constants {
	/**
	 * 网络访问参数常量
	 */
	public final static String UERNAME = "userName";
	public final static String PWD = "password";
	public final static String DIMECODE = "dimeCode";
	public final static String CARDCODE = "customerIdCardCode";
	public final static String ID = "id";
	public final static String INFOTYPE = "infoType";
	public final static String PAGENO = "pageNo";
	public final static String PAGESIZE = "pageSize";
	/**
	 * handler消息常量
	 */
	public final static int RELEVANCESUC = 10;		//客户关联成功
	public final static int RELEVANCEFAIL = 11;		//客户关联失败
	public final static int GETREMINDSUC = 12;		//获取提醒信息条数成功
	public final static int GETREMINDFAIL = 13; 	//获取提醒信息条数失败
	public final static int GETBRITHREMINDSUC = 14;	//获取生日提醒信息成功
	public final static int GETBRITHREMINDFAIL = 15;//获取生日提醒信息失败
	public final static int GETSALARYREMINDSUC = 16;//获取投资提醒信息成功
	public final static int GETSALARYREMINDFAIL = 17;//获取投资提醒信息失败
	public final static int VALIDATEACCOUNTSUC = 18;	//扫描登陆成功
	public final static int VALIDATEACCOUNTFAIL = 19;	//扫描登陆失败
	public final static int CONFIRMSUC = 20;	//确认扫描登陆成功
	public final static int CONFIRMFAIL = 21; 	//去人扫描登陆失败
	public final static int LOGINSUC = 22;		//登陆成功
	public final static int LOGINFAIL = 23;		//登陆失败
	public final static int NETFAIL = 24;		//网络访问失败
	public final static int CANCELLOGINFAIL = 25;	//取消登陆失败
	public final static int CANCELLOGINSUC = 26;	//取消登陆成功
	public final static int GETCUSTOMNUNSUC = 27;	//获取客户数量成功
	public final static int GETCUSTOMNUNFAIL = 28;	//获取客户数量失败
	
	public final static int RECONNECTFAIL = 60; 	//重连失败
	
	/**
	 * 用户账号信息
	 */
	public final static String SHAREUSER = "userxml";	//保存账号信息的share
	public final static String USERNAME = "username";
	public final static String PASSWD = "password";
	public final static String USERID = "id";
	public final static String LOGINSTATE = "loginstate"; //保存CRM服务器登陆状态 1为已经登陆，0为未登陆
	public final static String LOGINSTATEOPENFIRE = "loginstateopenfire";  //保存openfire服务器登陆状态， 1为登陆，0为未登陆
	public final static String SHUTDOWN = "shutdown";	//关机标示，1为关机，0为未关机
	public final static String IFNEEDLOGIN = "ifneedlogin";//标示是否需要在定时器中进行重连接，0为不需要，1为需要
	
	public static int displayWidth;		//屏幕宽度
	public static int displayHeight;		//屏幕高度
	
	public static int ifCancelProgress = 0;  //标记用户是否在线程运行过程中取消了progress
	
	public static boolean isMainReturn = false;
}
