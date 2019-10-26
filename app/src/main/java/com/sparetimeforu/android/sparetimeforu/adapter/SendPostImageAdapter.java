package com.sparetimeforu.android.sparetimeforu.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.activity.PictureActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.sparetimeforu.android.sparetimeforu.fragment.post.ErrandPostFragment.PHOTO_URL;

/**
 * SpareTimeForU
 * Created by Jin on 2019/10/8.
 * Email:17wjli6@stu.edu.cn
 */
public class SendPostImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SendPostImageAdapter(@Nullable List<String> data) {
        super(R.layout.send_post_image, data);
        this.setOnItemChildClickListener((BaseQuickAdapter adapter, View v, int position) -> {
            switch (v.getId()) {
                case R.id.post_image:
                    Intent intent = new Intent(mContext, PictureActivity.class);
                    intent.putExtra(PHOTO_URL, data.get(position));
                    mContext.startActivity(intent);
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.post_image);
        ImageView imageView = ((ImageView) helper.getView(R.id.post_image));
        Picasso.get().load(item).into(imageView);
    }
}
