package com.sparetimeforu.android.sparetimeforu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.user.User;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by HQY on 2018/11/15.
 */

public class PersonalFragment extends Fragment {

    public static String LoginServerUrl=" https://www.easy-mock.com/mock/5bebc836e0c6d321cade7458/success/LoginServer";

    //用户个人信息
    TextView personal_favourable_rate,personal_nickname,personal_signate;
    ImageView personal_avator;

    //用户界面需要相应的控件
    TextView personal_mission_received,personal_mission_released;

    User user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.personal_fragment,container,false);

        //初始化控件
        personal_avator=(ImageView)view.findViewById(R.id.personal_avator);
        personal_nickname=(TextView) view.findViewById(R.id.personal_nickname);
        personal_signate=(TextView) view.findViewById(R.id.personal_signate);
        personal_mission_received=(TextView) view.findViewById(R.id.personal_mission_received);
        personal_mission_released=(TextView) view.findViewById(R.id.personal_mission_released);
        personal_favourable_rate=(TextView) view.findViewById(R.id.personal_favourable_rate);
        initDate();

        return view;
    }

    /**
     * 获取用户信息
     */
    public void initDate(){
        //先用服务器，后续需要使用Activity间的信息传递
        OkHttpUtil.sendLoginOkHttpRequest(LoginServerUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"获取数据失败",Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String responseText=response.body().string();
                if(!TextUtils.isEmpty(responseText)){
                    user= HandleMessageUtil.handleLoginMessage(responseText);

                    //更新UI界面

                    if(user!=null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUI();

                        }
                    });
                    else Log.i("test1","获取数据失败");
                }

            }
        });
    }

    private void setUI(){
        personal_nickname.setText(user.getNick_name());
        personal_favourable_rate.setText("好评率"+user.getFavourable_rate()+"%");
        personal_signate.setText(user.getSignate());
    }




}
