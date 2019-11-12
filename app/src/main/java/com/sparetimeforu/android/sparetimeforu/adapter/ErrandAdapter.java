package com.sparetimeforu.android.sparetimeforu.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.sparetimeforu.android.sparetimeforu.util.ErrandDataBaseUtil;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;
import com.sparetimeforu.android.sparetimeforu.util.SystemDataBaseUtil;
import com.sparetimeforu.android.sparetimeforu.util.SystemMessageSendUtil;
import com.sparetimeforu.android.sparetimeforu.util.VerifyUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.sparetimeforu.android.sparetimeforu.adapter.IdleThingAdapter.parseDateString;

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
            intent.putExtra("errand_id", getItem(position).getErrand_id());
            mContext.startActivity(intent);
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

        TextView likeNum = helper.getView(R.id.errand_like);
        likeNum.setText(item.getLike_number() + "");

        TextView commentNum = helper.getView(R.id.errand_comment);
        commentNum.setText(item.getComment_number() + "");

        ImageView p1 = (ImageView) helper.getView(R.id.errand_picture1);
        ImageView share = (ImageView) helper.getView(R.id.errand_share);
        ImageView likeImageView = (ImageView) helper.getView(R.id.ic_like);

        Button accept_mission = (Button) helper.getView(R.id.Errand_accpet_mission);
        Button finish_mission = (Button) helper.getView(R.id.Errand_finish_mission);
        Button cancelMissionBtn = (Button) helper.getView(R.id.errand_cancel);
        Button confirmFinishMBtn = (Button) helper.getView(R.id.confirm_finish_mission);
        Button notFinishBtn = (Button) helper.getView(R.id.errand_not_finish);
        Button deleteBtn = (Button) helper.getView(R.id.Errand_delete);
        TextView mission_finished_text = helper.getView(R.id.Errand_finished_text);

        MyOnclickListener listener = new MyOnclickListener(item, helper.getAdapterPosition()
                , accept_mission, finish_mission, cancelMissionBtn, confirmFinishMBtn,
                notFinishBtn, deleteBtn, mission_finished_text);
        accept_mission.setOnClickListener(listener);
        finish_mission.setOnClickListener(listener);
        cancelMissionBtn.setOnClickListener(listener);
        confirmFinishMBtn.setOnClickListener(listener);
        notFinishBtn.setOnClickListener(listener);
        deleteBtn.setOnClickListener(listener);
        mission_finished_text.setOnClickListener(listener);

        helper.addOnClickListener(R.id.layout_avatar)
                .addOnClickListener(R.id.errand_share)
                .addOnClickListener(R.id.like_layout)
                .addOnClickListener(R.id.errand_comment);

