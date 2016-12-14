package com.edu.feicui.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.feicui.news.R;
import com.edu.feicui.news.entity.News;
import com.edu.feicui.news.utils.LoadImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public class NewsAdapter extends BaseAdapter {
    private List<News> list = new ArrayList<News>();
    private Context context;
    private LayoutInflater inflater;
    private LoadImage loadImage;

    public NewsAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        loadImage = new LoadImage(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public List<News> getData(){
        return list;
    }

    public void appendDataToAdapter(List<News> data,boolean isClear){
        if(data == null || data.size() == 0){
            return;
        }

        if(isClear){
            list.clear();
        }

        list.addAll(data);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = inflater.inflate(R.layout.news_content_list_item,null);
            holder = new ViewHolder();
            holder.tvContent = (TextView) view.findViewById(R.id.tv_content);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            holder.tvDate = (TextView) view.findViewById(R.id.tv_date);
            holder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        News news = list.get(i);
        holder.tvContent.setText(news.getSummary());
        holder.tvDate.setText(news.getStamp());
        holder.tvTitle.setText(news.getTitle());
        loadImage.displayBitmap(news.getIcon(),holder.ivIcon);

        return view;
    }

    class ViewHolder{
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvContent;
        TextView tvDate;
    }
}
