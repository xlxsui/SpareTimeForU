package com.sparetimeforu.android.sparetimeforu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.chrisbanes.photoview.PhotoView;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.activity.PictureActivity;
import com.sparetimeforu.android.sparetimeforu.entity.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/18.
 * Email:17wjli6@stu.edu.cn
 */

public class ErrandPostFragment extends Fragment {

    public static final String PHOTO_URL = "photo_url";

    @BindView(R.id.comment_list)
    RecyclerView mCommentList;
    @BindView(R.id.post_image_list)
    RecyclerView mPostImageList;

    public static ErrandPostFragment newInstance() {
        return new ErrandPostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_errand, container, false);
        ButterKnife.bind(this, view);

        mPostImageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        String[] strings = {"http://up.enterdesk.com/edpic/a5/d7/14/a5d7143d5de91533f46c618e5bd7ef78.jpg",
                "http://pic1.16pic.com/00/14/81/16pic_1481517_b.jpg",
                "http://img.besoo.com/file/201809/11/iyphhu1jqpa.jpg",
                "http://pic1.16pic.com/00/14/81/16pic_1481517_b.jpg",
                "http://pic1.16pic.com/00/14/81/16pic_1481517_b.jpg",
                "http://pic1.16pic.com/00/14/81/16pic_1481517_b.jpg",
                "http://pic1.16pic.com/00/14/81/16pic_1481517_b.jpg"};
        List<String> urls = new ArrayList<>();
        urls.addAll(Arrays.asList(strings));
        PostImageAdapter postImageAdapter = new PostImageAdapter(urls);
        postImageAdapter.setOnItemChildClickListener((BaseQuickAdapter adapter, View v, int position) -> {
            switch (v.getId()) {
                case R.id.post_image:
                    Toast.makeText(getActivity(), "You clicked: " + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), PictureActivity.class);
                    intent.putExtra(PHOTO_URL, urls.get(position));
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        });

        mPostImageList.setAdapter(postImageAdapter);

        mCommentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Comment comment = new Comment();
            comments.add(comment);
        }
        CommentAdapter commentAdapter = new CommentAdapter(comments);
        commentAdapter.setOnItemChildClickListener((BaseQuickAdapter adapter, View v, int position) -> {
            switch (v.getId()) {
                case R.id.comment_avatar:
                    Toast.makeText(getActivity(), "Avatar!", Toast.LENGTH_SHORT).show();
                case R.id.comment_picture1:
                    Toast.makeText(getActivity(), "You clicked: comment_picture1 " + position, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.comment_picture2:
                    Toast.makeText(getActivity(), "You clicked: comment_picture2 " + position, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.comment_picture3:
                    Toast.makeText(getActivity(), "You clicked: comment_picture3 " + position, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        });

        mCommentList.setAdapter(commentAdapter);

        return view;
    }


    private class PostImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


        public PostImageAdapter(@Nullable List<String> data) {
            super(R.layout.post_image, data);
            ViewCompat.setNestedScrollingEnabled(mPostImageList, false);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.addOnClickListener(R.id.post_image);
            PhotoView photoView = ((PhotoView) helper.getView(R.id.post_image));
            Picasso.get().load(item)
                    .into(photoView);

        }
    }

    private class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

        public CommentAdapter(@Nullable List<Comment> data) {
            super(R.layout.post_comment, data);
            ViewCompat.setNestedScrollingEnabled(mCommentList, false);
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
}