//        ErrandAdapter.this.setOnItemChildClickListener((adapter, view, position) -> {
//            FormBody formBody;
//            switch (view.getId()) {
//                case R.id.layout_avatar:
//                    //进入他人界面
//                    Intent intent = new Intent(activity, OthersPersonalActivity.class);
//                    intent.putExtra("user_Email", getItem(position).getUser_Email());
//                    activity.startActivity(intent);
//                    break;
//                case R.id.errand_share:
//                    Toast.makeText(mContext, "You clicked the share! ", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.like_layout:
//                    if (!VerifyUtil.isLogin(mContext)) {
//                        break;
//                    }
//                    Errand e = (Errand) adapter.getItem(position);
//                    if (e == null || STFUConfig.sUser == null) {
//                        break;
//                    }
//                    FormBody body = new FormBody.Builder()
//                            .add("post_type", 0 + "")
//                            .add("post_id", e.getErrand_id() + "")
//                            .add("auth_token", STFUConfig.sUser.getAuth_token())
//                            .build();
//                    OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/like", body, new okhttp3.Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            String str = response.body().string();
//                            try {
//                                JSONObject jsonObject = new JSONObject(str);
//                                if (jsonObject.getString("status").equals("success")) {
//                                    refreshItem(e.getErrand_id(), position);
//                                }
//                            } catch (JSONException e) {
//                                Logger.e(e.toString());
//                            }
//                        }
//                    });
//                    break;
//                case R.id.Errand_finished_text:
//                    break;
//                case R.id.Errand_accpet_mission:
//                    //注册管理员账号
//                    JMessageClient.register(STFUConfig.manager_username, STFUConfig.manager_username + "1", new BasicCallback() {
//                        @Override
//                        public void gotResult(int i, String s) {
//                            Logger.i("管理员注册成功");
//                        }
//                    });
//                    //发送请求到服务器  用户接收任务
//                    formBody = new FormBody.Builder()
//                            .add("auth_token", STFUConfig.sUser.getAuth_token())
//                            .add("post_id", getItem(position).getErrand_id() + "")
//                            .build();
//                    OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/receive_mission", formBody, new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            //发送系统通知
//
//                            try {
//                                JSONObject jsonObject = new JSONObject(response.body().string());
//                                String status = jsonObject.getString("status");
//                                if (status.equals("success")) {
//                                    SystemMessageSendUtil.send_System_message_errand(getItem(position).getUser_Email(),
//                                            getItem(position).getErrand_id(),
//                                            0, SystemMessageSendUtil.Errand_message_mode_errand_accepted);
//                                    ((Activity) mContext).runOnUiThread(() -> {
//                                        ErrandAdapter.this.remove(position);
//                                        ErrandAdapter.this.notifyItemRemoved(position);
//                                    });
//                                } else {
//                                    ((Activity) mContext).runOnUiThread(() -> {
//                                        Toast.makeText(mContext, "接受失败！", Toast.LENGTH_SHORT).show();
//                                    });
//                                }
//                            } catch (JSONException e) {
//                                Logger.e(e.toString());
//                            }
//                        }
//                    });
//
//                    break;
//                case R.id.Errand_finish_mission:
//                    //发送请求到服务器  用户完成任务
//                    formBody = new FormBody.Builder()
//                            .add("auth_token", STFUConfig.sUser.getAuth_token())
//                            .add("post_id", getItem(position).getErrand_id() + "")
//                            .build();
//                    OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/finish_mission", formBody, new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            //发送系统通知
//                            try {
//                                JSONObject jsonObject = new JSONObject(response.body().string());
//                                String status = jsonObject.getString("status");
//                                if (status.equals("success")) {
//                                    ((Activity) mContext).runOnUiThread(() -> {
//
//                                        Errand item = getItem(position);
//                                        SystemMessageSendUtil.send_System_message_errand(
//                                                item.getUser_Email(), item.getErrand_id(), 0,
//                                                SystemMessageSendUtil.Errand_message_mode_errand_solved);
//
//                                        accept_mission.setVisibility(View.GONE);
//                                        finish_mission.setVisibility(View.GONE);
//                                        cancelMissionBtn.setVisibility(View.GONE);
//                                        notFinishBtn.setVisibility(View.GONE);
//                                        confirmFinishMBtn.setVisibility(View.GONE);
//                                        deleteBtn.setVisibility(View.GONE);
//                                        mission_finished_text.setText("等待悬赏者确认");
//                                        mission_finished_text.setVisibility(View.VISIBLE);
//                                        Toast.makeText(mContext, "任务已经完成，等悬赏者确认！", Toast.LENGTH_SHORT).show();
//                                    });
//                                }
//                            } catch (JSONException e) {
//                                Logger.e(e.toString());
//                            }
//                        }
//                    });
//
//                    break;
//                case R.id.errand_cancel:
//                    //发送请求到服务器  用户取消任务
//                    formBody = new FormBody.Builder()
//                            .add("auth_token", STFUConfig.sUser.getAuth_token())
//                            .add("post_id", getItem(position).getErrand_id() + "")
//                            .build();
//                    OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/rollback_mission", formBody, new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            //发送系统通知
//                            try {
//                                JSONObject jsonObject = new JSONObject(response.body().string());
//                                String status = jsonObject.getString("status");
//                                if (status.equals("success")) {
//                                    ((Activity) mContext).runOnUiThread(() -> {
//                                        Toast.makeText(mContext, "任务已经取消！", Toast.LENGTH_SHORT).show();
//                                        ((Activity) mContext).runOnUiThread(() -> {
//                                            ErrandAdapter.this.remove(position);
//                                            ErrandAdapter.this.notifyItemRemoved(position);
//                                        });
//                                    });
//                                }
//                            } catch (JSONException e) {
//                                Logger.e(e.toString());
//                            }
//                        }
//                    });
//
//                    break;
//                case R.id.confirm_finish_mission:
//                    //发送请求到服务器  悬赏者确认完成任务
//                    formBody = new FormBody.Builder()
//                            .add("auth_token", STFUConfig.sUser.getAuth_token())
//                            .add("post_id", getItem(position).getErrand_id() + "")
//                            .build();
//                    OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/confirm_finish", formBody, new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            //发送系统通知
//                            try {
//                                JSONObject jsonObject = new JSONObject(response.body().string());
//                                String status = jsonObject.getString("status");
//                                if (status.equals("success")) {
//                                    ((Activity) mContext).runOnUiThread(() -> {
//                                        Toast.makeText(mContext, "悬赏者已确认，钱已经到对面账户了！", Toast.LENGTH_SHORT).show();
//
//                                        accept_mission.setVisibility(View.GONE);
//                                        finish_mission.setVisibility(View.GONE);
//                                        cancelMissionBtn.setVisibility(View.GONE);
//                                        notFinishBtn.setVisibility(View.GONE);
//                                        confirmFinishMBtn.setVisibility(View.GONE);
//                                        deleteBtn.setVisibility(View.GONE);
//                                        mission_finished_text.setText("双方确认完成");
//                                        mission_finished_text.setVisibility(View.VISIBLE);
//                                    });
//                                }
//                            } catch (JSONException e) {
//                                Logger.e(e.toString());
//                            }
//                        }
//                    });
//                    break;
//                case R.id.errand_not_finish:
//                    //发送请求到服务器  用户未真正完成任务
//                    Toast.makeText(mContext, "我们会对对方进行审批！", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.Errand_delete:
//                    ErrandDataBaseUtil.deleteErrandNow(item.getErrand_id());
//
//                    formBody = new FormBody.Builder()
//                            .add("post_id", item.getErrand_id() + "")
//                            .add("auth_token", STFUConfig.sUser.getAuth_token())
//                            .build();
//
//                    OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/delete_or_not", formBody, new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response.body().string());
//                                String status = jsonObject.getString("status");
//                                if (status.equals("success")) {
//                                    ((Activity) mContext).runOnUiThread(() -> {
//                                        ErrandAdapter.this.remove(position);
//                                        ErrandAdapter.this.notifyItemChanged(position);
//                                    });
//                                } else { // 非 success
//                                    ((Activity) mContext).runOnUiThread(() -> {
//                                        try {
//                                            String error = jsonObject.getString("error");
//                                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
//                                        } catch (JSONException e1) {
//                                            Logger.e(e1.toString());
//                                        }
//                                    });
//                                }
//                            } catch (JSONException e) {
//                                Logger.e(e.toString());
//                            }
//                        }
//                    });
//                    break;
//
//            }
//        });


        //set avatar img


        Picasso.get()
                .load(url + "/avatar/" + item.getUser_Avatar())
                .resize(100, 100)
                .centerCrop()
                .into(avatar);
        if (!item.getPicture_url_1().equals("")) {
            Picasso.get()
                    .load(url + "/mission_pictures/" + item.getPicture_url_1())
                    .resize(200, 200)
                    .centerCrop()
                    .into(p1, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            p1.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });
        } else {
            p1.setVisibility(View.GONE);
        }


        // 显示按钮状态，先登录，再判断是不是悬赏者
        if (VerifyUtil.isLoginStatus(mContext)) {
            // 悬赏者，从流程后到前判断
            if (item.getUser_Email().equals(STFUConfig.sUser.getEmail())) {
                if (item.getIs_confirmed_finish() == 1) { // 确认完成
                    accept_mission.setVisibility(View.GONE);
                    finish_mission.setVisibility(View.GONE);
                    cancelMissionBtn.setVisibility(View.GONE);
                    notFinishBtn.setVisibility(View.GONE);
                    confirmFinishMBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                    mission_finished_text.setText("任务已完成");
                    mission_finished_text.setVisibility(View.VISIBLE);
                } else if (item.getIs_finished() == 1) { // 未确认
                    accept_mission.setVisibility(View.GONE);
                    finish_mission.setVisibility(View.GONE);
                    cancelMissionBtn.setVisibility(View.GONE);
                    notFinishBtn.setVisibility(View.VISIBLE);
                    confirmFinishMBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setVisibility(View.GONE);
                } else if (item.getIs_received() == 1) { // 未完成，等待
                    accept_mission.setVisibility(View.GONE);
                    finish_mission.setVisibility(View.GONE);
                    cancelMissionBtn.setVisibility(View.GONE);
                    notFinishBtn.setVisibility(View.GONE);
                    confirmFinishMBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                    mission_finished_text.setText("正在进行…");
                    mission_finished_text.setVisibility(View.VISIBLE);
                } else if (item.getIs_received() == 0) { //未接受
                    accept_mission.setVisibility(View.GONE);
                    finish_mission.setVisibility(View.GONE);
                    cancelMissionBtn.setVisibility(View.GONE);
                    notFinishBtn.setVisibility(View.GONE);
                    confirmFinishMBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.VISIBLE);
                }

                //接收者
            } else {
                if (item.getIs_confirmed_finish() == 1) { // 悬赏者已确认
                    accept_mission.setVisibility(View.GONE);
                    finish_mission.setVisibility(View.GONE);
                    cancelMissionBtn.setVisibility(View.GONE);
                    notFinishBtn.setVisibility(View.GONE);
                    confirmFinishMBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                    mission_finished_text.setText("任务已完成");
                    mission_finished_text.setVisibility(View.VISIBLE);
                } else if (item.getIs_finished() == 1) { // 未确认，等待
                    accept_mission.setVisibility(View.GONE);
                    finish_mission.setVisibility(View.GONE);
                    cancelMissionBtn.setVisibility(View.GONE);
                    notFinishBtn.setVisibility(View.GONE);
                    confirmFinishMBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                    mission_finished_text.setText("待确认……");
                    mission_finished_text.setVisibility(View.VISIBLE);
                } else if (item.getIs_received() == 1) { // 未完成
                    accept_mission.setVisibility(View.GONE);
                    finish_mission.setVisibility(View.VISIBLE);
                    cancelMissionBtn.setVisibility(View.VISIBLE);
                    notFinishBtn.setVisibility(View.GONE);
                    confirmFinishMBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                    mission_finished_text.setVisibility(View.GONE);

                } else if (item.getIs_received() == 0) { // 未接受
                    accept_mission.setVisibility(View.VISIBLE);
                    finish_mission.setVisibility(View.GONE);
                    cancelMissionBtn.setVisibility(View.GONE);
                    notFinishBtn.setVisibility(View.GONE);
                    confirmFinishMBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                    mission_finished_text.setVisibility(View.GONE);
                }
            }

        } else { // 未登录，都GONE掉吧
            accept_mission.setVisibility(View.GONE);
            finish_mission.setVisibility(View.GONE);
            cancelMissionBtn.setVisibility(View.GONE);
            notFinishBtn.setVisibility(View.GONE);
            confirmFinishMBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            mission_finished_text.setText("登录后接受");
            mission_finished_text.setVisibility(View.VISIBLE);
        }


        //是否点赞
        if (VerifyUtil.isLoginStatus(mContext)) {
            FormBody body = new FormBody.Builder()
                    .add("auth_token", STFUConfig.sUser.getAuth_token())
                    .add("post_type", 0 + "")
                    .add("post_id", item.getErrand_id() + "")
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


    private class MyOnclickListener implements View.OnClickListener {
        Errand item;
        int position;
        Button accept_mission;
        Button finish_mission;
        Button cancelMissionBtn;
        Button confirmFinishMBtn;
        Button notFinishBtn;
        Button deleteBtn;
        TextView mission_finished_text;

        MyOnclickListener(Errand item, int position, Button accept_mission,
                          Button finish_mission,
                          Button cancelMissionBtn,
                          Button confirmFinishMBtn,
                          Button notFinishBtn,
                          Button deleteBtn,
                          TextView mission_finished_text) {
            this.item = item;
            this.position = position;
            this.accept_mission = accept_mission;
            this.finish_mission = finish_mission;
            this.cancelMissionBtn = cancelMissionBtn;
            this.confirmFinishMBtn = confirmFinishMBtn;
            this.notFinishBtn = notFinishBtn;
            this.deleteBtn = deleteBtn;
            this.mission_finished_text = mission_finished_text;
        }

        @Override
        public void onClick(View view) {
            FormBody formBody;
            switch (view.getId()) {
                case R.id.layout_avatar:
                    //进入他人界面
                    Intent intent = new Intent(activity, OthersPersonalActivity.class);
                    intent.putExtra("user_Email", item.getUser_Email());
                    activity.startActivity(intent);
                    break;
                case R.id.errand_share:
                    Toast.makeText(mContext, "You clicked the share! ", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.like_layout:
                    if (!VerifyUtil.isLogin(mContext)) {
                        break;
                    }
                    Errand e = (Errand) item;
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
                case R.id.Errand_finished_text:
                    break;
                case R.id.Errand_accpet_mission:
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
                            .add("post_id", getItem(position).getErrand_id() + "")
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
                                    SystemMessageSendUtil.send_System_message_errand(getItem(position).getUser_Email(),
                                            getItem(position).getErrand_id(),
                                            0, SystemMessageSendUtil.Errand_message_mode_errand_accepted);
                                    ((Activity) mContext).runOnUiThread(() -> {
                                        ErrandAdapter.this.remove(position);
                                        ErrandAdapter.this.notifyItemRemoved(position);
                                    });
                                } else {
                                    ((Activity) mContext).runOnUiThread(() -> {
                                        Toast.makeText(mContext, "接受失败！", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });

                    break;
                case R.id.Errand_finish_mission:
                    //发送请求到服务器  用户完成任务
                    formBody = new FormBody.Builder()
                            .add("auth_token", STFUConfig.sUser.getAuth_token())
                            .add("post_id", getItem(position).getErrand_id() + "")
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
                                    ((Activity) mContext).runOnUiThread(() -> {

                                        Errand item = getItem(position);
                                        SystemMessageSendUtil.send_System_message_errand(
                                                item.getUser_Email(), item.getErrand_id(), 0,
                                                SystemMessageSendUtil.Errand_message_mode_errand_solved);

                                        accept_mission.setVisibility(View.GONE);
                                        finish_mission.setVisibility(View.GONE);
                                        cancelMissionBtn.setVisibility(View.GONE);
                                        notFinishBtn.setVisibility(View.GONE);
                                        confirmFinishMBtn.setVisibility(View.GONE);
                                        deleteBtn.setVisibility(View.GONE);
                                        mission_finished_text.setText("待确认……");
                                        mission_finished_text.setVisibility(View.VISIBLE);
                                        Toast.makeText(mContext, "任务已经完成，等悬赏者确认！", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });

                    break;
                case R.id.errand_cancel:
                    //发送请求到服务器  用户取消任务
                    formBody = new FormBody.Builder()
                            .add("auth_token", STFUConfig.sUser.getAuth_token())
                            .add("post_id", getItem(position).getErrand_id() + "")
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
                                SystemMessageSendUtil.send_System_message_errand(item.getUser_Email(),item.getErrand_id(),0,SystemMessageSendUtil.Errand_message_mode_errand_canceled);
                                if (status.equals("success")) {
                                    ((Activity) mContext).runOnUiThread(() -> {
                                        Toast.makeText(mContext, "任务已经取消！", Toast.LENGTH_SHORT).show();
                                        ((Activity) mContext).runOnUiThread(() -> {
                                            ErrandAdapter.this.remove(position);
                                            ErrandAdapter.this.notifyItemRemoved(position);
                                        });
                                    });
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });

                    break;
                case R.id.confirm_finish_mission:
                    //发送请求到服务器  悬赏者确认完成任务
                    formBody = new FormBody.Builder()
                            .add("auth_token", STFUConfig.sUser.getAuth_token())
                            .add("post_id", getItem(position).getErrand_id() + "")
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
                                SystemMessageSendUtil.send_System_message_errand(item.getReceiver_email(),item.getErrand_id(),0,SystemMessageSendUtil.Errand_message_mode_errand_solved_agree);
                                if (status.equals("success")) {
                                    ((Activity) mContext).runOnUiThread(() -> {
                                        Toast.makeText(mContext, "悬赏者已确认，钱已经到对面账户了！", Toast.LENGTH_SHORT).show();

                                        accept_mission.setVisibility(View.GONE);
                                        finish_mission.setVisibility(View.GONE);
                                        cancelMissionBtn.setVisibility(View.GONE);
                                        notFinishBtn.setVisibility(View.GONE);
                                        confirmFinishMBtn.setVisibility(View.GONE);
                                        deleteBtn.setVisibility(View.GONE);
                                        mission_finished_text.setText("双方确认完成");
                                        mission_finished_text.setVisibility(View.VISIBLE);
                                    });
                                }
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        }
                    });
                    break;
                case R.id.errand_not_finish:
                    //发送请求到服务器  用户未真正完成任务
                    Toast.makeText(mContext, "我们会对对方进行审批！", Toast.LENGTH_SHORT).show();
                    SystemMessageSendUtil.send_System_message_errand(item.getReceiver_email(),item.getErrand_id(),0,SystemMessageSendUtil.Errand_message_mode_errand_solved_rejected);
                    break;
                case R.id.Errand_delete:
                    ErrandDataBaseUtil.deleteErrandNow(item.getErrand_id());

                    formBody = new FormBody.Builder()
                            .add("post_id", item.getErrand_id() + "")
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
                                    ((Activity) mContext).runOnUiThread(() -> {
                                        ErrandAdapter.this.remove(position);
                                        ErrandAdapter.this.notifyItemChanged(position);
                                    });
                                } else { // 非 success
                                    ((Activity) mContext).runOnUiThread(() -> {
                                        try {
                                            String error = jsonObject.getString("error");
                                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
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
