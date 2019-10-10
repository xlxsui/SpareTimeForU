package com.sparetimeforu.android.sparetimeforu.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * SpareTimeForU
 * Created by Jin on 2019/10/8.
 * Email:17wjli6@stu.edu.cn
 */
public class SendPostImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SendPostImageAdapter(@Nullable List<String> data) {
        super(R.layout.send_post_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView imageView = ((ImageView) helper.getView(R.id.post_image));
        Picasso.get().load(item).into(imageView);
    }
}
