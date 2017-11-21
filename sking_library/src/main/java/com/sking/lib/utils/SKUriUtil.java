package com.sking.lib.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by Im_jingwei on 2017/10/24.
 */

public class SKUriUtil {

    /**
     *  @time 2017/10/24  11:46
     *  @author Im_jingwei
     *  @param  activity
     *  @param  file
     *  @desc 适配安卓7.0私有权限问题
     *  @return uri
     */
    public static Uri getUri(Activity activity, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String authority = activity.getPackageName() + ".provider";
            return FileProvider.getUriForFile(activity, authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
