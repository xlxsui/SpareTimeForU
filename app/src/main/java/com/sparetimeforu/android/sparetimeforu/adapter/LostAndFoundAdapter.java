package com.sparetimeforu.android.sparetimeforu.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.activity.OthersPersonalActivity;
import com.sparetimeforu.android.sparetimeforu.activity.post.LostAndFoundPostActivity;
import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;
import com.sparetimeforu.android.sparetimeforu.util.VerifyUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.sparetimeforu.android.sparetimeforu.adapter.IdleThingAdapter.parseDateString;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/17.
 */

public class LostAndFoundAdapter extends BaseQuickAdapter<SearchThing, BaseViewHolder> {
    private String url;

    public LostAndFoundAdapter(List<SearchThing> searchThings) {
        super(R.layout.item_search_thing, searchThings);
        url = STFUConfig.HOST + "/static/search_pictures";
        this.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, LostAndFoundPostActivity.class);
            //把id传进去
            SearchThing item = (SearchThing) adapter.getItem(position);
            intent.putExtra("search_thing_id", item.getSearch_thing_id());
            mContext.startActivity(intent);
        });
        this.setOnItemChildClickListener(((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.like_layout:
                    if (!VerifyUtil.isLogin(mContext)) {
                        break;
                    }
                    SearchThing s = (SearchThing) adapter.getItem(position);
                    if (s == null || STFUConfig.sUser == null) {
                        break;
                    }
                    FormBody body = new FormBody.Builder()
                            .add("post_type", 3 + "")//3代表寻物帖子
                            .add("post_id", s.getSearch_thing_id() + "")
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
                                    refreshItem(s.getSearch_thing_id(), position);
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });
                    break;
                case R.id.layout_avatar:
                    Intent intent = new Intent(mContext, OthersPersonalActivity.class);
                    intent.putExtra("user_Email", getItem(position).getUser_Email());
                    mContext.startActivity(intent);
                    break;
            }
        }));

    }

    @Override
    protected void convert(BaseViewHolder helper, SearchThing item) {
        if (item == null) {
            return;
        }
        ImageView avatar = helper.getView(R.id.search_thing_avatar);

        TextView nick_name = helper.getView(R.id.search_thing_nick_name);
        nick_name.setText(item.getUser_Nickname());

        TextView caption = helper.getView(R.id.search_thing_caption);
        caption.setText(item.getContent());

        ImageView picture1 = helper.getView(R.id.search_thing_picture1);

        TextView date = helper.getView(R.id.search_thing_date);
        date.setText(parseDateString(item.getRelease_time()));

        TextView is_found = helper.getView(R.id.search_thing_status);

        if (item.getIs_found() == 1) is_found.setText("✔️ 已找到");
        else is_found.setText("\uD83D\uDD14 未找到");

        TextView likeNum = helper.getView(R.id.search_thing_like);
        likeNum.setText(item.getLike_number() + "");

        TextView commentNum = helper.getView(R.id.search_thing_comment);
        commentNum.setText(item.getComment_number() + "");

        // 点赞View
        ImageView likeImageView = helper.getView(R.id.ic_like);

        Picasso.get()
                .load(STFUConfig.HOST + "/static/avatar/" + item.getUser_Avatar())
                .resize(200, 200)
                .centerCrop()
                .into(avatar);
        if (!item.getPicture_url_1().equals("")) {
            Picasso.get()
                    .load(url + "/" + item.getPicture_url_1())
                    .into(picture1, new Callback() {
                        @Override
                        public void onSuccess() {
                            picture1.setVisibility(ImageView.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });
        } else {
            picture1.setVisibility(View.GONE);
        }

        helper.addOnClickListener(R.id.like_layout)
                .addOnClickListener(R.id.layout_avatar);

        if (VerifyUtil.isLoginStatus(mContext)) {
            FormBody body = new FormBody.Builder()
                    .add("auth_token", STFUConfig.sUser.getAuth_token())
                    .add("post_type", 3 + "")
                    .add("post_id", item.getSearch_thing_id() + "")
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
                                ((Activity) mContext).runOnUiThread(
                                        () -> likeImageView.setImageResource(R.drawable.ic_like));
                            } else {
                                ((Activity) mContext).runOnUiThread(
                                        () -> likeImageView.setImageResource(R.drawable.ic_like_before));
                            }
                        }
                    } catch (JSONException e) {
                        Logger.e(e.toString());
                    }
                }
            });
        }
    }

    /**
     * 刷新点赞的项
     *
     * @param itemId   帖子的id
     * @param position 列表的位置
     */
    private void refreshItem(int itemId, int position) {
        FormBody body = new FormBody.Builder()
                .add("post_id", itemId + "")
                .build();

        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/search_thing/get_post_by_id", body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SearchThing s = HandleMessageUtil.handlePost_Search_Thing_Message(response.body().string());
                ((Activity) mContext).runOnUiThread(() -> {
                    LostAndFoundAdapter.this.setData(position, s);
                });

            }
        });
    }

}
