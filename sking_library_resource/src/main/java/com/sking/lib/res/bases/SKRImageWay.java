package com.sking.lib.res.bases;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.sking.lib.base.SKObject;
import com.sking.lib.res.R;
import com.sking.lib.utils.SKBaseUtil;
import com.sking.lib.utils.SKFileUtil;

import java.io.File;


/**
 * 选择图片方式
 */
public class SKRImageWay extends SKObject {
	private Activity mContext;// 上下文对象
	private Fragment mFragment;// 上下文对象
	private Builder mBuilder;// 弹出对象
	protected int albumRequestCode;// 相册选择时startActivityForResult方法的requestCode值
	protected int cameraRequestCode;// 拍照选择时startActivityForResult方法的requestCode值
	private static final String IMAGE_TYPE = ".jpg";// 图片名后缀
	private String imagePathByCamera;// 拍照时图片保存路径

	/**
	 * 创建一个选择图片方式实例
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param albumRequestCode
	 *            相册选择时startActivityForResult方法的requestCode值
	 * @param cameraRequestCode
	 *            拍照选择时startActivityForResult方法的requestCode值
	 */
	public SKRImageWay(Activity mContext, int albumRequestCode,
                       int cameraRequestCode) {
		this.mContext = mContext;
		this.albumRequestCode = albumRequestCode;
		this.cameraRequestCode = cameraRequestCode;
	}

	/**
	 * 创建一个选择图片方式实例
	 * 
	 * @params mContext
	 *            上下文对象
	 * @param albumRequestCode
	 *            相册选择时startActivityForResult方法的requestCode值
	 * @param cameraRequestCode
	 *            拍照选择时startActivityForResult方法的requestCode值
	 */
	public SKRImageWay(Fragment mFragment, int albumRequestCode,
                       int cameraRequestCode) {
		this.mFragment = mFragment;
		this.albumRequestCode = albumRequestCode;
		this.cameraRequestCode = cameraRequestCode;
	}

	/**
	 * 显示图片选择对话
	 */
	public void show() {
		if (mBuilder == null) {
			mBuilder = new Builder(mContext == null ? mFragment.getActivity()
					: mContext);
			mBuilder.setTitle("请选择");
			mBuilder.setItems(R.array.skr_imgway, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					click(which);
				}
			});
		}
		mBuilder.show();
	}

	private void click(int which) {
		switch (which) {
		case 0:
			album();
			break;
		case 1:
			camera();
			break;
		case 2:
			break;
		}
	}

	public void album() {
		Intent it1;
		if (Build.VERSION.SDK_INT < 19) {
			it1 = new Intent(Intent.ACTION_GET_CONTENT);
			it1.setType("image/*");
		} else {
			it1 = new Intent(
					Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		if (mContext != null)
			mContext.startActivityForResult(it1, albumRequestCode);
		else
			mFragment.startActivityForResult(it1, albumRequestCode);
	}

	public void camera() {
		String imageName = SKBaseUtil.getFileName() + IMAGE_TYPE;
		Intent it3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String imageDir = SKFileUtil
				.getTempFileDir(mContext == null ? mFragment.getActivity()
						: mContext);
		imagePathByCamera = imageDir + imageName;
		File file = new File(imageDir);
		if (!file.exists())
			file.mkdir();
		// 设置图片保存路径
		File out = new File(file, imageName);
		Uri uri = Uri.fromFile(out);
		it3.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		if (mContext != null)
			mContext.startActivityForResult(it3, cameraRequestCode);
		else
			mFragment.startActivityForResult(it3, cameraRequestCode);
	}

	/**
	 * 获取拍照图片路径
	 * 
	 * @return 图片路径
	 */
	public String getCameraImage() {
		return imagePathByCamera;
	}

}