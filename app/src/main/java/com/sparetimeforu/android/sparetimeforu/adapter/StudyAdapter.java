package com.sparetimeforu.android.sparetimeforu.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.activity.post.StudyPostActivity;
import com.sparetimeforu.android.sparetimeforu.entity.Study;

import java.util.List;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/17.
 */

public class StudyAdapter extends BaseQuickAdapter<Study, BaseViewHolder> {
    public StudyAdapter(@Nullable List<Study> studies) {
        super(R.layout.item_study, studies);

        this.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, StudyPostActivity.class);
            mContext.startActivity(intent);
        });

    }

    @Override
    protected void convert(BaseViewHolder helper, Study item) {

    }
}
