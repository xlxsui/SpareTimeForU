package com.sparetimeforu.android.sparetimeforu.fragment;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.view.Window;
import android.view.WindowManager;
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
import com.sparetimeforu.android.sparetimeforu.util.StatusBarUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.Luban;

import static android.app.Activity.RESULT_OK;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/21.
 * Email:17wjli6@stu.edu.cn
 */

public class EditFragment extends Fragment {
    private String url;
    private static final int CHANGE_NICK_NAME = 0;
    private static final int CHANGE_SIGNATURE = 1;
    private static final int CHANGE_PHONE_CALL = 2;
    private static final int CHANGE_AVATOR = 3;
    private static final int CHANGE_SEX = 4;
    private static final int REQUEST_IMAGE_GET = 5;
    private static final int REQUEST_IMAGE_CAPTURE = 6;
    private static final int REQUEST_BIG_IMAGE_CUTTING = 7;

    private Toolbar toolbar;
    private ImageView personal_go_back;
    //每一行的相对布局，对应不同信息的修改
    private View view;
    private RelativeLayout edit_PhoneCall, edit_Avatar, edit_Signature, edit_Nickname, edit_Sex, edit_BG;
    //每一行布局中的TextView，用以更新个人信息
    private TextView tv_edit_phonecall, tv_edit_signature, tv_edit_sex, tv_edit_nickname;
    private ImageView iv_edit_avatar, iv_edit_bg;

    private PhotoPopupWindow mPhotoPopupWindow;


    private Uri mUri;//文件在SD卡的路径，非content uri，而是Uri.fromFile()
    private File originFile;//原始的文件
    private File cropFile;//裁剪之后的
    private File compressFile;//压缩后的

    private int isEditBG = 0;//0代表编辑头像，1代表背景


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

        personal_go_back = (ImageView) view.findViewById(R.id.menu_back_icon);
        personal_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        //初始化控件
        initWidgets();
        //初始化数据
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
                    takePhoto();
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
                    Uri uri = data.getData();//拿到相册应用的content uri，接下来转为自己的uri

                    File oldFile = new File(
                            getFilePathFromContentUri(uri, getActivity().getContentResolver()));
                    originFile = new File(Environment.getExternalStorageDirectory() + "/SpareTimeForU/picture",
                            System.currentTimeMillis() + ".jpeg");
                    copyFile(oldFile.getAbsolutePath(), originFile.getAbsolutePath());

                    uri = FileProvider.getUriForFile(getContext(),
                            "com.sparetimeforu.android.sparetimeforu.fileProvider", originFile);
                    cropPhoto(uri);
                    break;


                //拍照完之后干的事，想要裁剪，此时avatarFile对应SD卡中的文件
                case REQUEST_IMAGE_CAPTURE:
                    Uri pictureUri = FileProvider.getUriForFile(getContext(),
                            "com.sparetimeforu.android.sparetimeforu.fileProvider", originFile);
                    cropPhoto(pictureUri);
                    break;


                //裁剪完图片之后的操作,压缩，生成压缩图片的照片uri
                case REQUEST_BIG_IMAGE_CUTTING:
                    compressFile = compressImage(getActivity(), cropFile);
                    mUri = Uri.fromFile(compressFile);
                    // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片？？？不知道干嘛
                    Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intentBc.setData(mUri);
                    getActivity().sendBroadcast(intentBc);

