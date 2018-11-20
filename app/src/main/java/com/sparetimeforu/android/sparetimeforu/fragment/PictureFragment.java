package com.sparetimeforu.android.sparetimeforu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.sparetimeforu.android.sparetimeforu.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/21.
 * Email:17wjli6@stu.edu.cn
 */

public class PictureFragment extends Fragment {

    @BindView(R.id.picture_details)
    PhotoView mPhotoView;

    public static PictureFragment newInstance(Bundle bundle) {
        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picture_detail, container, false);
        ButterKnife.bind(this, view);
        Picasso.get().load(getArguments().getString("photo_url")).into(mPhotoView);

        mPhotoView.setOnClickListener((View v) -> getActivity().finish());

        return view;
    }
}
