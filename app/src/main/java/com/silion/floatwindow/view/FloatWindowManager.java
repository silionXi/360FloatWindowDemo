package com.silion.floatwindow.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.silion.floatwindow.R;
import com.silion.floatwindow.util.SystemUtils;

/**
 * Created by silion on 2016/6/30.
 */
public class FloatWindowManager {
    /**
     * 小悬浮窗view的实例
     */
    private static FloatWindowSmallView mSmallWindow;
    /**
     * 大悬浮窗view的实例
     */
    private static FloatWindowBigView mBigWindow;
    /**
     * 小悬浮窗View的参数
     */
    private static WindowManager.LayoutParams mSmallWindowParams;
    /**
     * 大悬浮窗View的参数
     */
    private static WindowManager.LayoutParams mBigWindowParams;
    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    /**
     * 创建一个小悬浮窗，初始化位置为屏幕的右边中间位置。
     *
     * @param context
     */
    public static void createSmallWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        if (mSmallWindow == null) {
            mSmallWindow = new FloatWindowSmallView(context);
            if (mSmallWindowParams == null) {
                mSmallWindowParams = new WindowManager.LayoutParams();
                mSmallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                mSmallWindowParams.format = PixelFormat.RGBA_8888;
                mSmallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                mSmallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                mSmallWindowParams.width = mSmallWindow.mWidth;
                mSmallWindowParams.height = mSmallWindow.mHeight;
                mSmallWindowParams.x = screenWidth;
                mSmallWindowParams.y = screenHeight / 2;
            }
        }
        mSmallWindow.setParams(mSmallWindowParams);
        windowManager.addView(mSmallWindow, mSmallWindowParams);
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeSmallWindow(Context context) {
        if (mSmallWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(mSmallWindow);
            mSmallWindow = null;
        }
    }

    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createBigWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        if (mBigWindow == null) {
            mBigWindow = new FloatWindowBigView(context);
            if (mBigWindowParams == null) {
                mBigWindowParams = new WindowManager.LayoutParams();
                mBigWindowParams.x = screenWidth / 2 - mBigWindow.mWidth / 2;
                mBigWindowParams.y = screenHeight / 2 - mBigWindow.mHeight / 2;
                mBigWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                mBigWindowParams.format = PixelFormat.RGBA_8888;
                mBigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                mBigWindowParams.width = mBigWindow.mWidth;
                mBigWindowParams.height = mBigWindow.mHeight;
            }
            windowManager.addView(mBigWindow, mBigWindowParams);
        }
    }

    /**
     * 将大悬浮窗从屏幕上移除。
     *
     * @param context
     *            必须为应用程序的Context.
     */
    public static void removeBigWindw(Context context) {
        if (mBigWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(mBigWindow);
            mBigWindow = null;
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
     *
     * @param context 可传入应用程序上下文。
     */
    public static void updateUsedPercent(Context context) {
        if (mSmallWindow != null) {
            TextView tvPercent = (TextView) mSmallWindow.findViewById(R.id.percent);
            tvPercent.setText(getUsedPercentValue(context));
        }
    }

    /**
     * 判断是否有悬浮窗显示在屏幕上
     *
     * @return
     */
    public static boolean isWindowShowing() {
        return mSmallWindow != null || mBigWindow != null;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager.
     *
     * @param context
     * @return
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 计算已使用内存的百分比，并返回。
     *
     * @param context 可传入应用程序上下文。
     * @return 已使用内存的百分比，以字符串形式返回。
     */
    public static String getUsedPercentValue(Context context) {
        long totalMem = SystemUtils.getTotalMemory(context); //返回kb
        long available = SystemUtils.getAvailableMemory(context) / 1024; //返回字节
        int percent = (int) ((totalMem - available) / (float) totalMem * 100);
        return percent + "%";
    }
}
