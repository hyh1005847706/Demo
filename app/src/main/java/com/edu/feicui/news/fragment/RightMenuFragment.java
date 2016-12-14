package com.edu.feicui.news.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.feicui.news.R;
import com.edu.feicui.news.activity.MyUserActivity;
import com.edu.feicui.news.base.BaseActivity;
import com.edu.feicui.news.biz.VersionManager;
import com.edu.feicui.news.entity.MessageEvent;
import com.edu.feicui.news.entity.Version;
import com.edu.feicui.news.listener.FailListener;
import com.edu.feicui.news.listener.SuccessListener;
import com.edu.feicui.news.utils.CommonUtils;
import com.edu.feicui.news.utils.LoadImage;
import com.edu.feicui.news.utils.Md5;
import com.edu.feicui.news.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2016/11/24.
 */
public class RightMenuFragment extends Fragment{
    private LinearLayout llUnlogin;
    private ImageView ivUnlogin;
    private TextView tvUnlogin;

    private LinearLayout llLogin;
    private ImageView ivLogin;
    private TextView tvLogin;

    private ImageView ivShareWeixin;
    private ImageView ivShareQQ;
    private ImageView ivShareFriend;
    private ImageView ivShareWeibo;

    private TextView tvUpdate;
    private long downloadId = -1;
    private DownloadManager downloadManager;

    private String link = null;
    String pkgName = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right_menu,container,false);
        llUnlogin = (LinearLayout) view.findViewById(R.id.ll_unlogin);
        ivUnlogin = (ImageView) view.findViewById(R.id.iv_unlogin);
        tvUnlogin = (TextView) view.findViewById(R.id.tv_unlogin);

        llLogin = (LinearLayout) view.findViewById(R.id.ll_login);
        ivLogin = (ImageView) view.findViewById(R.id.iv_login);
        tvLogin = (TextView) view.findViewById(R.id.tv_login);

        ivShareWeixin = (ImageView) view.findViewById(R.id.iv_share_weixin);
        ivShareQQ = (ImageView) view.findViewById(R.id.iv_share_qq);
        ivShareFriend = (ImageView) view.findViewById(R.id.iv_share_frient);
        ivShareWeibo = (ImageView) view.findViewById(R.id.iv_share_weibo);
        tvUpdate = (TextView) view.findViewById(R.id.tv_update);

        ivUnlogin.setOnClickListener(listener);
        tvUnlogin.setOnClickListener(listener);
        ivLogin.setOnClickListener(listener);
        tvLogin.setOnClickListener(listener);
        tvUpdate.setOnClickListener(listener);

        ivShareWeibo.setOnClickListener(listener);
        ivShareWeixin.setOnClickListener(listener);
        ivShareQQ.setOnClickListener(listener);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(receiver,filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(getActivity(),"androidv1101");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SharedPreferencesUtils.isLogin(getActivity())){
            llUnlogin.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
            //设置头像与用户名
            SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
            String username = sp.getString("username","");
            String headImage = sp.getString("headImage","");
            String localHeadImage = sp.getString("localHeadImage","");

            tvLogin.setText(username);
            File file = new File(localHeadImage);
            if(file.exists()){
                ivLogin.setImageURI(Uri.fromFile(file));
            }else {
                LoadImage loadImage = new LoadImage(getActivity());
                loadImage.displayBitmap(headImage,ivLogin);
            }
        }else {
            llLogin.setVisibility(View.GONE);
            llUnlogin.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MessageEvent event = new MessageEvent();
            BaseActivity activity = (BaseActivity) getActivity();
            switch (v.getId()){
                case R.id.iv_unlogin:
                    event.setType(MessageEvent.TYPE_LOGIN_FRAGMENT);
                    //返回权限命名
                    event.setFragmentFullName(LoginFragment.class.getName());
                    //发布事件
                    EventBus.getDefault().post(event);
                    break;
                case R.id.tv_unlogin:
                    event = new MessageEvent();
                    event.setType(MessageEvent.TYPE_LOGIN_FRAGMENT);
                    //返回权限命名
                    event.setFragmentFullName(LoginFragment.class.getName());
                    EventBus.getDefault().post(event);
                    break;
                case R.id.tv_login:
                    activity.startActivity(MyUserActivity.class);
                    activity.finish();
                    break;
                case R.id.iv_login:
                    activity.startActivity(MyUserActivity.class);
                    activity.finish();
                    break;
                case R.id.tv_update:
                    VersionManager.getUpdateVersion(getActivity(),successListener,failListener);

                    break;
                case R.id.iv_share_weixin:
                    Platform platform = ShareSDK.getPlatform(getActivity(), Wechat.NAME);
                    showShare(platform.getName());
                    break;
                case R.id.iv_share_weibo:
                    Platform platform1 = ShareSDK.getPlatform(getActivity(), SinaWeibo.NAME);
                    showShare(platform1.getName());
                    break;
                case R.id.iv_share_qq:
                    Platform platform2 = ShareSDK.getPlatform(getActivity(), QQ.NAME);
                    showShare(platform2.getName());
                    break;
            }
        }
    };

    private SuccessListener successListener = new SuccessListener() {
        @Override
        public void onSuccess(String json) {
            Gson gson = new Gson();
            Version version = gson.fromJson(json, Version.class);
            pkgName = version.getPkgName();
            String versionName = version.getVersion();
            link = version.getLink();
            String md5 = version.getMd5();
            String localMd5 = Md5.produceMd5(link);
            String localVersion = CommonUtils.getVersionCode(getActivity()) + "";
            if(!versionName.equals(localVersion)){
                if(md5.equals(localMd5)){
                    download();
                }else {
                    CommonUtils.showShortToast(getActivity(),"安装出现异常，请重试");
                }
            }else {
                CommonUtils.showShortToast(getActivity(),"您的版本已经是最新版本");
            }
        }
    };

    //下载更新包
    private void download(){
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(link);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("正在下载安装包");
        request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS,"update.apk");
        downloadId = downloadManager.enqueue(request);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            if(id == downloadId){
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);

                Cursor cursor = downloadManager.query(query);
                if(cursor.moveToFirst()){
                    String localFileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    Intent other = new Intent(Intent.ACTION_VIEW);
                    other.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    other.setDataAndType(Uri.parse("file://" + localFileName), "application/vnd.android.package-archive");
                    context.startActivity(intent);
                }
            }
        }
    };

    private FailListener failListener = new FailListener() {
        @Override
        public void onFail(String error) {

        }
    };

    private void showShare(String name) {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
// text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        oks.setPlatform(name);
// 启动分享GUI
        oks.show(getActivity());
    }

}
