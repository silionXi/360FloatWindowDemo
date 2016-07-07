package com.silion.floatwindow.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silion on 2016/6/29.
 */
public class AppUtils {

    //获取当前所在界面的包名
    public static String getTopActivityPackage(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<RunningAppProcessInfo> appProcessInfos = am.getRunningAppProcesses();
//            for (RunningAppProcessInfo appProcessInfo : appProcessInfos) {
//                if (appProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    packageName = appProcessInfo.processName;
//                }
//            }
            RunningAppProcessInfo appProcessInfo = appProcessInfos.get(0);
            packageName = appProcessInfo.processName;
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfos = am.getRunningTasks(5);
            ActivityManager.RunningTaskInfo taskInfo = taskInfos.get(0);
            packageName = taskInfo.topActivity.getPackageName();
        }
        return packageName;
    }

    /**
     * 获取属于桌面的应用的包名
     *
     * @param context
     * @return
     */
    public static List<String> getHomePackages(Context context) {
        PackageManager pm = context.getPackageManager();
        List<String> packageNames = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            packageNames.add(resolveInfo.activityInfo.packageName);
        }
        return packageNames;
    }

    /**
     * 判断当前界面是否是桌面
     *
     * @return
     */
    public static boolean isHome(Context context) {
        String packageName = getTopActivityPackage(context);
        List<String> homesPackages = getHomePackages(context);
        return homesPackages.contains(packageName);
    }
}
