package com.edu.feicui.news.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.feicui.news.R;
import com.edu.feicui.news.base.BaseActivity;
import com.edu.feicui.news.entity.MessageEvent;
import com.edu.feicui.news.fragment.ForgotPasswordFragment;
import com.edu.feicui.news.fragment.LeftMenuFragment;
import com.edu.feicui.news.fragment.LoginFragment;
import com.edu.feicui.news.fragment.MainFragment;
import com.edu.feicui.news.fragment.RegisterFragment;
import com.edu.feicui.news.fragment.RightMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {
    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvTitle;

    public static SlidingMenu slidingMenu;
    private Fragment leftMenuFragment;
    private Fragment rightMenuFragment;

    private long prevTime;
    private MainFragment mainFragment;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private ForgotPasswordFragment forgotPasswordFragment;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        addMainFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initSlidingMenu();
        ivLeft.setOnClickListener(listener);
        ivRight.setOnClickListener(listener);

        initMainFragment();
    }

    private void addMainFragment(){
        if(mainFragment == null){
            mainFragment = new MainFragment();
        }
        //tvTitle.setText("资讯");
        getSupportFragmentManager().beginTransaction().replace(R.id.rl_content,mainFragment).commit();
    }

    private void initMainFragment() {
        if(mainFragment == null){
            mainFragment = new MainFragment();
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.rl_content,mainFragment);
        ft.commit();
    }

    //当按下back键时会调用该方法
    @Override
    public void onBackPressed() {
        if(slidingMenu != null && slidingMenu.isMenuShowing()){
            slidingMenu.showContent();
        }else {
            twiceExit();
        }
    }

    private void twiceExit() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - prevTime > 1500){
            Toast.makeText(MainActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();
            prevTime = currentTime;
        }else{
            prevTime = currentTime;
            finish();;
            System.exit(0);
        }
    }

    //初始化侧滑
    private void initSlidingMenu(){
        leftMenuFragment = new LeftMenuFragment();
        rightMenuFragment = new RightMenuFragment();

        slidingMenu = new SlidingMenu(this);
        //设置侧滑模式，左侧滑，优侧滑，左右侧滑
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置触摸屏幕可以通过滑动显示侧滑菜单
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置侧滑菜单显示之后Activity剩余的空间大小
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_margin);
        //为侧滑菜单设置布局
        slidingMenu.setMenu(R.layout.layout_left_menu);
        //设置右边（二级）侧滑菜单
        slidingMenu.setSecondaryMenu(R.layout.layout_right_menu);
        //设置该侧滑菜单依附在这个activity内容里面
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.ll_left_container, leftMenuFragment).commit();

        getSupportFragmentManager().beginTransaction().
                replace(R.id.ll_right_container, rightMenuFragment).commit();
    }

    private void initView() {
        ivLeft = (ImageView) findViewById(R.id.iv_left_menu_icon);
        ivRight = (ImageView) findViewById(R.id.iv_right_menu_icon);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    //订阅者
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchFragment(MessageEvent event){
        String title = null;
        switch (event.getType()){
            case MessageEvent.TYPE_FORGOT_PASSWORD_FRAGMENT:
                if(forgotPasswordFragment == null){
                    forgotPasswordFragment = new ForgotPasswordFragment();
                }
                title = "忘记密码";
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_content,forgotPasswordFragment).commit();
                break;
            case MessageEvent.TYPE_MAIN_FRAGMENT:
                addMainFragment();
                title = "资讯";
                break;
            case MessageEvent.TYPE_LOGIN_FRAGMENT:
                if(loginFragment == null){
                    loginFragment = new LoginFragment();
                }
                title = "用户登录";
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_content,loginFragment).commit();
                break;
            case MessageEvent.TYPE_REGISTER_FRAGMENT:
                if(registerFragment == null){
                    registerFragment = new RegisterFragment();
                }
                title = "用户注册";
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_content,registerFragment).commit();
                break;
        }

        tvTitle.setText(title);

        if(slidingMenu != null && slidingMenu.isMenuShowing()){
            slidingMenu.showContent();
        }
    }

    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_left_menu_icon://当侧滑没有显示，则显示侧滑，当侧滑显示，则收起侧滑
                    if(slidingMenu != null && slidingMenu.isMenuShowing()){
                        slidingMenu.showContent();
                    }else if(slidingMenu != null){
                        slidingMenu.showMenu();
                    }
                    break;
                case R.id.iv_right_menu_icon:
                    if(slidingMenu != null && slidingMenu.isMenuShowing()){
                        slidingMenu.showContent();
                    }else if(slidingMenu != null){
                        slidingMenu.showSecondaryMenu();
                    }
                    break;
            }
        }
    };


}
