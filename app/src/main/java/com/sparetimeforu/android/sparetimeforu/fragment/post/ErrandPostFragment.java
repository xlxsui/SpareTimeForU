package com.sparetimeforu.android.sparetimeforu.fragment.post;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.activity.PictureActivity;
import com.sparetimeforu.android.sparetimeforu.adapter.CommentAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.PostImageAdapter;
import com.sparetimeforu.android.sparetimeforu.entity.Comment;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/18.
 * Email:17wjli6@stu.edu.cn
 */

public class ErrandPostFragment extends Fragment {
    private static final String TAG = "ErrandPostFragment";
    public static final String PHOTO_URL = "photo_url";
    private static  String picture_url=STFUConfig.HOST+"/static";
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


    @BindView(R.id.comment_list)
    RecyclerView mCommentList;
    @BindView(R.id.post_image_list)
    RecyclerView mPostImageList;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    public static ErrandPostFragment newInstance() {
        return new ErrandPostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_errand, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        init_Post_Data();

        return view;
    }
    private void init_Post_Data(){
        url= STFUConfig.HOST+"/mission/get_mission_by_mission_id";
        add_comment_url=STFUConfig.HOST+"/mission/comment/add";
        //从服务器获取帖子信息
        Intent intent=getActivity().getIntent();
        int errand_id=intent.getIntExtra("errand_id",0);
        if(errand_id!=0) {
            //发送请求到服务器
            FormBody formBody = new FormBody.Builder()
                    .add("errand_id", errand_id + "").build();
            OkHttpUtil.sendOkHttpPostRequest(url, formBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //显示网络状况不佳
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(getView(),"网络状况不佳",Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //拿到帖子数据,处理 json 字符串
                    String responseString=response.body().string();
                    errand= HandleMessageUtil.handlePost_Errand_Message(responseString);//获取帖子信息
                    comments=HandleMessageUtil.handlePost_Errand_Comment_Message(responseString);//获取评论信息
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            init_Views(errand,comments);
                        }
                    });
                }
            });
        }
    }

    public void init_Views(Errand errand,List<Comment> comments){
        //设置头像
        Picasso.get().
                load(picture_url+"/avatar/"+errand.getUser_Avatar()).
                resize(30,30).centerCrop().into(avatar);

        user_nick_name.setText(errand.getUser_Nickname());
        errand_reward.setText("￥"+errand.getMoney());
        errand_date.setText(errand.getRelease_time());
        deadline.setText(errand.getEnd_time());
        destination.setText(errand.getDestination());
        if(errand.getOrgin()==null||errand.getOrgin().equals("")){
            origin.setText("任务没有设置起点");
            origin.setTextColor(Color.GRAY);
        }else origin.setText(errand.getOrgin());
        caption.setText(errand.getContent());

        //设置图片
        List<String> urls = new ArrayList<String>();
        if(!errand.getPicture_url_1().equals("")) urls.add(errand.getPicture_url_1());
        if(!errand.getPicture_url_2().equals("")) urls.add(errand.getPicture_url_2());
        if(!errand.getPicture_url_3().equals("")) urls.add(errand.getPicture_url_3());
        mPostImageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        PostImageAdapter postImageAdapter = new PostImageAdapter(urls);
        ViewCompat.setNestedScrollingEnabled(mPostImageList, false);
        mPostImageList.setAdapter(postImageAdapter);

        mCommentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentAdapter = new CommentAdapter(comments);
        ViewCompat.setNestedScrollingEnabled(mCommentList, false);
        mCommentList.setAdapter(commentAdapter);

        //给errand_post_send设置点击事件
        errand_post_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=errand_post_comment.getText().toString();
                FormBody formBody=new FormBody.Builder()
                        .add("auth_token",STFUConfig.sUser.getAuth_token())
                        .add("content",content)
                        .add("post_id",errand.getErrand_id()+"").build();

                OkHttpUtil.sendOkHttpPostRequest(add_comment_url, formBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(getView(),"评论失败，请检查网络",Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                errand_post_comment.setText("");
                                Gson gson=new Gson();
                                try {
                                    String responseString=response.body().string();
                                    JSONObject responseJson=new JSONObject(responseString);
                                    String data=responseJson.getString("data");
                                    Comment comment=gson.fromJson(data,Comment.class);
                                    commentAdapter.addData(comment);
                                    Snackbar.make(getView(),"评论成功",Snackbar.LENGTH_SHORT).show();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
            }
        });
    }

    @OnClick(R.id.menu_back_icon)
    public void back() {
        Log.i(TAG, "onCreateView: ");
        getActivity().finish();
    }

}
