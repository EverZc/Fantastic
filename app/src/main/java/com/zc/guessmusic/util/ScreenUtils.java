package com.zc.guessmusic.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Panda_Program on 2017/2/5 0005.
 */

public class ScreenUtils {
    //获取屏幕相关参数
    public static DisplayMetrics getScreenSize(Context context){
        DisplayMetrics metrics=new DisplayMetrics();
        WindowManager windowManager= (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        Display display=windowManager.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }
    //获取屏幕density
    public static float getDeviceDensity(Context context){
        DisplayMetrics metrics=new DisplayMetrics();
        WindowManager windowManager= (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }
}
