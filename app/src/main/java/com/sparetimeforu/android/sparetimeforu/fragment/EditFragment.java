package com.sparetimeforu.android.sparetimeforu.fragment;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.BuildConfig;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.entity.PhotoPopupWindow;
import com.sparetimeforu.android.sparetimeforu.entity.User;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by HQY on 2018/11/25.
 */

public class EditFragment extends Fragment {
    private String url;
    private static final int CHANGE_NICK_NAME = 0;
    private static final int CHANGE_SIGNATURE = 1;
    private static final int CHANGE_PHONE_CALL = 2;
    private static final int CHANGE_AVATOR = 3;
    private static final int CHANGE_SEX = 4;
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_BIG_IMAGE_CUTTING = 3;

    private Toolbar toolbar;
    private ImageView personal_go_back;
    //每一行的相对布局，对应不同信息的修改
    private View view;
    private RelativeLayout edit_PhoneCall, edit_Avatar, edit_Signature, edit_Nickname, edit_Sex,iv_bg_change;
    //每一行布局中的TextView，用以更新个人信息
    private TextView tv_edit_phonecall, tv_edit_signature, tv_edit_sex, tv_edit_nickname;
    private ImageView iv_edit_avatar, iv_edit_bg;
    PhotoPopupWindow mPhotoPopupWindow;


    Uri mUri;//文件在SD卡的路径，非content uri，Uri.fromFile()
    File mFile;//在SD卡的路径的文件
    private int imageViewNum = 0;//0代表编辑头像，1代表背景

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        url = STFUConfig.HOST + "/user";

        view = inflater.inflate(R.layout.personal_edit_fragment, container, false);
        ButterKnife.bind(this, view);

