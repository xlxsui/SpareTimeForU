package com.sparetimeforu.android.sparetimeforu.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.BuildConfig;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.activity.FriendActivity;
import com.sparetimeforu.android.sparetimeforu.activity.LoginActivity;
import com.sparetimeforu.android.sparetimeforu.activity.PersonalActivity;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.activity.ConversationListActivity;
import com.sparetimeforu.android.sparetimeforu.fragment.module.ErrandFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.module.IdleThingFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.module.SearchThingFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.module.StudyFragment;
import com.sparetimeforu.android.sparetimeforu.entity.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import Listener.GlobalEventListener;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;

import static java.lang.System.exit;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/2.
 */

public class STFUFragment extends Fragment {
    private Toolbar mToolbar;// 工具栏
    private FrameLayout mMainFragmentLayout;// 放置四个fragment的布局
    private Fragment mErrandFragment;
    private Fragment mIdleThingFragment;
    private Fragment mStudyFragment;
    private Fragment mSearchThingFragment;
    private BottomNavigationView mBottomNavigationView;// 底部导航栏
    private DrawerLayout mDrawerLayout;// 侧滑栏布局
    private NavigationView mDrawerNavigationView;// 侧滑拦view
    private TextView slider_menu_signature, slider_menu_nick_name;// 个性签名、昵称
    private MenuItem mLoginMenuItem;// 登陆/注销选项
    private ImageView mAvatar;
    private ImageView mBGImageView;
    private LinearLayout mHeaderLinearLayout;
    private ImageView slideIcon;

    FragmentManager mFm;
    Account mAccount;

    private static final String CURRENT_BOTTOM_ITEM = "current_bottom_item";
    private static final int REQUEST_CODE_LOGIN = 0;


