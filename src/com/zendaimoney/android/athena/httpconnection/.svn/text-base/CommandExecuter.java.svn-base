package com.zendaimoney.android.athena.httpconnection;

import java.io.IOException;

import android.os.Handler;

public class CommandExecuter {
	private Object LOCK = new Object();
	private Handler mHandler;
	private BaseResponse mSavedResponse;
	private Thread mWorkerThread;
	
	public interface ResponseHandler{
	  void handleResponse(BaseResponse rsp);
	  void handleException(IOException e);
	  void onEnd();
	}
	
	public CommandExecuter(){	
	}
	
	public void setHandler(Handler handler){
		synchronized (LOCK) {
			mHandler = handler;
		}
	}
	
	public void execute(final BaseCommand command, final ResponseHandler rspHandler){
		mWorkerThread = new Thread(new Runnable(){
			public void run() {
				try {
					BaseResponse rsp = command.execute();
					postResponse(rspHandler, rsp);
					
				} catch (IOException e) {
					e.printStackTrace();
					postErrorResponse(rspHandler, e);
				}
			}
		});
		
		mWorkerThread.start( );
	}
	
	private void postResponse(final ResponseHandler rspHandler, final BaseResponse rsp){
		synchronized (LOCK) {
			if(mHandler != null){
				mHandler.post( new Runnable() {
					public void run() {
						rspHandler.onEnd();
						rspHandler.handleResponse(rsp);
					}
				});
			}
			else {
				// just save successful response
				mSavedResponse = rsp;			
			}
		}
	}
	
	private void postErrorResponse(final ResponseHandler rspHandler, final IOException e){
		synchronized (LOCK) {
			if(mHandler != null){
				mHandler.post( new Runnable() {
					public void run() {
						rspHandler.onEnd();
						rspHandler.handleException(e);	
					}
				});
			}
		}
	}
	
	public BaseResponse getResponse(){
		BaseResponse rsp = mSavedResponse;
		mSavedResponse = null;
		return rsp;
	}
	
	public boolean isRunning(){
		if(mWorkerThread == null){
			return false;
		}
		
		return ( mWorkerThread.getState() != Thread.State.TERMINATED &&
				 mWorkerThread.getState() != Thread.State.NEW );
	}
}
