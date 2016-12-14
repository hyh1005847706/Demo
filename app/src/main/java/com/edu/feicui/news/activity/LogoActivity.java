package com.edu.feicui.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.edu.feicui.news.R;

public class LogoActivity extends Activity{
	private ImageView logoImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo_activity);
		logoImage = (ImageView) findViewById(R.id.logo_image);
		
		AlphaAnimation animation = new AlphaAnimation(0f, 1.0f);
		animation.setDuration(2000);
		animation.setFillAfter(true);
		logoImage.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(LogoActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				
				overridePendingTransition(R.anim.right_in, R.anim.bottom_out);
			}
		});
	}
}
