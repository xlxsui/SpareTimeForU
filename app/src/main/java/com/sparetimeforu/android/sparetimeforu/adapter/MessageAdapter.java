package com.sparetimeforu.android.sparetimeforu.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;

import java.util.List;

import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HQY on 2019/7/18.
 */

public class MessageAdapter extends BaseQuickAdapter<Message,BaseViewHolder>{
    public MessageAdapter(List<Message> messages){
        super(R.layout.chat_box,messages);
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
                if (message_text!=null)
                content.setText(message_text);
            }else{
                tc1.setVisibility(View.GONE);
                tc2.setVisibility(View.VISIBLE);
                avatar2.setVisibility(View.GONE);
                avatar1.setVisibility(View.VISIBLE);
                if (message_text!=null)
                content.setText(message_text);
            }
        }
    }
}