        //设置Toolbar
        toolbar = view.findViewById(R.id.personal_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        //设置Toolbar中返回按钮的点击事件

        personal_go_back = (ImageView) view.findViewById(R.id.persnal_go_back);
        personal_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        //初始化控件
        initWidgets();
        //初始化数据（sUser1234）
        initUser();
        updateWidgets();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPhotoPopupWindow.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    // 判断系统中是否有处理该 Intent 的 Activity
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_GET);
                    } else {
                        Toast.makeText(getContext(), "未找到图片查看器", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mPhotoPopupWindow.dismiss();
                }
                break;
            case 300:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPhotoPopupWindow.dismiss();
                    imageCapture();
                } else {
                    mPhotoPopupWindow.dismiss();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                // 相册选取后干的事，裁剪，此时data.getData()对应选取图片应用的内容提供器的uri
                case REQUEST_IMAGE_GET:
                    try {
                        if (data != null) {
                            startBigPhotoZoom(data.getData());
                        }
                    } catch (Exception e) {
                        Logger.e(e.toString());
                    }
                    break;


                //拍照完之后干的事，想要裁剪，此时avatarFile对应SD卡中的文件
                case REQUEST_IMAGE_CAPTURE:
                    Uri pictureUri = FileProvider.getUriForFile(getContext(),
                            "com.sparetimeforu.android.sparetimeforu.fileProvider", mFile);
                    startBigPhotoZoom(pictureUri); //自己定义的内容提供器的uri传入会裁剪失败，努力修复
                    //拍照完成临时使用代码
//                    if (imageViewNum == 0) {
//                        iv_edit_avatar.setImageURI(pictureUri);
//                        sendChangeAvatarRequest();
//                    } else {
//                        iv_edit_bg.setImageURI(pictureUri);
//                        sendChangeBGRequest();
//                    }
                    break;


                //裁剪完图片之后的操作
                case REQUEST_BIG_IMAGE_CUTTING:
                    if (mUri != null) {
                        if (imageViewNum == 0) {
                            iv_edit_avatar.setImageURI(mUri);
                            sendChangeAvatarRequest();
                        } else {
                            iv_edit_bg.setImageURI(mUri);
                            sendChangeBGRequest();
                        }
                    }
                    break;
            }
        }
    }

    @OnClick(R.id.btn_confirm)
    public void changeAttr() {
        sendChangeAttrRequest();
    }

    private void sendChangeAttrRequest() {
        FormBody body = new FormBody.Builder().
                add("auth_token",STFUConfig.sUser.getAuth_token()).
                add("nickname", tv_edit_nickname.getText().toString()).
                add("gender", tv_edit_sex.getText().toString()).
                add("signature", tv_edit_signature.getText().toString()).
                add("phone", tv_edit_phonecall.getText().toString()).
                build();

        OkHttpUtil.sendOkHttpPostRequest(url + "/modify_profile", body,
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Logger.i(e.toString());
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            Logger.i(jsonObject.toString());
                            if (jsonObject.getString("status").equals("success")) {
                                //一个吐司
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                });
                                AccountManager accountManager = AccountManager.get(getContext());
                                Account[] account = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID);

                                if (account.length != 0 && STFUConfig.sUser == null) {
                                    accountManager.setUserData(account[0], "nickname", tv_edit_nickname.getText().toString());
                                    accountManager.setUserData(account[0], "gender", tv_edit_sex.getText().toString());
                                    accountManager.setUserData(account[0], "phone", tv_edit_phonecall.getText().toString());
                                    accountManager.setUserData(account[0], "signature", tv_edit_signature.getText().toString());

                                    STFUConfig.sUser.setEmail(accountManager.getUserData(account[0], "email"));
                                    STFUConfig.sUser.setNickname(accountManager.getUserData(account[0], "nickname"));
                                    STFUConfig.sUser.setAvatar_url(accountManager.getUserData(account[0], "avatar_url"));
                                    STFUConfig.sUser.setFavourable_rate(accountManager.getUserData(account[0], "favourable_rate"));
                                    STFUConfig.sUser.setGender(accountManager.getUserData(account[0], "gender"));
                                    STFUConfig.sUser.setPhone(accountManager.getUserData(account[0], "phone"));
                                    STFUConfig.sUser.setSignature(accountManager.getUserData(account[0], "signature"));
                                    STFUConfig.sUser.setBg_url(accountManager.getUserData(account[0], "bg_url"));
                                }
                                getActivity().finish();
                            } else if (jsonObject.getString("status").equals("error")) {
                                String error = jsonObject.getString("error");
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                });
                            }
                        } catch (JSONException e) {
                            Logger.e(e.toString());
                        }

                        getActivity().runOnUiThread(() -> {
                            updateNativeUser();
                        });
                    }//onResponse
                });
    }

    private void sendChangeAvatarRequest() {
        if (mUri != null) {
            mFile = new File(URI.create(mUri.toString()));
        }

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("avatar", mFile.getName(),
                        FormBody.create(MediaType.parse("*"), mFile))
                .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                .build();

        OkHttpUtil.sendOkHttpPostRequest(url + "/modify_avatar", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.i(e.toString());
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "修改头像失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "修改头像成功", Toast.LENGTH_SHORT).show();
                    JMessageClient.updateUserAvatar(mFile, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            Logger.i("更换头像成功");
                        }
                    });
                    updateNativeUser();
                });
            }
        });
    }

    private void sendChangeBGRequest() {
        if (mUri != null) {
            mFile = new File(URI.create(mUri.toString()));
        }

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("personal_bg", mFile.getName(),
                        FormBody.create(MediaType.parse("*"), mFile))
                .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                .build();

        OkHttpUtil.sendOkHttpPostRequest(url + "/modify_personal_bg", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.i(e.toString());
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "修改背景失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "修改背景成功", Toast.LENGTH_SHORT).show();
                    updateNativeUser();
                });
            }
        });
    }

    /**
     * 创建对话框已修改信息
     *
     * @param changingUserAttr 要修改的东西（以字符串的形式传入）
     * @param changeType       要修改的类型，预先定义的类静态变量
     */
    private void showAlertDialog(String changingUserAttr, int changeType) {
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.personal_edit_input, null);
        EditText edit_input = (EditText) view1.findViewById(R.id.edit_input);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请输入修改后的信息");
        builder.setIcon(R.mipmap.ic_launcher_round);

        builder.setView(view1);
        builder.setPositiveButton("确认", (dialogInterface, i) -> {
            String input = edit_input.getText().toString();
            if (!input.equals("")) {

                //根据要修改的信息类型修改用户信息
                if (STFUConfig.sUser != null) {
                    switch (changeType) {
                        case CHANGE_NICK_NAME:
                            STFUConfig.sUser.setNickname(input);
                            break;
                        case CHANGE_PHONE_CALL:

                            if (isInteger(input))
                                STFUConfig.sUser.setPhone(input);
                            else
                                Toast.makeText(getActivity(), "修改电话时必须输入数字", Toast.LENGTH_SHORT).show();
                            break;
                        case CHANGE_SIGNATURE:
                            STFUConfig.sUser.setSignature(input);
                            break;
                    }
                    updateWidgets();
                }
            } else {
                Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("取消", (dialogInterface, i) -> {

        });
        builder.show();
    }

    private void initUser() {
        STFUConfig.sUser=((User) getActivity().getIntent().getSerializableExtra("user"));
        Logger.i(STFUConfig.sUser.toString());
    }

    private void updateNativeUser() {
        FormBody body = new FormBody.Builder()
                .add("auth_token", STFUConfig.sUser.getAuth_token())
                .build();

        OkHttpUtil.sendOkHttpPostRequest(url + "/refresh", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "更新用户信息失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取新的user，并存到本地AccountManger数据库
                STFUConfig.sUser=(HandleMessageUtil.handleLoginMessage(response.body().string()));
                AccountManager am = AccountManager.get(getContext());
                Account account = new Account(STFUConfig.sUser.getEmail(), BuildConfig.APPLICATION_ID);
                boolean isAdded = am.addAccountExplicitly(account, "", null);//安全起见，不存密码
                am.setAuthToken(account, "normal", STFUConfig.sUser.getAuth_token());
                am.setUserData(account, "email", STFUConfig.sUser.getEmail());
                am.setUserData(account, "nickname", STFUConfig.sUser.getNickname());
                am.setUserData(account, "signature", STFUConfig.sUser.getSignature());
                am.setUserData(account, "avatar_url", STFUConfig.sUser.getAvatar_url());
                am.setUserData(account, "favourable_rate", STFUConfig.sUser.getFavourable_rate());
                am.setUserData(account, "phone", STFUConfig.sUser.getPhone());
                am.setUserData(account, "gender", STFUConfig.sUser.getGender());
                am.setUserData(account, "bg_url", STFUConfig.sUser.getBg_url());

                //更新STFUConfig.User,更新组件
                getActivity().runOnUiThread(() -> {
                    STFUConfig.sUser=(STFUConfig.sUser);
                    updateWidgets();
                    Toast.makeText(getContext(), "更新用户信息成功", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void initWidgets() {

        edit_PhoneCall = (RelativeLayout) view.findViewById(R.id.edit_Phonecall);
        edit_Avatar = (RelativeLayout) view.findViewById(R.id.edit_Avatar);
        edit_Signature = (RelativeLayout) view.findViewById(R.id.edit_Signature);
        edit_Nickname = (RelativeLayout) view.findViewById(R.id.edit_Nickname);
        iv_bg_change=(RelativeLayout)view.findViewById(R.id.iv_bg_change);
        edit_Sex = (RelativeLayout) view.findViewById(R.id.edit_Sex);

        tv_edit_nickname = (TextView) view.findViewById(R.id.tv_edit_nickname);
        tv_edit_signature = (TextView) view.findViewById(R.id.tv_edit_signature);
        tv_edit_sex = (TextView) view.findViewById(R.id.tv_edit_sex);
        tv_edit_phonecall = (TextView) view.findViewById(R.id.tv_edit_phonecall);

        iv_edit_avatar = (ImageView) view.findViewById(R.id.iv_edit_avatar);
        iv_edit_bg = (ImageView) view.findViewById(R.id.iv_edit_bg);


        //添加监听
        MyListener myListener = new MyListener();
        edit_PhoneCall.setOnClickListener(myListener);
        edit_Avatar.setOnClickListener(myListener);
        iv_edit_bg.setOnClickListener(myListener);
        iv_bg_change.setOnClickListener(myListener);
        edit_Signature.setOnClickListener(myListener);
        edit_Nickname.setOnClickListener(myListener);
        edit_Sex.setOnClickListener(myListener);

    }

    private void updateWidgets() {
        if (STFUConfig.sUser != null) {
            tv_edit_nickname.setText(STFUConfig.sUser.getNickname());
            tv_edit_phonecall.setText(STFUConfig.sUser.getPhone());
            tv_edit_sex.setText(STFUConfig.sUser.getGender());
            tv_edit_signature.setText(STFUConfig.sUser.getSignature());
            Logger.i(STFUConfig.sUser.toString());
            Picasso.get()
                    .load(STFUConfig.HOST + "/static/avatar/" + STFUConfig.sUser.getAvatar_url())
                    .resize(200, 200)
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(iv_edit_avatar);
            Picasso.get()
                    .load(STFUConfig.HOST + "/static/personal_background/" + STFUConfig.sUser.getBg_url())
                    .resize(400, 300)
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(iv_edit_bg);
        }
    }

    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }


    /**
     * 大图模式切割图片
     * 直接创建一个文件将切割后的图片写入
     *
     * @param uri 被裁剪图片的uri
     */
    private void startBigPhotoZoom(Uri uri) {
        Logger.i("startBigPhotoZoom: " + uri.toString());
        Uri imageUri = null;//本地存放输出的uri
        // 创建大图文件夹
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String storage = Environment.getExternalStorageDirectory().getPath();
            File dirFile = new File(storage + "/SpareTimeForU/bigAvatar");
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    Logger.e("文件夹创建失败");
                } else {
                    Logger.i("文件夹创建成功");
                }
            }
            File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
            imageUri = Uri.fromFile(file);
        }
        // 开始切割
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");//文件输入uri
        intent.putExtra("crop", "true");
        if (imageViewNum == 0) {
            intent.putExtra("aspectX", 1); // 裁剪框比例
            intent.putExtra("aspectY", 1);
        } else {
            intent.putExtra("aspectX", 1920); // 裁剪框比例
            intent.putExtra("aspectY", 1080);
        }
        if (imageViewNum == 0) {
            intent.putExtra("outputX", 600); // 输出图片大小
            intent.putExtra("outputY", 600);
        } else {
            intent.putExtra("outputX", 1920); // 输出图片大小
            intent.putExtra("outputY", 1080);
        }
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false); // 不直接返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 返回一个文件，保存到sd卡中
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        mUri = imageUri;//之后直接从SD卡中读取
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            Logger.i("可以裁剪");
            startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING);
        } else {
            if (imageViewNum == 0) {
                iv_edit_avatar.setImageURI(uri);
            } else {
                iv_edit_bg.setImageURI(uri);
            }

        }

    }

    /**
     * 拍照
     */
    private void imageCapture() {
        Intent intent;
        Uri pictureUri;
        File pictureFile = new File(Environment.getExternalStorageDirectory() + "/SpareTimeForU/bigAvatar",
                System.currentTimeMillis() + ".jpg");
        mFile = pictureFile;
        if (pictureFile.exists()) {
            pictureFile.getAbsoluteFile().delete();
        }

        //把file转变成content provider的uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //两者要对应同一个路径
            pictureUri = FileProvider.getUriForFile(getContext(), "com.sparetimeforu.android.sparetimeforu.fileProvider", pictureFile);
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pictureUri = Uri.fromFile(pictureFile);
        }

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(getContext(), "未找到相机", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 监听器，判断哪个id被点击，执行对应的操作
     */
    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            View rootView;//图片弹出窗口需要的根View
            switch (view.getId()) {
                case R.id.edit_Avatar:
                    imageViewNum = 0;
                    //弹出对话框选择头像
                    mPhotoPopupWindow = new PhotoPopupWindow(getActivity(),
                            v1 -> {
                                // 拍照和文件权限申请
                                if (ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    // 权限还没有授予，进行申请
                                    ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{Manifest.permission.CAMERA,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                                } else {
                                    // 如果权限已经申请过，直接进行图片选择
                                    mPhotoPopupWindow.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    // 判断系统中是否有处理该 Intent 的 Activity
                                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                        startActivityForResult(intent, REQUEST_IMAGE_GET);
                                    } else {
                                        Toast.makeText(getContext(), "未找到图片查看器", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            v12 -> {
                                // 拍照及文件权限申请
                                if (ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    // 权限还没有授予，进行申请
                                    ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{Manifest.permission.CAMERA,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                                } else {
                                    // 权限已经申请，直接拍照
                                    mPhotoPopupWindow.dismiss();
                                    imageCapture();
                                }
                            }
                    );
                    rootView = LayoutInflater.from(getContext())
                            .inflate(R.layout.activity_fragment, null);
                    mPhotoPopupWindow.showAtLocation(rootView,
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;

                case R.id.iv_bg_change:
                    imageViewNum = 1;
                    //弹出对话框选择头像
                    mPhotoPopupWindow = new PhotoPopupWindow(getActivity(),
                            v1 -> {
                                // 拍照和文件权限申请
                                if (ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    // 权限还没有授予，进行申请
                                    ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{Manifest.permission.CAMERA,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                                } else {
                                    // 如果权限已经申请过，直接进行图片选择
                                    mPhotoPopupWindow.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    // 判断系统中是否有处理该 Intent 的 Activity
                                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                        startActivityForResult(intent, REQUEST_IMAGE_GET);
                                    } else {
                                        Toast.makeText(getContext(), "未找到图片查看器", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            v12 -> {
                                // 拍照及文件权限申请
                                if (ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    // 权限还没有授予，进行申请
                                    ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{Manifest.permission.CAMERA,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                                } else {
                                    // 权限已经申请，直接拍照
                                    mPhotoPopupWindow.dismiss();
                                    imageCapture();
                                }
                            }
                    );
                    rootView = LayoutInflater.from(getContext())
                            .inflate(R.layout.activity_fragment, null);
                    mPhotoPopupWindow.showAtLocation(rootView,
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                    break;

                case R.id.edit_Nickname:
                    //弹出对话框修改昵称
                    showAlertDialog("nickname", CHANGE_NICK_NAME);
                    break;
                case R.id.edit_Phonecall:
                    //弹出对话框修改手机号码
                    showAlertDialog("phone", CHANGE_PHONE_CALL);


                    break;
                case R.id.edit_Sex:
                    //弹出对话框修改性别
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    int defaultChoice = 1;
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setTitle("请选择性别");
                    final String[] sex = {"男", "女"};

                    if (STFUConfig.sUser != null && STFUConfig.sUser.getGender().equals("男"))
                        defaultChoice = 0;
                    builder.setSingleChoiceItems(sex, defaultChoice, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "性别为：" + sex[which], Toast.LENGTH_SHORT).show();

                            if (STFUConfig.sUser != null) {
                                STFUConfig.sUser.setGender(sex[which]);
                            }
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateWidgets();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();

                    break;
                case R.id.edit_Signature:
                    //弹出对话框修改昵称
                    showAlertDialog("signature", CHANGE_SIGNATURE);
            }

            //修改信息完成后进行界面的更新
            updateWidgets();
        }
    }
}
