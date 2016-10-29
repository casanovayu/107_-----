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

	private Iservice iservice;//�Զ����м��˶���
	private MyConn conn;
	private static SeekBar sb;
	public static Handler handler=new Handler(){
		public  void handleMessage(android.os.Message msg) {
			//��ȡmsg��Я��������
			Bundle data = msg.getData();
			//��ȡ��������ʱ���͵�ǰ����
			int duration = data.getInt("duration");
			int currentPosition = data.getInt("currentPosition");
			
			//����seekbar �Ľ���
			sb.setMax(duration);
			sb.setProgress(currentPosition);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//��Ϸ�ʽ��������
		//��1���ȵ���startservice  Ŀ���ǿ��Ա�֤�����ں�̨��������
		Intent intent = new Intent(this,MusicService.class);
		startService(intent);
		//��2������bindService  Ŀ����Ϊ�˻�ȡ���Ƕ�����м��˶���  ���Լ�ӵĵ��÷�������ķ���
		conn = new MyConn();
		bindService(intent, conn, BIND_AUTO_CREATE);
		
		//��3���ҵ�seekbar  ���ý���
		sb = (SeekBar) findViewById(R.id.sb);
		
		//[4]��seekbar���ü����¼�
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			//���϶�ֹͣ��ʱ�����
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				iservice.callSeekTo(seekBar.getProgress());
			}
			//���϶���ʼ
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			//�����ȸı�
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		//��Activity���ٵ�ʱ��  ������  Ϊ�˲�����ɫ��־ linkedй¶
		unbindService(conn);
		super.onDestroy();
	}

	// �����ť ��������
	public void click1(View v) {
		iservice.callPlayMusic();
	}

	// �����ť ֹͣ����
	public void click2(View v) {
		iservice.callPauseMusic();
	}

	// �����ť ������������
	public void click3(View v) {
		iservice.callReplayMusic();
	}

	private class MyConn implements ServiceConnection{

		//���������ӳɹ�
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//��ȡ�Զ�����м��˶���
			iservice=(Iservice) service;
			
			
		}

		
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
		
		
	}
	
}
