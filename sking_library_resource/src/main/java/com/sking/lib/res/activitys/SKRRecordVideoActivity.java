package com.sking.lib.res.activitys;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioSource;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore.Video;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;

import com.sking.lib.base.SKActivityManager;
import com.sking.lib.res.R;
import com.sking.lib.res.videos.SKRSupportedSizesReflect;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * TODO
 * @author cuiran
 * @version 1.0.0
 */
public class SKRRecordVideoActivity extends Activity implements Callback
 {
	private String mCurrentVideoFilename;
	private Uri mCurrentVideoUri;
	private ContentValues mCurrentVideoValues;
	private ContentResolver mContentResolver;
	private MediaRecorder mediarecorder;// 录制视频的类
	private SurfaceView surfaceview;// 显示视频的控件
	private  Camera camera;
	//实现这个接口的Callback接口
	private SurfaceHolder surfaceHolder;
	private Button changeButt;
	private int recordTime = 5;
	private boolean isRecord = false;//是否正在录制true录制中 false未录制
	public boolean isCameraBack = false;
	private Button recordIv;
	private int cameraCount = 0;
	private int cameraPosition = 0;//0代表前置摄像头，1代表后置摄像头
	private Chronometer timer;
	private Intent intent;
	private static final String TAG = "TAG-CameraActivity";
	final int sdkVersion = Build.VERSION.SDK_INT;
	private Button sendButt,cancelButt;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skr_layout_activity_video_record);
		SKActivityManager.add(this);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);// 选择支持半透明模式,在有surfaceview的activity中使用。
