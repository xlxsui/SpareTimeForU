package com.sparetimeforu.android.sparetimeforu.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.activity.ConversationListActivity;
import com.sparetimeforu.android.sparetimeforu.activity.FriendActivity;
import com.sparetimeforu.android.sparetimeforu.activity.LoginActivity;
import com.sparetimeforu.android.sparetimeforu.activity.PersonalActivity;
import com.sparetimeforu.android.sparetimeforu.activity.PersonalMoneyActivity;
import com.sparetimeforu.android.sparetimeforu.activity.post.SendPostActivity;
import com.sparetimeforu.android.sparetimeforu.entity.SystemMessage;
import com.sparetimeforu.android.sparetimeforu.entity.User;
import com.sparetimeforu.android.sparetimeforu.fragment.module.ErrandFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.module.IdleThingFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.module.SearchThingFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.module.StudyFragment;
import com.sparetimeforu.android.sparetimeforu.util.QueryPreferences;
import com.sparetimeforu.android.sparetimeforu.util.VerifyUtil;
import com.sparetimeforu.android.sparetimeforu.util.StatusBarUtils;
import com.sparetimeforu.android.sparetimeforu.util.SystemDataBaseUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.weavey.loading.lib.LoadingLayout;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import Listener.GlobalEventListener;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

