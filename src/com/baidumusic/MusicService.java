/**
 * @Title: MusicService.java 
 * @Package com.baidumusic 
 * @Description: TODO() 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016年10月18日 下午3:34:31 
 * @version V1.0   
 */
package com.baidumusic;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

/**
 * @ClassName: MusicService 
 * @Description: TODO() 
 * @author A18ccms a18ccms_gmail_com 
 * @date 2016年10月18日 下午3:34:31 
 *  
 */
public class MusicService extends Service {

	private MediaPlayer mediaPlayer;

	/* <p>Title: onBind</p> 
	 * <p>Description: </p> 
	 * @param arg0
	 * @return 
	 * @see android.app.Service#onBind(android.content.Intent) 
	 */
	//把定义的中间人对象返回
	@Override
	public IBinder onBind(Intent arg0) {

		return new MyBinder();
	}
	
	//服务一开启就执行这个方法
	@Override
	public void onCreate() {
		//[1]初始化mediaplayer
		mediaPlayer = new MediaPlayer();
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
	
	//实现播放指定的位置
	public void seekTo(int position){
		mediaPlayer.seekTo(position);
		
	}
	
	//播放音乐方法
	public void playMusic(){
		
		//[2]设置要播放的资源位置  path  可以是网络路径也可以是本地路径
		try {
			
			mediaPlayer.reset();
			mediaPlayer.setDataSource("/data/data/Magic_Mullet.mp3");
			//[3]准备播放
			mediaPlayer.prepare();
			//[4]开始播放
			mediaPlayer.start();
			
			//[5]更新进度条
			updateSeekBar();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	

	//更新进度条的方法
	private void updateSeekBar() {
		
		//[1]获取当前播放的总时长
		final int duration = mediaPlayer.getDuration();
		
		
		
		//[2]使用Timer  定时器去定时获取当前进度
		final Timer timer=new Timer();
		final TimerTask task=new TimerTask() {
			
			@Override
			public void run() {
				//[3]一秒钟获取一次当前进度
				int currentPosition = mediaPlayer.getCurrentPosition();
				
				//[4]拿着我们在mainactivity创建的handler发消息  消息就可以携带数据
				Message msg=Message.obtain();
				Bundle bundle = new Bundle();
				bundle.putInt("duration", duration);
				bundle.putInt("currentPosition", currentPosition);
				msg.setData(bundle);
				//发送一条消息  mainactivity里面的handlermessage就会执行
				MainActivity.handler.sendMessage(msg);
				
			}
		};
		//100毫秒后  每隔一秒执行一次run方法
		timer.schedule(task, 100, 1000);
		mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {
			
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				int currenttime=mp.getCurrentPosition()/1000;
				int min=currenttime/60;
				int sec=currenttime%60;
				Toast.makeText(getApplicationContext(), "跳转到了："+min+"分"+sec+"秒", 1).show();
			}
		});
		//当歌曲执行完毕后把timer和timertask取消
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				timer.cancel();
				task.cancel();
			}
		});
		
	}

	//暂停音乐的方法
	public void pauseMusic(){
		//[1]暂停音乐
		mediaPlayer.pause();
	}
	
	//继续音乐的方法
	public void replayMusic(){
		//继续播放
		mediaPlayer.start();
	}
		
	//1 在服务的内部定义一个中间人对象（IBinder）
	private class MyBinder extends Binder implements Iservice{

		//调用播放音乐的方法
		@Override
		public void callPlayMusic() {
			playMusic();
		}

		//调用暂停音乐的方法
		@Override
		public void callPauseMusic() {
			pauseMusic();
		}

		//调用继续播放的方法
		@Override
		public void callReplayMusic() {
			replayMusic();
		}

		/* <p>Title: callSeekTo</p> 
		 * <p>Description: </p> 
		 * @param position 
		 * @see com.baidumusic.Iservice#callSeekTo(int) 
		 */
		@Override
		public void callSeekTo(int position) {
			seekTo(position);
		}
		
		
	}
	
}
