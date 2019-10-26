package com.sparetimeforu.android.sparetimeforu.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HQY on 2019/7/18.
 */

public class MessageAdapter extends BaseQuickAdapter<Message,BaseViewHolder>{
    Bitmap bitmap_self_avatar,bitmap_other_side_avatar;
    public MessageAdapter(List<Message> messages){
        super(R.layout.chat_box,messages);
        Message temp_message=null;
        //初始化两个用户头像  对方和本机用户
        for(int i=messages.size()-1;i>0;i--){
            //找到一个对方用户发送的message
            if(!messages.get(i).getFromUser().getUserName().equals(STFUConfig.sUser.getEmail())){
                temp_message=messages.get(i);
                break;
            }
        }
        if(temp_message!=null){
            JMessageClient.getUserInfo(temp_message.getFromUser().getUserName(), new GetUserInfoCallback() {
                @Override
                public void gotResult(int i, String s, UserInfo userInfo) {
                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override
                        public void gotResult(int i, String s, Bitmap bitmap) {
                            bitmap_other_side_avatar=bitmap;
                        }
                    });
                }
            });
        }

        //获取本机用户头像
        JMessageClient.getMyInfo().getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                bitmap_self_avatar=bitmap;
            }
        });

    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        TextView tc1=(TextView)helper.getView(R.id.chat_box_tc1);
        TextView tc2=(TextView)helper.getView(R.id.chat_box_tc2);
        TextView content=(TextView)helper.getView(R.id.chat_item_content);
        CircleImageView avatar1=(CircleImageView) helper.getView(R.id.chat_box_avatar1);
        CircleImageView avatar2=(CircleImageView) helper.getView(R.id.chat_box_avatar2);
        TextContent messageContent=(TextContent) item.getContent();
        String message_text=messageContent.getText();//获取对方发送过来的内容
        //以下开始根据对方发送信息以及我方发送信息来显示
        if(item.getFromUser()!=null){
            if(item.getFromUser().getUserName().equals(STFUConfig.sUser.getEmail())){//我发送的信息,显示右侧头像，向右边靠拢
                tc2.setVisibility(View.GONE);
                tc1.setVisibility(View.VISIBLE);
                avatar1.setVisibility(View.GONE);
                avatar2.setVisibility(View.VISIBLE);
                //修改我的头像
                avatar2.setImageBitmap(bitmap_self_avatar);
                if (message_text!=null)
                content.setText(message_text);
            }else{
                tc1.setVisibility(View.GONE);
                tc2.setVisibility(View.VISIBLE);
                avatar2.setVisibility(View.GONE);
                avatar1.setVisibility(View.VISIBLE);
                avatar1.setImageBitmap(bitmap_other_side_avatar);
                if (message_text!=null)
                content.setText(message_text);
            }
        }
    }
}
