package com.sparetimeforu.android.sparetimeforu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.activity.ChattingActivity;

import java.util.List;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by HQY on 2019/7/20.
 */

public class FriendAdapter extends BaseQuickAdapter<UserInfo,BaseViewHolder>{
    Activity activity;
    public FriendAdapter(List<UserInfo> userInfos, Activity activity){
        super(R.layout.fragment_friend_item,userInfos);
        this.activity=activity;
    }
    @Override
    protected void convert(BaseViewHolder helper, UserInfo item) {
        ImageView friend_avatar=(ImageView)helper.getView(R.id.friend_head);
        item.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if(i==0){//获取头像成功
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            friend_avatar.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        });
        TextView friend_name=(TextView)helper.getView(R.id.friend_nickname);
        friend_name.setText(item.getNickname());
        Button start_chatting=(Button)helper.getView(R.id.friend_start_chatting);
        start_chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=item.getUserName();
                Logger.i(item.toString());
                //将信息传进chattingactivity
                Intent intent=new Intent(activity, ChattingActivity.class);
                intent.putExtra("username",userName);//将聊天对象的用户名传进去
                intent.putExtra("userNickname",item.getNickname());
                activity.startActivity(intent);
            }
        });

    }
}
