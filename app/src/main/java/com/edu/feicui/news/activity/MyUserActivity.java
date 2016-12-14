package com.edu.feicui.news.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.feicui.news.R;
import com.edu.feicui.news.adapter.LoginLogAdapter;
import com.edu.feicui.news.base.BaseActivity;
import com.edu.feicui.news.biz.UserManager;
import com.edu.feicui.news.entity.BaseEntity;
import com.edu.feicui.news.entity.LoginLog;
import com.edu.feicui.news.entity.UserResponse;
import com.edu.feicui.news.entity.Users;
import com.edu.feicui.news.listener.FailListener;
import com.edu.feicui.news.listener.SuccessListener;
import com.edu.feicui.news.parser.UserParser;
import com.edu.feicui.news.utils.CommonUtils;
import com.edu.feicui.news.utils.LoadImage;
import com.edu.feicui.news.utils.SharedPreferencesUtils;
import com.edu.feicui.news.utils.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */
public class MyUserActivity extends BaseActivity {
    private LinearLayout llContainer;
    private ImageView ivUnLoginImage;
    private TextView tvUsername;
    private TextView tvScore;
    private TextView tvCommentNum;
    private ListView lvLogin;
    private Button btnExit;

    LoadImage loadImage;
    LoginLogAdapter loginLogAdapter;
    PopupWindow popupWindow;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_user);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivUnLoginImage = (ImageView) findViewById(R.id.iv_unlogin_image);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        tvScore = (TextView) findViewById(R.id.tv_score);
        tvCommentNum = (TextView) findViewById(R.id.tv_comment_num);
        lvLogin = (ListView) findViewById(R.id.lv_login);
        btnExit = (Button) findViewById(R.id.btn_exit);

        loadImage = new LoadImage(this);
        //从本地显示用户名与头像
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        tvUsername.setText(sp.getString("username",""));
        String headImage = sp.getString("headImage","");
        String localHeadImage = sp.getString("localHeadImage","");
        File file = new File(localHeadImage);
        if(file.exists()){
            Uri uri = Uri.fromFile(file);
            ivUnLoginImage.setImageURI(uri);
        }else {
            loadImage.displayBitmap(headImage,ivUnLoginImage);
        }

        loadImage.displayBitmap(headImage,ivUnLoginImage);

        loginLogAdapter = new LoginLogAdapter(this);
        lvLogin.setAdapter(loginLogAdapter);

        ivUnLoginImage.setOnClickListener(listener);
        btnExit.setOnClickListener(listener);

        sendRequest();
        initPopupWindow();

    }

    private void initPopupWindow(){
        View view = getLayoutInflater().inflate(R.layout.popup_photo,null);
        LinearLayout llTakePhoto = (LinearLayout) view.findViewById(R.id.ll_take_photo);
        LinearLayout llSelPhoto = (LinearLayout) view.findViewById(R.id.ll_sel_photo);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);

        llSelPhoto.setOnClickListener(listener);
        llTakePhoto.setOnClickListener(listener);
    }

    private void sendRequest(){
        UserManager.getUserInfo(this, new SuccessListener() {
            @Override
            public void onSuccess(String json) {
                BaseEntity<Users> baseEntity = UserParser.parseUser(json);
                if(baseEntity.getStatus().equals("0")){
                    Users user = baseEntity.getData();
                    tvUsername.setText(user.getUid());
                    tvScore.setText("积分：" + user.getIntegration());
                    tvCommentNum.setText(user.getComnum() + "");
                    if(!TextUtils.isEmpty(user.getPortrait())){
                        loadImage.displayBitmap(user.getPortrait(),ivUnLoginImage);
                    }

                    SharedPreferencesUtils.saveUsers(MyUserActivity.this,user);
                    List<LoginLog> loginLogList = user.getLoginlog();
                    loginLogAdapter.appendDataToAdapter(loginLogList);
                    loginLogAdapter.notifyDataSetChanged();
                }else {
                    CommonUtils.showShortToast(MyUserActivity.this,"获取用户信息失败");
                }
            }
        }, new FailListener() {
            @Override
            public void onFail(String error) {
                CommonUtils.showShortToast(MyUserActivity.this,"获取用户信息失败");
            }
        });
    }

    public void back(View view){
        startActivity(MainActivity.class);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_unlogin_image:
                    popupWindow.showAtLocation(llContainer, Gravity.BOTTOM,0,0);
                    break;
                case R.id.btn_exit:
                    SharedPreferencesUtils.clearUser(MyUserActivity.this);
                    Intent intent = new Intent(MyUserActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.ll_take_photo:
                    popupWindow.dismiss();
                    takePhoto();
                    break;
                case R.id.ll_sel_photo:
                    popupWindow.dismiss();
                    selectPhoto();
                    break;
            }
        }
    };

    //通过拍照获取图片
    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);
    }

    //从图片库中获取图片
    private void selectPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //指定获取内容的数据类型
        intent.setType("image/*");
        //对图片进行裁剪
        intent.putExtra("crop","true");
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX",80);
        intent.putExtra("outputY",80);
        intent.putExtra("scale",true);
        intent.putExtra("return-data",true);
