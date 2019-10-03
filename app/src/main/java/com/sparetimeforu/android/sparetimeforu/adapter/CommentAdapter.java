package com.sparetimeforu.android.sparetimeforu.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.chrisbanes.photoview.PhotoView;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
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

    private String url;
    public CommentAdapter(@Nullable List<Comment> data) {
        super(R.layout.post_comment, data);
        url= STFUConfig.HOST+"/static";
        CommentAdapter.this.setOnItemChildClickListener((adapter, v, position) -> {
            switch (v.getId()) {
                case R.id.comment_avatar:
                    Toast.makeText(mContext, "Avatar!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        TextView user_nickname=helper.getView(R.id.comment_nick_name);
        TextView date=helper.getView(R.id.comment_date);
        ImageView user_avatar=helper.getView(R.id.comment_avatar);
        TextView comment_floor=helper.getView(R.id.comment_floor);
        TextView caption =helper.getView(R.id.comment_caption);

        user_nickname.setText(item.getUser_nickname());
        date.setText(item.getTime());
        comment_floor.setText("#"+(helper.getPosition()+1));
        caption.setText(item.getContent());

        Picasso.get().load(url+"/avatar/"+item.getUser_avatar()).resize(30,30).centerCrop().into(user_avatar);
    }
}
