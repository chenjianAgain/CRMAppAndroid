package com.zendaimoney.android.athena.httpconnection;

import java.util.List;

import org.apache.http.cookie.Cookie;

public class BaseResponse {
	
	public static final int SUCCSS = 1;
	public static final int FAILED = 0;
	
	public boolean okey;
	public int errno;
	
	public List<Cookie> cookies;
	
	BaseResponse(){
		okey = false;
		errno = -1;
	}
	
	public boolean isOK(){ return okey;}
	public int getErrorCode() {return errno;}
}