//		videoPath = XtomFileUtil.getTempFileDir(SKRRecordVideoActivity.this)+"temp_mm_video.mp4";
		initView();
		intent = getIntent();
		setResult(RESULT_CANCELED,intent);
	}

	private void initView() {
		
		mContentResolver = getContentResolver();
		surfaceview = (SurfaceView) this.findViewById(R.id.record_surface_view);
		recordIv = (Button)findViewById(R.id.record_start_and_stop);
		changeButt = (Button)findViewById(R.id.record_switch_butt);
		sendButt = (Button)findViewById(R.id.record_send_butt);
		cancelButt = (Button)findViewById(R.id.record_send_cancle);
		
		timer = (Chronometer)this.findViewById(R.id.record_time);  
		timer.setBase(SystemClock.elapsedRealtime());
		SurfaceHolder holder = surfaceview.getHolder();// 取得holder
		holder.addCallback(this); // holder加入回调接口
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// setType必须设置，要不出错.
		
		recordIv.setVisibility(View.VISIBLE);
		sendButt.setVisibility(View.GONE);
		cancelButt.setVisibility(View.GONE);
		recordIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				recordVideo();
			}
		});
		
		changeButt.setBackgroundResource(R.drawable.skr_bg_button_record_video_switch_camera);
		changeButt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeCamera();
			}
		});
		timer.setOnChronometerTickListener(new OnChronometerTickListener() {
			
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				// TODO Auto-generated method stub
				String time = chronometer.getText().toString();
				int n = Integer.parseInt(time.substring(time.length()-1,time.length()));
				if(n<3)
				{
					recordIv.setEnabled(false);
				}else
				{
					recordIv.setEnabled(true);
				}
				if (n>recordTime) {// 判断五秒之后停止
					recordVideo();
				} 
			}
		});
		sendButt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doReturnToCaller(true);
			}
		});

		cancelButt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doReturnToCaller(false);
			}
		});
	}
	
	/**
	 * 开始录制/停止录制
	 * TODO
	 * @param
	 */
	public void recordVideo(){
		if(isRecord){
			stopMethod();
		}else{
			isRecord=true;
			recordIv.setBackgroundResource(R.drawable.skr_bg_button_record_video_stop);
			mediarecorder = new MediaRecorder();// 创建mediarecorder对象
			/**
			 * 设置竖着录制
			 */
			if(camera!=null){
				camera.release();
			}
			
			if(cameraPosition==1){
				camera = Camera.open(CameraInfo.CAMERA_FACING_BACK);//打开摄像头
		         camera.setDisplayOrientation(90);
		         mediarecorder.setOrientationHint(90);//视频旋转90度
			}else{
				 camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);//打开摄像头
		         camera.setDisplayOrientation(90);
				 mediarecorder.setOrientationHint(270);//视频旋转90度
			}
		    camera.unlock();
		    mediarecorder.setCamera(camera);
		 // 设置录制视频源为Camera(相机)
		    mediarecorder.setAudioSource(AudioSource.CAMCORDER);
		    mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
			createVideoPath();
			mediarecorder.setOutputFile(mCurrentVideoFilename);
			mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
			mediarecorder.setMaxFileSize(4 * 720 * 720);
//			mediarecorder.setMaxDuration(5000);//最大期限 
			try {
				// 准备录制
				mediarecorder.prepare();
				// 开始录制
				mediarecorder.start();
				timer.setBase(SystemClock.elapsedRealtime()); // 将计时器清零    
				timer.start(); //开始计时  
			} catch (IllegalStateException e) {
				Log.d(TAG,
						"IllegalStateException preparing MediaRecorder: "+ e.getMessage());
				releaseMediaRecorder();
			} catch (IOException e) {
				Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
				releaseMediaRecorder();
			}
		}
	}
	private void releaseMediaRecorder() {
		if (mediarecorder != null) {
			mediarecorder.reset(); // clear recorder configuration
			mediarecorder.release(); // release the recorder object
			mediarecorder = null;
			camera.lock(); // lock camera for later use
		}
	}
	@TargetApi(15)
	public void set_QUALITY() {
		boolean hasqvga = false, has480p = false, hasCIF = false, hasQCIF = false;
		if (sdkVersion >= 15) {
			hasqvga = CamcorderProfile
					.hasProfile(CamcorderProfile.QUALITY_QVGA);
		}
		if (sdkVersion >= 11) {
			has480p = CamcorderProfile
					.hasProfile(CamcorderProfile.QUALITY_480P);
			hasCIF = CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_CIF);
			hasQCIF = CamcorderProfile
					.hasProfile(CamcorderProfile.QUALITY_QCIF);
		}

		if (hasqvga) {
			Log.d(TAG, "CamcorderProfile.QUALITY_QVGA");
			mediarecorder.setProfile(CamcorderProfile
					.get(CamcorderProfile.QUALITY_QVGA));
		} else if (has480p) {
			Log.d(TAG, "CamcorderProfile.QUALITY_480P");
			mediarecorder.setProfile(CamcorderProfile
					.get(CamcorderProfile.QUALITY_480P));
		} else if (hasCIF) {
			Log.d(TAG, "CamcorderProfile.QUALITY_CIF");
			mediarecorder.setProfile(CamcorderProfile
					.get(CamcorderProfile.QUALITY_CIF));
		} else if (hasQCIF) {
			Log.d(TAG, "CamcorderProfile.QUALITY_QCIF");
			mediarecorder.setProfile(CamcorderProfile
					.get(CamcorderProfile.QUALITY_QCIF));
		} else {
			Log.d(TAG, "CamcorderProfile.QUALITY_LOW");
			mediarecorder.setProfile(CamcorderProfile
					.get(CamcorderProfile.QUALITY_LOW));
		}
	}
	 /**
	  * 切换前后摄像头
	  * **/
	 public void changeCamera()
	 {
		 cameraCount=Camera.getNumberOfCameras();
			if(isCameraBack){
				isCameraBack=false;
			}else{
				isCameraBack=true;
			}
		 int cameraCount = 0;
         CameraInfo cameraInfo = new CameraInfo();
         cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
         for(int i = 0; i < cameraCount; i++) {
             Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
             if(cameraPosition == 1) {
                 //现在是后置，变更为前置
                 if(cameraInfo.facing  == CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                 	
                 	 camera.stopPreview();//停掉原来摄像头的预览
                     camera.release();//释放资源
                     camera = null;//取消原来摄像头
                     camera = Camera.open(i);//打开当前选中的摄像头
                     try {
//                    	 initCamera(camera);
                    	 camera.setDisplayOrientation(90);
                         camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                     } catch (IOException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }
                     camera.startPreview();//开始预览
                     cameraPosition = 0;
                     break;
                 }
             } else {
                 //现在是前置， 变更为后置
                 if(cameraInfo.facing  == CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                     camera.stopPreview();//停掉原来摄像头的预览
                     camera.release();//释放资源
                     camera = null;//取消原来摄像头
                     camera = Camera.open(i);//打开当前选中的摄像头
                     try {
//                    	 initCamera(camera);
                    	 camera.setDisplayOrientation(90);
                         camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                     } catch (IOException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }
                     camera.startPreview();//开始预览
                     cameraPosition = 1;
                     break;
                 }
             	}
         	}
		}
	 
	 
	/* (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		// 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
		surfaceHolder = holder;
	}
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
       
		final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;
        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
	/* (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		surfaceHolder = holder;
		try {
			if(isCameraBack){
				camera = Camera.open(CameraInfo.CAMERA_FACING_BACK);//打开摄像头
			}else{
				camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);//打开摄像头
			}
			 
			//设置camera预览的角度，因为默认图片是倾斜90度的   
			camera.setDisplayOrientation(90); 
			Size pictureSize=null;
			Size previewSize=null;
			Camera.Parameters parameters = camera.getParameters();
			parameters.setPreviewFrameRate(5);
			//设置旋转代码
			parameters.setRotation(90);  
//			parameters.setPictureFormat(PixelFormat.JPEG);
			List<Size> supportedPictureSizes = SKRSupportedSizesReflect.getSupportedPictureSizes(parameters);
			List<Size> supportedPreviewSizes = SKRSupportedSizesReflect.getSupportedPreviewSizes(parameters);

			if ( supportedPictureSizes != null &&supportedPreviewSizes != null &&supportedPictureSizes.size() > 0 &&supportedPreviewSizes.size() > 0) {
					//2.x
					pictureSize = supportedPictureSizes.get(0);
					int maxSize = 1280;
					if(maxSize > 0){
						for(Size size : supportedPictureSizes){
							if(maxSize >= Math.max(size.width,size.height)){
								pictureSize = size;
								break;
							}
						}
					}

					WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
					Display display = windowManager.getDefaultDisplay();
					DisplayMetrics displayMetrics = new DisplayMetrics();
					display.getMetrics(displayMetrics);

					previewSize = getOptimalPreviewSize(supportedPreviewSizes,display.getWidth(),display.getHeight()); 
					parameters.setPictureSize(pictureSize.width, pictureSize.height);
					parameters.setPreviewSize(previewSize.width, previewSize.height);								

				}
//			camera.setParameters(parameters);//啊护卫
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	/* (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		if(camera!=null){
			camera.release();
		}
		surfaceview = null;
		surfaceHolder = null;
		if (surfaceHolder != null) {
			surfaceHolder=null;
		}
		if (mediarecorder != null) {
			mediarecorder=null;
		}
	}
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        { 
        	stopMethod();
			finish();
        }
        return false;
    }  
	public void stopMethod()
	{
		isRecord=false;
//		recordIv.setBackgroundResource(R.drawable.video_record_start);
		recordIv.setVisibility(View.GONE);
		sendButt.setVisibility(View.VISIBLE);
		cancelButt.setVisibility(View.VISIBLE);
//		RelativeLayout.LayoutParams buttonParams = (android.widget.RelativeLayout.LayoutParams) recordIv.getLayoutParams();
//		buttonParams.width = 75;
//		buttonParams.height = 75;
		if (mediarecorder != null) {
			// 停止录制
			mediarecorder.stop();
			// 释放资源
			mediarecorder.release();
			mediarecorder = null;
			registerVideo();
		}
		if(camera!=null){
			camera.release();
		}
		timer.stop();//停止计时  
		timer.setText("00:05");

	}
	private void createVideoPath() {
		long dateTaken = System.currentTimeMillis();
		String title = createName(dateTaken);
		String filename = title + ".3gp";

		String cameraDirPath = getTempFileDir(this);
		String filePath = cameraDirPath +"/"+ filename;

		ContentValues values = new ContentValues(7);
		values.put(Video.Media.TITLE, title);
		values.put(Video.Media.DISPLAY_NAME, filename);
		values.put(Video.Media.DATE_TAKEN, dateTaken);
		values.put(Video.Media.MIME_TYPE, "video/3gpp");
		values.put(Video.Media.DATA, filePath);
		mCurrentVideoFilename = filePath;
		Log.v(TAG, "Current camera video filename: " + mCurrentVideoFilename);
		mCurrentVideoValues = values;
	}
	
	@SuppressLint("SimpleDateFormat")
	private String createName(long dateTaken) {
		Date date = new Date(dateTaken);
		SimpleDateFormat dateFormat = new SimpleDateFormat("'VID'_yyyyMMdd_HHmmss");
		return dateFormat.format(date);
	}
	
	private void registerVideo() {
		
		Uri videoTable = Uri.parse("content://media/external/video/media");
		mCurrentVideoValues.put(Video.Media.SIZE, new File(mCurrentVideoFilename).length());
		try {
			mCurrentVideoUri = mContentResolver.insert(videoTable,mCurrentVideoValues);
		} catch (Exception e) {
			// We failed to insert into the database. This can happen if
			// the SD card is unmounted.
			mCurrentVideoUri = null;
			mCurrentVideoFilename = null;
		} finally {
			Log.v(TAG, "Current video URI: " + mCurrentVideoUri);
		}
	}
	
	/**
	 * 获取临时文件存放目录
	 * 
	 * @param context
	 * @return
	 */
	public static final String getTempFileDir(Context context) {
		String path = isExternalMemoryAvailable() ? context.getExternalFilesDir(null).getPath() + "/temp/" : context.getFilesDir().getPath() + "/temp/";
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
		return path;
	}
	
	/**
	 * 判断是否有外部存储
	 * 
	 * @return 如果有返回true，否则false
	 */
	public static final boolean isExternalMemoryAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}
	
	private void doReturnToCaller(boolean valid) {
		int resultCode;
		if (valid) {
			resultCode = RESULT_OK;
			intent.setData(mCurrentVideoUri);
			intent.putExtra("recordTime", recordTime);
		} else {
			resultCode = RESULT_CANCELED;
		}
		setResult(resultCode, intent);
		finish();
	}

	 @Override
	 protected void onDestroy() {
		 super.onDestroy();
		 SKActivityManager.remove(this);
	 }
 }
