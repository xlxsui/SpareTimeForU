package com.sparetimeforu.android.sparetimeforu.fragment.module;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.baidu.mapapi.animation.Animation;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.animation.Transformation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.adapter.ErrandAdapter;
import com.sparetimeforu.android.sparetimeforu.data.DataServer;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.util.ErrandDataBaseUtil;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

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
    private String destination_location;//用来标识请求的地点
    private static String GetErrandServerUrl = STFUConfig.HOST + "/mission/refresh_newest";
    private Activity activityForUi;//请求失败事用于更新UI
    private int origin=0;//初始化为任务数据库中最大的任务id

    public RequestErrand(RequestErrandCallBack callBack,String location,Activity activity) {
        mCallBack = callBack;
        mHandler = new Handler(Looper.getMainLooper());
        destination_location=location;
        activityForUi=activity;
    }

    @Override
    public void run() {
        //网络请求数据并返回
                //请求网络数据
        FormBody body=new FormBody.Builder()
                .add("destination",destination_location)
                //.add("last_errand_id",get_Last_Errand_Id());
                .add("biggest_id", 1+"")
                        .build();
        origin=0;//每次获取完数据库更新的任务数据，origin都设置为0，重新开始从数据库中拿数据
        OkHttpUtil.sendOkHttpPostRequest(GetErrandServerUrl, body, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        activityForUi.runOnUiThread(() -> Toast.makeText(activityForUi,
                                "无法获取任务信息，请检查网络是否正常", Toast.LENGTH_SHORT).show());
                        mCallBack.fail(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        //获取新数据成功，将数据加入数据库
                        HandleMessageUtil.handleErrandMessage(response.body().string());
                        //获取数据库中最新的任务返回并显示给用户
                        final List<Errand> errand_data = ErrandDataBaseUtil.getErrand_data(origin,destination_location);
                        //把orgin更新
                        if(errand_data.size()!=0)
                        origin+=errand_data.size();
                        mCallBack.success((errand_data));
                    }
                });


    }
}


/**
 * 跑腿的Fragment
 */
public class ErrandFragment extends Fragment implements BaiduMap.OnMarkerClickListener{
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
    //宿舍坐标经纬度
    private double[][] MarkerMessage={{23.418345,116.644475},{23.417967,116.644713},{23.422602,116.638519},{23.422224,116.638982},
        {23.422291,116.638829},{23.417553,116.635007},{23.417714,116.634895},{23.417142,116.640096}};
    //宿舍名称
    private String[] MarkerName={"德馨","至诚","明德","敬一","修远","知行","思源","弘毅"};
    private static final int MarkerNum = 8;
    private String destination_location="随机";//网络请求任务终点，初始为"随机"

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
        LitePal.initialize(getContext());
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

        setUpMarker();

//注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
//开启地图定位图
        mLocationClient.start();
    }


    public void  setUpMarker(){
        //构建marker图标
        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.marker_icon);
        Matrix matrix=new Matrix();
        matrix.postScale(0.16f,0.16f);
        bm=Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,true);
        BitmapDescriptor bitmap=BitmapDescriptorFactory.fromBitmap(bm);
        for(int i=0;i<MarkerNum;i++){
            Bundle bundle=new Bundle();
            bundle.putString("MarkerName",MarkerName[i]);
            //定义Maker坐标点
            LatLng point = new LatLng(MarkerMessage[i][0],MarkerMessage[i][1]);//经纬度设置
//构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option1 = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
//在地图上添加Marker，并显示
            Marker marker=(Marker)mBaiduMap.addOverlay(option1);
            marker.setExtraInfo(bundle);
            Animation animation=new Transformation(new LatLng(MarkerMessage[i][0]+0.0003,MarkerMessage[i][1]));
            animation.setDuration(1000);
            marker.setAnimation(animation);
            marker.startAnimation();
        }
        mBaiduMap.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Bundle bundle=marker.getExtraInfo();
        String marker_name=bundle.getString("MarkerName");
        destination_location =marker_name;
        int i;
        for(i=0;i<MarkerNum;i++){
            if(marker_name.equals(MarkerName[i]))
                break;
        }
        //动画效果
        Animation animation=new Transformation(new LatLng(MarkerMessage[i][0]+0.0003,MarkerMessage[i][1]));
        animation.setDuration(1000);
        marker.setAnimation(animation);
        marker.startAnimation();
        Toast.makeText(getContext(),"您点击了"+marker_name+"，切换地图中",Toast.LENGTH_SHORT).show();
        //刷新任务列表，制定终点为 marker_name
        refresh();
        Animation animation1=new Transformation(new LatLng(MarkerMessage[i][0],MarkerMessage[i][1]));
        animation1.setDuration(500);
        marker.setAnimation(animation1);
        marker.startAnimation();
        return false;
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
                .setTitle("定位权限不可用")
                .setMessage("由于此应用需要用到地图功能，请同意使用定位权限\n否则，您将无法正常使用顺手邦")
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

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }



    private void initRefreshLayout() {
        //下拉刷新部分
        mErrandRefreshLayout.setOnRefreshListener(() -> refresh());
        //上拉刷新部分
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int last_visible_item=((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int total_item_num=((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();
                Log.i("test2",last_visible_item+"");
                if(last_visible_item>=total_item_num){
                    loadMore();//获取更多任务
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void refresh() {
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        new RequestErrand(new RequestErrandCallBack() {
            @Override
            public void success(List<Errand> data) {

                //do something

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupAdapter(data);
                        Toast.makeText(getActivity(),"获取数据成功",Toast.LENGTH_SHORT).show();
                        mAdapter.setEnableLoadMore(true);
                        mErrandRefreshLayout.setRefreshing(false);
                        Snackbar.make(getView(), "Refresh finished! ",Snackbar.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void fail(Exception e) {
                Snackbar.make(getView(), "Network error! ",Snackbar.LENGTH_SHORT).show();
                mAdapter.setEnableLoadMore(true);
                mErrandRefreshLayout.setRefreshing(false);
            }
        },destination_location,getActivity()).start();
    }
    private void loadMore(){
        List<Errand> errands= ErrandDataBaseUtil.getErrand_data(mAdapter.getItemCount(),destination_location);
        if(errands==null){
            Toast.makeText(getContext(),"已经加载完毕,没有更多任务了",Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<errands.size();i++){
                mAdapter.addData(errands.get(i));
            }
        }
    }

    @Override
    public void onResume() {
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
                        if(marginTop>1000) marginTop=1000;
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