    public static STFUFragment newInstance() {
        return new STFUFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        JMessageClient.init(getContext());
        final View view = inflater.inflate(R.layout.fragment_stfu, container, false);
        ButterKnife.bind(this, view);
        mFm = getActivity().getSupportFragmentManager();


        /**
         * 工具栏及其组件
         */
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        TextView title = (TextView) view.findViewById(R.id.title_top);
        title.setText("汕大顺手邦");
        slideIcon = (ImageView) view.findViewById(R.id.menu_slide_icon);
        slideIcon.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        ImageView searchIcon = (ImageView) view.findViewById(R.id.menu_search_icon);
        searchIcon.setOnClickListener(v -> Toast.makeText(getActivity(), "Spare time for you!",
                Toast.LENGTH_SHORT)
                .show());

        /**
         * 主片段及其布局
         */
        mMainFragmentLayout = (FrameLayout) view.findViewById(R.id.main_fragment);

        /**
         * 设置底部导航
         */
        mBottomNavigationView =
                (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.navigation_errand:
                            loadErrandFragment();
                            return true;
                        case R.id.navigation_second_hand:
                            loadIdleThingFragment();
                            return true;
                        case R.id.navigation_study:
                            loadStudyFragment();
                            return true;
                        case R.id.navigation_search:
                            loadSearchThingFragment();
                            return true;
                    }
                    return false;

                });


        /**
         * 设置侧滑栏
         */

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mBGImageView = new ImageView(getContext());

        mDrawerNavigationView = (NavigationView) view.findViewById(R.id.slider_menu);
        mDrawerNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.slider_menu_task:
                    Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case R.id.slider_menu_personal_letter:
                    //点击之后就把图标变回原样
                    set_Message_point_icon(0);
                    //进入消息通知界面
                    Intent intent1=new Intent(getContext(),ConversationListActivity.class);
                    getActivity().startActivity(intent1);
                    break;
                case R.id.slider_menu_login:
                    if (mAccount != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                            AccountManager.get(getContext()).removeAccountExplicitly(mAccount);
                            STFUConfig.sUser=null;
                            mAccount = null;
                            updateViews();
                        }
                    }
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);
                    break;
                case R.id.slider_menu_friend:
                    Intent intent2=new Intent(getContext(), FriendActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.slider_menu_exit:
                    getActivity().finish();
                    getActivity().moveTaskToBack(true);
                    exit(0);
            }

            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        View mDrawerHeaderView = mDrawerNavigationView.getHeaderView(0);
        mHeaderLinearLayout = mDrawerHeaderView.findViewById(R.id.drawer_header_layout);
        mAvatar = (ImageView) mDrawerHeaderView.findViewById(R.id.slider_menu_avatar);
        try {
            Random random = new Random();
            int i = random.nextInt(11) + 1;
            InputStream ims = getActivity().getAssets().open("avatar/ic_avatar" + i + ".jpg");
            Drawable avatar = Drawable.createFromStream(ims, null);
            mAvatar.setImageDrawable(avatar);
        } catch (IOException e) {
            Logger.e("onCreateView: ", e);
        }
        mAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PersonalActivity.class);
            startActivity(intent);
        });

        Menu mDrawerMenuView = mDrawerNavigationView.getMenu();//侧滑栏的菜单
        mLoginMenuItem = mDrawerMenuView.getItem(2);//第三个菜单项


        //初次加载的界面
        if (savedInstanceState == null) {
            loadErrandFragment();
        } else {
            switch (savedInstanceState.getInt(CURRENT_BOTTOM_ITEM)) {
                case R.id.navigation_errand:
                    loadErrandFragment();
                    break;
                case R.id.navigation_second_hand:
                    loadIdleThingFragment();
                    break;
                case R.id.navigation_study:
                    loadStudyFragment();
                    break;
                case R.id.navigation_search:
                    loadSearchThingFragment();
                    break;
                default:
                    loadErrandFragment();
                    break;
            }
        }

        slider_menu_nick_name = (TextView) mDrawerHeaderView.findViewById(R.id.slider_menu_nick_name);
        slider_menu_signature = (TextView) mDrawerHeaderView.findViewById(R.id.slider_menu_signature);


        /*
        *  设置全局GlobalEventListener
        */
        if(STFUConfig.globalEventListener==null){
            STFUConfig.globalEventListener=new GlobalEventListener();
        }
        STFUConfig.globalEventListener.setStfuFragment(this);
        JMessageClient.registerEventReceiver(STFUConfig.globalEventListener);
        init_Message_point_icon();
        return view;
    }


    public void init_Message_point_icon(){
        //获取是否有未读消息
        if(JMessageClient.getAllUnReadMsgCount()>=0){
            set_Message_point_icon(0);
        }else set_Message_point_icon(1);
    }

    //用mode来标识，切换图标
    //mode=0  正常图标
    //mode=1  右上角有红点的图标
    public boolean set_Message_point_icon(int  mode){
        //修改左上角以及侧拉栏的图标，变成右上有红点
        switch (mode){
            case 0:
                mDrawerNavigationView.getMenu().findItem(R.id.slider_menu_personal_letter).setIcon(R.drawable.ic_personal_letter);
                slideIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_slide));
                break;
            case 1:
                mDrawerNavigationView.getMenu().findItem(R.id.slider_menu_personal_letter).setIcon(R.drawable.ic_menu_slide);
                slideIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_personal_letter));
                break;
        }
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();

        AccountManager accountManager = AccountManager.get(getContext());
        Account[] account = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID);

        if (account.length != 0 && STFUConfig.sUser == null) {
            STFUConfig.sUser=new User();
            mAccount = account[0];
            STFUConfig.sUser.setEmail(accountManager.getUserData(account[0], "email"));
            STFUConfig.sUser.setNickname(accountManager.getUserData(account[0], "nickname"));
            STFUConfig.sUser.setAvatar_url(accountManager.getUserData(account[0], "avatar_url"));
            STFUConfig.sUser.setFavourable_rate(accountManager.getUserData(account[0], "favourable_rate"));
            STFUConfig.sUser.setGender(accountManager.getUserData(account[0], "gender"));
            STFUConfig.sUser.setPhone(accountManager.getUserData(account[0], "phone"));
            STFUConfig.sUser.setSignature(accountManager.getUserData(account[0], "signature"));
            STFUConfig.sUser.setBg_url(accountManager.getUserData(account[0], "bg_url"));

            Logger.i(STFUConfig.sUser.toString());
            //设置auth_token
            new GetAuthThread().start();
        }
        if (STFUConfig.sUser != null) {
            updateViews();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //bind the fragment with bottom navigation item
        outState.putInt(CURRENT_BOTTOM_ITEM, mBottomNavigationView.getSelectedItemId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_LOGIN) {
            if (data == null) {
                return;
            }
            STFUConfig.sUser=((User) data.getSerializableExtra("user"));
            AccountManager accountManager = AccountManager.get(getContext());
            Account[] account = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID);
            mAccount = account[0];
            updateViews();
        }
    }

    @OnClick(R.id.fab)
    public void onFABClicked() {
        Toast.makeText(getContext(), "Toast from FAB", Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载Item Fragment到main_fragment布局
     */
    public void loadErrandFragment() {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //把layout生成一个view对象
        inflater.inflate(R.layout.fragment_errand, mMainFragmentLayout);
        mErrandFragment = mFm.findFragmentById(R.id.errand_container);

        if (mErrandFragment == null) {
            //假如fragment为空，这时才重新创建一个，并且提交显示
            mErrandFragment = new ErrandFragment();
        }
        mFm.beginTransaction()
                .replace(R.id.main_fragment, mErrandFragment)
                .commit();
        Logger.d("load errand fragment");
    }

    public void loadIdleThingFragment() {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //把layout生成一个view对象
        inflater.inflate(R.layout.fragment_idle_thing, mMainFragmentLayout);
        mIdleThingFragment = mFm.findFragmentById(R.id.idle_thing_container);
        if (mIdleThingFragment == null) {
            mIdleThingFragment = new IdleThingFragment();
        }
        mFm.beginTransaction()
                .replace(R.id.main_fragment, mIdleThingFragment)
                .commit();
    }

    public void loadStudyFragment() {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //把layout生成一个view对象
        inflater.inflate(R.layout.fragment_study, mMainFragmentLayout);
        mStudyFragment = mFm.findFragmentById(R.id.study_container);
        if (mStudyFragment == null) {
            mStudyFragment = new StudyFragment();
        }

        mFm.beginTransaction()
                .replace(R.id.main_fragment, mStudyFragment)
                .commit();
    }

    public void loadSearchThingFragment() {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_search_thing, mMainFragmentLayout);
        mSearchThingFragment = mFm.findFragmentById(R.id.search_thing_container);
        if (mSearchThingFragment == null) {
            mSearchThingFragment = new SearchThingFragment();
        }
        mFm.beginTransaction()
                .replace(R.id.main_fragment, mSearchThingFragment)
                .commit();
    }

    private void updateViews() {
        if (STFUConfig.sUser.getEmail() != null) {
            slider_menu_nick_name.setText(STFUConfig.sUser.getNickname());
            slider_menu_signature.setText(STFUConfig.sUser.getSignature());
            mLoginMenuItem.setTitle(getString(R.string.logout));
            Picasso.get()
                    .load(STFUConfig.HOST + "/static/avatar/" + STFUConfig.sUser.getAvatar_url())
                    .resize(200, 200)
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(mAvatar);
            //设置背景图片
            Picasso.get()
                    .load(STFUConfig.HOST + "/static/personal_background/" + STFUConfig.sUser.getBg_url())
                    .resize(1920, 1080)
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(mBGImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mBGImageView.getDrawable().setColorFilter(0x33000000, PorterDuff.Mode.SRC_ATOP);
                            mHeaderLinearLayout.setBackground(mBGImageView.getDrawable());
                        }

                        @Override
                        public void onError(Exception e) {
                            Logger.e(e.toString());
                        }
                    });

        } else {
            slider_menu_nick_name.setText(getString(R.string.app_name));
            slider_menu_signature.setText(getString(R.string.spare_time_for_u));
            mLoginMenuItem.setTitle(getString(R.string.login));
            Picasso.get()
                    .load("file:///android_asset/avatar/ic_avatar1.jpg")
                    .resize(200, 200)
                    .centerCrop()
                    .into(mAvatar);
            mHeaderLinearLayout.setBackground(getResources().getDrawable(R.drawable.pink_background));
        }
    }

    class GetAuthThread extends Thread {
        @Override
        public void run() {
            //获取设置，authToken
            AccountManagerFuture<Bundle> future = AccountManager.get(getContext())
                    .getAuthToken(mAccount, "normal",
                            null, false, null, null);
            Bundle result;
            try {
                result = future.getResult();
                String authToken;
                authToken = result.getString(AccountManager.KEY_AUTHTOKEN);
                STFUConfig.sUser.setAuth_token(authToken);
            } catch (Exception e) {
                Logger.e(e.toString());
            }
        }
    }

}





























