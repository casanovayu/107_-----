package com.baidumusic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

	private Iservice iservice;//自定义中间人对象
	private MyConn conn;
	private static SeekBar sb;
	public static Handler handler=new Handler(){
		public  void handleMessage(android.os.Message msg) {
			//获取msg里携带的数据
			Bundle data = msg.getData();
			//获取歌曲的总时长和当前进度
			int duration = data.getInt("duration");
			int currentPosition = data.getInt("currentPosition");
			
			//设置seekbar 的进度
			sb.setMax(duration);
			sb.setProgress(currentPosition);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//混合方式开启服务
		//【1】先调用startservice  目的是可以保证服务在后台长期运行
		Intent intent = new Intent(this,MusicService.class);
		startService(intent);
		//【2】调用bindService  目的是为了获取我们定义的中间人对象  可以间接的调用服务里面的方法
		conn = new MyConn();
		bindService(intent, conn, BIND_AUTO_CREATE);
		
		//【3】找到seekbar  设置进度
		sb = (SeekBar) findViewById(R.id.sb);
		
		//[4]改seekbar设置监听事件
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			//当拖动停止的时候调用
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				iservice.callSeekTo(seekBar.getProgress());
			}
			//当拖动开始
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			//当进度改变
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		//当Activity销毁的时候  解绑服务  为了不报红色日志 linked泄露
		unbindService(conn);
		super.onDestroy();
	}

	// 点击按钮 播放音乐
	public void click1(View v) {
		iservice.callPlayMusic();
	}

	// 点击按钮 停止音乐
	public void click2(View v) {
		iservice.callPauseMusic();
	}

	// 点击按钮 继续播放音乐
	public void click3(View v) {
		iservice.callReplayMusic();
	}

	private class MyConn implements ServiceConnection{

		//当服务连接成功
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//获取自定义的中间人对象
			iservice=(Iservice) service;
			
			
		}

		
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
		
		
	}
	
}
