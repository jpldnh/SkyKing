package com.sking.lib.res.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.util.Log;
import android.widget.Toast;

import com.sking.lib.interfaces.SKInterfaceListtener;
import com.sking.lib.res.classes.SKRBottomImageWay;
import com.sking.lib.res.config.SKRConfig;
import com.sking.lib.utils.SKFileUtil;
import com.sking.lib.utils.SKImageUtil;
import com.sking.lib.utils.SKTimeUtil;
import com.sking.lib.utils.SKUriUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SKRPhotoSelectUtil {

    private String tempPath;
    private static Activity mContext;
    private String imagePathCamera;
    private static SKRBottomImageWay SKRBottomImageWay;
    private static SKRPhotoSelectUtil photoUtil = new SKRPhotoSelectUtil();
    private static SKRProgressDialogUtil progressDialog;
    private static SKInterfaceListtener.SKOnStringBackClickListener onBackClickForString;
    private int albumSize = 0;

    /**
     * 单例模式
     */
    synchronized public static SKRPhotoSelectUtil getInstance(Activity context) {
        init(context);
        return photoUtil;
    }

    /**
     * 单例模式
     */
    synchronized public static SKRPhotoSelectUtil getInstance(Fragment fragment) {
        init(fragment);
        return photoUtil;
    }

    public static void init(Activity context) {
        mContext = context;
        SKRBottomImageWay = new SKRBottomImageWay(context, 1, 2);
        progressDialog = new SKRProgressDialogUtil(context);
    }

    public static void init(Fragment fragment) {
        mContext = fragment.getActivity();
        SKRBottomImageWay = new SKRBottomImageWay(fragment, 1, 2);
        progressDialog = new SKRProgressDialogUtil(mContext);
    }

    //拍照
    public void camera() {
        tempPath = null;
        SKRBottomImageWay.camera();
    }

    //相册
    public void album() {
        tempPath = null;
        SKRBottomImageWay.album();
    }

    //相册多选
    public void albumArray() {
        tempPath = null;
        SKRBottomImageWay.albumArray();
    }

    //从相册选取取后处理,单选
    public void dealAlbum(Intent data) {
        if (data == null)
            return;
        Uri selectedImageUri = data.getData();
        // 获取图片路径
        String[] proj = {MediaStore.Images.Media.DATA};
        final CursorLoader loader = new CursorLoader(mContext);
        loader.setUri(selectedImageUri);
        loader.setProjection(proj);
        loader.registerListener(0, new OnLoadCompleteListener<Cursor>() {

            @Override
            public void onLoadComplete(Loader<Cursor> arg0, Cursor cursor) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String imagepath = cursor.getString(columnIndex);
                editImage(imagepath, 3);
                loader.stopLoading();
                cursor.close();
            }
        });
        loader.startLoading();
    }

    //调用拍照后处理，可编辑不压缩
    public void dealCamera() {
        imagePathCamera = SKRBottomImageWay.getCameraImage();
        editImage(imagePathCamera, 3);
    }

    //返回tempPath路径，前两个方法调用过之后才可调用
    public String getTempPath() {
        return tempPath;
    }

    public void clearTempPath() {
        tempPath = null;
    }

    //编辑照片方法
    private void editImage(String path, int requestCode) {
        File file = new File(path);
//		startPhotoZoom(Uri.fromFile(file), requestCode);
        //适配android7.0
        startPhotoZoom(SKUriUtil.getUri(mContext, file), requestCode);
    }

    private void startPhotoZoom(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", SKRConfig.IMAGE_WIDTH);
        intent.putExtra("aspectY", SKRConfig.IMAGE_WIDTH);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", SKRConfig.IMAGE_WIDTH);
        intent.putExtra("outputY", SKRConfig.IMAGE_WIDTH);
        intent.putExtra("return-data", false);
        mContext.startActivityForResult(intent, requestCode);
    }

    private Uri getTempUri() {
        //此处使用Uri.fromFile，若使用KUriUtil.getUri有问题，原因未知
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        String savedir = SKFileUtil.getTempFileDir(mContext);
        File dir = new File(savedir);
        if (!dir.exists())
            dir.mkdirs();
        // 保存入sdCard
        tempPath = savedir + SKTimeUtil.getCurrentTime("yyyyMMdd_HHmmss") + ".jpg";// 保存路径
        File file = new File(tempPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return file;
    }

    /**
     * 调用拍照后处理，可编辑不压缩
     **/
    public void dealCameraCompress() {
        imagePathCamera = SKRBottomImageWay.getCameraImage();
        albumSize = 1;
        new CompressPicTask().execute(imagePathCamera);
    }

    /**
     * 相册多选处理
     **/
    public void dealAlbumArray(Intent data) {
        // 自定义相册选择时处理方法
        if (data == null)
            return;
        ArrayList<String> imgList = data.getStringArrayListExtra("images");
        albumSize = imgList.size();
        if (imgList == null)
            return;
        for (String img : imgList) {
            Log.i("imgpath", img);
            new CompressPicTask().execute(img);
        }
    }

    /**
     * 压缩图片
     */
    private class CompressPicTask extends AsyncTask<String, Void, Integer> {
        String compressPath;

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String path = params[0];
                String savedir = SKFileUtil.getTempFileDir(mContext);
                compressPath = SKImageUtil.compressPictureWithSaveDir(path,
                        SKRConfig.IMAGE_HEIGHT, SKRConfig.IMAGE_WIDTH,
                        SKRConfig.IMAGE_QUALITY, savedir, mContext);
                return 0;
            } catch (IOException e) {
                return 1;
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setText("正在压缩图片...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressDialog.setCancelable(false);
            progressDialog.cancel();
            switch (result) {
                case 0:
                    onBackClickForString.onBackResult(compressPath);
                    break;
                case 1:
                    Toast.makeText(mContext, "图片压缩失败！", Toast.LENGTH_LONG);
                    break;
            }
        }
    }

    public void setOnBackClick(SKInterfaceListtener.SKOnStringBackClickListener onBackClick) {
        onBackClickForString = onBackClick;
    }

    public int getAlbumSize() {
        return albumSize;
    }

    public void setSelectSize(int size) {
        SKRBottomImageWay.setSelectSize(size);
    }

}
