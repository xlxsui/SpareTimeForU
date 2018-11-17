package com.sparetimeforu.android.sparetimeforu.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;

import java.util.List;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/17.
 */

public class SearchThingAdapter extends BaseQuickAdapter<SearchThing, BaseViewHolder> {
    public SearchThingAdapter(List<SearchThing> searchThings) {
        super(R.layout.item_search_thing, searchThings);

    }

    @Override
    protected void convert(BaseViewHolder helper, SearchThing item) {

    }


}
