package com.sparetimeforu.android.sparetimeforu.fragment.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.chrisbanes.photoview.PhotoView;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.activity.PictureActivity;
import com.sparetimeforu.android.sparetimeforu.adapter.CommentAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.PostImageAdapter;
import com.sparetimeforu.android.sparetimeforu.entity.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/18.
 * Email:17wjli6@stu.edu.cn
 */

public class ErrandPostFragment extends Fragment {
    private static final String TAG = "ErrandPostFragment";
    public static final String PHOTO_URL = "photo_url";

    @BindView(R.id.comment_list)
    RecyclerView mCommentList;
    @BindView(R.id.post_image_list)
    RecyclerView mPostImageList;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static ErrandPostFragment newInstance() {
        return new ErrandPostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_errand, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mPostImageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        String[] strings = {"http://pic1.16pic.com/00/14/81/16pic_1481517_b.jpg",
                "http://up.enterdesk.com/edpic/a5/d7/14/a5d7143d5de91533f46c618e5bd7ef78.jpg",
                "http://img.besoo.com/file/201809/11/iyphhu1jqpa.jpg"};
        List<String> urls = new ArrayList<>();
        urls.addAll(Arrays.asList(strings));
        PostImageAdapter postImageAdapter = new PostImageAdapter(urls);
        ViewCompat.setNestedScrollingEnabled(mPostImageList, false);
        mPostImageList.setAdapter(postImageAdapter);

        mCommentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Comment comment = new Comment();
            comments.add(comment);
        }
        CommentAdapter commentAdapter = new CommentAdapter(comments);
        ViewCompat.setNestedScrollingEnabled(mCommentList, false);
        mCommentList.setAdapter(commentAdapter);


        return view;
    }

    @OnClick(R.id.menu_back_icon)
    public void back() {
        Log.i(TAG, "onCreateView: ");
        getActivity().finish();
    }

}
