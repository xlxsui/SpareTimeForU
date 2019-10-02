package com.sparetimeforu.android.sparetimeforu.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.activity.post.ErrandPostActivity;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.squareup.picasso.Picasso;


import java.io.InputStream;
import java.util.List;
import java.util.Random;

/**
 * Created by Jin on 2018/11/13.
 * <p>
 * SpareTimeForU
 */

public class ErrandAdapter extends BaseQuickAdapter<Errand, BaseViewHolder> {
    private String url;

    public ErrandAdapter(List<Errand> errands) {
        super(R.layout.item_errand, errands);

        url= STFUConfig.HOST+"/static/errand_picture";
        ErrandAdapter.this.setOnItemClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemClick: You clicked me");
            Toast.makeText(mContext, "You clicked the item: " + position, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, ErrandPostActivity.class);
            mContext.startActivity(intent);
        });

        ErrandAdapter.this.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.errand_avatar:
                    Toast.makeText(mContext, "You clicked the avatar! ", Toast.LENGTH_SHORT).show();
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
        TextView grade = ((TextView) helper.getView(R.id.errand_grade));
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
//        Picasso.get()
//                .load("file:///android_asset/avatar/ic_avatar" + i + ".jpg")
//                .into(avatar);

//        String str1 = "帮我去近邻宝拿个小姐姐！谢谢啦";
//        String str2 = "去二饭帮忙打包一份猪脚饭！顺便加饭";
//        String str3 = "有没有小哥哥或者小姐姐帮我去东门买两斤apple！辛苦啦";
//        String str4 = "我感冒了，浑身没力，好难受，宿舍又没人。谁能帮我去校医院拿点感冒药，十分感谢";
//        String str5 = "求一位小哥哥帮我去近邻宝抬个大包裹回来，谢谢啦";
//        i = random.nextInt(5) + 1;
//        switch (i) {
//            case 1:
//                caption.setText(str1);
//                break;
//            case 2:
//                caption.setText(str2);
//                break;
//            case 3:
//                caption.setText(str3);
//                break;
//            case 4:
//                caption.setText(str4);
//                break;
//            case 5:
//                caption.setText(str5);
//                break;
//        }

        Picasso.get()
                .load(url+"/"+item.getUser_Avatar())
                .resize(200, 200)
                .centerCrop()
                .into(avatar);
        Picasso.get()
                .load(url+"/"+item.getPicture_url_1())
                .resize(200, 200)
                .centerCrop()
                .into(p1);
        Picasso.get()
                .load(url+"/"+item.getPicture_url_2())
                .resize(200, 200)
                .centerCrop()
                .into(p2);
        Picasso.get()
                .load(url+"/"+item.getPicture_url_3())
                .resize(200, 200)
                .centerCrop()
                .into(p3);
    }

}
