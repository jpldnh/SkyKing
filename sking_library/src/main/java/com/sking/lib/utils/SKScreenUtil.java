package com.sking.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 获取屏幕大小及状态栏高度。。。。工具类
 *
 * @author YangZitian
 * @creation date 2012-8-29 上午10:54:47
 */
public class SKScreenUtil {
    private static final String TAG = "WindowSize";
    /**
     * 屏幕高度
     */
    private static int height = 0;
    /**
     * 屏幕宽度
     */
    private static int width = 0;
    /**
     * 状态栏高度
     */
    private static int statusBarHeight = 0;

    @SuppressWarnings("deprecation")
    public static void get(Context context) {
        if (height == 0 || width == 0) {
            Activity ac = (Activity) context;
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) ac
                    .getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
//            width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
//            height = wm.getDefaultDisplay().getHeight();// 屏幕高度
            width = metrics.widthPixels;// 屏幕宽度
            height = metrics.heightPixels;// 屏幕高度
            Rect rect = new Rect();
            ac.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            statusBarHeight = rect.top; // 状态栏高度
            SKLogger.d(TAG, "height=" + height + " width=" + width
                    + " statusBarHeight=" + statusBarHeight);
            if (width != 0) {
                SKSharedPreferencesUtil.save(ac, "windowWidth",
                        ((Integer) width).toString());
                SKSharedPreferencesUtil.save(ac, "windowHeight",
                        ((Integer) height).toString());
                SKSharedPreferencesUtil.save(ac, "windowStatusBarHeight",
                        ((Integer) statusBarHeight).toString());
            }
        }
    }

    public static int getHeight(Context context) {
        String h = null;
        if (height == 0) {
            h = SKSharedPreferencesUtil.get(context, "windowHeight");
            if (h == null)
                get(context);
        }
        return (height != 0) ? height : (h != null) ? Integer.valueOf(h) : 0;
    }

    public static int getWidth(Context context) {
        String h = null;
        if (width == 0) {
            h = SKSharedPreferencesUtil.get(context, "windowWidth");
            if (h == null)
                get(context);
        }
        return (width != 0) ? width : (h != null) ? Integer.valueOf(h) : 0;
    }

    public static int getStatusBarHeight(Context context) {
        String h = null;
        if (statusBarHeight == 0) {
            h = SKSharedPreferencesUtil.get(context, "windowStatusBarHeight");
            if (h == null)
                get(context);
        }
        return (statusBarHeight != 0) ? statusBarHeight : (h != null) ? Integer
                .valueOf(h) : 0;
    }


    /**
     * @param activity
     * @param color
     * @time 2017/9/26  13:36
     * @author Im_jingwei
     * @desc 设置状态栏
     */
    public static void setStatusBar(Activity activity, int color) {
        //当前手机版本为5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            activity.getWindow().setStatusBarColor(color);
    }

    /**
     * @param activity
     * @param bo       是否透明
     * @time 2017/9/26  13:36
     * @author Im_jingwei
     * @desc 设置透明状态栏
     */
    public static void setStatusBarTransparent(Activity activity, boolean bo) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;
        Window window = activity.getWindow();
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (bo) {
            //如果为全透明模式，取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置状态栏为透明
            window.setStatusBarColor(Color.TRANSPARENT);
            //设置window的状态栏不可见
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            //如果为半透明模式，添加设置Window半透明的Flag
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        //view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            mChildView.setFitsSystemWindows(false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    /**
     * @param activity
     * @param color
     * @time 2017/9/26  13:36
     * @author Im_jingwei
     * @desc 设置导航栏状态
     */
    public static void setNevigationBar(Activity activity, int color) {
        //当前手机版本为5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            activity.getWindow().setNavigationBarColor(color);
    }
    /**
     * @param activity
     * @time 2017/9/26  13:36
     * @author Im_jingwei
     * @desc 设置导航栏状态
     */
    public static void setNevigationBarTransparent(Activity activity) {
        //当前手机版本为5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * @param activity
     * @param bo  true visiable ,false unvisiable
     * @time 2017/9/26  13:36
     * @author Im_jingwei
     * @desc 设置全屏
     */
    public static void setFullCreen(Activity activity,boolean bo) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;
        if (bo) {
//            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    /**
     * 获取屏幕密度规格
     *
     * @param context
     * @return
     */
    public static final String getDpi(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        String dpi = null;
        if (density == 0.75f)
            dpi = "ldpi";
        else if (density == 1.0f)
            dpi = "mdpi";
        else if (density == 1.5f)
            dpi = "hdpi";
        else if (density == 2f)
            dpi = "xhdpi";
        else if (density == 3f)
            dpi = "xxhdpi";
        return dpi;
    }

}
