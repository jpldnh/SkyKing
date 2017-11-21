package com.sking.lib.res.exp.rqcode;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.sking.lib.res.exp.R;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by HuHu on 2016/4/17.
 */
public class CodeCaptureActivity extends Activity implements SurfaceHolder.Callback {

    private TextView titleText;
    private TextView titleLeft;
    private TextView titleRight;
    private ViewfinderView viewFinderView;
    private CaptureActivityHandler handler;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skr_layout_activity_capture);
        titleText = (TextView) findViewById(R.id.top_title_text);
        titleLeft = (TextView) findViewById(R.id.top_title_left);
        titleRight = (TextView) findViewById(R.id.top_title_right);
        viewFinderView = (ViewfinderView) findViewById(R.id.viewFinderView);
        CameraManager.init(getApplication());
        getCameraPermission();
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
        }

    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }


    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(this, "未获取到相关信息！", Toast.LENGTH_SHORT).show();
            ReStartCode();
        } else {
//            Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, getIntent().putExtra("RQ_RESULT", resultString));
            finish();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            Log.e("initCamera: ", e.getMessage());
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    public ViewfinderView getViewfinderView() {
        return viewFinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewFinderView.drawViewfinder();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 重新开始扫描
     */
    private void ReStartCode() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
        onResume();
    }

    /**
     * @param
     * @time 2017/10/17  16:49
     * @author Im_jingwei
     * @desc 相机权限
     */
    private void getCameraPermission() {
        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            ActivityCompat.requestPermissions(CodeCaptureActivity.this, new String[]{Manifest.permission.CAMERA}, 0x1100);
        } else {
//            onResume();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    //用户禁止
                    Toast.makeText(CodeCaptureActivity.this, "授权失败，相机无法使用！", Toast.LENGTH_SHORT).show();
                } else {
                    //用户选择不再提示，禁止权限
//                    PermissionMonitorService.start(this);//这里一般会提示用户进入权限设置界面
                    Toast.makeText(CodeCaptureActivity.this, "相机权限被禁止，请手动打开！", Toast.LENGTH_SHORT).show();
                }
                return;
            } else {
                //获取授权
//                onResume();
            }
        }
    }
}