//        intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection",true);

        startActivityForResult(intent,200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //拍照请求返回
        if(requestCode == 100){
            if(resultCode == Activity.RESULT_OK){
                Bundle bundle = intent.getExtras();
                bitmap = (Bitmap) bundle.get("data");
                //将图片处理成圆角并保存
                save(bitmap);
            }
        }else if(requestCode == 200){
            if(resultCode == Activity.RESULT_OK){
                Uri originalUri = intent.getData();//获得图片的Uri
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),originalUri);
                    save(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    File cacheDir = null;
    private void save(Bitmap bitmap){
        cacheDir = new File(getExternalCacheDir(),"newsClient");
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }

        File file = new File(cacheDir,"headImage.jpg");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream)){
                //发送网络请求上传头像
                UserManager.uploadUserImage(MyUserActivity.this, Url.USER_IMAGE,file,successListener,failListener);
                //图片路径缓存
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


//        if(bitmap != null){
//            //转换为圆角图片
//            bitmap = convertRountBitmap();
//            //保存图片
//            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//                File dir = Environment.getExternalStorageDirectory();
//                File headDir = new File(dir, "newsClient");
//                if(headDir.exists()){
//                    headDir.mkdirs();
//                }
//
//                File headFile = new File(headDir, "news_head.jpg");
//                OutputStream outputStream = null;
//                try {
//                    outputStream = new FileOutputStream(headFile);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
//                    //发送请求，上次图像
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }finally {
//                    if(outputStream != null){
//                        try {
//                            outputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//            }
//        }
    }

    private SuccessListener successListener = new SuccessListener() {
        @Override
        public void onSuccess(String json) {
            //解析返回的json字符串
            Gson gson = new Gson();
            BaseEntity<UserResponse> baseEntity = gson.fromJson(json,new TypeToken<BaseEntity<UserResponse>>(){}.getType());
            if(baseEntity.getStatus().equals("0")){
                if(baseEntity.getData().getResult() == 0){
                    ivUnLoginImage.setImageBitmap(bitmap);
                    File file = new File(cacheDir,"headImage.jpg");
                    SharedPreferencesUtils.saveUserHeadImagePath(MyUserActivity.this,file.getPath());
                    return;
                }
            }
            CommonUtils.showShortToast(MyUserActivity.this,"上传头像失败，请重试");
        }
    };

    private FailListener failListener = new FailListener() {
        @Override
        public void onFail(String error) {

        }
    };

    private Bitmap convertRountBitmap(){
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.userbg);
        Bitmap dest = Bitmap.createBitmap(srcBitmap.getWidth(),srcBitmap.getHeight(),srcBitmap.getConfig());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(bitmap,0,0,paint);
        return bitmap;
    }
}
