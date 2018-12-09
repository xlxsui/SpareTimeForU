package com.sparetimeforu.android.sparetimeforu.adapter;

import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;

import java.util.List;

/**
 * Created by HQY on 2018/12/2.
 */

public class ErrandReceivedAdapter extends BaseQuickAdapter<Errand, BaseViewHolder> {

    public ErrandReceivedAdapter(List<Errand> errands){
        super(R.layout.mission_received_item,errands);
        ErrandReceivedAdapter.this.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(mContext,"你点击了这个任务"+position,Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void convert(BaseViewHolder helper, Errand errand) {

    }
}
