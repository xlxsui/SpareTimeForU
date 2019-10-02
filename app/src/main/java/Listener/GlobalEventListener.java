package Listener;

import android.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.activity.ChattingActivity;
import com.sparetimeforu.android.sparetimeforu.fragment.ChattingFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.ConversationListFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.STFUFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.module.StudyFragment;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by HQY on 2019/7/20.
 */

public class GlobalEventListener {
    ChattingFragment chattingFragment;
    ConversationListFragment conversationListFragment;
    STFUFragment stfuFragment;
    //还需要一个系统通知的fragment  Manager_messageListFragment manager_messageListFragment;

    public void setConversationListFragment(ConversationListFragment conversationListFragment) {
        this.conversationListFragment = conversationListFragment;
    }

    public void setChattingFragment(ChattingFragment chattingFragment) {
        this.chattingFragment = chattingFragment;
    }
    public void setStfuFragment(STFUFragment stfuFragment){
        this.stfuFragment=stfuFragment;
    }

    public void onEvent(MessageEvent event){

    }
    public void onEventMainThread(MessageEvent event){
        Message message=event.getMessage();
        UserInfo userInfo=message.getFromUser();
        final Conversation conversation= JMessageClient.getSingleConversation(userInfo.getUserName());
        if(!userInfo.getUserName().equals(STFUConfig.manager_username)){
            if(chattingFragment!=null){
                //此时正在聊天界面中
                List<Message> messages=conversation.getAllMessage();
                Logger.i("获取了消息");
                chattingFragment.setConversation_Messages_Position(conversation,messages,messages.size()-conversation.getUnReadMsgCnt());
            }else if(conversationListFragment!=null){
                //此时正在私信列表界面中
                List<Conversation> conversations=JMessageClient.getConversationList();
                conversationListFragment.setAdapter(conversations);
            }else if(stfuFragment!=null){
                //修改为红点图标
                stfuFragment.set_Message_point_icon(1);
            }
        }

    }

}
