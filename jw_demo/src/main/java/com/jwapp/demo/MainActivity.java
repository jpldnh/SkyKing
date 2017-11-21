package com.jwapp.demo;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sking.lib.base.SKActivity;
import com.sking.lib.interfaces.SKInterfaceListtener;
import com.sking.lib.net.SKNetTask;
import com.sking.lib.res.activitys.SKRRecordVideoActivity;
import com.sking.lib.res.models.SKRRegionInfo;
import com.sking.lib.res.utils.SKRDialogUtil;
import com.sking.lib.res.views.SKRScrollViewSinglePicker;
import com.sking.lib.res.views.SKRWheelViewSinglePicker;
import com.sking.lib.res.views.SKRWheelViewTimePicker;
import com.sking.lib.utils.SKLogger;
import com.sking.lib.utils.SKPermissionUtil;
import com.sking.lib.utils.SKToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends SKActivity {

    private Button qrButt, album, videoBUtt, timeButt, dialogButt, singButt, newWidgetButt,testButt;
    private SKRScrollViewSinglePicker bootomTakePhoto;
    private SKRWheelViewTimePicker timePicker;
    private SKRWheelViewSinglePicker singPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        requestPermission();
    }

    @Override
    protected boolean onKeyBack() {
        return false;
    }

    @Override
    protected boolean onKeyMenu() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case 1:// 本地选择视频
                dealAlbumVideo(data);
                break;
            case 2:// 录制视频
                dealRecordVideo(data);
                break;
        }
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void findView() {

        qrButt = (Button) findViewById(R.id.main_butt_qr);
        album = (Button) findViewById(R.id.main_butt_album);
        videoBUtt = (Button) findViewById(R.id.main_butt_video);
        timeButt = (Button) findViewById(R.id.main_butt_time);
        dialogButt = (Button) findViewById(R.id.main_butt_dialog);
        singButt = (Button) findViewById(R.id.main_butt_single);
        newWidgetButt = (Button) findViewById(R.id.main_butt_new_widget);
        testButt = (Button) findViewById(R.id.main_butt_test);
    }

    @Override
    protected void setListener() {
        qrButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QrCodeActivity.class);
                startActivity(intent);
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CycleViewActivity.class);
                startActivity(intent);
            }
        });
        videoBUtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomPopuTakePhoto();
            }
        });
        timeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        dialogButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        singButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingPicker();
            }
        });
        newWidgetButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewWidgetActivity.class);
                startActivity(intent);
            }
        });
        testButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TestActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void networkRequestBefore(SKNetTask netTask) {

    }

    @Override
    protected void networkRequestAfter(SKNetTask netTask) {

    }

    @Override
    protected void networkRequestSuccess(SKNetTask netTask, Object result) {

    }

    //圆角button
    public void showBottomPopuTakePhoto() {
        bootomTakePhoto = new SKRScrollViewSinglePicker(mContext, takePhotoOnClick, new String[]{"录制", "从相册选择"});
        //显示窗口
        bootomTakePhoto.showConnerlayoutStyle(qrButt, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);//显示直角布局
//        bootomTakePhoto.showRightAnglelayoutStyle(qrButt, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);//显示圆角布局
    }

    private View.OnClickListener takePhotoOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            bootomTakePhoto.dismiss();
            switch (v.getId()) {
                case 0:
                    recordVideo();
                    break;
                case 1:
                    selectVideo();
                    break;
            }
        }
    };

    //录制视频
    private void recordVideo() {
        Intent intent = new Intent(mContext, SKRRecordVideoActivity.class);
        mContext.startActivityForResult(intent, 2);
    }

    // 录制视频回掉处理
    private void dealRecordVideo(Intent data) {
        Uri uri = data.getData();
        Cursor cursor = this.getContentResolver().query(uri, null, null, null,
                null);
        if (cursor != null && cursor.moveToNext()) {
            final String videoPath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Video.VideoColumns.DATA));
            cursor.close();
            SKLogger.i("videoPath=", videoPath);
            sendVideo(videoPath);
        }
    }

    //从相册选择视频
    private void selectVideo() {
        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        it.setType("video/*");
        startActivityForResult(it, 1);
    }

    // 相册选择视频回掉处理
    private void dealAlbumVideo(Intent data) {
        if (data == null)
            return;
        Uri selectedImageUri = data.getData();
        // 获取视频路径
        String[] proj = {MediaStore.Images.Media.DATA};
        final CursorLoader loader = new CursorLoader(mContext);
        loader.setUri(selectedImageUri);
        loader.setProjection(proj);
        loader.registerListener(0, new Loader.OnLoadCompleteListener<Cursor>() {

            @Override
            public void onLoadComplete(Loader<Cursor> arg0, Cursor cursor) {
                int columnIndex = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                final String videoPath = cursor.getString(columnIndex);
                SKLogger.i("videoPath=", videoPath);
                sendVideo(videoPath);
                loader.stopLoading();
                cursor.close();
            }
        });
        loader.startLoading();
    }

    //压缩发送视频
    private void sendVideo(final String videoPath) {
        File file = new File(videoPath);
        if (!file.exists()) {
            SKToastUtil.showShortToast(mContext, "视频文件不存在");
            return;
        }

        long k = file.length() / 1024;
        double m = (double) k / (double) 1024;
        if (m > 5) {
            SKToastUtil.showShortToast(mContext, "您选择的视频太大了哦，亲");
            return;
        }

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    Integer s = mp.getDuration() / 1000;
                    if (s % 1000 != 0)
                        s++;
                    SKToastUtil.showLongToast(mContext, "发送视频");
                    mp.release();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            //
        }
    }

    //选择生日
    public void showTimePicker() {
        timePicker = new SKRWheelViewTimePicker(mContext, popuWidnwTimePickerOnClick2);
        //显示窗口
        timePicker.showPopuAtLocation(qrButt, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private SKInterfaceListtener.SKOnStringBackClickListener popuWidnwTimePickerOnClick2 = new SKInterfaceListtener.SKOnStringBackClickListener() {
        @Override
        public void onBackResult(String string) {
            Toast.makeText(MainActivity.this, string.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    //选择
    public void showSingPicker() {

        ArrayList<SKRRegionInfo> regionInfos = new ArrayList<SKRRegionInfo>();// String.valueOf(type)
        for (int i = 0; i < 30; i++) {
            SKRRegionInfo regionInfo = new SKRRegionInfo();
            regionInfo.setId(i);
            regionInfo.setParent(i);
            regionInfo.setName("山东" + i);
            regionInfo.setType(0);
            regionInfos.add(regionInfo);
        }
        singPicker = new SKRWheelViewSinglePicker(mContext, popuWidnwTimePickerOnClick3, regionInfos);
        //显示窗口
        singPicker.showPopuAtLocation(qrButt, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private SKInterfaceListtener.SKOnObjectBackClickListener popuWidnwTimePickerOnClick3 = new SKInterfaceListtener.SKOnObjectBackClickListener() {
        @Override
        public void onBackResult(Object result) {
            Toast.makeText(MainActivity.this, ((SKRRegionInfo) result).getName(), Toast.LENGTH_SHORT).show();
        }
    };


    //dialog
    private void showDialog() {

//        SKRDialogUtil dialogUtil = new SKRDialogUtil(mContext);
//        dialogUtil.showInputPasswordDialog("请输入密码");
        SKRDialogUtil baseButtonDialogUtil = new SKRDialogUtil(mContext);
        baseButtonDialogUtil.setButtonOnclickListener(new SKInterfaceListtener.SKOnClickLitener() {
            @Override
            public void onBackClick() {
                SKToastUtil.showLongToast(mContext, "dkdkf");
            }
        });
        baseButtonDialogUtil.show();
    }


    /**
     * 获取6.0动态权限
     */
    public void requestPermission()
    {
        SKPermissionUtil.requesetPermission(this,new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    /**
     * 授权回调
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        SKPermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

}
