package com.sparetimeforu.android.sparetimeforu.fragment;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.adapter.MessageAdapter;

import java.util.List;

import Listener.GlobalEventListener;
import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;

/**
 * Created by HQY on 2019/7/20.
 */

public class ChattingFragment extends Fragment implements View.OnClickListener{
    @Nullable
    public EditText message_content_edittext;
    public ImageButton send_message_button;
    public RecyclerView message_recyclerview;
    public ImageView chat_go_back;
    public TextView title_top;
    private boolean get_my_avatar=false;
    private boolean get_other_avatar=false;
    private String chat_user_name;//跟你对话的人的名字
    private String chat_user_Nickname;
    private Conversation conversation;//当前会话
    private List<Message> messages;//会话信息
    private int position;
    private Bitmap bitmap_self_avatar,bitmap_other_side_avatar;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        JMessageClient.init(getActivity().getApplicationContext());
        View view=inflater.inflate(R.layout.fragment_chatting,container,false);
        send_message_button=(ImageButton)view.findViewById(R.id.chat_item_send_message);
        message_content_edittext=(EditText)view.findViewById(R.id.send_message_content);
        send_message_button.setOnClickListener(this);
        message_recyclerview=(RecyclerView)view.findViewById(R.id.chat_item_RecyclerView);
        message_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        Toolbar toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        chat_go_back=(ImageView)view.findViewById(R.id.menu_back_icon);
        chat_go_back.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {
            STFUConfig.globalEventListener.setChattingFragment(null);
            getActivity().finish();}});
        chat_user_name=getActivity().getIntent().getStringExtra("username");
        chat_user_Nickname=getActivity().getIntent().getStringExtra("userNickname");
        title_top=(TextView)view.findViewById(R.id.title_top);
        title_top.setText(chat_user_Nickname);
        if(STFUConfig.globalEventListener!=null) {
            STFUConfig.globalEventListener.setChattingFragment(this);
            JMessageClient.registerEventReceiver(STFUConfig.globalEventListener);
        }
        init_Date();
        return view;
    }

    private void init_Date(){
        //初次进入该activity 根据用户名获取会话对象conversation,并初始化messages
        conversation=JMessageClient.getSingleConversation(chat_user_name,null);
        if(conversation==null) conversation=Conversation.createSingleConversation(chat_user_name);
        messages=conversation.getAllMessage();
        position=messages.size()-conversation.getUnReadMsgCnt();
        getAvatar_Bitmap();
    }
    public void setConversation_Messages_Position(Conversation conversation,List<Message> messages,int position){//GlobalEventListener 接收信息后调用该函数，用以设置此recyclerview的参数
        this.conversation=conversation;
        this.messages=messages;
        this.position=position;
        setupAdapter();
    }
    public void getAvatar_Bitmap(){
        //初始化两个用户头像  对方和本机用户
        Message temp_message=null;
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
                            get_other_avatar=true;
                            setupAdapter();
                        }
                    });
                }
            });
        }else {
            get_other_avatar=true;
        }
        //获取本机用户头像
        JMessageClient.getMyInfo().getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                bitmap_self_avatar=bitmap;
                get_my_avatar=true;
                setupAdapter();
            }
        });

    }
    public void setupAdapter(){//根据Messages 设置Recyclerview  的数据
        if(get_other_avatar&&get_other_avatar)
        if(messages!=null){
            MessageAdapter messageAdapter=new MessageAdapter(messages,getActivity(),bitmap_self_avatar,bitmap_other_side_avatar);
            messageAdapter.isFirstOnly(false);
            message_recyclerview.setAdapter(messageAdapter);
            message_recyclerview.scrollToPosition(position-1);
            conversation.resetUnreadCount();
            position=messages.size();
        }
    }

    public void onClick(View view) {
        if(view.getId()==R.id.chat_item_send_message){//点击发送按钮
            //获取输入内容
            String send_content=message_content_edittext.getText().toString();
            if (!send_content.equals("")){
                final Message message=JMessageClient.createSingleTextMessage(chat_user_name,null,send_content);
                if(message!=null){
                    JMessageClient.sendMessage(message);
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if(i==0){
                                Toast.makeText(getApplicationContext(),"发送成功",Toast.LENGTH_SHORT);
                                messages.add(message);
                                position++;
                                setupAdapter();
                                message_content_edittext.setText("");
                            }else{
                                Toast.makeText(getApplicationContext(),"发送失败，请检查您的网络",Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"消息发送失败，请重试",Toast.LENGTH_SHORT);
                }

            }else{
                Toast.makeText(getApplicationContext(),"请输入内容",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public static ChattingFragment newInstance(){
        return new ChattingFragment();
    }
}
