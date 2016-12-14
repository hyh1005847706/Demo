package com.edu.feicui.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.feicui.news.R;
import com.edu.feicui.news.entity.LoginLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */
public class LoginLogAdapter extends BaseAdapter {
    private List<LoginLog> list = new ArrayList<LoginLog>();
    private Context context;
    private LayoutInflater inflater;

    public LoginLogAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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

    public void appendDataToAdapter(List<LoginLog> dataList){
        if(dataList == null || dataList.size() == 0){
            return;
        }

        list.clear();
        list.addAll(dataList);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = inflater.inflate(R.layout.list_item_user,null);
            holder = new ViewHolder();
            holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
            holder.tvAddress = (TextView) view.findViewById(R.id.tv_address);
            holder.tvDevice = (TextView) view.findViewById(R.id.tv_device);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        LoginLog loginLog = list.get(i);
        String[] time = loginLog.getTime().split(" ");
        holder.tvTime.setText(time[0]);
        holder.tvAddress.setText(loginLog.getAddress());
        holder.tvDevice.setText(loginLog.getDevice() == 0 ? "手机客户端" : "pc端");

        return view;
    }

    class ViewHolder{
        TextView tvTime;
        TextView tvAddress;
        TextView tvDevice;
    }
}
