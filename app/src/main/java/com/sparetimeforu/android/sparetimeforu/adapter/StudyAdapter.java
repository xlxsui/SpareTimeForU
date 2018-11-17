package com.sparetimeforu.android.sparetimeforu.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.entity.Study;

import java.util.List;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/17.
 */

public class StudyAdapter extends BaseQuickAdapter<Study, BaseViewHolder> {
    public StudyAdapter(@Nullable List<Study> studies) {
        super(R.layout.item_study, studies);

    }

    @Override
    protected void convert(BaseViewHolder helper, Study item) {

    }
}
