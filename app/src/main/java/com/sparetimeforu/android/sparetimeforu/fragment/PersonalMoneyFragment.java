package com.sparetimeforu.android.sparetimeforu.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.weavey.loading.lib.LoadingLayout;

/**
 * Created by HQY on 2019/11/6.
 */

public class PersonalMoneyFragment  extends Fragment implements View.OnClickListener{
    TextView money_value;
    Button cash,recharge;//提现、充值
    Toolbar toolbar;
    ImageView imageView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_money,null);

        imageView=(ImageView)view.findViewById(R.id.menu_back_icon);
        toolbar=(Toolbar)view.findViewById(R.id.toolbar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        money_value=(TextView)view.findViewById(R.id.my_money_value);
        //money_value.setText(STFUConfig.sUser.getMoney());
        money_value.setText("165");
        cash=(Button)view.findViewById(R.id.cash_button);
        recharge=(Button)view.findViewById(R.id.recharge_button);
        cash.setOnClickListener(this);
        recharge.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cash_button:
                cash_function();
                break;
            case R.id.recharge_button:
                recharge_function();
                break;
        }
    }
    private boolean cash_function(){
        //弹出dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"您取消了提现",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //发送请求到服务器
                Toast.makeText(getActivity(),"提交请求成功，后台审核中，耐心等待哦",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setTitle("提现可得"+12+"元，您确定要提现吗?");
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
        return true;
    }

    private boolean recharge_function(){
        //弹出dialog 充值数额

        //发送请求到服务器
        //返回成功
        return true;
    }

    public static PersonalMoneyFragment newInstance() {
        return new PersonalMoneyFragment();
    }
}
