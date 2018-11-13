package com.sparetimeforu.android.sparetimeforu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.data.DataServer;
import com.sparetimeforu.android.sparetimeforu.entity.Status;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by Jin on 2018/11/13.
 */

public class ErrandAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {

    public ErrandAdapter() {
        super(R.layout.errand_item, DataServer.getSampleData(20));
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
        Context context = ((ImageView) helper.getView(R.id.errand_avatar)).getContext();
        ImageView avatar = (ImageView) helper.getView(R.id.errand_avatar);

        try {
            Random random = new Random();
            int i = random.nextInt(11) + 1;
            InputStream ims = context.getAssets().open("avatar/ic_avatar" + i + ".jpg");
            Drawable drawable = Drawable.createFromStream(ims, null);
            avatar.setImageDrawable(drawable);

        } catch (IOException e) {
            Log.e(TAG, "convert: ", e);
        }


    }
}
