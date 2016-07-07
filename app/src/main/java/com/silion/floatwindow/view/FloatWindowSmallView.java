package com.silion.floatwindow.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.silion.floatwindow.R;

import java.lang.reflect.Field;

/**
 * Created by silion on 2016/6/30.
 */
public class FloatWindowSmallView extends LinearLayout {
    private final WindowManager mWindowManager;
    public int mWidth;
    public int mHeight;
    private float mXInView;
    private float mYInView;
    private float mXDownInScreen;
    /**
     * 记录系统状态栏的高度
     */
    private static int mStatusBarHeight;
    private float mYDownInScreen;
    private float mXInScreen;
    private float mYInScreen;
    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    public FloatWindowSmallView(Context context) {
        super(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        View view = findViewById(R.id.small_window_layout);
        mWidth = view.getLayoutParams().width;
        mHeight = view.getLayoutParams().height;
        TextView tvPercent = (TextView) findViewById(R.id.percent);
        tvPercent.setText(FloatWindowManager.getUsedPercentValue(context));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                mXInView = event.getX(); //相对于控件的x
                mYInView = event.getY(); //相对于控件的y
                mXDownInScreen = event.getRawX(); //相对于屏幕的x
                mYDownInScreen = event.getRawY() - getStatusBarHeight();
                mXInScreen = event.getRawX();
                mYInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                mXInScreen = event.getRawX();
                mYInScreen = event.getRawY() - getStatusBarHeight();
                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，mXDownInScreen == mXInScreen && mYDownInScreen == mYInScreen，则视为触发了单击事件
                if (Math.abs(mXDownInScreen - mXInScreen) < mWidth && Math.abs(mYDownInScreen - mYInScreen) < mHeight) {
                    openBigWindow();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void openBigWindow() {
        FloatWindowManager.createBigWindow(getContext());
        FloatWindowManager.removeSmallWindow(getContext());
    }

    private void updateViewPosition() {
        mParams.x = (int) (mXInScreen - mXInView);
        mParams.y = (int) (mYInScreen - mYInView);
        mWindowManager.updateViewLayout(this, mParams);
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (mStatusBarHeight == 0) {
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                Field field = clazz.getField("status_bar_height");
                int x = (int) field.get(object);
                mStatusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mStatusBarHeight;
    }
}
