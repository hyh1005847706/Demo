package com.edu.feicui.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.edu.feicui.news.R;
import com.edu.feicui.news.activity.ShowNewsActivity;
import com.edu.feicui.news.adapter.NewsAdapter;
import com.edu.feicui.news.adapter.NewsTypeAdapter;
import com.edu.feicui.news.base.BaseActivity;
import com.edu.feicui.news.biz.NewsManager;
import com.edu.feicui.news.db.NewsDBManager;
import com.edu.feicui.news.entity.News;
import com.edu.feicui.news.entity.SubType;
import com.edu.feicui.news.parser.NewsParser;
import com.edu.feicui.news.utils.CommonUtils;
import com.edu.feicui.news.view.HorizontalListView;
import com.edu.feicui.news.xlistview.XListView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class MainFragment extends Fragment {

    private ImageView ivTypeMore;
    private HorizontalListView hlType;
    private XListView xListView;

    private NewsDBManager newsDBManager;
    private NewsTypeAdapter newsTypeAdapter;

    private NewsAdapter newsAdapter;

    //分类编号
    private int subid = 1;
    //加载数据模式
    private int refreshMode = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        ivTypeMore = (ImageView) view.findViewById(R.id.iv_type_more);
        hlType = (HorizontalListView) view.findViewById(R.id.hl_type);
        xListView = (XListView) view.findViewById(R.id.listview);
        newsTypeAdapter = new NewsTypeAdapter(getActivity());
        newsDBManager = new NewsDBManager(getActivity());
        hlType.setAdapter(newsTypeAdapter);
        hlType.setOnItemClickListener(typeItemClickListener);

        loadNewsType();

        newsAdapter = new NewsAdapter(getActivity());
        //启用上拉加载
        xListView.setPullLoadEnable(true);
        //启用下拉刷新
        xListView.setPullRefreshEnable(true);
        xListView.setXListViewListener(listViewListener);
        xListView.setAdapter(newsAdapter);
        xListView.setOnItemClickListener(newsItemClickListener);
        refreshMode = NewsManager.MODE_PULL_REFRESH;
        loadNextNews(true);
        //显示加载进度对话框
        ((BaseActivity)getActivity()).showDialog(null,false);
        return view;
    }

    private AdapterView.OnItemClickListener newsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            News news = (News) adapterView.getItemAtPosition(i);
            Bundle bundle = new Bundle();
            bundle.putSerializable("news",news);
            BaseActivity activity = (BaseActivity) getActivity();
            activity.startActivity(ShowNewsActivity.class,bundle);
        }
    };

    private AdapterView.OnItemClickListener typeItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SubType subType = (SubType) adapterView.getItemAtPosition(i);
            subid = subType.getSubid();
            newsTypeAdapter.setCurrentPosition(i);
            newsTypeAdapter.notifyDataSetChanged();
            ((BaseActivity)getActivity()).cancelDialog();
            loadNextNews(true);
        }
    };

    private XListView.IXListViewListener listViewListener = new XListView.IXListViewListener(){
        //下拉刷新会回调该方法
        @Override
        public void onRefresh() {
            refreshMode = NewsManager.MODE_PULL_REFRESH;
            loadNextNews(false);
        }

        //上拉加载会回调该方法
        @Override
        public void onLoadMore() {
            refreshMode = NewsManager.MODE_LOAD_MORE;
            loadPrevNews();
        }
    };

    //加载新数据
    private void loadNextNews(boolean isNewType){
        int nid = 1;
        //如果isNewType为true，则是第一次加载数据，此时nid=1，如果不是第一次加载数据，则需要获取列表中第一条数据的新闻编号
        if(!isNewType){
            if(newsAdapter.getData().size() > 0){
                nid = newsAdapter.getData().get(0).getNid();
            }
        }

        if(CommonUtils.isNetConnect(getActivity())){
            NewsManager.getNewsList(getActivity(),subid,refreshMode,nid,newsListener,errorListener);
        }else{
            //从缓存中获取新闻数据
        }
    }

    //加载以前的数据
    private void  loadPrevNews(){
        //没有数据，不需要进行上拉加载数据
        if(newsAdapter.getData().size() == 0){
            return;
        }

        int lastIndex = newsAdapter.getData().size() - 1;
        int nid = newsAdapter.getData().get(lastIndex).getNid();
        if(CommonUtils.isNetConnect(getActivity())){
            NewsManager.getNewsList(getActivity(),subid,refreshMode,nid,newsListener,errorListener);
        }
    }

    //加载新闻分类
    private void loadNewsType(){
        //先判断数据库中是否有缓存的数据，如果有，则使用缓存的数据，如果没有，则判断是否有网络，有网络，则去服务器端加载数据
        if(newsDBManager.getNewsSubType().size() == 0){
            if(CommonUtils.isNetConnect(getActivity())){
                NewsManager.getNewsType(getActivity(),newsTypeListener,errorListener);
            }
        }else{
            List<SubType> list = newsDBManager.getNewsSubType();
            newsTypeAdapter.appendDataToAdapter(list,true);
            newsTypeAdapter.notifyDataSetChanged();
        }
    }

    //获取新闻分类成功之后回调的接口
    private Response.Listener<String> newsTypeListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String json) {
            List<SubType> list = NewsParser.parseNewsYype(json);
            newsTypeAdapter.appendDataToAdapter(list,true);
            newsTypeAdapter.notifyDataSetChanged();
            //将新闻分类数据进行缓存
            newsDBManager.saveNewsType(list);
        }
    };

    private Response.Listener<String> newsListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String json) {
            List<News> list = NewsParser.parseNews(json);
            boolean isClear = refreshMode == NewsManager.MODE_PULL_REFRESH ? true : false;
            newsAdapter.appendDataToAdapter(list, isClear);
            newsAdapter.notifyDataSetChanged();
            xListView.stopLoadMore();
            xListView.stopRefresh();
            xListView.setRefreshTime(CommonUtils.getCurrentDate());

            ((BaseActivity)getActivity()).cancelDialog();
        }
    };

    //获取新闻分类失败之后回调的方法
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            //取消进度对话框
            xListView.stopLoadMore();
            xListView.stopRefresh();
            Toast.makeText(getActivity(),"加载数据失败",Toast.LENGTH_SHORT).show();

            ((BaseActivity)getActivity()).cancelDialog();
        }
    };
}
