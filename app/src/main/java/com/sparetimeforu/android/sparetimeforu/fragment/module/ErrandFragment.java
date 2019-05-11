package com.sparetimeforu.android.sparetimeforu.fragment.module;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.adapter.ErrandAdapter;
import com.sparetimeforu.android.sparetimeforu.data.DataServer;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/3.
 */


interface RequestErrandCallBack {
    void success(List<Errand> data);

    void fail(Exception e);
}

class RequestErrand extends Thread {
    private RequestErrandCallBack mCallBack;
    private Handler mHandler;


    public RequestErrand(RequestErrandCallBack callBack) {
        mCallBack = callBack;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        Random random = new Random();
        int i = random.nextInt(5);
        switch (i) {
            case 0:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.fail(new RuntimeException("fail"));
                    }
                });
                break;
            case 1:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.success(DataServer.getErrandData(5));
                    }
                });
                break;
            default:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.success(DataServer.getErrandData(5));
                    }
                });
                break;

        }
    }
}


/**
 * 跑腿的Fragment
 */
public class ErrandFragment extends Fragment {
    //have what, do what

    private RecyclerView mRecyclerView;
    private ErrandAdapter mAdapter;
    private List<Errand> mErrands;
    private SwipeRefreshLayout mErrandRefreshLayout;
    private View view;
    private LinearLayout headview;
    //map
    private LocationClient mLocationClient;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    boolean isFirstLoc=true;
    private UiSettings uiSettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        Logger.d("Create the errand fragment View.");
        view = inflater.inflate(R.layout.fragment_errand_new_main, container, false);
        initialize_Map();
        headview=(LinearLayout)view.findViewById(R.id.imageView2);
        headview.setOnTouchListener(new MyDragListener());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_errand_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter(DataServer.getErrandData(2));
        mErrandRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.errand_flash_layout);
        mErrandRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        initRefreshLayout();
        return view;
    }
    private void initialize_Map(){
        getPermission();
        mMapView=(MapView)view.findViewById(R.id.mapView);

        mBaiduMap=mMapView.getMap();
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mBaiduMap.setMyLocationEnabled(true);
        uiSettings=mBaiduMap.getUiSettings();
//通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

//设置locationClientOption
        mLocationClient.setLocOption(option);

        //定义Maker坐标点
        LatLng point = new LatLng(39.963175, 116.400244);
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_33);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option1 = new MarkerOptions()
                .position(point)
                .icon(bitmap);
//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option1);

//注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
//开启地图定位图
        mLocationClient.start();
    }


    private void getPermission(){
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(getActivity(), permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission();
            }
        }
    }

    private void showDialogTipUserRequestPermission(){
        new AlertDialog.Builder(getActivity())
                .setTitle("存储权限不可用")
                .setMessage("由于此应用需要用到地图功能，请同意使用定位权限\n否则，您将无法正常使用支付宝")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }}).setCancelable(false).show();

    }
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(getActivity(), permissions, 321);
    }

    private void setupAdapter(List<Errand> errands) {

        mAdapter = new ErrandAdapter(errands);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.isFirstOnly(false);

        //set main fragment header_errand
//        View view = getLayoutInflater().inflate(R.layout.header_main_fragment,
//                (ViewGroup) mRecyclerView.getParent(), false);
//        ImageView img = (ImageView) view.findViewById(R.id.header_main_fragment);
//        Picasso.get().load(R.drawable.header_errand)
//                .resize(200, 200)
//                .centerCrop()
//                .into(img);
//        mAdapter.addHeaderView(view);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        mErrandRefreshLayout.setOnRefreshListener(() -> refresh());
    }

    private void refresh() {
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        new RequestErrand(new RequestErrandCallBack() {
            @Override
            public void success(List<Errand> data) {
                Snackbar.make(getView(), "Refresh finished! ",Snackbar.LENGTH_SHORT).show();
                //do something
                setupAdapter(data);

                mAdapter.setEnableLoadMore(true);
                mErrandRefreshLayout.setRefreshing(false);
            }

            @Override
            public void fail(Exception e) {
                Snackbar.make(getView(), "Network error! ",Snackbar.LENGTH_SHORT).show();
                mAdapter.setEnableLoadMore(true);
                mErrandRefreshLayout.setRefreshing(false);
            }
        }).start();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        super.onDestroy();
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override

        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            if(isFirstLoc){
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection()).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                isFirstLoc=false;
            }


        }
    }

    private float lastY=0;
    private float marginTop;
    private int count=0;//触发多次touchevent才移动
    private boolean first_touche=true;
    public class MyDragListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    uiSettings.setScrollGesturesEnabled(false);
                    RelativeLayout.LayoutParams lp1=(RelativeLayout.LayoutParams) view.getLayoutParams();
                    if(first_touche){
                        //第一次触摸屏幕获取初始鼠标值
                        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                        DisplayMetrics dm = new DisplayMetrics();
                        wm.getDefaultDisplay().getMetrics(dm);
                        float height=dm.heightPixels;
                        marginTop=lp1.topMargin+height;
                    }else {
                        marginTop=lp1.topMargin;
                    }
                    lastY=motionEvent.getY()+marginTop;
                    Log.i("test1",""+marginTop);
                    break;
                case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                        float currentY;
                        currentY=motionEvent.getY()+marginTop;
                        marginTop+=((currentY-lastY));
                        Log.i("test1","currentY:"+currentY+"lastY:"+lastY);
                        Log.i("test1",""+marginTop);
                        if(marginTop<0) marginTop=0;
                        lp.topMargin=Math.round(marginTop);
                        lastY=currentY;
                        headview.setLayoutParams(lp);
                        count=0;

                    break;
                case MotionEvent.ACTION_UP:
                    uiSettings.setScrollGesturesEnabled(true);
                    lastY=0;
                    if(first_touche)
                    first_touche=false;
                    break;
            }
            return true;
        }
    }
}





















