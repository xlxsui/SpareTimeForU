package com.sparetimeforu.android.sparetimeforu.fragment.post;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.adapter.SendPostImageAdapter;
import com.sparetimeforu.android.sparetimeforu.entity.PhotoPopupWindow;
import com.sparetimeforu.android.sparetimeforu.fragment.DatePickerFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.EditFragment;
import com.sparetimeforu.android.sparetimeforu.util.VerifyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sparetimeforu.android.sparetimeforu.fragment.EditFragment.compressImage;
import static com.sparetimeforu.android.sparetimeforu.fragment.EditFragment.copyFile;
import static com.sparetimeforu.android.sparetimeforu.fragment.EditFragment.getFilePathFromContentUri;


/**
 * SpareTimeForU
 * Created by Jin on 2019/10/5.
 * Email:17wjli6@stu.edu.cn
 */
public class SendPostFragment extends Fragment {

    private static final int REQUEST_DATE = 0;

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_IMAGE_GET = 5;
    private static final int REQUEST_IMAGE_CAPTURE = 6;
    private static final int REQUEST_BIG_IMAGE_CUTTING = 7;

    @BindView(R.id.content)
    EditText mContentTextView;
    @BindView(R.id.image_list)
    RecyclerView mImageList;
    @BindView(R.id.origin)
    EditText mOriginEditText;
    @BindView(R.id.destination)
    EditText mDestinationEditText;
    @BindView(R.id.btn_time)
    Button mTimeButton;
    @BindView(R.id.money)
    EditText mMoneyEditText;
    @BindView(R.id.add_image)
    ImageButton mAddImageButton;
    @BindView(R.id.add_more_image)
    ImageButton mAddMoreImageButton;
    @BindView(R.id.remove_all_images)
    ImageButton mRemoveImageButton;
    @BindView(R.id.linearLayout3)
    LinearLayout mPlaceLayout;
    @BindView(R.id.linearLayout5)
    LinearLayout mMoneyLayout;

    private SendPostImageAdapter mAdapter;
    private PhotoPopupWindow mPhotoPopupWindow;

    private File picture1, picture2, picture3,
            originFile, cropFile, compressFile;//原始的文件，裁剪，压缩
    private Uri compressUri, uri1, uri2, uri3;
    private int imageNum = 0, postType;
    private String timeString = "";


    public static SendPostFragment newInstance() {
        return new SendPostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send_post, container, false);
        ButterKnife.bind(this, view);

        postType = getActivity().getIntent().getIntExtra("current_item", R.id.navigation_errand);

        switch (postType) {
            case R.id.navigation_errand:
                break;
            case R.id.navigation_second_hand:
                mTimeButton.setVisibility(Button.GONE);
                mPlaceLayout.setVisibility(LinearLayout.GONE);
                break;
            case R.id.navigation_study:
            case R.id.navigation_search:
                mTimeButton.setVisibility(Button.GONE);
                mPlaceLayout.setVisibility(LinearLayout.GONE);
                mMoneyLayout.setVisibility(LinearLayout.GONE);
                break;
        }
        mImageList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        setupAdapter();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_DATE:
                    Date date = (Date) data
                            .getSerializableExtra(DatePickerFragment.EXTRA_DATE);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy kk:mm", Locale.ENGLISH);
                    //EEEE 星期几 MMMM 月 dd日 yyyy年 kk24小时 mm分钟
                    timeString = (String) dateFormat.format(date);
                    mTimeButton.setText(timeString);
                    break;

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
                    compressUri = Uri.fromFile(compressFile);
                    // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
                    Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intentBc.setData(compressUri);
                    getActivity().sendBroadcast(intentBc);
                    if (originFile != null || cropFile != null) {
                        originFile.delete();
                        cropFile.delete();
                    }

                    //开始处理RecyclerView图片处理
                    dealWithImage();

