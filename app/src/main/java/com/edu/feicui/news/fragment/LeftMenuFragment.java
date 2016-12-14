package com.edu.feicui.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.edu.feicui.news.R;
import com.edu.feicui.news.activity.MainActivity;
import com.edu.feicui.news.entity.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class LeftMenuFragment extends Fragment{
    private RelativeLayout rlNews;
    private RelativeLayout rlCollect;
    private RelativeLayout rlLocal;
    private RelativeLayout rlPhoto;
    private RelativeLayout rlComment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left_menu,container,false);
        rlComment = (RelativeLayout) view.findViewById(R.id.rl_comment);
        rlPhoto = (RelativeLayout) view.findViewById(R.id.rl_photo);
        rlCollect = (RelativeLayout) view.findViewById(R.id.rl_collect);
        rlLocal = (RelativeLayout) view.findViewById(R.id.rl_local);
        rlNews = (RelativeLayout) view.findViewById(R.id.rl_news);

        rlNews.setOnClickListener(listener);
        rlCollect.setOnClickListener(listener);
        rlLocal.setOnClickListener(listener);
        rlComment.setOnClickListener(listener);
        rlPhoto.setOnClickListener(listener);

        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            //重置颜色为黑色
            rlNews.setBackgroundColor(0);
            rlCollect.setBackgroundColor(0);
            rlComment.setBackgroundColor(0);
            rlLocal.setBackgroundColor(0);
            rlPhoto.setBackgroundColor(0);

            MessageEvent event = new MessageEvent();

            switch (view.getId()){
                case R.id.rl_collect:
                    rlCollect.setBackgroundColor(getResources().getColor(R.color.left_menu_selected_color));
                    Toast.makeText(getActivity(), "收藏", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rl_comment:
                    rlComment.setBackgroundColor(getResources().getColor(R.color.left_menu_selected_color));
                    Toast.makeText(getActivity(), "评论", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rl_local:
                    rlLocal.setBackgroundColor(getResources().getColor(R.color.left_menu_selected_color));
                    Toast.makeText(getActivity(), "本地", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rl_news:
                    rlNews.setBackgroundColor(getResources().getColor(R.color.left_menu_selected_color));
                    Toast.makeText(getActivity(), "新闻", Toast.LENGTH_SHORT).show();
                    event.setType(MessageEvent.TYPE_MAIN_FRAGMENT);
                    event.setFragmentFullName(MainActivity.class.getName());
                    EventBus.getDefault().post(event);
                    break;
                case R.id.rl_photo:
                    rlPhoto.setBackgroundColor(getResources().getColor(R.color.left_menu_selected_color));
                    Toast.makeText(getActivity(), "图片", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
