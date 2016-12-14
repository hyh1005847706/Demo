package com.edu.feicui.news.entity;

/**
 * Created by Administrator on 2016/12/2.
 */

//EventBus派发的事件实体类
public class MessageEvent {
    //将要添加LoginFragment
    public static final int TYPE_LOGIN_FRAGMENT = 1;
    //将要添加RegisterFragment
    public static final int TYPE_REGISTER_FRAGMENT = 2;
    //将要添加ForgotPasswordFragment
    public static final int TYPE_FORGOT_PASSWORD_FRAGMENT = 3;
    //将要添加MainFragment
    public static final int TYPE_MAIN_FRAGMENT = 4;
    //显示未登录视图组件
    public static final int TYPE_RIGHT_MENU_UNLOGIN = 5;
    //显示已登录视图组件
    public static final int TYPE_RIGHT_MENU_LOGIN = 6;

    private int type;
    private String fragmentFullName;

    public MessageEvent() {
    }

    public MessageEvent(int type, String fragmentFullName) {
        this.type = type;
        this.fragmentFullName = fragmentFullName;
    }

    public static int getTypeLoginFragment() {
        return TYPE_LOGIN_FRAGMENT;
    }

    public static int getTypeRegisterFragment() {
        return TYPE_REGISTER_FRAGMENT;
    }

    public static int getTypeForgotPasswordFragment() {
        return TYPE_FORGOT_PASSWORD_FRAGMENT;
    }

    public static int getTypeMainFragment() {
        return TYPE_MAIN_FRAGMENT;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFragmentFullName() {
        return fragmentFullName;
    }

    public void setFragmentFullName(String fragmentFullName) {
        this.fragmentFullName = fragmentFullName;
    }
}
