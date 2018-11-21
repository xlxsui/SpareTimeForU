package com.sparetimeforu.android.sparetimeforu.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.chrisbanes.photoview.PhotoView;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.activity.PictureActivity;
import com.sparetimeforu.android.sparetimeforu.entity.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/21.
 * Email:17wjli6@stu.edu.cn
 */

public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    public CommentAdapter(@Nullable List<Comment> data) {
        super(R.layout.post_comment, data);
        CommentAdapter.this.setOnItemChildClickListener((adapter, v, position) -> {
            switch (v.getId()) {
                case R.id.comment_avatar:
                    Toast.makeText(mContext, "Avatar!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.comment_picture1:
                    Toast.makeText(mContext, "You clicked: comment_picture1 " + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, PictureActivity.class);
                    mContext.startActivity(intent);
                    break;
                case R.id.comment_picture2:
                    Toast.makeText(mContext, "You clicked: comment_picture2 " + position, Toast.LENGTH_SHORT).show();
                    intent = new Intent(mContext, PictureActivity.class);
                    mContext.startActivity(intent);
                    break;
                case R.id.comment_picture3:
                    Toast.makeText(mContext, "You clicked: comment_picture3 " + position, Toast.LENGTH_SHORT).show();
                    intent = new Intent(mContext, PictureActivity.class);
                    mContext.startActivity(intent);
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        helper.addOnClickListener(R.id.comment_picture1)
                .addOnClickListener(R.id.comment_picture2)
                .addOnClickListener(R.id.comment_picture3)
                .addOnClickListener(R.id.comment_avatar);
        PhotoView p1 = (PhotoView) helper.getView(R.id.comment_picture1);
        PhotoView p2 = (PhotoView) helper.getView(R.id.comment_picture2);
        PhotoView p3 = (PhotoView) helper.getView(R.id.comment_picture3);
        Picasso.get().load("http://pic1.16pic.com/00/14/81/16pic_1481517_b.jpg").into(p1);
        Picasso.get().load("http://img.besoo.com/file/201809/11/iyphhu1jqpa.jpg").into(p2);
        Picasso.get().load("http://up.enterdesk.com/edpic/a5/d7/14/a5d7143d5de91533f46c618e5bd7ef78.jpg").into(p3);

    }
}
