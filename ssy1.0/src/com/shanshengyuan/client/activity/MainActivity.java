/**
 * 
 */
package com.shanshengyuan.client.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;

/**
 * @author Administrator
 *
 */
public class MainActivity extends BaseActivity implements OnResultListener{
	
	private MainActivity self = null;
	
	private Animation mPackAnim; // 动画
	private AnimationListener mPackListener;
	
	private Animation mAnim; // 动画
	private AnimationListener mListener;
	
	private Button resBtn;
	private ImageView picImg;
	
	private LinearLayout secLy;
	private ImageView img;
	private LinearLayout rightLy;
	private ImageView leftImg;
	private LinearLayout leftLy;
	private TextView fooderTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		initViews();
		
		mPackListener = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// initTimer();
				secLy.setVisibility(View.VISIBLE);
				rightLy.setVisibility(View.VISIBLE);
				leftImg.setVisibility(View.VISIBLE);
				leftLy.setVisibility(View.VISIBLE);
				fooderTv.startAnimation(mAnim);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		};

		mPackAnim = AnimationUtils.loadAnimation(self, R.anim.image_classfiy_left_to_right);
		mPackAnim.setAnimationListener(mPackListener);
		
		
		mListener = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		};

		mAnim = AnimationUtils.loadAnimation(self, R.anim.image_amount_left_to_right);
		mAnim.setAnimationListener(mListener);
		
		setListener();
	}
	
	private void setListener(){
		resBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				picImg.startAnimation(mPackAnim);
			}
		});
		
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					Toast.makeText(self, "点击了按钮", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void initViews(){
		resBtn = (Button) findViewById(R.id.result_btn);
		picImg = (ImageView) findViewById(R.id.food_classify);
		secLy = (LinearLayout)findViewById(R.id.food_amount);
		img = (ImageView)findViewById(R.id.classfiy_img);
		rightLy = (LinearLayout)findViewById(R.id.right_ly);
		leftImg = (ImageView)findViewById(R.id.left_img);
		leftLy = (LinearLayout)findViewById(R.id.left_ly);
		fooderTv = (TextView)findViewById(R.id.fooder_tv);
		
	}

	@Override
	public void onSuccess(int actionType) {
		// TODO Auto-generated method stub
		super.onSuccess(actionType);
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
		// TODO Auto-generated method stub
		super.onFailed(actionType, result);
	}
}
