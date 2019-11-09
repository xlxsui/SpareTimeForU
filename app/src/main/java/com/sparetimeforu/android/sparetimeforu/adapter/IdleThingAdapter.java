package com.sparetimeforu.android.sparetimeforu.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.activity.post.IdleThingPostActivity;
import com.sparetimeforu.android.sparetimeforu.entity.IdleThing;
import com.sparetimeforu.android.sparetimeforu.fragment.STFUFragment;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;
import com.sparetimeforu.android.sparetimeforu.util.VerifyUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/14.
 */

public class IdleThingAdapter extends BaseQuickAdapter<IdleThing, BaseViewHolder> {
    private String url;

    public IdleThingAdapter(List<IdleThing> idleThings) {

        super(R.layout.item_idle_thing, idleThings);
        url = STFUConfig.HOST + "/static/idle_pictures";
        IdleThingAdapter.this.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, IdleThingPostActivity.class);
            //把id传进去
            IdleThing item = (IdleThing) adapter.getItem(position);
            intent.putExtra("idle_thing_id", item.getIdle_thing_id());
            mContext.startActivity(intent);
        });
        IdleThingAdapter.this.setOnItemChildClickListener(((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.like_layout:// 点赞
                    if (!VerifyUtil.isLogin(mContext)) {
                        break;
                    }
                    IdleThing i = (IdleThing) adapter.getItem(position);
                    if (i == null || STFUConfig.sUser == null) {
                        break;
                    }
                    FormBody body = new FormBody.Builder()
                            .add("post_type", 1 + "")
                            .add("post_id", i.getIdle_thing_id() + "")
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
                                    refreshItem(i.getIdle_thing_id(), position);
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });

                    break;
                case R.id.idle_thing_avatar:
                    break;
                default:
                    break;
            }
        }));
    }

    @Override
    protected void convert(BaseViewHolder helper, IdleThing item) {
        if (item == null) {
            return;
        }
        ImageView avatar = helper.getView(R.id.idle_thing_avatar);
        TextView nick_name = helper.getView(R.id.idle_thing_nick_name);
        nick_name.setText(item.getUser_Nickname());

        TextView caption = helper.getView(R.id.idle_thing_caption);
        caption.setText(item.getContent());

        ImageView picture1 = helper.getView(R.id.idle_thing_picture1);
        TextView price = helper.getView(R.id.idle_thing_price);
        price.setText("\uD83D\uDCB0" + item.getMoney());

        TextView likeNum = helper.getView(R.id.idle_thing_like);
        likeNum.setText(item.getLike_number() + "");

        TextView commentNum = helper.getView(R.id.idle_thing_comment);
        commentNum.setText(item.getComment_number() + "");

        //填充发布时间
        TextView mDateTextView = helper.getView(R.id.idle_thing_date);
        String correctString = parseDateString(item.getRelease_time());// 从字符串得到一个Date
        mDateTextView.setText(correctString);

        // 点赞View
        ImageView likeImageView = helper.getView(R.id.ic_like);

        helper.addOnClickListener(R.id.like_layout)
                .addOnClickListener(R.id.idle_thing_avatar);// 点赞添加点击事件

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

        if (VerifyUtil.isLoginStatus(mContext)) {
            FormBody body = new FormBody.Builder()
                    .add("auth_token", STFUConfig.sUser.getAuth_token())
                    .add("post_type", 1 + "")
                    .add("post_id", item.getIdle_thing_id() + "")
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

        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/idle_thing/get_post_by_id", body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                IdleThing idleThing = HandleMessageUtil.handlePost_IdleThing_Message(response.body().string());
                ((Activity) mContext).runOnUiThread(() -> {
                    IdleThingAdapter.this.setData(position, idleThing);
                });

            }
        });
    }

    public static String parseDateString(String s) {
        if(s!=null){
            Date time = null;
            try {
                time = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss", Locale.ENGLISH)
                        .parse(s);// 从字符串得到一个Date
            } catch (ParseException e) {
                Logger.e(e.toString());
            }

            Calendar today = Calendar.getInstance(); // 今天
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DAY_OF_YEAR, -1); // 昨天
            Calendar bYesterday = Calendar.getInstance();
            bYesterday.add(Calendar.DAY_OF_YEAR, -2); // 前天
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_YEAR, 1); // 明天
            Calendar aTomorrow = Calendar.getInstance();
            aTomorrow.add(Calendar.DAY_OF_YEAR, 1); // 后天

            Calendar postDay = Calendar.getInstance();
            postDay.setTime(time); // your date

            String timeString;

            if (today.get(Calendar.YEAR) == postDay.get(Calendar.YEAR)
                    && today.get(Calendar.DAY_OF_YEAR) == postDay.get(Calendar.DAY_OF_YEAR)) {
                timeString = new SimpleDateFormat("今天 kk:mm", Locale.ENGLISH).format(time);
            } else if (yesterday.get(Calendar.YEAR) == postDay.get(Calendar.YEAR)
                    && yesterday.get(Calendar.DAY_OF_YEAR) == postDay.get(Calendar.DAY_OF_YEAR)) {
                timeString = new SimpleDateFormat("昨天 kk:mm", Locale.ENGLISH).format(time);
            } else if (bYesterday.get(Calendar.YEAR) == postDay.get(Calendar.YEAR)
                    && bYesterday.get(Calendar.DAY_OF_YEAR) == postDay.get(Calendar.DAY_OF_YEAR)) {
                timeString = new SimpleDateFormat("前天 kk:mm", Locale.ENGLISH).format(time);
            } else if (tomorrow.get(Calendar.YEAR) == postDay.get(Calendar.YEAR)
                    && tomorrow.get(Calendar.DAY_OF_YEAR) == postDay.get(Calendar.DAY_OF_YEAR)) {
                timeString = new SimpleDateFormat("明天 kk:mm", Locale.ENGLISH).format(time);
            } else if (aTomorrow.get(Calendar.YEAR) == postDay.get(Calendar.YEAR)
                    && aTomorrow.get(Calendar.DAY_OF_YEAR) == postDay.get(Calendar.DAY_OF_YEAR)) {
                timeString = new SimpleDateFormat("后天 kk:mm", Locale.ENGLISH).format(time);
            } else {
                timeString = new SimpleDateFormat("MM-dd kk:mm", Locale.ENGLISH).format(time);
            }
            return timeString;
        }else return null;
    }
}
