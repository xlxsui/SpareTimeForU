package com.sparetimeforu.android.sparetimeforu.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.entity.IdleThing;
import com.sparetimeforu.android.sparetimeforu.entity.Study;

import java.util.List;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/14.
 */

public class IdleThingAdapter extends BaseQuickAdapter<IdleThing, BaseViewHolder> {
    public IdleThingAdapter(List<IdleThing> idleThings) {
        super(R.layout.item_idle_thing, idleThings);
    }

    @Override
    protected void convert(BaseViewHolder helper, IdleThing item) {

    }
}
