package com.zendaimoney.android.athena;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.zendaimoney.android.athena.update.VersionInfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Utils {
	
	private static String virsioncode;
    private static String macadress;
	
	/**
	 * 创建加载等待框
	 * @param context
	 * @param msg
	 * @return
	 */
	   public static Dialog createLoadingDialog(Context context, String msg) {  
	    	
	        LayoutInflater inflater = LayoutInflater.from(context);  
	        View mview = inflater.inflate(R.layout.myprogressdialog, null);// 得到加载view   

	        ImageView spaceshipImage = (ImageView) mview.findViewById(R.id.waitimg);  
	        TextView tipTextView = (TextView) mview.findViewById(R.id.waittips);// 提示文字   
	        
	        AnimationDrawable animationDrawable = (AnimationDrawable) spaceshipImage.getBackground();  
            animationDrawable.start();  
//	        // 加载动画   
//	        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
//	                context, R.anim.loading_animation);  
//	        // 使用ImageView显示动画   
//	        spaceshipImage.startAnimation(hyperspaceJumpAnimation);  
//	        tipTextView.setText(msg);// 设置加载信息   
	        tipTextView.setVisibility(View.GONE);
	  
	        Dialog loadingDialog = new Dialog(context, R.style.dialog);// 创建自定义样式dialog   
	  
	        loadingDialog.setCancelable(false);// 不可以用“返回键”取消   
	        loadingDialog.setContentView(mview, new LinearLayout.LayoutParams(  
	                LinearLayout.LayoutParams.FILL_PARENT,  
	                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局   
	        return loadingDialog;  
	  
	    } 
	   
	   /**
	    * 确认是否要呼叫客服
	    * @param context
	    * @param phone
	    */
		public static void showCallDialog(final Context context, final String phone){
			
			new AlertDialog.Builder(context)
			.setTitle("提示")
			.setMessage("是否确认拨打电话：" + phone)
			.setPositiveButton("确认拨打", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					try{
						Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
						context.startActivity(phoneIntent);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			})
			.setNegativeButton("取消拨打", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).show();
		}
		
		
		
		//MD5加密，32位   
	public static String MD5(String str) {
//		MessageDigest md5 = null;
//		try {
//			md5 = MessageDigest.getInstance("MD5");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "";
//		}
//		char[] charArray = str.toCharArray();
//		byte[] byteArray = new byte[charArray.length];
//		for (int i = 0; i < charArray.length; i++) {
//			byteArray[i] = (byte) charArray[i];
//		}
//		byte[]  md5Bytes = md5.digest(byteArray); 
//		StringBuffer hexValue = new StringBuffer();
//		for (int i = 0; i < md5Bytes.length; i++) {
//			int val = ((int) md5Bytes[i]) & 0xff;
//			if (val < 16) {
//				hexValue.append("0");
//			}
//			hexValue.append(Integer.toHexString(val));
//		}
//		return hexValue.toString();
		
		return encryptToMD5(str);
//		return str;
	}

			
	/**
	 * 进行MD5加密
	 * 
	 * @param info
	 *            要加密的信息
	 * @return String 加密后的字符串
	 */
	public static String encryptToMD5(String info) {
		byte[] digesta = null;
		try {
			// 得到一个md5的消息摘要
			MessageDigest alga = MessageDigest.getInstance("MD5");
			// 添加要进行计算摘要的信息
			alga.update(info.getBytes());
			// 得到该摘要
			digesta = alga.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 将摘要转为字符串
		String rs = byte2hex(digesta);
		return rs;
	}

			
	/**
	 * 将二进制转化为16进制字符串
	 * 
	 * @param b
	 *            二进制字节数组
	 * @return String
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
			
	/**
	 * 获取版本号
	 * @param mcontext
	 * @return
	 */
	public static String getVersion(Context mcontext){
		
		if(virsioncode==null){
			
			try {
				virsioncode =  mcontext.getPackageManager().getPackageInfo(VersionInfo.PACKAGE_NAME, 0).versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return virsioncode;
	}
	
	
	public static String getMacAdress(Context context){
		
		if(macadress == null){
			WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			macadress = mWifiManager.getConnectionInfo().getMacAddress();
		}
//		Log.i(TAG, "-----macadress="+macadress);
		return macadress;
	}
}
