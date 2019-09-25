package com.sparetimeforu.android.sparetimeforu.entity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.sparetimeforu.android.sparetimeforu.R;

/**
 * ChangeAvatar
 * Created by Jin on 2019/5/31.
 * Email:17wjli6@stu.edu.cn
 */
public class PhotoPopupWindow extends PopupWindow {
    private View mView;
    private Context mContext;
    private View.OnClickListener mSelectListener;
    private View.OnClickListener mCaptureListener;

    public PhotoPopupWindow(Activity context, View.OnClickListener selectListener, View.OnClickListener captureListener) {
        super(context);
        this.mContext = context;
        this.mSelectListener = selectListener;
        this.mCaptureListener = captureListener;
        Init();
    }

    private void Init() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_item, null);
        Button btn_camera = (Button) mView.findViewById(R.id.icon_btn_camera);
        Button btn_select = (Button) mView.findViewById(R.id.icon_btn_select);
        Button btn_cancel = (Button) mView.findViewById(R.id.icon_btn_cancel);

        btn_select.setOnClickListener(mSelectListener);
        btn_camera.setOnClickListener(mCaptureListener);
        btn_cancel.setOnClickListener(v -> dismiss());

        //导入布局
        this.setContentView(mView);
        //animation
        this.setAnimationStyle(R.style.popwindow_anim_style);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //set touchable
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);

        //outside window touch to close
        mView.setOnTouchListener((v, event) -> {
            int height = mView.findViewById(R.id.ll_pop).getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss();
                }
            }
            return true;
        });
    }
}
