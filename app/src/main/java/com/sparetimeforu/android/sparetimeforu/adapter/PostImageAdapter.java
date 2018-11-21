package com.sparetimeforu.android.sparetimeforu.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.chrisbanes.photoview.PhotoView;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.activity.PictureActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.sparetimeforu.android.sparetimeforu.fragment.post.ErrandPostFragment.PHOTO_URL;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/21.
 * Email:17wjli6@stu.edu.cn
 */

public class PostImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public PostImageAdapter(@Nullable List<String> data) {
        super(R.layout.post_image, data);
        this.setOnItemChildClickListener((BaseQuickAdapter adapter, View v, int position) -> {
            switch (v.getId()) {
                case R.id.post_image:
                    Toast.makeText(mContext, "You clicked: " + position, Toast.LENGTH_SHORT).show();
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
        PhotoView photoView = ((PhotoView) helper.getView(R.id.post_image));
        Picasso.get().load(item)
                .into(photoView);

    }
}