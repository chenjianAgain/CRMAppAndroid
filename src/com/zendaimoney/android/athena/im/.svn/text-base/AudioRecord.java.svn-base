package com.zendaimoney.android.athena.im;


import java.io.IOException;
import java.util.Calendar;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.media.MediaRecorder.OutputFormat;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.zendaimoney.android.athena.AppLog;
import com.zendaimoney.android.athena.im.util.DateUtil;
import com.zendaimoney.android.athena.im.util.FileUtil;



/**
 * 语音操作类
 * @author Administrator
 *
 */
public class AudioRecord{
    private static final String LOG_TAG = "AudioRecord";
    private static MediaRecorder mRecorder = null;
    private static MediaPlayer   mPlayer = null;
    
    private static String currPlayingAudio; //当前正在播放的语音
    
    private static TimeCount mTimeCount;
    

    /**
     * 播放语音
     * @param mFileName
     */
    public static  void startPlaying(String mFileName) {
    	
    	if(TextUtils.isEmpty(mFileName)){
    		return;
    	}
    	
    	if(mPlayer==null || !mPlayer.isPlaying()){
    		
    		mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(mFileName);
                mPlayer.prepare();
                mPlayer.start();
                
                currPlayingAudio = mFileName; 
                AppLog.i(LOG_TAG, "-------------------------------mPlayer.getDuration()="+mPlayer.getDuration());
                
                mTimeCount =  new TimeCount((long)mPlayer.getDuration(), 500);
                mTimeCount .start();          
                
            } catch (IOException e) {
            	AppLog.e(LOG_TAG, "prepare() failed"); 
            }
            
    	}else{
    		
    		stopPlaying();
    		mTimeCount.cancel();
    		
    		if(currPlayingAudio !=null && !currPlayingAudio.equals(mFileName)){
				startPlaying(mFileName);
				currPlayingAudio = mFileName;
			}
    	}
 
    }
    /**
     * 停止播放语音
     */
    public static void stopPlaying() {
    	
    	if(mPlayer!=null && mPlayer.isPlaying()){
    		
    		mPlayer.stop();
    		mPlayer.release();
            mPlayer = null;           
    	}     
    }
    

    /**
     * 开始录音
     * @return 文件名
     */
    public static String startRecording() {
    	
    	String time = DateUtil.date2Str(Calendar.getInstance(),"yyyyMMddHHmmss");  
    	String mFileName =  FileUtil.createFileOnSD("/Athena/chat/audio/", time+".amr");
    	
    	
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFormat(OutputFormat.RAW_AMR);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
        	AppLog.e(LOG_TAG, "-----startRecording-----prepare() failed---e:"+e);
        }
        mRecorder.start();
        
//         byte[] buffer = new byte[bs];
//        while (isRun) {  
//        	int r = ar.read(buffer, 0, bs);  
//        	int v = 0;  
//        	// 将 buffer 内容取出，进行平方和运算  
//        	for (int i = 0; i < buffer.length; i++) {  
//        	// 这里没有做运算的优化，为了更加清晰的展示代码  
//        	v += buffer[i] * buffer[i];  
//        	}  
//        	// 平方和除以数据总长度，得到音量大小。可以获取白噪声值，然后对实际采样进行标准化。  
//        	// 如果想利用这个数值进行操作，建议用 sendMessage 将其抛出，在 Handler 里进行处理。  
//        	Log.d("spl", String.valueOf(v / (float) r));  

        mRecorder.setOnInfoListener(new OnInfoListener() {
			
			@Override
			public void onInfo(MediaRecorder mr, int what, int extra) {
				// TODO Auto-generated method stub
				AppLog.i(LOG_TAG, "--------------------setOnInfoListener-------------------what="+what+"------extra="+extra);
			}
		});
//        Log.i(LOG_TAG, "---------------------------------mRecorder.getMaxAmplitude()="+mRecorder.getMaxAmplitude()+"---"+mRecorder.getAudioSourceMax());
        return mFileName;
    }
    
    /**
     * 停止录音
     */
    public static void stopRecording() {
    	
    	try {
    		if(mRecorder!=null){ 
        		mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
        	}
        } catch (Exception e) {
        	AppLog.e(LOG_TAG, "----stopRecording----e:"+e);
        }
    	
    }
    
    /**
     * 获取语音消息时间长
     * @param mFileName
     * @return
     */
    public static int getDuration(String mFileName){
    	
    	if(TextUtils.isEmpty(mFileName)){
    		return 0;
    	}
    	
    	 int duration = 0;
        try {
        	
        	MediaPlayer mPlayer = new MediaPlayer();
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			
			duration = mPlayer.getDuration();
		        if(duration>0 && duration<1000){
		        	return 1;
		        }
		        duration = duration/1000;
		        mPlayer.release();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
       
        return duration;
    }



	static class TimeCount extends CountDownTimer {
		
		/**
		 * 计时器  语音播放完后停止
		 * @param millisInFuture 总时长
		 * @param countDownInterval 时间间隔
		 */
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发

			stopPlaying();
			AppLog.i(LOG_TAG, "---------onFinish---------stopPlaying----");
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示

		}
	}
}
