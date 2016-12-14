package com.edu.feicui.news.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.edu.feicui.news.R;
import com.edu.feicui.news.adapter.ViewPagerAdapter;


public class LeadActivity extends Activity {
	private ViewPager viewPager;
	private ImageView[] imageViews = new ImageView[4];
	private int[] imageArray = {
			R.drawable.welcome,R.drawable.wy,
			R.drawable.bd,R.drawable.small
	};
	private List<View> viewList = new ArrayList<View>();
	private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("lead_config", Context.MODE_PRIVATE);
        boolean isFirstRun = sp.getBoolean("isFirstRun", true);
        if(isFirstRun){
        	setContentView(R.layout.activity_logo);
        	initView();
        	
        	initViewPager();
        }else{
        	Intent intent = new Intent(LeadActivity.this, LogoActivity.class);
			startActivity(intent);
			finish();
			
        }
    }

	private void initViewPager() {
		for(int i = 0;i < imageArray.length;i++){
			ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.lead_image, null);
			imageView.setBackgroundResource(imageArray[i]);
			viewList.add(imageView);
		}
		
		pagerAdapter = new ViewPagerAdapter(viewList);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				for(int i = 0;i < imageArray.length;i++){
					imageViews[i].setAlpha(128);
				}
				imageViews[position].setAlpha(255);
				
				if(imageViews[position].equals(imageViews[3])){
					SharedPreferences sp = getSharedPreferences("lead_config", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sp.edit();
					editor.putBoolean("isFirstRun", false);
					editor.commit();
					
					Intent intent = new Intent(LeadActivity.this, LogoActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.right_in, R.anim.bottom_out);
					finish();
	
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		imageViews[0] = (ImageView) findViewById(R.id.imageView1);
		imageViews[1] = (ImageView) findViewById(R.id.imageView2);
		imageViews[2] = (ImageView) findViewById(R.id.imageView3);
		imageViews[3] = (ImageView) findViewById(R.id.imageView4);
		imageViews[1].setAlpha(128);
		imageViews[2].setAlpha(128);
		imageViews[3].setAlpha(128);
	} 
}
