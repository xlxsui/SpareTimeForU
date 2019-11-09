package com.sparetimeforu.android.sparetimeforu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.activity.PostReleasedActivity;
import com.sparetimeforu.android.sparetimeforu.entity.SystemMessage;

import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * 系统通知界面的适配器
 * Created by HQY on 2019/10/2.
 */

public class SystemMessageListAdapter extends BaseQuickAdapter<SystemMessage,BaseViewHolder> {
    private final String type_string[]={
            "好友","评论","发布任务信息","点赞" //根据系统通知的类型选择type_string
    };
    private final int header_resources[]={
            R.drawable.ic_22,R.drawable.ic_33,R.drawable.ic_22_hide,R.drawable.ic_33_hide   //根据系统通知的类型修改图标url
    };
    Activity activity;
    public SystemMessageListAdapter(List<SystemMessage> messageList, Activity activity){
        super(R.layout.system_message_list_item,messageList);
        this.activity=activity;
    }
    @Override
    protected void convert(BaseViewHolder helper, SystemMessage item) {
        //获取信息内容
        ImageView header=helper.getView(R.id.system_message_list_item_header);
        TextView user_name=helper.getView(R.id.system_message_list_item_usernickname);
        TextView date=helper.getView(R.id.system_message_list_item_date);
        TextView content=helper.getView(R.id.system_message_list_item_content);
        TextView hint=helper.getView(R.id.system_message_list_item_hint);
        TextView type=helper.getView(R.id.system_message_list_item_type);
        TextView red_point_tag=helper.getView(R.id.system_message_list_item_red_point_tag);
        RelativeLayout relativeLayout=helper.getView(R.id.system_message_list_item);
        type.setText(type_string[item.getType()]);
        if(item.getType()==SystemMessage.Approve_Message) hint.setText(activity.getResources().getString(R.string.system_message_list_item_hint_approval));
        else if(item.getType()==SystemMessage.Errand_Message)   hint.setText(activity.getResources().getString(R.string.system_message_list_item_hint_errand_message));
        else if(item.getType()==SystemMessage.Add_Friend_Message) hint.setText(activity.getResources().getString(R.string.system_message_list_item_hint_add_friend));
        date.setText(item.getDate());
        header.setImageDrawable(activity.getResources().getDrawable(header_resources[item.getType()]));
        user_name.setText(item.getUser_nickname());

        if(item.getIs_first_time_show()!=1){
            red_point_tag.setVisibility(View.GONE);
        }
        content.setText(item.getContent());
        //设置点击事件  点击之后，红点都消失
        switch (item.getType()){
            case SystemMessage.Add_Friend_Message://加好友信息
                //弹出dialog询问是否添加为好友
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                red_point_tag.setVisibility(View.GONE);
                                item.setIs_first_time_show(0);
                                show_Confirm_Dialog(item.getUser_nickname(),item.getUser_Email());
                            }
                        });
                    }
                });
                break;
            case SystemMessage.Comment_Message://评论信息
                //跳转到评论专属页面
                content.setText("评论了您的帖子");
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转到评论专属页面
                        red_point_tag.setVisibility(View.GONE);
                        item.setIs_first_time_show(0);
                    }
                });
                break;
            case SystemMessage.Errand_Message://帖子相关信息
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        red_point_tag.setVisibility(View.GONE);
                        item.setIs_first_time_show(0);
                        //跳转到帖子信息界面
                        if(item.getContent().equals("完成了您的任务")) hint.setText("点击确认完成任务");
                        Intent intent=new Intent(activity, PostReleasedActivity.class);
                        activity.startActivity(intent);
                    }
                });
                break;
            case SystemMessage.Approve_Message://点赞信息
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //标注已读
                        red_point_tag.setVisibility(View.GONE);
                        item.setIs_first_time_show(0);
                    }
                });
                break;
        }
    }

    private void show_Confirm_Dialog(String user_nick_name,String user_name){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        int defaultChoice = 1;
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setTitle("是否添加 "+user_nick_name+" 为好友");
        final String[] sex = {"是", "否"};

        if (STFUConfig.sUser != null && STFUConfig.sUser.getGender().equals("男"))
            defaultChoice = 0;
        builder.setSingleChoiceItems(sex, defaultChoice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity, "性别为：" + sex[which], Toast.LENGTH_SHORT).show();

                if (STFUConfig.sUser != null) {
                    STFUConfig.sUser.setGender(sex[which]);
                }
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //接收好友请求
                ContactManager.acceptInvitation(user_name, null, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if(i==0){
                            Toast.makeText(activity, "添加好友成功", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(activity, "添加好友失败\n原因:"+s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
