package com.sparetimeforu.android.sparetimeforu.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.activity.OthersPersonalActivity;
import com.sparetimeforu.android.sparetimeforu.activity.post.ErrandPostActivity;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.entity.SystemMessage;
import com.sparetimeforu.android.sparetimeforu.util.SystemMessageSendUtil;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;
import com.sparetimeforu.android.sparetimeforu.util.VerifyUtil;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.sparetimeforu.android.sparetimeforu.adapter.IdleThingAdapter.parseDateString;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by Jin on 2018/11/13.
 * <p>
 * SpareTimeForU
 */

public class ErrandAdapter extends BaseQuickAdapter<Errand, BaseViewHolder> {
    Activity activity;
    private String url;

    public ErrandAdapter(List<Errand> errands, Activity activity) {
        super(R.layout.item_errand, errands);
        this.activity = activity;

        url = STFUConfig.HOST + "/static";
        ErrandAdapter.this.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, ErrandPostActivity.class);
            //intent.putExtra("errand_id",((Errand)adapter.getItem(position)).getErrand_id());
            intent.putExtra("errand_id", 502);
            mContext.startActivity(intent);
        });

        ErrandAdapter.this.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.errand_avatar:
                    //进入他人界面
                    Toast.makeText(mContext, "You clicked the avatar! ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, OthersPersonalActivity.class);
                    intent.putExtra("user_Email", errands.get(position).getUser_Email());
                    activity.startActivity(intent);
                    break;
                case R.id.errand_share:
                    Toast.makeText(mContext, "You clicked the share! ", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.errand_like:
                    if (!VerifyUtil.isLogin(mContext)) {
                        break;
                    }
                    Errand e = (Errand) adapter.getItem(position);
                    if (e == null || STFUConfig.sUser == null) {
                        break;
                    }
                    FormBody body = new FormBody.Builder()
                            .add("post_type", 0 + "")
                            .add("post_id", e.getErrand_id() + "")
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
                                    refreshItem(e.getErrand_id(), position);
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });
                    break;
                case R.id.errand_comment:
                    Toast.makeText(mContext, "You clicked the comment! ", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

    }

    @Override
    protected void convert(BaseViewHolder helper, Errand item) {
        if (item == null || item.getContent() == null) {
            return;
        }
        ImageView avatar = (ImageView) helper.getView(R.id.errand_avatar);
        TextView nickName = (TextView) helper.getView(R.id.errand_nick_name);
        nickName.setText(item.getUser_Nickname());

        TextView date = ((TextView) helper.getView(R.id.errand_date));
        date.setText(parseDateString(item.getRelease_time()));

        TextView reward = ((TextView) helper.getView(R.id.errand_reward));
        reward.setText("\uD83D\uDCB0" + item.getMoney());

        TextView origin = ((TextView) helper.getView(R.id.errand_origin));
        origin.setText(item.getOrigin());

        TextView destination = ((TextView) helper.getView(R.id.errand_destination));
        destination.setText(item.getDestination());

        TextView deadline = ((TextView) helper.getView(R.id.errand_deadline));
        if (item.getEnd_time() != null) {
            deadline.setText(parseDateString(item.getEnd_time()));
        }

        TextView caption = ((TextView) helper.getView(R.id.errand_caption));
        caption.setText(item.getContent());

        ImageView p1 = (ImageView) helper.getView(R.id.errand_picture1);
        ImageView p2 = (ImageView) helper.getView(R.id.errand_picture2);
        ImageView p3 = (ImageView) helper.getView(R.id.errand_picture3);
        ImageView share = (ImageView) helper.getView(R.id.errand_share);
        ImageView like = (ImageView) helper.getView(R.id.errand_like);
        Button accept_mission=(Button)helper.getView(R.id.Errand_accpet_mission);
        Button finish_mission=(Button)helper.getView(R.id.Errand_finish_mission);
        TextView mission_finished_text=(TextView)helper.getView(R.id.Errand_finished_text);
        LinearLayout comment = (LinearLayout) helper.getView(R.id.errand_comment);

        helper.addOnClickListener(R.id.errand_avatar)
                .addOnClickListener(R.id.errand_share)
                .addOnClickListener(R.id.errand_like)
                .addOnClickListener(R.id.errand_comment);


        //set avatar img
        Picasso.get()
                .load(url + "/avatar/" + item.getUser_Avatar())
                .resize(100, 100)
                .centerCrop()
                .into(avatar);
        Picasso.get()
                .load(url + "/mission_pictures/" + item.getPicture_url_1())
                .resize(200, 200)
                .centerCrop()
                .into(p1);
        Picasso.get()
                .load(url + "/mission_pictures/" + item.getPicture_url_2())
                .resize(200, 200)
                .centerCrop()
                .into(p2);
        Picasso.get()
                .load(url + "/mission_pictures/" + item.getPicture_url_3())
                .resize(200, 200)
                .centerCrop()
                .into(p3);

        if(item.getIs_received()==1) {//任务已被接受
            accept_mission.setVisibility(View.INVISIBLE);
            if(false){//任务未完成
                finish_mission.setVisibility(View.VISIBLE);
            }else{
                mission_finished_text.setVisibility(View.VISIBLE);
                mission_finished_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //发送请求到服务器
                        FormBody formBody=new FormBody.Builder()
                                .add("errand_id",item.getErrand_id()+"").build();

                        OkHttpUtil.sendOkHttpPostRequest(url, formBody, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(activity,"网络请求出错",Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //发送系统通知
                                SystemMessageSendUtil.send_System_message_errand(item.getUser_Email(),item.getErrand_id(),0,SystemMessageSendUtil.Errand_message_mode_errand_solved);

                            }
                        });
                    }
                });
            }
        }else{
            accept_mission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //注册管理员账号
                    JMessageClient.register(STFUConfig.manager_username, STFUConfig.manager_username + "1", new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            Logger.i("管理员注册成功");
                        }
                    });
                    //发送请求到服务器  用户接收任务
                    Toast.makeText(activity,"发送接收任务请求",Toast.LENGTH_SHORT).show();
                    FormBody formBody=new FormBody.Builder()
                            .add("user_id",STFUConfig.sUser.getUser_id()+"")
                            .add("errand_id",item.getErrand_id()+"")
                            .build();
                    OkHttpUtil.sendOkHttpPostRequest(url, formBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(activity,"网络请求错误，请检查网络",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //发送系统通知
                            SystemMessageSendUtil.send_System_message_errand(item.getUser_Email(),item.getErrand_id(),0,SystemMessageSendUtil.Errand_message_mode_errand_accepted);
                        }
                    });

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
                .add("errand_id", itemId + "")
                .build();

        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/get_mission_by_mission_id", body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Errand errand = HandleMessageUtil.handlePost_Errand_Message(response.body().string());
                ((Activity) mContext).runOnUiThread(() -> {
                    ErrandAdapter.this.setData(position, errand);
                });
            }
        });
    }

}
