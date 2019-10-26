package com.sparetimeforu.android.sparetimeforu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.activity.ChattingActivity;
import com.sparetimeforu.android.sparetimeforu.activity.SystemMessageListActivity;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 通知界面Recyclerview的适配器
 * Created by HQY on 2019/7/20.
 */

public class ConversationListAdapter extends BaseQuickAdapter<Conversation,BaseViewHolder> {
    Activity activity;
    public ConversationListAdapter(List<Conversation> conversationList, Activity activity){
        super(R.layout.conversation_list_item,conversationList);
        this.activity=activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, Conversation item) {
        final Conversation item1=item;
        ImageView avatar = (ImageView) helper.getView(R.id.message_friend_head);
        TextView message_user_name = (TextView) helper.getView(R.id.message_user);
        TextView message_count = (TextView) helper.getView(R.id.conversation_list_item_message_count);
        TextView format1=(TextView) helper.getView(R.id.conversation_list_item_format1);
        TextView format2=(TextView) helper.getView(R.id.conversation_list_item_format2);

        if(!item.getTargetId().equals(STFUConfig.manager_username))     message_user_name.setText(((UserInfo) item.getTargetInfo()).getNickname());
        else message_user_name.setText("");
        int messages_count_num = item.getUnReadMsgCnt();

        if(messages_count_num!=0){
            if (!item.getTargetId().equals(STFUConfig.manager_username) )   message_count.setText(""+messages_count_num );
            else {
                format1.setText("共接收到");
                message_count.setText(""+messages_count_num);
                format2.setText("条系统通知");
            }
        }else{
            if (item.getTargetId().equals(STFUConfig.manager_username) ){
                message_user_name.setText("系统消息");
            }
            message_count.setVisibility(View.GONE);
            format1.setVisibility(View.GONE);
            format2.setVisibility(View.GONE);
        }

        if(!item.getTargetId().equals(STFUConfig.manager_username)){//用户发来的信息s
            //修改头像
            JMessageClient.getUserInfo(item.getTargetId(), new GetUserInfoCallback() {
                @Override
                public void gotResult(int i, String s, UserInfo userInfo) {
                    message_user_name.setText(userInfo.getNickname());
                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback(){
                        @Override
                        public void gotResult(int i, String s, Bitmap bitmap) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    avatar.setImageBitmap(bitmap);//修改头像
                                }
                            });
                        }
                    });
                }
            });
        }else{
            avatar.setImageDrawable(activity.getDrawable(R.mipmap.ic_launcher_round));
        }

        RelativeLayout relativeLayout = (RelativeLayout) helper.getView(R.id.conversation_list_item);
        if (!item.getTargetId().equals(STFUConfig.manager_username)) {
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //用户发送给用户的信息
                    Intent intent = new Intent(activity, ChattingActivity.class);
                    intent.putExtra("username", item1.getTargetId());
                    intent.putExtra("userNickname", ((UserInfo) item1.getTargetInfo()).getNickname());
                    activity.startActivity(intent);
                }
            });
        }else{//系统发送给用户的消息
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //进入系统消息通知页面
                    Intent intent=new Intent(activity, SystemMessageListActivity.class);
                    //处理系统消息
                    List<Message> messages=item1.getAllMessage();
                    int un_read_message_count=item1.getUnReadMsgCnt();
                    HandleMessageUtil.handleSystemMessage(messages,un_read_message_count);
                    item1.setUnReadMessageCnt(0);
                    activity.startActivity(intent);
                }
            });
        }

    }
}
