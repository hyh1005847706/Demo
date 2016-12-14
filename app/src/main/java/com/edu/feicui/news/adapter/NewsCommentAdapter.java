package com.edu.feicui.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.feicui.news.R;
import com.edu.feicui.news.entity.ShowComments;
import com.edu.feicui.news.utils.LoadImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
public class NewsCommentAdapter extends BaseAdapter {
    List<ShowComments> list = new ArrayList<ShowComments>();
    private Context context;
    private LayoutInflater inflater;


    public NewsCommentAdapter(Context context){
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<ShowComments> getData(){
        return list;
    }

    public void appendDataToAdapter(List<ShowComments> data,boolean isClear){
        if(data == null || data.size() == 0){
            return;
        }

        if(isClear){
            list.clear();
        }

        list.addAll(data);
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = inflater.inflate(R.layout.comment_list_item,null);
            holder = new ViewHolder();
            holder.tvName = (TextView) view.findViewById(R.id.tv_comment_username);
            holder.tvDate = (TextView) view.findViewById(R.id.tv_coomment_date);
            holder.tvContent = (TextView) view.findViewById(R.id.tv_comment);
            holder.ivIcon = (ImageView) view.findViewById(R.id.iv_comment_icon);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        ShowComments showComments = list.get(i);
        holder.tvName.setText(showComments.getUid());
        holder.tvDate.setText(showComments.getStamp());
        holder.tvContent.setText(showComments.getContent());

        String uri = showComments.getPortrait();
        LoadImage loadImage = new LoadImage(context);
        loadImage.displayBitmap(uri,holder.ivIcon);

        return view;
    }

    class ViewHolder{
        TextView tvName;
        TextView tvDate;
        TextView tvContent;
        ImageView ivIcon;
    }
}
