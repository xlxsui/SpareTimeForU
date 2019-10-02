package com.sparetimeforu.android.sparetimeforu.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.activity.post.IdleThingPostActivity;
import com.sparetimeforu.android.sparetimeforu.entity.IdleThing;
import com.sparetimeforu.android.sparetimeforu.entity.Study;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/14.
 */

public class IdleThingAdapter extends BaseQuickAdapter<IdleThing, BaseViewHolder> {
    private String url;
    public IdleThingAdapter(List<IdleThing> idleThings) {

        super(R.layout.item_idle_thing, idleThings);
        url= STFUConfig.HOST+"/static/idle_pictures";
        this.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, IdleThingPostActivity.class);
            mContext.startActivity(intent);
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, IdleThing item) {
        ImageView avatar=helper.getView(R.id.idle_thing_avatar);
        TextView nick_name=helper.getView(R.id.idle_thing_nick_name);
        nick_name.setText(item.getUser_Nickname());
        TextView caption=helper.getView(R.id.idle_thing_caption);
        caption.setText(item.getContent());
        ImageView picture1=helper.getView(R.id.idle_thing_picture1);
        ImageView picture2=helper.getView(R.id.idle_thing_picture2);
        ImageView picture3=helper.getView(R.id.idle_thing_picture3);
        TextView date=helper.getView(R.id.idle_thing_date);
        date.setText(item.getRelease_time());
        TextView price=helper.getView(R.id.idle_thing_price);
        price.setText("￥"+item.getMoney());


        Picasso.get()
                .load(url+"/"+item.getUser_Avatar())
                .resize(200, 200)
                .centerCrop()
                .into(avatar);
        Picasso.get()
                .load(url+"/"+item.getPicture_url_1())
                .resize(200, 200)
                .centerCrop()
                .into(picture1);
        Picasso.get()
                .load(url+"/"+item.getPicture_url_2())
                .resize(200, 200)
                .centerCrop()
                .into(picture2);
        Picasso.get()
                .load(url+"/"+item.getPicture_url_3())
                .resize(200, 200)
                .centerCrop()
                .into(picture3);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入他人界面

            }
        });
    }
}
