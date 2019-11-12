package com.sparetimeforu.android.sparetimeforu.fragment.post;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.activity.OthersPersonalActivity;
import com.sparetimeforu.android.sparetimeforu.adapter.CommentAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.ErrandAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.PostImageAdapter;
import com.sparetimeforu.android.sparetimeforu.entity.Comment;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.fragment.EditFragment;
import com.sparetimeforu.android.sparetimeforu.util.ErrandDataBaseUtil;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;
import com.sparetimeforu.android.sparetimeforu.util.SystemMessageSendUtil;
import com.sparetimeforu.android.sparetimeforu.util.VerifyUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.sparetimeforu.android.sparetimeforu.adapter.IdleThingAdapter.parseDateString;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/18.
 * Email:17wjli6@stu.edu.cn
 */

public class ErrandPostFragment extends Fragment {
    private static final String TAG = "ErrandPostFragment";
    public static final String PHOTO_URL = "photo_url";
    private static String picture_url = STFUConfig.HOST + "/static";
    private String url;
    private String add_comment_url;
    private CommentAdapter commentAdapter;
    private List<Comment> comments;
    private Errand errand;

    @BindView(R.id.post_avatar)
    ImageView avatar;
    @BindView(R.id.post_nick_name)
    TextView user_nick_name;
    @BindView(R.id.post_reward)
    TextView errand_reward;
    @BindView(R.id.post_date)
    TextView errand_date;
    @BindView(R.id.errand_origin)
    TextView origin;
    @BindView(R.id.errand_deadline)
    TextView deadline;
    @BindView(R.id.errand_destination)
    TextView destination;
    @BindView(R.id.post_caption)
    TextView caption;
    @BindView(R.id.errand_post_send)
    ImageButton errand_post_send;
    @BindView(R.id.errand_post_comment)
    EditText errand_post_comment;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.summary_like)
    TextView mLikeTextView;
    @BindView(R.id.summary_comment)
    TextView mCommentTextView;
    @BindView(R.id.comment_list)
    RecyclerView mCommentList;
    @BindView(R.id.post_image_list)
    RecyclerView mPostImageList;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.post_like)
    ImageView mLikeImageView;
    @BindView(R.id.post_accept)
    Button mAcceptBtn;
    @BindView(R.id.post_finish)
    Button mFinishBtn;
    @BindView(R.id.post_cancel)
    Button mCancelBtn;
    @BindView(R.id.confirm_finish)
    Button mConfirmBtn;
    @BindView(R.id.post_not_finish)
    Button mNotFinishBtn;
    @BindView(R.id.post_delete)
    Button mDeleteBtn;
    @BindView(R.id.post_status_text)
    TextView mStatusTextView;


    int postID;
    Errand mErrand;


    public static ErrandPostFragment newInstance() {
        return new ErrandPostFragment();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_errand, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        init_Post_Data();

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorAccent, R.color.colorPrimaryDark);
        mRefreshLayout.setOnRefreshListener(this::refresh);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postID = getActivity().getIntent().getIntExtra("errand_id", 1);
    }

    private void init_Post_Data() {
        mRefreshLayout.setRefreshing(true);
        url = STFUConfig.HOST + "/mission/get_mission_by_mission_id";
        add_comment_url = STFUConfig.HOST + "/mission/comment/add";
        //从服务器获取帖子信息
        Intent intent = getActivity().getIntent();
        int errand_id = intent.getIntExtra("errand_id", 0);
        if (errand_id != 0) {
            //发送请求到服务器
            FormBody formBody = new FormBody.Builder()
                    .add("errand_id", errand_id + "").build();
            OkHttpUtil.sendOkHttpPostRequest(url, formBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //显示网络状况不佳
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        mRefreshLayout.setRefreshing(false);
                        Snackbar.make(getView(), "网络状况不佳", BaseTransientBottomBar.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //拿到帖子数据,处理 json 字符串
                    String responseString = response.body().string();
                    errand = HandleMessageUtil.handlePost_Errand_Message(responseString);//获取帖子信息
                    mErrand = errand;
                    comments = HandleMessageUtil.handlePost_Errand_Comment_Message(responseString);//获取评论信息
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        mRefreshLayout.setRefreshing(false);
                        initViews(errand, comments);
                    });
                }
            });
        }
    }

    public void initViews(Errand errand, List<Comment> comments) {
        //设置头像
        if (errand == null) return;
        Picasso.get().
                load(picture_url + "/avatar/" + errand.getUser_Avatar()).
                resize(30, 30).centerCrop().into(avatar);

        user_nick_name.setText(errand.getUser_Nickname());
        errand_reward.setText("￥" + errand.getMoney());
        errand_date.setText(parseDateString(errand.getRelease_time()));
        deadline.setText(parseDateString(errand.getEnd_time()));
        destination.setText(errand.getDestination());
        if (errand.getOrigin() == null || errand.getOrigin().equals("")) {
            origin.setText("随意");
            origin.setTextColor(Color.GRAY);
        } else origin.setText(errand.getOrigin());
        caption.setText(errand.getContent());

        //设置图片
        List<String> urls = new ArrayList<String>();
        if (!errand.getPicture_url_1().equals(""))
            urls.add(STFUConfig.HOST + "/static/mission_pictures/" + errand.getPicture_url_1());
        if (!errand.getPicture_url_2().equals(""))
            urls.add(STFUConfig.HOST + "/static/mission_pictures/" + errand.getPicture_url_2());
        if (!errand.getPicture_url_3().equals(""))
            urls.add(STFUConfig.HOST + "/static/mission_pictures/" + errand.getPicture_url_3());
        mPostImageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        PostImageAdapter postImageAdapter = new PostImageAdapter(urls);
        ViewCompat.setNestedScrollingEnabled(mPostImageList, false);
        mPostImageList.setAdapter(postImageAdapter);

        mCommentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentAdapter = new CommentAdapter(comments);
        ViewCompat.setNestedScrollingEnabled(mCommentList, false);
        mCommentList.setAdapter(commentAdapter);

        mCommentTextView.setText(getString(R.string.comment_number,
                errand.getComment_number() + ""));
        mLikeTextView.setText(getString(R.string.like_number,
                errand.getLike_number() + ""));

        //给errand_post_send设置点击事件
        errand_post_send.setOnClickListener(view -> {
            if (!VerifyUtil.isLogin(getActivity())) {
                return;
            }
            if (errand_post_comment.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "评论不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String content = errand_post_comment.getText().toString();
            FormBody formBody = new FormBody.Builder()
                    .add("auth_token", STFUConfig.sUser.getAuth_token())
                    .add("content", content)
                    .add("post_id", errand.getErrand_id() + "").build();

            OkHttpUtil.sendOkHttpPostRequest(add_comment_url, formBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(getView(), "评论失败，请检查网络", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errand_post_comment.setText("");
                            Gson gson = new Gson();
                            try {
                                String responseString = response.body().string();
                                JSONObject responseJson = new JSONObject(responseString);
                                String data = responseJson.getString("data");
                                Comment comment = gson.fromJson(data, Comment.class);
//                                        commentAdapter.addData(comment);
                                refresh();
//                                        Snackbar.make(getView(), "评论成功", BaseTransientBottomBar.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
        });

        if (VerifyUtil.isLoginStatus(getActivity())) {
            FormBody body = new FormBody.Builder()
                    .add("auth_token", STFUConfig.sUser.getAuth_token())
                    .add("post_type", 0 + "")
                    .add("post_id", postID + "")
                    .build();

            OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/like/is_like", body, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {
                            if (jsonObject.getString("is_like").equals("like")) {
                                if (getActivity() == null) return;
                                getActivity().runOnUiThread(
                                        () -> mLikeImageView.setImageResource(R.drawable.ic_like));
                            } else {
                                if (getActivity() == null) return;
                                getActivity().runOnUiThread(
                                        () -> mLikeImageView.setImageResource(R.drawable.ic_like_before));
                            }
                        }
                    } catch (JSONException e) {
                        Logger.e(e.toString());
                    }
                }
            });
        }

        MyOnclickListener listener = new MyOnclickListener();
        mAcceptBtn.setOnClickListener(listener);
        mFinishBtn.setOnClickListener(listener);
        mCancelBtn.setOnClickListener(listener);
        mConfirmBtn.setOnClickListener(listener);
        mNotFinishBtn.setOnClickListener(listener);
        mDeleteBtn.setOnClickListener(listener);

        // 显示按钮状态，先登录，再判断是不是悬赏者
        if (VerifyUtil.isLoginStatus(getActivity())) {
            // 悬赏者，从流程后到前判断
            if (errand.getUser_Email().equals(STFUConfig.sUser.getEmail())) {
                if (errand.getIs_confirmed_finish() == 1) { // 确认完成
                    mAcceptBtn.setVisibility(View.GONE);
                    mFinishBtn.setVisibility(View.GONE);
                    mCancelBtn.setVisibility(View.GONE);
                    mNotFinishBtn.setVisibility(View.GONE);
                    mConfirmBtn.setVisibility(View.GONE);
                    mDeleteBtn.setVisibility(View.GONE);
                    mStatusTextView.setText("任务已完成");
                    mStatusTextView.setVisibility(View.VISIBLE);
                } else if (errand.getIs_finished() == 1) { // 未确认
                    mAcceptBtn.setVisibility(View.GONE);
                    mFinishBtn.setVisibility(View.GONE);
                    mCancelBtn.setVisibility(View.GONE);
                    mNotFinishBtn.setVisibility(View.VISIBLE);
                    mConfirmBtn.setVisibility(View.VISIBLE);
                    mDeleteBtn.setVisibility(View.GONE);
                    mStatusTextView.setVisibility(View.GONE);
                } else if (errand.getIs_received() == 1) { // 未完成，等待
                    mAcceptBtn.setVisibility(View.GONE);
                    mFinishBtn.setVisibility(View.GONE);
                    mCancelBtn.setVisibility(View.GONE);
                    mNotFinishBtn.setVisibility(View.GONE);
                    mConfirmBtn.setVisibility(View.GONE);
                    mDeleteBtn.setVisibility(View.GONE);
                    mStatusTextView.setText("正在进行…");
                    mStatusTextView.setVisibility(View.VISIBLE);
                } else if (errand.getIs_received() == 0) { //未接受
                    mAcceptBtn.setVisibility(View.GONE);
                    mFinishBtn.setVisibility(View.GONE);
                    mCancelBtn.setVisibility(View.GONE);
                    mNotFinishBtn.setVisibility(View.GONE);
                    mConfirmBtn.setVisibility(View.GONE);
                    mDeleteBtn.setVisibility(View.VISIBLE);
                    mStatusTextView.setVisibility(View.GONE);
                }

                //接收者
            } else {
                if (errand.getIs_confirmed_finish() == 1) { // 悬赏者已确认
                    mAcceptBtn.setVisibility(View.GONE);
                    mFinishBtn.setVisibility(View.GONE);
                    mCancelBtn.setVisibility(View.GONE);
                    mNotFinishBtn.setVisibility(View.GONE);
                    mConfirmBtn.setVisibility(View.GONE);
                    mDeleteBtn.setVisibility(View.GONE);
                    mStatusTextView.setText("任务已完成");
                    mStatusTextView.setVisibility(View.VISIBLE);
                } else if (errand.getIs_finished() == 1) { // 未确认，等待
                    mAcceptBtn.setVisibility(View.GONE);
                    mFinishBtn.setVisibility(View.GONE);
                    mCancelBtn.setVisibility(View.GONE);
                    mNotFinishBtn.setVisibility(View.GONE);
                    mConfirmBtn.setVisibility(View.GONE);
                    mDeleteBtn.setVisibility(View.GONE);
                    mStatusTextView.setText("待确认……");
                    mStatusTextView.setVisibility(View.VISIBLE);
                } else if (errand.getIs_received() == 1) { // 未完成
                    mAcceptBtn.setVisibility(View.GONE);
                    mFinishBtn.setVisibility(View.VISIBLE);
                    mCancelBtn.setVisibility(View.VISIBLE);
                    mNotFinishBtn.setVisibility(View.GONE);
                    mConfirmBtn.setVisibility(View.GONE);
                    mDeleteBtn.setVisibility(View.GONE);
                    mStatusTextView.setVisibility(View.GONE);

                } else if (errand.getIs_received() == 0) { // 未接受
                    mAcceptBtn.setVisibility(View.VISIBLE);
                    mFinishBtn.setVisibility(View.GONE);
                    mCancelBtn.setVisibility(View.GONE);
                    mNotFinishBtn.setVisibility(View.GONE);
                    mConfirmBtn.setVisibility(View.GONE);
                    mDeleteBtn.setVisibility(View.GONE);
                    mStatusTextView.setVisibility(View.GONE);
                }
            }

        } else { // 未登录，都GONE掉吧
            mAcceptBtn.setVisibility(View.GONE);
            mFinishBtn.setVisibility(View.GONE);
            mCancelBtn.setVisibility(View.GONE);
            mNotFinishBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.GONE);
            mDeleteBtn.setVisibility(View.GONE);
            mStatusTextView.setText("登录后接受");
            mStatusTextView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.menu_back_icon)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.post_like)
    public void like() {
        if (!VerifyUtil.isLogin(getActivity())) {
            return;
        }
        if (postID == 0 || STFUConfig.sUser == null) {
            return;
        }
        FormBody body = new FormBody.Builder()
                .add("post_type", 0 + "")
                .add("post_id", postID + "")

                .add("auth_token", STFUConfig.sUser.getAuth_token())
                .build();
        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/like", body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getString("status").equals("success")) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> refresh());
                    }
                } catch (JSONException e) {
                    Logger.e(e.toString());
                }
            }
        });
    }


    private void refresh() {
        commentAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        mRefreshLayout.setRefreshing(true);
        new RefreshPost(new RefreshCallBack<Errand>() {
            @Override
            public void success(Errand errand, List<Comment> comments) {
                //do something
                Snackbar.make(getView(), R.string.refresh_success, BaseTransientBottomBar.LENGTH_SHORT).show();
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    initViews(errand, comments);
                    commentAdapter.setEnableLoadMore(true);
                    mRefreshLayout.setRefreshing(false);
                });
            }

            @Override
            public void fail(Exception e) {
                Snackbar.make(getView(), R.string.network_error, BaseTransientBottomBar.LENGTH_SHORT).show();
                commentAdapter.setEnableLoadMore(true);
                mRefreshLayout.setRefreshing(false);
            }
        }, getActivity(), postID, RefreshPost.ERRAND_TYPE).start();
    }

    private class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            FormBody formBody;
            switch (view.getId()) {
                case R.id.layout_avatar:
                    //进入他人界面
                    Intent intent = new Intent(getActivity(), OthersPersonalActivity.class);
                    intent.putExtra("user_Email", mErrand.getUser_Email());
                    getActivity().startActivity(intent);
                    break;

                case R.id.post_accept:
                    //注册管理员账号
                    JMessageClient.register(STFUConfig.manager_username, STFUConfig.manager_username + "1", new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            Logger.i("管理员注册成功");
                        }
                    });
                    //发送请求到服务器  用户接收任务
                    formBody = new FormBody.Builder()
                            .add("auth_token", STFUConfig.sUser.getAuth_token())
                            .add("post_id", mErrand.getErrand_id() + "")
                            .build();
                    OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/receive_mission", formBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //发送系统通知

                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String status = jsonObject.getString("status");
                                if (status.equals("success")) {
                                    SystemMessageSendUtil.send_System_message_errand(mErrand.getUser_Email(),
                                            mErrand.getErrand_id(),
                                            0, SystemMessageSendUtil.Errand_message_mode_errand_accepted);
                                    (getActivity()).runOnUiThread(() -> {
                                        mAcceptBtn.setVisibility(View.GONE);
                                        mFinishBtn.setVisibility(View.VISIBLE);
                                        mCancelBtn.setVisibility(View.VISIBLE);
                                        mNotFinishBtn.setVisibility(View.GONE);
                                        mConfirmBtn.setVisibility(View.GONE);
                                        mDeleteBtn.setVisibility(View.GONE);
                                        mStatusTextView.setVisibility(View.GONE);
                                    });
                                } else {
                                    (getActivity()).runOnUiThread(() -> {
                                        Toast.makeText(getActivity(), "接受失败！", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });

                    break;
                case R.id.post_finish:
                    //发送请求到服务器  用户完成任务
                    formBody = new FormBody.Builder()
                            .add("auth_token", STFUConfig.sUser.getAuth_token())
                            .add("post_id", mErrand.getErrand_id() + "")
                            .build();
                    OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/finish_mission", formBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //发送系统通知
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String status = jsonObject.getString("status");
                                if (status.equals("success")) {
                                    (getActivity()).runOnUiThread(() -> {

                                        Errand item = mErrand;
                                        SystemMessageSendUtil.send_System_message_errand(
                                                item.getUser_Email(), item.getErrand_id(), 0,
                                                SystemMessageSendUtil.Errand_message_mode_errand_solved);

                                        mAcceptBtn.setVisibility(View.GONE);
                                        mFinishBtn.setVisibility(View.GONE);
                                        mCancelBtn.setVisibility(View.GONE);
                                        mNotFinishBtn.setVisibility(View.GONE);
                                        mConfirmBtn.setVisibility(View.GONE);
                                        mDeleteBtn.setVisibility(View.GONE);
                                        mStatusTextView.setText("待确认……");
                                        mStatusTextView.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity(), "任务已经完成，等悬赏者确认！", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });

                    break;
                case R.id.post_cancel:
                    //发送请求到服务器  用户取消任务
                    formBody = new FormBody.Builder()
                            .add("auth_token", STFUConfig.sUser.getAuth_token())
                            .add("post_id", mErrand.getErrand_id() + "")
                            .build();
                    OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/rollback_mission", formBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //发送系统通知
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String status = jsonObject.getString("status");
                                if (status.equals("success")) {
                                    (getActivity()).runOnUiThread(() -> {
                                        Toast.makeText(getActivity(), "任务已经取消！", Toast.LENGTH_SHORT).show();
                                        (getActivity()).runOnUiThread(() -> {
                                            mAcceptBtn.setVisibility(View.VISIBLE);
                                            mFinishBtn.setVisibility(View.GONE);
                                            mCancelBtn.setVisibility(View.GONE);
                                            mNotFinishBtn.setVisibility(View.GONE);
                                            mConfirmBtn.setVisibility(View.GONE);
                                            mDeleteBtn.setVisibility(View.GONE);
                                            mStatusTextView.setVisibility(View.GONE);
                                        });
                                    });
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });

                    break;
                case R.id.confirm_finish:
                    //发送请求到服务器  悬赏者确认完成任务
                    formBody = new FormBody.Builder()
                            .add("auth_token", STFUConfig.sUser.getAuth_token())
                            .add("post_id", mErrand.getErrand_id() + "")
                            .build();
                    OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/confirm_finish", formBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //发送系统通知
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String status = jsonObject.getString("status");
                                if (status.equals("success")) {
                                    (getActivity()).runOnUiThread(() -> {
                                        Toast.makeText(getActivity(), "悬赏者已确认，钱已经到对面账户了！", Toast.LENGTH_SHORT).show();

                                        mAcceptBtn.setVisibility(View.GONE);
                                        mFinishBtn.setVisibility(View.GONE);
                                        mCancelBtn.setVisibility(View.GONE);
                                        mNotFinishBtn.setVisibility(View.GONE);
                                        mConfirmBtn.setVisibility(View.GONE);
                                        mDeleteBtn.setVisibility(View.GONE);
                                        mStatusTextView.setText("双方确认完成");
                                        mStatusTextView.setVisibility(View.VISIBLE);
                                    });
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });
                    break;
                case R.id.post_not_finish:
                    //发送请求到服务器  用户未真正完成任务
                    Toast.makeText(getActivity(), "我们会对对方进行审批！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.post_delete:
                    ErrandDataBaseUtil.deleteErrandNow(mErrand.getErrand_id());

                    formBody = new FormBody.Builder()
                            .add("post_id", mErrand.getErrand_id() + "")
                            .add("auth_token", STFUConfig.sUser.getAuth_token())
                            .build();

                    OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/delete_or_not", formBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String status = jsonObject.getString("status");
                                if (status.equals("success")) {
                                    (getActivity()).runOnUiThread(() -> {
                                        EditFragment.updateNativeUser(getActivity());
                                        getActivity().finish();
                                    });
                                } else { // 非 success
                                    (getActivity()).runOnUiThread(() -> {
                                        try {
                                            String error = jsonObject.getString("error");
                                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e1) {
                                            Logger.e(e1.toString());
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });
                    break;

            }
        }
    }
}
