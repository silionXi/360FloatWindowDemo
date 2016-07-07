package com.silion.floatwindow.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.silion.floatwindow.R;
import com.silion.floatwindow.service.FloatWindowService;

/**
 * Created by silion on 2016/7/6.
 */
public class FloatWindowBigView extends LinearLayout {
    public final int mHeight;
    public final int mWidth;

    public FloatWindowBigView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
        View view = findViewById(R.id.big_window_layout);
        mWidth = view.getLayoutParams().width;
        mHeight = view.getLayoutParams().height;
        Button btClose = (Button) findViewById(R.id.close);
        btClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                FloatWindowManager.removeBigWindw(context);
                FloatWindowManager.removeSmallWindow(context);
                Intent intent = new Intent(context, FloatWindowService.class);
                context.stopService(intent);
            }
        });
        Button btBack = (Button) findViewById(R.id.back);
        btBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowManager.removeBigWindw(context);
                FloatWindowManager.createSmallWindow(context);
            }
        });
    }
}
