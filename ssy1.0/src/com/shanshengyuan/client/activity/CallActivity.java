package com.shanshengyuan.client.activity;

import org.linphone.core.LinphoneCore;

import zwan.phone.api.activity.ZwanphoneService;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import cn.com.zwan.phone.ZwanphoneManager;

import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.R;

public class CallActivity extends BaseActivity implements OnClickListener,OnKeyListener{
	
	
	private TextView Digit1;//数字1
	private TextView Digit2;//数字2
	private TextView Digit3;//数字3
	private TextView Digit4;//数字4
	private TextView Digit5;//数字5
	private TextView Digit6;//数字6
	private TextView Digit7;//数字7
	private TextView Digit8;//数字8
	private TextView Digit9;//数字9
	private TextView Digit0;//数字0
	private TextView Digitstar;//数字*
	private TextView Digitj;//数字#
	
	private Chronometer    timer;
	private Button calloutBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_kehu);
		
		timer = (Chronometer)this.findViewById(R.id.chronometer);
		
		Digit1=(TextView)findViewById(R.id.Digit1);
		Digit1.setOnClickListener(this);
		Digit2=(TextView)findViewById(R.id.Digit2);
		Digit2.setOnClickListener(this);
		Digit3=(TextView)findViewById(R.id.Digit3);
		Digit3.setOnClickListener(this);
		Digit4=(TextView)findViewById(R.id.Digit4);
		Digit4.setOnClickListener(this);
		Digit5=(TextView)findViewById(R.id.Digit5);
		Digit5.setOnClickListener(this);
		Digit6=(TextView)findViewById(R.id.Digit6);
		Digit6.setOnClickListener(this);
		Digit7=(TextView)findViewById(R.id.Digit7);
		Digit7.setOnClickListener(this);
		Digit8=(TextView)findViewById(R.id.Digit8);
		Digit8.setOnClickListener(this);
		Digit9=(TextView)findViewById(R.id.Digit9);
		Digit9.setOnClickListener(this);
		Digit0=(TextView)findViewById(R.id.Digit0);
		Digit0.setOnClickListener(this);
		Digitstar=(TextView)findViewById(R.id.Digitstar);
		Digitstar.setOnClickListener(this);
		Digitj=(TextView)findViewById(R.id.Digitj);
		Digitj.setOnClickListener(this);
		
		calloutBtn = (Button)findViewById(R.id.call_out);
		calloutBtn.setOnClickListener(this);
		
		  // 将计时器清零  
		           timer.setBase(SystemClock.elapsedRealtime());   
	           //开始计时  
	             timer.start();  

	}

	@Override
	public void onClick(View v) {
		int vid=v.getId();
		switch (vid) {
		case R.id.call_out:
		{
			exitapp();
			break;
		}
		
		case R.id.Digit1:case R.id.Digit2:case R.id.Digit3:case R.id.Digit4:case R.id.Digit5:case R.id.Digit6
		:case R.id.Digit7:case R.id.Digit8:case R.id.Digit9:case R.id.Digit0:case R.id.Digitstar:case R.id.Digitj:
		{//拨号
			String keynum=((TextView)v).getText().toString();
			sendDTMF(keynum);
		}
		
		default:
			break;
		}
	}

	
	//send dtmf
		private void sendDTMF(String keyCode)
		{
			char mKeyCode = keyCode.subSequence(0, 1).charAt(0);
			if (!ZwanphoneService.isReady()) return;
			LinphoneCore lc = ZwanphoneManager.getLc();
			lc.stopDtmf();
			if (lc.isIncall()) {
				lc.sendDtmf(mKeyCode);
			}
		}

		private void exitapp()
		{
			ZwanphoneManager.getInstance().terminateAllCalls();
			finish();
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				return false;
			}
		
			return super.onKeyDown(keyCode, event);
		}
}
