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
import com.sparetimeforu.android.sparetimeforu.activity.post.LostAndFoundPostActivity;
import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/17.
 */

public class LostAndFoundAdapter extends BaseQuickAdapter<SearchThing, BaseViewHolder> {
    private String url;
    public LostAndFoundAdapter(List<SearchThing> searchThings) {
        super(R.layout.item_search_thing, searchThings);
        url= STFUConfig.HOST+"/static/search_pictures";
        this.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, LostAndFoundPostActivity.class);
            mContext.startActivity(intent);
        });

    }

    @Override
    protected void convert(BaseViewHolder helper, SearchThing item) {
        ImageView avatar=helper.getView(R.id.search_thing_avatar);
        TextView nick_name=helper.getView(R.id.search_thing_nick_name);
        nick_name.setText(item.getUser_Nickname());
        TextView caption=helper.getView(R.id.search_thing_caption);
        caption.setText(item.getContent());
        ImageView picture1=helper.getView(R.id.search_thing_picture1);
        ImageView picture2=helper.getView(R.id.search_thing_picture2);
        ImageView picture3=helper.getView(R.id.search_thing_picture3);
        TextView date=helper.getView(R.id.search_thing_date);
        date.setText(item.getRelease_time());
        TextView is_found=helper.getView(R.id.search_thing_status);
        if(item.isIs_found()==1) is_found.setText("已找到");
        else is_found.setText("未找到");

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
