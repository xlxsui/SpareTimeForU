package com.sparetimeforu.android.sparetimeforu.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.entity.User;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

/**
 * Created by HQY on 2018/11/25.
 */

public class EditFragment extends Fragment {

    private static final int changeNickName = 0;
    private static final int changeSignature = 1;
    private static final int changePhonecall = 2;
    private static final int changeAvator = 3;
    private static final int changeSex = 4;

    private User user;


    //每一行的相对布局，对应不同信息的修改
    private View view;
    private RelativeLayout edit_PhoneCall, edit_Avatar, edit_Signature, edit_Nickname, edit_Sex;

    //每一行布局中的TextView，用以更新个人信息

    private TextView tv_edit_phonecall, tv_edit_signature, tv_edit_sex, tv_edit_nickname;
    private ImageView iv_edit_avatar;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.personal_edit_fragment, container, false);

        //初始化控件
        init();
        //初始化数据（sUser1234）
        initDate();
        changUI();


        return view;
    }

    private void init() {

        edit_PhoneCall = (RelativeLayout) view.findViewById(R.id.edit_Phonecall);
        edit_Avatar = (RelativeLayout) view.findViewById(R.id.edit_Avatar);
        edit_Signature = (RelativeLayout) view.findViewById(R.id.edit_Signature);
        edit_Nickname = (RelativeLayout) view.findViewById(R.id.edit_Nickname);
        edit_Sex = (RelativeLayout) view.findViewById(R.id.edit_Sex);

        tv_edit_nickname = (TextView) view.findViewById(R.id.tv_edit_nickname);
        tv_edit_signature = (TextView) view.findViewById(R.id.tv_edit_signature);
        tv_edit_sex = (TextView) view.findViewById(R.id.tv_edit_sex);
        tv_edit_phonecall = (TextView) view.findViewById(R.id.tv_edit_phonecall);

        iv_edit_avatar = (ImageView) view.findViewById(R.id.iv_edit_avatar);


        //添加监听
        MyListener myListener = new MyListener();
        edit_PhoneCall.setOnClickListener(myListener);
        edit_Avatar.setOnClickListener(myListener);
        edit_Signature.setOnClickListener(myListener);
        edit_Nickname.setOnClickListener(myListener);
        edit_Sex.setOnClickListener(myListener);

    }

    private void initDate() {
        Intent intent = getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.edit_Avatar:
                    //弹出对话框选择头像
                    Toast.makeText(getActivity(), "你点击了修改头像", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.edit_Nickname:
                    //弹出对话框修改昵称
                    showAlertDialog("nickname", changeNickName);
                    break;
                case R.id.edit_Phonecall:
                    //弹出对话框修改手机号码
                    showAlertDialog("phone", changePhonecall);


                    break;
                case R.id.edit_Sex:
                    //弹出对话框修改性别
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    int defaultChoice = 1;
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setTitle("请选择性别");
                    final String[] sex = {"男", "女"};

                    if (user != null && user.getGender().equals("男"))
                        defaultChoice = 0;
                    builder.setSingleChoiceItems(sex, defaultChoice, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "性别为：" + sex[which], Toast.LENGTH_SHORT).show();

                            if (user != null) {
                                user.setGender(sex[which]);
                            }
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            changUI();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();

                    break;
                case R.id.edit_Signature:
                    //弹出对话框修改昵称
                    showAlertDialog("signature", changeSignature);
            }

            //修改信息完成后进行界面的更新
            changUI();
        }
    }

    private void changUI() {
        if (user != null) {
            tv_edit_nickname.setText(user.getNickname());
            tv_edit_phonecall.setText(user.getPhone());
            tv_edit_sex.setText(user.getGender());
            tv_edit_signature.setText(user.getSignature());
            Picasso.get()
                    .load(user.getAvatar_url())
                    .resize(200, 200)
                    .centerCrop()
                    .into(iv_edit_avatar);
        }
    }


    /**
     * 创建对话框已修改信息
     *
     * @param changingUserAttr 要修改的东西（以字符串的形式传入）
     * @param changeType       要修改的类型，预先定义的类静态变量
     */
    private void showAlertDialog(String changingUserAttr, int changeType) {
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.personal_edit_input, null);
        EditText edit_input = (EditText) view1.findViewById(R.id.edit_input);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请输入修改后的信息");
        builder.setIcon(R.mipmap.ic_launcher_round);

        builder.setView(view1);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String input = edit_input.getText().toString();
                if (input != null) {

                    //根据要修改的信息类型修改用户信息
                    if (user != null) {
                        switch (changeType) {
                            case changeNickName:
                                user.setNickname(input);
                                break;
                            case changePhonecall:

                                if (isInteger(input))
                                    user.setPhone(input);
                                else
                                    Toast.makeText(getActivity(), "修改电话时必须输入数字", Toast.LENGTH_SHORT).show();
                                break;
                            case changeSignature:
                                user.setSignature(input);
                                break;
                        }
                    }
                    //发送信息到服务器修改个人信息
                    sendMessageToServer(changingUserAttr + "=" + input, changeType);

                } else {
                    Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


    /**
     * @param message The changed Message
     * @param type    The change type(changeNickname :0 ; etc...)
     */
    private void sendMessageToServer(String message, int type) {

    }

    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();


    }
}
