package com.sparetimeforu.android.sparetimeforu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sparetimeforu.android.sparetimeforu.recycler.view.item.Errand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin on 2018/11/3.
 */


/**
 * 跑腿的Fragment
 */
public class ErrandFragment extends Fragment {
    //have what, do what

    private RecyclerView mRecyclerView;
    private ErrandAdapter mAdapter;
    private List<Errand> mErrands;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("Errand", "-----Create the errand fragment View.");
        View view = inflater.inflate(R.layout.fragment_errand_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_errand_main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();

        return view;
    }

    private void setupAdapter() {

        List<Errand> errands = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            Errand errand = new Errand();
            errands.add(errand);
        }
        mAdapter = new ErrandAdapter(errands);
        Log.d("Errand", "-----attaching errand adapter.");
        mRecyclerView.setAdapter(mAdapter);
        Log.d("Errand", "-----setup adapter finished.");

    }


    /**
     * the errand_item's ViewHolder
     */
    private class ErrandHolder extends RecyclerView.ViewHolder {

        private Errand mErrand;
        private TextView mErrandCaption;


        public ErrandHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.errand_item, parent, false));
            Log.d("Errand", "-----Creating ViewHolder now");
//            mErrandCaption = (TextView) itemView.findViewById(R.id.errand_caption);
        }

        public void bind(Errand errand) {
            mErrand = errand;
//            mErrandCaption.setText(mErrand.getUUID().toString());

        }

    }

    /**
     * errand_recycler_view adapter
     */
    private class ErrandAdapter extends RecyclerView.Adapter<ErrandHolder> {

        public ErrandAdapter(List<Errand> errands) {
            mErrands = errands;
            Log.d("Errand", "-----Creating errand adapter");
        }


        @Override
        public ErrandHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Log.d("Errand", "-----Would like to create ViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ErrandHolder(layoutInflater, parent);
        }

        /**
         * @param holder   current holder
         * @param position ViewHolder's position
         */
        @Override
        public void onBindViewHolder(ErrandHolder holder, int position) {
            Errand errand = mErrands.get(position);
            holder.bind(errand);

        }

        @Override
        public int getItemCount() {
            return mErrands.size();
        }

    }


}





















