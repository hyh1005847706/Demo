package com.edu.feicui.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.edu.feicui.news.R;
import com.edu.feicui.news.adapter.NewsCommentAdapter;
import com.edu.feicui.news.base.BaseActivity;
import com.edu.feicui.news.biz.NewsManager;
import com.edu.feicui.news.entity.BaseEntity;
import com.edu.feicui.news.entity.BaseEntityList;
import com.edu.feicui.news.entity.CommentUser;
import com.edu.feicui.news.entity.ShowComments;
import com.edu.feicui.news.listener.FailListener;
import com.edu.feicui.news.listener.SuccessListener;
import com.edu.feicui.news.parser.NewsParser;
import com.edu.feicui.news.utils.CommonUtils;
import com.edu.feicui.news.utils.OkHttpUtils;
import com.edu.feicui.news.utils.SharedPreferencesUtils;
import com.edu.feicui.news.utils.Url;
import com.edu.feicui.news.xlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/1.
 */
public class CommentActivity extends BaseActivity {
    private XListView xListView;
    private EditText etWriteComment;
    private ImageView ivSend;
    private NewsCommentAdapter newsCommentAdapter;
    List<ShowComments> list = new ArrayList<ShowComments>();

    private int dir = 1;
    private int cid = 1;
    private int nid = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        xListView = (XListView) findViewById(R.id.xListView);
        etWriteComment = (EditText) findViewById(R.id.et_write_comment);
        ivSend = (ImageView) findViewById(R.id.iv_send);

        newsCommentAdapter = new NewsCommentAdapter(this);
        xListView.setAdapter(newsCommentAdapter);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);
        xListView.setXListViewListener(listViewListener);

        Intent intent = getIntent();
        nid = intent.getIntExtra("nid", 0);

        OkHttpUtils.doGet(Url.GET_COMMENT + "?ver=0&nid=" + nid + "&type=1&stamp=yyyyMMdd&cid=1&dir=1&cnt=20",successListener,failListener);

        ivSend.setOnClickListener(listener);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String imei = CommonUtils.getIMEI(getBaseContext());
            String token = SharedPreferencesUtils.getToken(getBaseContext());
            String ctx = etWriteComment.getText().toString();
            OkHttpUtils.doGet(Url.SEND_COMMENT + "?ver=0&nid=" + nid + "&token=" + token + "&imei=" + imei + "&ctx=" + ctx,postSuccessListener,failListener);
        }
    };

   private SuccessListener postSuccessListener = new SuccessListener() {
       @Override
       public void onSuccess(String json) {
           Gson gson = new Gson();
           BaseEntity<CommentUser> baseEntity = gson.fromJson(json,new TypeToken<BaseEntity<CommentUser>>(){}.getType());
           if(baseEntity.getStatus().equals("0")){
               if(baseEntity.getData().getResult() == 0){
                   etWriteComment.setText(null);
                   loadNewNews();
               }else {
                   CommonUtils.showShortToast(CommentActivity.this, baseEntity.getData().getExplain());
               }
           }else {
               CommonUtils.showShortToast(CommentActivity.this,"发送失败，请重试");
           }
       }
   };

    public void back(View view){
        finish();
    }

    private XListView.IXListViewListener listViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            dir = NewsManager.MODE_PULL_REFRESH;
            loadNewNews();
        }

        @Override
        public void onLoadMore() {
            dir = NewsManager.MODE_LOAD_MORE;
            loadOldNews(false);
        }
    };

    private void loadNewNews(){
        dir = 1;

       if(CommonUtils.isNetConnect(CommentActivity.this)){
           String stamp = CommonUtils.getCurrentDate();
           OkHttpUtils.doGet(Url.GET_COMMENT + "?ver=0&nid=" + nid + "&type=1&stamp=" + stamp + "yyyyMMdd&cid=1&dir=" + dir + "&cnt=20",successListener,failListener);
           CommonUtils.showShortToast(CommentActivity.this,cid + "");
       }
    }

    private void loadOldNews(boolean isNewContent){
        dir = 2;

        if(!isNewContent){
            if(newsCommentAdapter.getData().size() > 0){
                cid = newsCommentAdapter.getData().get(newsCommentAdapter.getData().size() - 1).getCid();
            }
        }
        if(CommonUtils.isNetConnect(CommentActivity.this)){
            OkHttpUtils.doGet(Url.GET_COMMENT + "?ver=0&nid=" + nid + "&type=1&stamp=yyyyMMdd&cid=" + cid + "&dir=" + dir + "&cnt=20",successListener,failListener);
        }
    }

    private SuccessListener successListener = new SuccessListener() {
        @Override
        public void onSuccess(String json) {
            Gson gson = new Gson();
            BaseEntity<List<ShowComments>> baseEntity = gson.fromJson(json,new TypeToken<BaseEntity<List<ShowComments>>>(){}.getType());
            xListView.stopRefresh();
            xListView.stopLoadMore();
            List<ShowComments> list = baseEntity.getData();
            boolean isClear = dir == NewsManager.MODE_PULL_REFRESH ? true : false;
            newsCommentAdapter.appendDataToAdapter(list,isClear);
            newsCommentAdapter.notifyDataSetChanged();
            xListView.setRefreshTime(CommonUtils.getCurrentDate());
//            }else {
//                CommonUtils.showShortToast(CommentActivity.this,"网络异常，请重试");
//            }
        }
    };

        private FailListener failListener = new FailListener() {
            @Override
            public void onFail(String error) {
                Toast.makeText(CommentActivity.this, "加载失败，请重试", Toast.LENGTH_SHORT).show();
                xListView.stopRefresh();
                xListView.stopLoadMore();
            }
        };
    }
