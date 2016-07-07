package com.silion.floatwindow.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.silion.floatwindow.util.AppUtils;
import com.silion.floatwindow.view.FloatWindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class FloatWindowService extends Service {
    private Timer mTimer;
    /**
     * 用于在线程中创建或移除悬浮窗
     */
    private Handler mHandler = new Handler();

    public FloatWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new RefreshTask(), 0, 1000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private class RefreshTask extends TimerTask {

        @Override
        public void run() {
            if (isHome() && !FloatWindowManager.isWindowShowing()) {
                //当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.createSmallWindow(getApplicationContext());
                    }
                });
            } else if (!isHome() && FloatWindowManager.isWindowShowing()) {
                // 当前界面不是桌面，且有悬浮窗显示，则移除悬浮窗。
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.removeSmallWindow(getApplicationContext());
                    }
                });
            } else if (isHome() && FloatWindowManager.isWindowShowing()) {
                // 当前界面是桌面，且有悬浮窗显示，则更新内存数据。
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.updateUsedPercent(getApplicationContext());
                    }
                });
            }
        }
    }

    private boolean isHome() {
        return AppUtils.isHome(getApplicationContext());
    }
}
