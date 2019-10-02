package com.sparetimeforu.android.sparetimeforu.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.util.List;

import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by HQY on 2019/7/20.
 */

public class ConversationListAdapter extends BaseQuickAdapter<Conversation,BaseViewHolder> {
    Context context;
    public ConversationListAdapter(List<Conversation> conversationList, Context context){
        super(R.layout.conversation_list_item,conversationList);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Conversation item) {
        ImageView avatar=(ImageView)helper.getView(R.id.message_friend_head);
        TextView message_user_name=(TextView)helper.getView(R.id.message_user);
        message_user_name.setText(((UserInfo)item.getTargetInfo()).getNickname());
        TextView message_count=(TextView)helper.getView(R.id.message_count);
        int messages_count_num=item.getUnReadMsgCnt();
        message_count.setText(messages_count_num+"");
        RelativeLayout relativeLayout=(RelativeLayout)helper.getView(R.id.conversation_list_item);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!item.getTargetId().equals(STFUConfig.manager_username)) {
                    //用户发送给用户的信息
                    Intent intent=new Intent(context, ChattingActivity.class);
                    intent.putExtra("username",item.getTargetId());
                    intent.putExtra("userNickname",((UserInfo)item.getTargetInfo()).getNickname());
                    context.startActivity(intent);

                }else{
                    //系统发送给用户的信息
                    //进入系统通知界面
                }
            }
        });
    }
}