                    if (isEditBG == 0) {
                        sendChangeAvatarRequest();
                        iv_edit_avatar.setImageURI(mUri);
                    } else {
                        sendChangeBGRequest();
                        iv_edit_bg.setImageURI(mUri);
                    }
                    if (originFile != null || compressFile != null) {
                        originFile.delete();
                        cropFile.delete();
                    }
                    break;
            }
        }
    }

    @OnClick(R.id.btn_confirm)
    public void changeAttr() {
        sendChangeAttrRequest();
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
                STFUConfig.sUser = (HandleMessageUtil.handleLoginMessage(response.body().string()));
                AccountManager am = AccountManager.get(getContext());
                Account account = new Account(STFUConfig.sUser.getEmail(), BuildConfig.APPLICATION_ID);
                boolean isAdded = am.addAccountExplicitly(account, "", null);//安全起见，不存密码
                am.setAuthToken(account, "normal", STFUConfig.sUser.getAuth_token());
                am.setUserData(account, "email", STFUConfig.sUser.getEmail());
                am.setUserData(account, "user_id", STFUConfig.sUser.getUser_id() + "");
                am.setUserData(account, "nickname", STFUConfig.sUser.getNickname());
                am.setUserData(account, "signature", STFUConfig.sUser.getSignature());
                am.setUserData(account, "avatar_url", STFUConfig.sUser.getAvatar_url());
                am.setUserData(account, "favourable_rate", STFUConfig.sUser.getFavourable_rate());
                am.setUserData(account, "phone", STFUConfig.sUser.getPhone());
                am.setUserData(account, "gender", STFUConfig.sUser.getGender());
                am.setUserData(account, "bg_url", STFUConfig.sUser.getBg_url());
                //更新极光信息
                UserInfo userInfo=JMessageClient.getMyInfo();
                userInfo.setNickname(STFUConfig.sUser.getNickname());
                JMessageClient.updateMyInfo(UserInfo.Field.all, userInfo, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {

                    }
                });
                //更新STFUConfig.User,更新组件
                getActivity().runOnUiThread(() -> {
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
        edit_BG = (RelativeLayout) view.findViewById(R.id.iv_bg_change);
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
        edit_BG.setOnClickListener(myListener);
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
                    .resize(600, 600)
                    .centerCrop()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(iv_edit_avatar);
            Picasso.get()
                    .load(STFUConfig.HOST + "/static/personal_background/" + STFUConfig.sUser.getBg_url())
                    .resize(960, 593)
                    .centerCrop()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(iv_edit_bg);
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        Intent intent;
        Uri pictureUri;
        File pictureFile = new File(Environment.getExternalStorageDirectory() + "/SpareTimeForU/picture",
                System.currentTimeMillis() + ".jpeg");
        originFile = pictureFile;
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
     * 图片裁剪
     * 直接创建一个文件将裁剪后的图片写入
     *
     * @param uri 被裁剪图片的uri
     */
    private void cropPhoto(Uri uri) {
        Uri imageUri = null;//本地存放输出的uri
        // 创建大图文件夹
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String storage = Environment.getExternalStorageDirectory().getPath();
            File dirFile = new File(storage + "/SpareTimeForU/picture");
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    Logger.e("文件夹创建失败");
                } else {
                    Logger.i("文件夹创建成功");
                }
            }
            File file = new File(dirFile, System.currentTimeMillis() + ".jpeg");
            cropFile = file;
            imageUri = Uri.fromFile(file);
        }
        // 开始切割
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");//文件输入uri
        intent.putExtra("crop", "true");

        // 注意一定要添加该项权限，否则会提示无法裁剪，我透，好像权限不一样会有问题
        //相册，此模式返回的content uri不属于当前应用，权限相册应用持有，无法赋予读和写的权限，否则报错。
        //所以把相册的uri转为自己的content uri就好了
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


        if (isEditBG == 0) {
            intent.putExtra("aspectX", 1); // 裁剪框比例
            intent.putExtra("aspectY", 1);
        } else {
            intent.putExtra("aspectX", 1920); // 裁剪框比例
            intent.putExtra("aspectY", 1186);
        }
        if (isEditBG == 0) {
            intent.putExtra("outputX", 1000); // 输出图片大小
            intent.putExtra("outputY", 1000);
        } else {
            intent.putExtra("outputX", 1920); // 输出图片大小
            intent.putExtra("outputY", 1186);
        }
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false); // 不直接返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 返回一个文件，保存到sd卡中
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING);
        } else {
            if (isEditBG == 0) {
                iv_edit_avatar.setImageURI(uri);
            } else {
                iv_edit_bg.setImageURI(uri);
            }
        }

    }

    /**
     * 压缩图片
     *
     * @param file 要压缩的图片
     * @return
     */
    public static File compressImage(Context context, File file) {
        try {
            File result = Luban.with(context)
                    .load(file)
                    .setTargetDir(file.getParent())
                    .get(file.getAbsolutePath());

            return result;
        } catch (IOException e) {
            Logger.e(e.toString());
        }
        return file;
    }


    private void sendChangeAttrRequest() {
        FormBody body = new FormBody.Builder().
                add("auth_token", STFUConfig.sUser.getAuth_token()).
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

                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            updateNativeUser();
                        });
//                        getActivity().finish();
                    }//onResponse
                });

    }

    private void sendChangeAvatarRequest() {

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("avatar", compressFile.getName(),
                        FormBody.create(MediaType.parse("*"), compressFile))
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
                    JMessageClient.updateUserAvatar(compressFile, new BasicCallback() {
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

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("personal_bg", compressFile.getName(),
                        FormBody.create(MediaType.parse("*"), compressFile))
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

    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 把content uri转为 文件路径
     *
     * @param contentUri      要转换的content uri
     * @param contentResolver 解析器
     * @return
     */
    public static String getFilePathFromContentUri(Uri contentUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(contentUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath$Name String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
     * @param newPath$Name String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
     * @return <code>true</code> if and only if the file was copied;
     * <code>false</code> otherwise
     */
    public static File copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            File newFile = new File(newPath$Name);
            if (!oldFile.exists()) {
                Logger.e("copyFile:  oldFile not exist.");
                return null;
            } else if (!oldFile.isFile()) {
                Logger.e("copyFile:  oldFile not file.");
                return null;
            } else if (!oldFile.canRead()) {
                Logger.e("copyFile:  oldFile cannot read.");
                return null;
            }


            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return newFile;
        } catch (Exception e) {
            Logger.e(e.toString());
            return null;
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
                    isEditBG = 0;
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
                                    takePhoto();
                                }
                            }
                    );
                    rootView = LayoutInflater.from(getContext())
                            .inflate(R.layout.activity_fragment, null);
                    mPhotoPopupWindow.showAtLocation(rootView,
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;

                case R.id.iv_bg_change:
                    isEditBG = 1;
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
                                    takePhoto();
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
                    builder.setSingleChoiceItems(sex, defaultChoice, (dialog, which) -> {
                        Toast.makeText(getActivity(), "性别为：" + sex[which], Toast.LENGTH_SHORT).show();

                        if (STFUConfig.sUser != null) {
                            STFUConfig.sUser.setGender(sex[which]);
                        }
                    });
                    builder.setPositiveButton("确定", (dialog, which) -> updateWidgets());
                    builder.setNegativeButton("取消", (dialog, which) -> updateWidgets());
                    builder.show();

                    break;
                case R.id.edit_Signature:
                    //弹出对话框修改昵称
                    showAlertDialog("signature", CHANGE_SIGNATURE);
            }
        }
    }
}
