package com.sking.lib.res.classes;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.sking.lib.res.activitys.SKRAlbumMultipleActivity;
import com.sking.lib.utils.SKFileUtil;
import com.sking.lib.utils.SKTimeUtil;
import com.sking.lib.utils.SKUriUtil;

import java.io.File;

/**
 * 选择图片方式
 */
public class SKRBottomImageWay {
    private Activity mContext;// 上下文对象
    private Fragment mFragment;// 上下文对象
    private Builder mBuilder;// 弹出对象
    protected int albumRequestCode;// 相册选择时startActivityForResult方法的requestCode值
    protected int cameraRequestCode;// 拍照选择时startActivityForResult方法的requestCode值
    private static final String IMAGE_TYPE = ".jpg";// 图片名后缀
    private String imagePathByCamera;// 拍照时图片保存路径
    private static int selectSize = 10;

    /**
     * 创建一个选择图片方式实例
     *
     * @param mContext          上下文对象
     * @param albumRequestCode  相册选择时startActivityForResult方法的requestCode值
     * @param cameraRequestCode 拍照选择时startActivityForResult方法的requestCode值
     */
    public SKRBottomImageWay(Activity mContext, int albumRequestCode,
                             int cameraRequestCode) {
        this.mContext = mContext;
        this.albumRequestCode = albumRequestCode;
        this.cameraRequestCode = cameraRequestCode;
    }

    /**
     * 创建一个选择图片方式实例
     *
     * @param mFragment
     * @param albumRequestCode  相册选择时startActivityForResult方法的requestCode值
     * @param cameraRequestCode 拍照选择时startActivityForResult方法的requestCode值
     */
    public SKRBottomImageWay(Fragment mFragment, int albumRequestCode,
                             int cameraRequestCode) {
        this.mFragment = mFragment;
        this.albumRequestCode = albumRequestCode;
        this.cameraRequestCode = cameraRequestCode;
    }

    //调用系统选择单张照片
    public void album() {
        Intent it1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (mContext != null)
            mContext.startActivityForResult(it1, albumRequestCode);
        else
            mFragment.startActivityForResult(it1, albumRequestCode);
    }

    //自定义图片选择可选多张
    public void albumArray() {
        // 注意：若不重写该方法则使用系统相册选取(对应的onActivityResult中的处理方法也应不同)
        if (mContext != null) {
            Intent it = new Intent(mContext, SKRAlbumMultipleActivity.class);
            it.putExtra("limitCount", selectSize);// 图片选择张数限制
            mContext.startActivityForResult(it, albumRequestCode);
        } else {
            Intent it = new Intent(mFragment.getContext(), SKRAlbumMultipleActivity.class);
            it.putExtra("limitCount", selectSize);// 图片选择张数限制
            mFragment.startActivityForResult(it, albumRequestCode);
            mContext = null;
        }
    }

    public void camera() {
        Activity activity;
        if (mContext != null)
            activity = mContext;
        else
            activity = mFragment.getActivity();
        String imageName = SKTimeUtil.getCurrentTime("yyyyMMdd_HHmmss") + IMAGE_TYPE;
        Intent it3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imageDir = SKFileUtil.getTempFileDir(activity);
        imagePathByCamera = imageDir + imageName;
        Log.i("picpath_camera",imagePathByCamera);
        File file = new File(imageDir);
        if (!file.exists())
            file.mkdir();
        // 设置图片保存路径
        File out = new File(file, imageName);
        //Uri uri = Uri.fromFile(out);
        //适配android7.0
        it3.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        it3.putExtra(MediaStore.EXTRA_OUTPUT, SKUriUtil.getUri(activity,out));
        Log.i("picpath_cameraUri",SKUriUtil.getUri(activity,out).toString());
        activity.startActivityForResult(it3, cameraRequestCode);
    }

    /**
     * 获取拍照图片路径
     *
     * @return 图片路径
     */
    public String getCameraImage() {
        return imagePathByCamera;
    }

    public void setSelectSize(int size) {
        selectSize = size;
    }

}