                default:
                    break;
            }

        }
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
                        Toast.makeText(getContext(), "未找到相册", Toast.LENGTH_SHORT).show();
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

    public void setupAdapter() {
        List<String> strings = new ArrayList<>();
        mAdapter = new SendPostImageAdapter(strings);
        mImageList.setAdapter(mAdapter);
    }

    @OnClick(R.id.menu_back_icon)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.btn_confirm)
    public void sendPost() {
        if (!VerifyUtil.isLogin(getActivity())) {
            return;
        }
        RequestBody body = buildRequestBody();
        String uri = "";
        switch (postType) {
            case R.id.navigation_errand:
                uri = STFUConfig.HOST + "/mission/insert";
                break;
            case R.id.navigation_second_hand:
                uri = STFUConfig.HOST + "/idle_thing/insert";
                break;
            case R.id.navigation_study:
                uri = STFUConfig.HOST + "/study/insert";
                break;
            case R.id.navigation_search:
                uri = STFUConfig.HOST + "/search_thing/insert";
                break;
        }
        if (body != null) {
            OkHttpUtil.sendOkHttpPostRequest(uri, body, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "发布失败，请检查网络", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String status = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        status = jsonObject.getString("status");
                    } catch (Exception e) {
                        Logger.e(e.toString());
                    }
                    if (status.equals("success")) {
                        if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "发布成功", Toast.LENGTH_SHORT).show();
                            EditFragment.updateNativeUser(getActivity());
                            getActivity().finish();
                        });
                    } else {
                        if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "发布失败", Toast.LENGTH_SHORT).show();
                        });
                    }

                }
            });
        }

    }

    @OnClick(R.id.btn_time)
    public void setDeadline() {
        FragmentManager manager = getFragmentManager();
        Date date = new Date();
        DatePickerFragment dialog = DatePickerFragment
                .newInstance(date);
        dialog.setTargetFragment(SendPostFragment.this, REQUEST_DATE);
        dialog.show(manager, DIALOG_DATE);
    }

    @OnClick(R.id.add_image)
    public void addImage() {
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
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                    } else {
                        // 如果权限已经申请过，直接进行图片选择
                        mPhotoPopupWindow.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        // 判断系统中是否有处理该 Intent 的 Activity
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_IMAGE_GET);
                        } else {
                            Toast.makeText(getContext(), "未找到相册", Toast.LENGTH_SHORT).show();
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
        View rootView = LayoutInflater.from(getContext())
                .inflate(R.layout.activity_fragment, null);
        mPhotoPopupWindow.showAtLocation(rootView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    @OnClick(R.id.add_more_image)
    public void addMoreImage() {
        if (imageNum >= 3) {
            Toast.makeText(getActivity(), R.string.reached_limit, Toast.LENGTH_SHORT).show();
        } else {
            addImage();
        }
    }

    @OnClick(R.id.remove_all_images)
    public void removeImages() {
        for (int i = 0; i < imageNum; i++) {
            mAdapter.remove(0);
            mAdapter.notifyItemRemoved(0);
        }
        imageNum = 0;
        compressFile = picture3 = picture2 = picture1 = null;
        compressUri = uri3 = uri2 = uri1 = null;

        mAddImageButton.setVisibility(ImageView.VISIBLE);
        mAddMoreImageButton.setVisibility(ImageView.GONE);
        mRemoveImageButton.setVisibility(ImageView.GONE);

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
            pictureFile.delete();
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

//        intent.putExtra("aspectX", 1920); // 裁剪框比例
//        intent.putExtra("aspectY", 1080);
//        intent.putExtra("outputX", 1920); // 输出图片大小
//        intent.putExtra("outputY", 1080);

        intent.putExtra("scale", true);
        intent.putExtra("return-data", false); // 不直接返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 返回一个文件，保存到sd卡中
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING);
        } else {
            cropFile = new File(getFilePathFromContentUri(uri, getActivity().getContentResolver()));
        }
    }

    /**
     * 处理RecyclerView图片处理 底部ImageButton变化
     */
    private void dealWithImage() {
        if (compressFile == null && compressUri == null || imageNum >= 3) {
            return;
        }

        switch (imageNum) {
            case 0:
                picture1 = compressFile;
                uri1 = compressUri;
                break;
            case 1:
                picture2 = compressFile;
                uri2 = compressUri;
                break;
            case 2:
                picture3 = compressFile;
                uri3 = compressUri;
                break;
        }
        mAdapter.addData(imageNum, compressUri.toString());
        mAdapter.notifyItemChanged(imageNum);
        imageNum++;

        if (imageNum != 0) {
            mAddImageButton.setVisibility(ImageView.GONE);
            mAddMoreImageButton.setVisibility(ImageView.VISIBLE);
            mRemoveImageButton.setVisibility(ImageView.VISIBLE);
        }
    }

    /**
     * 根据不同的情况生成请求body，迷
     */
    private RequestBody buildRequestBody() {
        RequestBody body = null;
        if (isTextOk()) {
            switch (postType) {
                case R.id.navigation_errand:
                    switch (imageNum) {
                        case 0:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .addFormDataPart("origin", mOriginEditText.getText().toString())
                                    .addFormDataPart("destination", mDestinationEditText.getText().toString())
                                    .addFormDataPart("end_time", timeString)
                                    .addFormDataPart("money", mMoneyEditText.getText().toString())
                                    .build();
                            break;
                        case 1:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .addFormDataPart("origin", mOriginEditText.getText().toString())
                                    .addFormDataPart("destination", mDestinationEditText.getText().toString())
                                    .addFormDataPart("end_time", timeString)
                                    .addFormDataPart("money", mMoneyEditText.getText().toString())
                                    .addFormDataPart("picture_1", picture1.getName(),
                                            FormBody.create(MediaType.parse("*"), picture1))
                                    .build();
                            break;
                        case 2:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .addFormDataPart("origin", mOriginEditText.getText().toString())
                                    .addFormDataPart("destination", mDestinationEditText.getText().toString())
                                    .addFormDataPart("end_time", timeString)
                                    .addFormDataPart("money", mMoneyEditText.getText().toString())
                                    .addFormDataPart("picture_1", picture1.getName(),
                                            FormBody.create(MediaType.parse("*"), picture1))
                                    .addFormDataPart("picture_2", picture2.getName(),
                                            FormBody.create(MediaType.parse("*"), picture2))
                                    .build();
                            break;
                        case 3:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .addFormDataPart("origin", mOriginEditText.getText().toString())
                                    .addFormDataPart("destination", mDestinationEditText.getText().toString())
                                    .addFormDataPart("end_time", timeString)
                                    .addFormDataPart("money", mMoneyEditText.getText().toString())
                                    .addFormDataPart("picture_1", picture1.getName(),
                                            FormBody.create(MediaType.parse("*"), picture1))
                                    .addFormDataPart("picture_2", picture2.getName(),
                                            FormBody.create(MediaType.parse("*"), picture2))
                                    .addFormDataPart("picture_3", picture3.getName(),
                                            FormBody.create(MediaType.parse("*"), picture3))
                                    .build();
                            break;
                    }
                    break;
                case R.id.navigation_second_hand:
                    switch (imageNum) {
                        case 0:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .addFormDataPart("money", mMoneyEditText.getText().toString())
                                    .build();
                            break;
                        case 1:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .addFormDataPart("money", mMoneyEditText.getText().toString())
                                    .addFormDataPart("picture_1", picture1.getName(),
                                            FormBody.create(MediaType.parse("*"), picture1))
                                    .build();
                            break;
                        case 2:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .addFormDataPart("money", mMoneyEditText.getText().toString())
                                    .addFormDataPart("picture_1", picture1.getName(),
                                            FormBody.create(MediaType.parse("*"), picture1))
                                    .addFormDataPart("picture_2", picture2.getName(),
                                            FormBody.create(MediaType.parse("*"), picture2))
                                    .build();
                            break;
                        case 3:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .addFormDataPart("money", mMoneyEditText.getText().toString())
                                    .addFormDataPart("picture_1", picture1.getName(),
                                            FormBody.create(MediaType.parse("*"), picture1))
                                    .addFormDataPart("picture_2", picture2.getName(),
                                            FormBody.create(MediaType.parse("*"), picture2))
                                    .addFormDataPart("picture_3", picture3.getName(),
                                            FormBody.create(MediaType.parse("*"), picture3))
                                    .build();
                            break;
                    }
                    break;
                case R.id.navigation_study:
                case R.id.navigation_search:
                    switch (imageNum) {
                        case 0:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .build();
                            break;
                        case 1:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .addFormDataPart("picture_1", picture1.getName(),
                                            FormBody.create(MediaType.parse("*"), picture1))
                                    .build();
                            break;
                        case 2:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .addFormDataPart("picture_1", picture1.getName(),
                                            FormBody.create(MediaType.parse("*"), picture1))
                                    .addFormDataPart("picture_2", picture2.getName(),
                                            FormBody.create(MediaType.parse("*"), picture2))
                                    .build();
                            break;
                        case 3:
                            body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("auth_token", STFUConfig.sUser.getAuth_token())
                                    .addFormDataPart("content", mContentTextView.getText().toString())
                                    .addFormDataPart("picture_1", picture1.getName(),
                                            FormBody.create(MediaType.parse("*"), picture1))
                                    .addFormDataPart("picture_2", picture2.getName(),
                                            FormBody.create(MediaType.parse("*"), picture2))
                                    .addFormDataPart("picture_3", picture3.getName(),
                                            FormBody.create(MediaType.parse("*"), picture3))
                                    .build();
                            break;
                    }
                    break;
            }
        } else {
            Toast.makeText(getActivity(), R.string.right_content, Toast.LENGTH_SHORT).show();
        }

        return body;
    }

    private boolean isTextOk() {
        boolean isOK = false;

        switch (postType) {
            case R.id.navigation_errand:
                isOK = !(mContentTextView.getText().toString().equals("")
                        || mOriginEditText.getText().toString().equals("")
                        || mDestinationEditText.getText().toString().equals("")
                        || mMoneyEditText.getText().toString().equals(""));
                break;
            case R.id.navigation_second_hand:
                isOK = !(mContentTextView.getText().toString().equals("")
                        || mMoneyEditText.getText().toString().equals(""));
                break;
            case R.id.navigation_study:
            case R.id.navigation_search:
                isOK = !mContentTextView.getText().toString().equals("");
                break;
        }
        return isOK;
    }
}
