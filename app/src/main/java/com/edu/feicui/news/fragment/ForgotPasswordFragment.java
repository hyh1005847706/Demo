package com.edu.feicui.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.edu.feicui.news.R;
import com.edu.feicui.news.entity.BaseEntity;
import com.edu.feicui.news.entity.MessageEvent;
import com.edu.feicui.news.entity.UserResponse;
import com.edu.feicui.news.listener.FailListener;
import com.edu.feicui.news.listener.SuccessListener;
import com.edu.feicui.news.utils.CommonUtils;
import com.edu.feicui.news.utils.OkHttpUtils;
import com.edu.feicui.news.utils.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/12/2.
 */
public class ForgotPasswordFragment extends Fragment {
    private EditText etEmail;
    private Button btnConfirm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password,container,false);
        etEmail = (EditText) view.findViewById(R.id.et_email);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                if(!CommonUtils.verifyEmail(email)){
                    CommonUtils.showShortToast(getActivity(),"请输入正确的邮箱");
                    return;
                }

                OkHttpUtils.doGet(Url.FORGOT_PASSWORD + "?ver=0&email=" + email,successListener,failListener);
            }
        });

        return view;
    }

    private SuccessListener successListener = new SuccessListener() {
        @Override
        public void onSuccess(String json) {
            Gson gson = new Gson();
            BaseEntity<UserResponse> baseEntity = gson.fromJson(json,new TypeToken<BaseEntity<UserResponse>>(){}.getType());
            if(baseEntity.getStatus().equals("0")){
                if(baseEntity.getData().getResult() == 0){
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setType(MessageEvent.TYPE_LOGIN_FRAGMENT);
                    EventBus.getDefault().post(messageEvent);
                    CommonUtils.showShortToast(getActivity(),"成功发送至邮箱");
                    return;
                }else {
                    CommonUtils.showShortToast(getActivity(),baseEntity.getData().getExplain());
                }
            }else {
                CommonUtils.showShortToast(getActivity(),"发送失败，请重试");
            }
        }
    };

    private FailListener failListener = new FailListener() {
        @Override
        public void onFail(String error) {
            CommonUtils.showShortToast(getActivity(),"发送失败，请重试");
        }
    };
}
