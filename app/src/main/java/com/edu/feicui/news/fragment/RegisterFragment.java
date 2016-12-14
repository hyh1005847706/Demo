package com.edu.feicui.news.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.edu.feicui.news.R;
import com.edu.feicui.news.entity.BaseEntity;
import com.edu.feicui.news.entity.DaoMaster;
import com.edu.feicui.news.entity.DaoSession;
import com.edu.feicui.news.entity.MessageEvent;
import com.edu.feicui.news.entity.User;
import com.edu.feicui.news.entity.UserDao;
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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/2.
 */
public class RegisterFragment extends Fragment {
    private EditText etEmail;
    private EditText etNickname;
    private EditText etPassword;
    private Button btnRegister;
    private CheckBox cbAgree;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        etEmail = (EditText) view.findViewById(R.id.et_email);
        etNickname = (EditText) view.findViewById(R.id.et_nickname);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        btnRegister = (Button) view.findViewById(R.id.btn_register);
        cbAgree = (CheckBox) view.findViewById(R.id.cb_agree);

//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String username = etNickname.getText().toString();
//                String password = etPassword.getText().toString();
//                String email = etEmail.getText().toString();
//
//                if(TextUtils.isEmpty(username)){
//                    Toast.makeText(getActivity(),"用户名不能为空",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if(TextUtils.isEmpty(password)){
//                    Toast.makeText(getActivity(),"密码不能为空",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if(TextUtils.isEmpty(email)){
//                    Toast.makeText(getActivity(),"邮箱不能为空",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),"news_users.db");
//                DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
//                DaoSession daoSession = daoMaster.newSession();
//
//                UserDao userDao = daoSession.getUserDao();
//                User user = userDao.queryBuilder().where(UserDao.Properties.Email.eq(email)).build().unique();
//                if(user != null){
//                    Toast.makeText(getActivity(),"对不起，该用户已存在",Toast.LENGTH_SHORT).show();
//                }else{
//                    user = new User(username,password,email);
//                    Toast.makeText(getActivity(),"恭喜你，注册成功",Toast.LENGTH_SHORT).show();
//                    userDao.insert(user);
//                }
//            }
//        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String username = etNickname.getText().toString();
                String password = etPassword.getText().toString();
                boolean isAgree = cbAgree.isChecked();

                if(!isAgree){
                    Toast.makeText(getActivity(),"必须同意条款才可以注册",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!CommonUtils.verifyEmail(email)){
                    CommonUtils.showShortToast(getActivity(),"请输入正确的邮箱");
                    return;
                }

                if(!CommonUtils.verifyName(username)){
                    CommonUtils.showShortToast(getActivity(),"用户名必须符合要求");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    CommonUtils.showShortToast(getActivity(),"密码不能为空");
                    return;
                }

                if(!CommonUtils.verifyPassword(password)){
                    CommonUtils.showShortToast(getActivity(),"密码必须在6~24位之间");
                    return;
                }

                OkHttpUtils.doGet(Url.REGISTER + "?ver=0&uid=" + username + "&pwd=" + password + "&email=" + email,successListener,failListener);
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
                    SharedPreferencesUtils.saveBaseEntity(getActivity(),baseEntity);
                    CommonUtils.showShortToast(getActivity(),"注册成功");
                    return;
                }
                CommonUtils.showShortToast(getActivity(),baseEntity.getData().getExplain());
            }else{
                CommonUtils.showShortToast(getActivity(),"注册失败，请重试");
            }
        }
    };

    private FailListener failListener = new FailListener() {
        @Override
        public void onFail(String error) {
            CommonUtils.showShortToast(getActivity(),"访问失败，请重试");
        }
    };

}
