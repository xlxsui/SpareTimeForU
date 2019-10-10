package com.sparetimeforu.android.sparetimeforu.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.activity.OthersPersonalActivity;
import com.sparetimeforu.android.sparetimeforu.activity.post.ErrandPostActivity;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.squareup.picasso.Picasso;


import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Jin on 2018/11/13.
 * <p>
 * SpareTimeForU
 */

public class ErrandAdapter extends BaseQuickAdapter<Errand, BaseViewHolder> {
    Activity activity;
    private String url;

    public ErrandAdapter(List<Errand> errands,Activity activity) {
        super(R.layout.item_errand, errands);
        this.activity=activity;

        url= STFUConfig.HOST+"/static";
        ErrandAdapter.this.setOnItemClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemClick: You clicked me");
            Toast.makeText(mContext, "You clicked the item: " + position, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, ErrandPostActivity.class);
            //intent.putExtra("errand_id",((Errand)adapter.getItem(position)).getErrand_id());
            intent.putExtra("errand_id",502);
            mContext.startActivity(intent);
        });

        ErrandAdapter.this.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.errand_avatar:
                    //进入他人界面
                    Toast.makeText(mContext, "You clicked the avatar! ", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(activity,OthersPersonalActivity.class);
                    intent.putExtra("user_Email",errands.get(position).getUser_Email());
                    activity.startActivity(intent);
                    break;
                case R.id.errand_share:
                    Toast.makeText(mContext, "You clicked the share! ", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.errand_like:
                    Toast.makeText(mContext, "You clicked the like! ", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.errand_comment:
                    Toast.makeText(mContext, "You clicked the comment! ", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

    }

    @Override
    protected void convert(BaseViewHolder helper, Errand item) {
        ImageView avatar = (ImageView) helper.getView(R.id.errand_avatar);
        TextView nickName = (TextView) helper.getView(R.id.errand_nick_name);
        nickName.setText(item.getUser_Nickname());
        TextView date = ((TextView) helper.getView(R.id.errand_date));
        TextView reward = ((TextView) helper.getView(R.id.errand_reward));
        reward.setText(item.getMoney()+"");
        TextView origin = ((TextView) helper.getView(R.id.errand_origin));
        origin.setText(item.getOrgin());
        TextView destination = ((TextView) helper.getView(R.id.errand_destination));
        destination.setText(item.getDestination());
        TextView deadline = ((TextView) helper.getView(R.id.errand_deadline));
        deadline.setText(item.getEnd_time());
        TextView caption = ((TextView) helper.getView(R.id.errand_caption));
        caption.setText(item.getContent());
        ImageView p1 = (ImageView) helper.getView(R.id.errand_picture1);
        ImageView p2 = (ImageView) helper.getView(R.id.errand_picture2);
        ImageView p3 = (ImageView) helper.getView(R.id.errand_picture3);
        ImageView share = (ImageView) helper.getView(R.id.errand_share);
        ImageView like = (ImageView) helper.getView(R.id.errand_like);
        Button button=(Button)helper.getView(R.id.Errand_accpet_mission);

        LinearLayout comment = (LinearLayout) helper.getView(R.id.errand_comment);

        helper.addOnClickListener(R.id.errand_avatar)
                .addOnClickListener(R.id.errand_share)
                .addOnClickListener(R.id.errand_like)
                .addOnClickListener(R.id.errand_comment);


        //set avatar img
        InputStream ims;
        Drawable drawable;
        reward.setText(item.getMoney()+"");
       date.setText(item.getRelease_time());
       nickName.setText(item.getUser_Nickname());

        Picasso.get()
                .load(url+"/avatar/"+item.getUser_Avatar())
                .resize(30, 30)
                .centerCrop()
                .into(avatar);
        Picasso.get()
                .load(url+"/mission_pictures/"+item.getPicture_url_1())
                .resize(200, 200)
                .centerCrop()
                .into(p1);
        Picasso.get()
                .load(url+"/mission_pictures/"+item.getPicture_url_2())
                .resize(200, 200)
                .centerCrop()
                .into(p2);
        Picasso.get()
                .load(url+"/mission_pictures/"+item.getPicture_url_3())
                .resize(200, 200)
                .centerCrop()
                .into(p3);

        if(item.getIs_received()==1) {
            button.setVisibility(View.INVISIBLE);
        }else{
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //发送请求到服务器  用户接收任务
                }
            });
        }

    }

}
