/**
 * @Title: MusicService.java 
 * @Package com.baidumusic 
 * @Description: TODO() 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016��10��18�� ����3:34:31 
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
 * @date 2016��10��18�� ����3:34:31 
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
	//�Ѷ�����м��˶��󷵻�
	@Override
	public IBinder onBind(Intent arg0) {

		return new MyBinder();
	}
	
	//����һ������ִ���������
	@Override
	public void onCreate() {
		//[1]��ʼ��mediaplayer
		mediaPlayer = new MediaPlayer();
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
	
	//ʵ�ֲ���ָ����λ��
	public void seekTo(int position){
		mediaPlayer.seekTo(position);
		
	}
	
	//�������ַ���
	public void playMusic(){
		
		//[2]����Ҫ���ŵ���Դλ��  path  ����������·��Ҳ�����Ǳ���·��
		try {
			
			mediaPlayer.reset();
			mediaPlayer.setDataSource("/data/data/Magic_Mullet.mp3");
			//[3]׼������
			mediaPlayer.prepare();
			//[4]��ʼ����
			mediaPlayer.start();
			
			//[5]���½�����
			updateSeekBar();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	

	//���½������ķ���
	private void updateSeekBar() {
		
		//[1]��ȡ��ǰ���ŵ���ʱ��
		final int duration = mediaPlayer.getDuration();
		
		
		
		//[2]ʹ��Timer  ��ʱ��ȥ��ʱ��ȡ��ǰ����
		final Timer timer=new Timer();
		final TimerTask task=new TimerTask() {
			
			@Override
			public void run() {
				//[3]һ���ӻ�ȡһ�ε�ǰ����
				int currentPosition = mediaPlayer.getCurrentPosition();
				
				//[4]����������mainactivity������handler����Ϣ  ��Ϣ�Ϳ���Я������
				Message msg=Message.obtain();
				Bundle bundle = new Bundle();
				bundle.putInt("duration", duration);
				bundle.putInt("currentPosition", currentPosition);
				msg.setData(bundle);
				//����һ����Ϣ  mainactivity�����handlermessage�ͻ�ִ��
				MainActivity.handler.sendMessage(msg);
				
			}
		};
		//100�����  ÿ��һ��ִ��һ��run����
		timer.schedule(task, 100, 1000);
		mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {
			
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				int currenttime=mp.getCurrentPosition()/1000;
				int min=currenttime/60;
				int sec=currenttime%60;
				Toast.makeText(getApplicationContext(), "��ת���ˣ�"+min+"��"+sec+"��", 1).show();
			}
		});
		//������ִ����Ϻ��timer��timertaskȡ��
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				timer.cancel();
				task.cancel();
			}
		});
		
	}

	//��ͣ���ֵķ���
	public void pauseMusic(){
		//[1]��ͣ����
		mediaPlayer.pause();
	}
	
	//�������ֵķ���
	public void replayMusic(){
		//��������
		mediaPlayer.start();
	}
		
	//1 �ڷ�����ڲ�����һ���м��˶���IBinder��
	private class MyBinder extends Binder implements Iservice{

		//���ò������ֵķ���
		@Override
		public void callPlayMusic() {
			playMusic();
		}

		//������ͣ���ֵķ���
		@Override
		public void callPauseMusic() {
			pauseMusic();
		}

		//���ü������ŵķ���
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
