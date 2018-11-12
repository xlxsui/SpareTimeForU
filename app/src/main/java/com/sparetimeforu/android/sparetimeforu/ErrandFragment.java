package com.sparetimeforu.android.sparetimeforu;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sparetimeforu.android.sparetimeforu.recycler.view.item.Errand;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private SwipeRefreshLayout errand_flashlayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("Errand", "-----Create the errand fragment View.");
        View view = inflater.inflate(R.layout.fragment_errand_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_errand_main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();

        /**
         *refresh Layout
         */
        errand_flashlayout = (SwipeRefreshLayout) view.findViewById(R.id.errand_reflash_layout);
        errand_flashlayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);


        errand_flashlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //get data refresh UI


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        errand_flashlayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        return view;
    }

    private void setupAdapter() {

        List<Errand> errands = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
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
        private static final String TAG = "ErrandHolder";

        private Errand mErrand;
        private TextView mErrandCaption;
        private ImageView mAvatar;


        public ErrandHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.errand_item, parent, false));
            Log.d("Errand", "-----Creating ViewHolder now");
            mErrandCaption = (TextView) itemView.findViewById(R.id.errand_caption);
            mAvatar = (ImageView) itemView.findViewById(R.id.errand_avatar);
        }

        public void bind(Errand errand) {
            mErrand = errand;
            mErrandCaption.setText(mErrand.getUUID().toString());

            try {
                Random random = new Random();
                int i = random.nextInt(11) + 1;
                InputStream ims = getActivity().getAssets().open("avatar/ic_avatar" + i + ".jpg");
                Drawable avatar = Drawable.createFromStream(ims, null);
                mAvatar.setImageDrawable(avatar);

            } catch (IOException e) {
                Log.e(TAG, "bind: ", e);
            }


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





















