package com.sparetimeforu.android.sparetimeforu.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.entity.Comment;
import com.sparetimeforu.android.sparetimeforu.util.VerifyUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.sparetimeforu.android.sparetimeforu.adapter.IdleThingAdapter.parseDateString;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/21.
 * Email:17wjli6@stu.edu.cn
 */

public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    private String url;

    public CommentAdapter(@Nullable List<Comment> data) {
        super(R.layout.post_comment, data);
        url = STFUConfig.HOST + "/static";


        CommentAdapter.this.setOnItemChildClickListener((adapter, v, position) -> {
            switch (v.getId()) {
                case R.id.comment_avatar:
                    Toast.makeText(mContext, "Avatar!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        });

        if (VerifyUtil.isLoginStatus(mContext)) {
            this.setOnItemLongClickListener((adapter, view, position) -> {
                Comment comment = getItem(position);
                assert comment != null;
                if (comment.getUser_id() == STFUConfig.sUser.getUser_id()) {
                    Snackbar.make(view, "确定删除吗？", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("删除", v -> {
                        FormBody body = new FormBody.Builder()
                                .add("auth_token", STFUConfig.sUser.getAuth_token())
                                .add("id", comment.getId() + "")
                                .build();

                        String url;
                        if (comment.getPost_type().equals("mission")) {
                            url = STFUConfig.HOST + "/mission/comment/delete";
                        } else if (comment.getPost_type().equals("idle_thing")) {
                            url = STFUConfig.HOST + "/idle_thing/comment/delete";
                        } else if (comment.getPost_type().equals("study")) {
                            url = STFUConfig.HOST + "/study/comment/delete";
                        } else {
                            url = STFUConfig.HOST + "/search_thing/comment/delete";
                        }

                        OkHttpUtil.sendOkHttpPostRequest(url, body, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    String status = jsonObject.getString("status");
                                    if (status.equals("success")) {
                                        ((Activity) mContext).runOnUiThread(() -> {
                                            Toast.makeText(mContext, "已删除！", Toast.LENGTH_SHORT).show();
                                        });
                                    } else {
                                        ((Activity) mContext).runOnUiThread(() -> {
                                            Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
                                        });
                                    }

                                } catch (JSONException e) {
                                    Logger.e(e.toString());
                                }
                            }
                        });
                    }).show();
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        TextView user_nickname = helper.getView(R.id.comment_nick_name);
        TextView date = helper.getView(R.id.comment_date);
        ImageView user_avatar = helper.getView(R.id.comment_avatar);
        TextView comment_floor = helper.getView(R.id.comment_floor);
        TextView caption = helper.getView(R.id.comment_caption);

        user_nickname.setText(item.getUser_nickname());
        date.setText(parseDateString(item.getTime()));
        comment_floor.setText("#" + (helper.getAdapterPosition() + 1));
        caption.setText(item.getContent());

        helper.addOnClickListener(R.id.comment_avatar);

        Picasso.get().load(url + "/avatar/" + item.getUser_avatar()).resize(100, 100).centerCrop().into(user_avatar);
    }
}
