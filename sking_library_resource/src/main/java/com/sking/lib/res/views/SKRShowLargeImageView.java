package com.sking.lib.res.views;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;

import com.sking.lib.base.SKObject;
import com.sking.lib.imageload.SKImageCache;
import com.sking.lib.imageload.SKImageTask;
import com.sking.lib.imageload.SKImageWorker;
import com.sking.lib.res.R;
import com.sking.lib.res.photoviiew.SKRPhotoView;
import com.sking.lib.res.photoviiew.SKRPhotoViewAttacher;
import com.sking.lib.utils.SKFileUtil;
import com.sking.lib.utils.SKTimeUtil;
import com.sking.lib.utils.SKToastUtil;

import java.net.MalformedURLException;
import java.net.URL;


public class SKRShowLargeImageView extends SKObject {
	private Activity mContext;
	private View father;
	private PopupWindow mWindow;
	private ViewGroup mViewGroup;

	private SKRPhotoView mImageView;
	private ProgressBar mProgressBar;
	private SKImageWorker imageWorker;
	private String localPath;
	private String urlPath;

	public SKRShowLargeImageView(Activity context, View activityRootView) {
		mContext = context;
		imageWorker = new SKImageWorker(mContext);
		father = activityRootView;
		mWindow = new PopupWindow(mContext);
		mWindow.setWidth(LayoutParams.MATCH_PARENT);
		mWindow.setHeight(LayoutParams.MATCH_PARENT);
		mWindow.setBackgroundDrawable(new ColorDrawable(0x55000000));
		mWindow.setFocusable(true);
		mWindow.setAnimationStyle(R.style.SKR_Style_PopuWindow_Bottom);
		mViewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(
				R.layout.skr_layout_view_show_large_img, null);
		findView();
		setListener();
		mWindow.setContentView(mViewGroup);

	}

	/**
	 * 设置网络图片地址
	 * 
	 * @params path
	 */
	public void setImageURL(String urlPath) {
		this.urlPath = urlPath;
		this.localPath = null;
		try {
			URL url = new URL(urlPath);
			ImageTask task = new ImageTask(mImageView, url, mContext);

			imageWorker.loadImage(task);

		} catch (MalformedURLException e) {
			//
		}
	}

	/**
	 * 设置本地图片地址
	 * 
	 * @param localPath
	 */
	public void setImagePath(String localPath) {
		this.urlPath = null;
		this.localPath = localPath;
		ImageTask task = new ImageTask(mImageView, localPath, mContext);
		imageWorker.loadImage(task);

	}

	private void findView() {
		mImageView = (SKRPhotoView) mViewGroup.findViewById(R.id.imageview);
		mProgressBar = (ProgressBar) mViewGroup.findViewById(R.id.progressbar);
	}

	private void setListener() {
		mWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});
		mViewGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dimiss();
			}
		});
		mImageView.setOnPhotoTapListener(new SKRPhotoViewAttacher.OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View view, float x, float y) {
				dimiss();
			}
		});
		mImageView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Builder builder = new Builder(mContext);
				String[] items = { "保存到手机", "取消" };
				builder.setItems(items, new DialogClickListener());
				builder.show();
				return true;
			}
		});
	}

	private class DialogClickListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0:// 保存到手机
				copy();
				break;
			case 1:// 取消

				break;
			}
		}

		// 复制文件
		private void copy() {
			if (!SKFileUtil.isExternalMemoryAvailable()) {
				SKToastUtil.showShortToast(mContext, "没有SD卡,不能复制");
				return;
			}
			String imgPath;
			if (isNull(urlPath)) {
				imgPath = localPath;
			} else {
				imgPath = SKImageCache.getInstance(mContext).getPathAtLoacal(
						urlPath);
			}
			String saveDir = SKFileUtil.getExternalMemoryPath();
			String pakage = mContext.getPackageName();
			String folder = "images";
			int dot = pakage.lastIndexOf('.');
			if (dot != -1) {
				folder = pakage.substring(dot + 1);
			}
			saveDir += ("/hemaapp/" + folder + "/");
			String fileName = SKTimeUtil
					.getCurrentTime("yyyy-MM-dd_HH-mm-ss") + ".jpg";
			String savePath = saveDir + fileName;
			if (SKFileUtil.copy(imgPath, savePath)) {
				SKToastUtil.showShortToast(mContext, "图片已保存至" + saveDir);
			} else {
				SKToastUtil.showShortToast(mContext, "图片保存失败");
			}
		}
	}

	public void show() {
		mWindow.showAtLocation(father, Gravity.BOTTOM, 0, 0);
	}

	public void dimiss() {
		mWindow.dismiss();
	}

	private class ImageTask extends SKImageTask {

		public ImageTask(ImageView imageView, URL url, Object context) {
			super(imageView, url, context);
		}

		public ImageTask(ImageView imageView, String path, Object context) {
			super(imageView, path, context);
		}

		@Override
		public void success() {
			mImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
			super.success();
		}

		@Override
		public void failed() {
			mImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
			super.failed();
		}

		@Override
		public void beforeload() {
			mImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
			super.beforeload();
		}

	}
}
