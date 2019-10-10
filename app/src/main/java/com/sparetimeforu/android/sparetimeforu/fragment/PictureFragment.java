package com.sparetimeforu.android.sparetimeforu.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Timer;

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

        mPhotoView.setOnLongClickListener(v -> {
            File file = getFileFromImageView(mPhotoView);

            //刷新画廊，并查看那里的图像
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            getActivity().sendBroadcast(intent);
            return true;
        });

        return view;
    }

    public File getFileFromImageView(ImageView iv) {
        BitmapDrawable draw = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = draw.getBitmap();

        File dir = new File(Environment.getExternalStorageDirectory() +
                "/SpareTimeForU/STFU");
        dir.mkdirs();

        Date date = new Date();
        File picture = new File(dir,
                date.toString().replace(" ", "_") + ".jpeg");

        FileOutputStream outStream = null;

        try {
            outStream = new FileOutputStream(picture);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            Toast.makeText(getActivity(), "图片保存成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Logger.e(e.toString());
        }

        return picture;

    }

}
