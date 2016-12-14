package com.edu.feicui.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.edu.feicui.news.R;
import com.edu.feicui.news.activity.MyUserActivity;
import com.edu.feicui.news.entity.BaseEntity;
import com.edu.feicui.news.entity.MessageEvent;
import com.edu.feicui.news.entity.UserResponse;
import com.edu.feicui.news.listener.FailListener;
import com.edu.feicui.news.listener.SuccessListener;
import com.edu.feicui.news.utils.CommonUtils;
import com.edu.feicui.news.utils.OkHttpUtils;
import com.edu.feicui.news.utils.SharedPreferencesUtils;
import com.edu.feicui.news.utils.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/12/2.
 */
public class LoginFragment extends Fragment {
    private EditText etNickname;
    private EditText etPassword;
    private Button btnRegister;
    private Button btnForgotPassword;
    private Button btnLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        etNickname = (EditText) view.findViewById(R.id.et_nickname);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        btnRegister = (Button) view.findViewById(R.id.btn_register);
        btnForgotPassword = (Button) view.findViewById(R.id.btn_forgot_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);

        btnRegister.setOnClickListener(listener);
        btnForgotPassword.setOnClickListener(listener);
        btnLogin.setOnClickListener(listener);

        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MessageEvent event = new MessageEvent();
            switch (v.getId()){
                case R.id.btn_register:
                    event.setType(MessageEvent.TYPE_REGISTER_FRAGMENT);
                    event.setFragmentFullName(RegisterFragment.class.getName());
                    EventBus.getDefault().post(event);
                    break;
                case R.id.btn_forgot_password:
                    event.setType(MessageEvent.TYPE_FORGOT_PASSWORD_FRAGMENT);
                    event.setFragmentFullName(ForgotPasswordFragment.class.getName());
                    EventBus.getDefault().post(event);
                    break;
                case R.id.btn_login:
                    String username = etNickname.getText().toString();
                    String password = etPassword.getText().toString();

                    if(!CommonUtils.verifyName(username)){
                        CommonUtils.showShortToast(getActivity(),"请输入正确的用户名");
                        return;
                    }

                    if(!CommonUtils.verifyPassword(password)){
                        CommonUtils.showShortToast(getActivity(),"亲，您输入的密码不正确");
                        return;
                    }

                    OkHttpUtils.doGet(Url.LOGIN + "?ver=0&device=0&uid=" + username + "&pwd=" + password, successListener,failListener);

                    break;
            }
        }
    };

    private SuccessListener successListener = new SuccessListener() {
        @Override
        public void onSuccess(String json) {
            Gson gson = new Gson();
            BaseEntity<UserResponse> baseEntity = gson.fromJson(json,new TypeToken<BaseEntity<UserResponse>>(){}.getType());
            if(baseEntity.getStatus().equals("0")){
                if(baseEntity.getData().getResult() == 0){
                    SharedPreferencesUtils.saveBaseEntity(getActivity(),baseEntity);
//                    MessageEvent messageEvent = new MessageEvent();
//                    messageEvent.setType(MessageEvent.TYPE_MAIN_FRAGMENT);
//                    EventBus.getDefault().post(messageEvent);



                    startActivity(new Intent(getActivity(), MyUserActivity.class));
                    getActivity().overridePendingTransition(R.anim.right_in,R.anim.bottom_out);
                }else {
                    CommonUtils.showShortToast(getActivity(),baseEntity.getData().getExplain());
                }
            }else {
                CommonUtils.showShortToast(getActivity(),"网络异常，请重试");
            }
        }
    };

    private FailListener failListener = new FailListener() {
        @Override
        public void onFail(String error) {
            CommonUtils.showShortToast(getActivity(),"网络异常，请重试");
        }
    };
}
