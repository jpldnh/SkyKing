package com.sking.lib.res.activitys;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sking.lib.base.SKActivityManager;
import com.sking.lib.res.R;
import com.sking.lib.res.album.SKRAlbumAdapter;
import com.sking.lib.res.album.SKRImageFloder;
import com.sking.lib.res.utils.SKRProgressDialogUtil;
import com.sking.lib.res.views.SKRPopuWindowListImageDir;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * 相册选择图片页面
 */
public class SKRAlbumMultipleActivity extends Activity implements SKRPopuWindowListImageDir.OnImageDirSelected {
    private TextView titleLeft;
    private TextView titleText;
    private TextView titleRight;
    /**
     * 图片选择张数限制
     */
    private int limitCount;
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所有的图片
     */
    private List<String> mImgs;

    private GridView mGirdView;
    private SKRAlbumAdapter mAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<SKRImageFloder> mImageFloders = new ArrayList<SKRImageFloder>();
    private RelativeLayout mBottomLy;
    private TextView mChooseDir;
    private TextView mImageCount;
    int totalCount = 0;
    private int mScreenHeight;
    private SKRPopuWindowListImageDir mBasePopupWindowListImageDir;
    private SKRProgressDialogUtil progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.skr_layout_activity_album);
        super.onCreate(savedInstanceState);
        SKActivityManager.add(this);
        getExras();
        findView();
        setListener();
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        progressDialog = new SKRProgressDialogUtil(this);
        getImages();
        initEvent();

    }

    protected void getExras() {
        limitCount = getIntent().getIntExtra("limitCount", 0);
    }

    protected void findView() {
        titleText = (TextView) findViewById(R.id.top_title_text);
        titleLeft = (TextView) findViewById(R.id.top_title_left);
        titleRight = (TextView) findViewById(R.id.top_title_right);

        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
    }

    protected void setListener() {
        titleText.setText("选择图片");
        titleLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleRight.setText("确定");
        titleRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                if (mAdapter != null) {
                    it.putExtra("images", mAdapter.mSelectedImage);
                }
                setResult(RESULT_OK, it);
                finish();
            }
        });
    }


    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mHandler.sendEmptyMessage(0x101);
        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = SKRAlbumMultipleActivity.this.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    Log.e("TAG", path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    SKRImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new SKRImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    String[] strings = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    });
                    if (strings != null) {
                        int picSize = strings.length;
                        totalCount += picSize;

                        imageFloder.setCount(picSize);
                        mImageFloders.add(imageFloder);

                        if (picSize > mPicsSize) {
                            mPicsSize = picSize;
                            mImgDir = parentFile;
                        }
                    }
                }
                mCursor.close();

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();

    }

    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mBottomLy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBasePopupWindowListImageDir
                        .setAnimationStyle(R.style.SKR_Style_PopuWindow_Bottom);
                mBasePopupWindowListImageDir.showAsDropDown(mBottomLy, 0, 0);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void selected(SKRImageFloder floder) {
        mImgDir = new File(floder.getDir());
        List<File> files = (List<File>) Arrays.asList(mImgDir
                .listFiles(filefiter));

        Collections.sort(files, new FileComparator());
        mImgs.clear();
        for (File file : files) {
            mImgs.add(file.getPath());
        }
        setAdapter();
        mImageCount.setText(floder.getCount() + "张");
        mChooseDir.setText(floder.getName());
        mBasePopupWindowListImageDir.dismiss();

    }

    private void setAdapter() {
        if (mAdapter == null) {
            // 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
            mAdapter = new SKRAlbumAdapter(getApplicationContext(), mImgs, R.layout.skr_layout_view_album_grid_item, mImgDir.getAbsolutePath(), limitCount);
            mGirdView.setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(mImgs);
            mAdapter.setDirPath(mImgDir.getAbsolutePath());
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        List<File> files = (List<File>) Arrays.asList(mImgDir
                .listFiles(filefiter));

        Collections.sort(files, new FileComparator());
        if (mImgs == null) {
            mImgs = new ArrayList<String>();
        }
        mImgs.clear();
        for (File file : files) {
            mImgs.add(file.getPath());
        }
        setAdapter();
        mImageCount.setText(totalCount + "张");
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mBasePopupWindowListImageDir = new SKRPopuWindowListImageDir(
                LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.skr_layout_view_album_list_dir, null));

        mBasePopupWindowListImageDir.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mBasePopupWindowListImageDir.setOnImageDirSelected(this);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what)
            {
                case 0x101:
                    progressDialog.setText("正在加载...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    break;
                case 0x110:
                    progressDialog.setCancelable(false);
                    progressDialog.cancel();
                    // 为View绑定数据
                    data2View();
                    // 初始化展示文件夹的popupWindw
                    initListDirPopupWindw();
                    break;
            }

        }
    };

    private class FileComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            if (lhs.lastModified() < rhs.lastModified()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private FileFilter filefiter = new FileFilter() {

        @Override
        public boolean accept(File f) {
            String tmp = f.getName().toLowerCase();
            if (tmp.endsWith(".png") || tmp.endsWith(".jpg")
                    || tmp.endsWith(".jpeg")) {
                return true;
            }
            return false;
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SKActivityManager.remove(this);
    }
}
