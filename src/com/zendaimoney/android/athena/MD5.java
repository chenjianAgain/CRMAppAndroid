package com.zendaimoney.android.athena;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public final static String MD5(String s)
	{
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		// char decDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7'};
		try{ 
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp); byte[] md = mdTemp.digest();
			int j = md.length; char str[] = new char[j * 3]; 
			int k = 0; 
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
				str[k++] = hexDigits[byte0 & 3];
				// str[k++] = decDigits[byte0 >>>5 &7]; 
				// str[k++] = decDigits[byte0 >>>2&7];
				// str[k++] = decDigits[byte0 &3]; 
			} 
			return new String(str); 
		}
		catch (Exception e){ 
			e.printStackTrace(); 
			return null; 
			}
	}
	

	public static String encryptToMD5(String info) {
		byte[] digesta = null;
		try {
			MessageDigest alga = MessageDigest.getInstance("MD5");
			alga.update(info.getBytes());
			digesta = alga.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		String rs = byte2hex(digesta);
		return rs;
	}

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
}