import static cn.jpush.im.android.api.JMessageClient.getAllUnReadMsgCount;
import static cn.jpush.im.android.api.JMessageClient.getMyInfo;
import static cn.jpush.im.android.api.JMessageClient.init;
import static cn.jpush.im.android.api.JMessageClient.registerEventReceiver;
import static cn.jpush.im.android.api.JMessageClient.updateUserAvatar;
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
    private LoadingLayout loadingLayout;

    FragmentManager mFm;
    Account mAccount;

    private static final String CURRENT_BOTTOM_ITEM = "current_bottom_item";
    private static final int REQUEST_CODE_LOGIN = 0;
    private long mLastClickTime = 0;
    private final int DOUBLE_CLICK_INTERVAL = 400;


    public static STFUFragment newInstance() {
        return new STFUFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        init(getContext());
        final View view = inflater.inflate(R.layout.fragment_stfu, container, false);
        ButterKnife.bind(this, view);
        mFm = getActivity().getSupportFragmentManager();

        /**
         * 工具栏及其组件
         */
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        TextView title = (TextView) view.findViewById(R.id.title_top);
        title.setText(R.string.app_name);
        title.setOnClickListener((v) -> {
            long currentClickTime = System.currentTimeMillis();
            if (currentClickTime - mLastClickTime <= DOUBLE_CLICK_INTERVAL) {
                // 双击
                switch (mBottomNavigationView.getSelectedItemId()) {
                    case R.id.navigation_errand:
                        loadErrandFragment("");
                        break;
                    case R.id.navigation_second_hand:
                        loadIdleThingFragment("");
                        break;
                    case R.id.navigation_study:
                        loadStudyFragment("");
                        break;
                    case R.id.navigation_search:
                        loadSearchThingFragment("");
                        break;
                }
            } else {
                mLastClickTime = System.currentTimeMillis();
                // 单击
            }


        });
        slideIcon = (ImageView) view.findViewById(R.id.menu_slide_icon);
        setHasOptionsMenu(true);//打开填充菜单，要实现onCreateOptionsMenu方法


        /*
        * 网络加载界面
        * */
        LoadingLayout.getConfig().setErrorText("出错啦~请稍后重试！")
                .setNoNetworkText("无网络连接，请检查您的网络···")
                .setEmptyImage(R.drawable.loading_failed)
                .setNoNetworkImage(R.drawable.loading_failed)
                .setReloadButtonText("点我重试哦")
                .setReloadButtonTextSize(14)
                .setReloadButtonTextColor(R.color.gray_color);



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
                            loadErrandFragment("");
                            return true;
                        case R.id.navigation_second_hand:
                            loadIdleThingFragment("");
                            return true;
                        case R.id.navigation_study:
                            loadStudyFragment("");
                            return true;
                        case R.id.navigation_search:
                            loadSearchThingFragment("");
                            return true;
                    }
                    return false;

                });


        //设置抽屉栏
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
                    Intent intent1 = new Intent(getContext(), ConversationListActivity.class);
                    getActivity().startActivity(intent1);
                    break;
                case R.id.slider_menu_money:
                    Intent intent3=new Intent(getContext(), PersonalMoneyActivity.class);
                    Toast.makeText(getContext(),"你点击了我的积分",Toast.LENGTH_SHORT).show();
                    startActivity(intent3);
                    break;
                case R.id.slider_menu_login:
                    if (mAccount != null) {
                        AccountManager.get(getContext()).removeAccountExplicitly(mAccount);
                        STFUConfig.sUser = null;
                        mAccount = null;
                        updateViews();
                    }
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);
                    break;
                case R.id.slider_menu_friend:
                    Intent intent2 = new Intent(getContext(), FriendActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.slider_menu_exit:
                    getActivity().finish();
                    getActivity().moveTaskToBack(true);
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
            if (VerifyUtil.isLogin(getActivity())) {
                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                startActivity(intent);
            }
        });

        Menu mDrawerMenuView = mDrawerNavigationView.getMenu();//侧滑栏的菜单
        mLoginMenuItem = mDrawerMenuView.getItem(3);//第4个菜单项


        //初次加载的界面
        if (savedInstanceState == null) {
            loadErrandFragment("");
        } else {
            switch (savedInstanceState.getInt(CURRENT_BOTTOM_ITEM)) {
                case R.id.navigation_errand:
                    loadErrandFragment("");
                    break;
                case R.id.navigation_second_hand:
                    loadIdleThingFragment("");
                    break;
                case R.id.navigation_study:
                    loadStudyFragment("");
                    break;
                case R.id.navigation_search:
                    loadSearchThingFragment("");
                    break;
                default:
                    loadErrandFragment("");
                    break;
            }
        }

        slider_menu_nick_name = (TextView) mDrawerHeaderView.findViewById(R.id.slider_menu_nick_name);
        slider_menu_signature = (TextView) mDrawerHeaderView.findViewById(R.id.slider_menu_signature);


        /*
         *  设置全局GlobalEventListener
         */
        init_STFUConfig();
        STFUConfig.globalEventListener.setStfuFragment(this);
        registerEventReceiver(STFUConfig.globalEventListener);
        init_Message_point_icon();
        return view;
    }

    /**
     * 这里主要是从本地读取已经登录的用户
     */
    @Override
    public void onResume() {
        super.onResume();

        AccountManager accountManager = AccountManager.get(getContext());
        Account[] account = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID);

        if (account.length != 0 && STFUConfig.sUser == null) {
            STFUConfig.sUser = new User();
            mAccount = account[0];
            STFUConfig.sUser.setEmail(accountManager.getUserData(account[0], "email"));
            STFUConfig.sUser.setUser_id(
                    Integer.parseInt(accountManager.getUserData(account[0], "user_id"))
            );
            STFUConfig.sUser.setNickname(accountManager.getUserData(account[0], "nickname"));
            STFUConfig.sUser.setAvatar_url(accountManager.getUserData(account[0], "avatar_url"));
            STFUConfig.sUser.setFavourable_rate(accountManager.getUserData(account[0], "favourable_rate"));
            STFUConfig.sUser.setGender(accountManager.getUserData(account[0], "gender"));
            STFUConfig.sUser.setPhone(accountManager.getUserData(account[0], "phone"));
            STFUConfig.sUser.setSignature(accountManager.getUserData(account[0], "signature"));
            STFUConfig.sUser.setBg_url(accountManager.getUserData(account[0], "bg_url"));
            JMessageClient.login(STFUConfig.sUser.getEmail(), STFUConfig.sUser.getEmail() + "1", new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        com.orhanobut.logger.Logger.i("极光登陆成功");
                    }
                }
            });
            //设置auth_token
            new GetAuthThread().start();

            //设置极光头像
            if (getMyInfo() != null) {//极光未设置头像
                if (getMyInfo().getAvatar() == null) {
                    try {
                        File file = new File("");
                        Bitmap bitmap = Picasso.get().load(STFUConfig.sUser.getAvatar_url()).get();
                        if (bitmap != null) {
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            updateUserAvatar(file, new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    Logger.i("更新头像成功");
                                }
                            });
                        }
                        file.delete();
                    } catch (Exception e) {
                        Logger.e(e.toString());
                    }
                }
            }

        }
        if (STFUConfig.sUser != null) {
            updateViews();
            VerifyUtil.isTokenValid(getActivity());
        }
        //初始化Systemmessage
        if(STFUConfig.systemMessages==null||STFUConfig.systemMessages.size()==0){
            STFUConfig.systemMessages= SystemDataBaseUtil.getSystemMessage_data(0);
        }
        if(STFUConfig.systemMessages==null) STFUConfig.systemMessages=new ArrayList<SystemMessage>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //将片段与底部导航项绑定
        outState.putInt(CURRENT_BOTTOM_ITEM, mBottomNavigationView.getSelectedItemId());
    }

    /**
     * 创建搜索菜单
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_stfu_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // 最小化搜索框
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);// 第一次清除焦点，输入内容
                    searchView.setIconified(true);
                }

                QueryPreferences.setStoredQuery(getActivity(), query);
                switch (mBottomNavigationView.getSelectedItemId()) {
                    case R.id.navigation_errand:
                        loadErrandFragment(query);
                        return true;
                    case R.id.navigation_second_hand:
                        loadIdleThingFragment(query);
                        return true;
                    case R.id.navigation_study:
                        loadStudyFragment(query);
                        return true;
                    case R.id.navigation_search:
                        loadSearchThingFragment(query);
                        return true;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Logger.w(newText);
                return false;
            }
        });

        searchView.setOnSearchClickListener(v -> {
            String query = QueryPreferences.getStoredQuery(getActivity());
            searchView.setQuery(query, false);
        });
    }

    /**
     * 搜索清除
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(getActivity(), null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            STFUConfig.sUser = ((User) data.getSerializableExtra("user"));
            AccountManager accountManager = AccountManager.get(getContext());
            Account[] account = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID);
            mAccount = account[0];
            updateViews();
        }
    }

    private void init_STFUConfig() {
        if (STFUConfig.globalEventListener == null) {
            STFUConfig.globalEventListener = new GlobalEventListener();
        }
        if (STFUConfig.systemMessages == null) {
            STFUConfig.systemMessages = new ArrayList<SystemMessage>();
        }

        //初始化toolbar的高度
        AppBarLayout.LayoutParams lp1=(AppBarLayout.LayoutParams)mToolbar.getLayoutParams();
        if(STFUConfig.stfu_tool_bar_height==0) STFUConfig.stfu_tool_bar_height=lp1.height;
    }


    public void init_Message_point_icon() {
        //获取是否有未读消息
        if (getAllUnReadMsgCount() > 0) {
            set_Message_point_icon(1);
        } else set_Message_point_icon(0);
    }

    //用mode来标识，切换图标
    //mode=0  正常图标
    //mode=1  右上角有红点的图标
    public boolean set_Message_point_icon(int mode) {
        //修改左上角以及侧拉栏的图标，变成右上有红点
        switch (mode) {
            case 0:
                mDrawerNavigationView.getMenu().findItem(R.id.slider_menu_personal_letter).setIcon(R.drawable.ic_personal_letter);
                //slideIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_personal_letter));
                slideIcon.setVisibility(View.INVISIBLE);
                break;
            case 1:
                mDrawerNavigationView.getMenu().findItem(R.id.slider_menu_personal_letter).setIcon(R.drawable.ic_slide_message);
                slideIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_slide_red_point));
                slideIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        set_Message_point_icon(0);
                        Intent intent1 = new Intent(getContext(), ConversationListActivity.class);
                        getActivity().startActivity(intent1);
                    }
                });
                break;
        }
        return true;
    }


    @OnClick(R.id.fab)
    public void onFABClicked() {
        if (VerifyUtil.isLogin(getActivity())) {
            Intent intent = new Intent(getActivity(), SendPostActivity.class);
            intent.putExtra("current_item", mBottomNavigationView.getSelectedItemId());
            startActivity(intent);
        }
    }

    /**
     * 加载Item Fragment到main_fragment布局
     */
    public void loadErrandFragment(String query) {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //把layout生成一个view对象
        inflater.inflate(R.layout.fragment_errand, mMainFragmentLayout);
        mErrandFragment = mFm.findFragmentById(R.id.errand_container);

        if (mErrandFragment == null) {
            //假如fragment为空，这时才重新创建一个，并且提交显示
            mErrandFragment = new ErrandFragment();
        }
        Bundle args = new Bundle();
        if (!query.equals("")) {
            args.putString("query", query);
        }
        mErrandFragment.setArguments(args);
        mFm.beginTransaction()
                .replace(R.id.main_fragment, mErrandFragment)
                .commit();
    }

    public void loadIdleThingFragment(String query) {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //把layout生成一个view对象
        inflater.inflate(R.layout.fragment_idle_thing, mMainFragmentLayout);
        mIdleThingFragment = mFm.findFragmentById(R.id.idle_thing_container);
        if (mIdleThingFragment == null) {
            mIdleThingFragment = new IdleThingFragment();
        }
        Bundle args = new Bundle();
        if (!query.equals("")) {
            args.putString("query", query);
        }
        mIdleThingFragment.setArguments(args);
        mFm.beginTransaction()
                .replace(R.id.main_fragment, mIdleThingFragment)
                .commit();
    }

    public void loadStudyFragment(String query) {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //把layout生成一个view对象
        inflater.inflate(R.layout.fragment_study, mMainFragmentLayout);
        mStudyFragment = mFm.findFragmentById(R.id.study_container);
        if (mStudyFragment == null) {
            mStudyFragment = new StudyFragment();
        }
        Bundle args = new Bundle();
        if (!query.equals("")) {
            args.putString("query", query);
        }
        mStudyFragment.setArguments(args);
        mFm.beginTransaction()
                .replace(R.id.main_fragment, mStudyFragment)
                .commit();
    }

    public void loadSearchThingFragment(String query) {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_search_thing, mMainFragmentLayout);
        mSearchThingFragment = mFm.findFragmentById(R.id.search_thing_container);
        if (mSearchThingFragment == null) {
            mSearchThingFragment = new SearchThingFragment();
        }
        Bundle args = new Bundle();
        if (!query.equals("")) {
            args.putString("query", query);
        }
        mSearchThingFragment.setArguments(args);
        mFm.beginTransaction()
                .replace(R.id.main_fragment, mSearchThingFragment)
                .commit();
    }

    private void updateViews() {
        if (STFUConfig.sUser != null) {
            slider_menu_nick_name.setText(STFUConfig.sUser.getNickname());
            slider_menu_signature.setText(STFUConfig.sUser.getSignature());
            mLoginMenuItem.setTitle(getString(R.string.logout));
            Picasso.get()
                    .load(STFUConfig.HOST + "/static/avatar/" + STFUConfig.sUser.getAvatar_url())
                    .resize(200, 200)
                    .centerCrop()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(mAvatar);
            //设置背景图片
            Picasso.get()
                    .load(STFUConfig.HOST + "/static/personal_background/" + STFUConfig.sUser.getBg_url())
                    .resize(1920, 1080)
                    .centerCrop()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(mBGImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mBGImageView.getDrawable().setColorFilter(0x33000000, PorterDuff.Mode.SRC_ATOP);
                            mHeaderLinearLayout.setBackground(mBGImageView.getDrawable());
                        }

                        @Override
                        public void onError(Exception e) {
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





























