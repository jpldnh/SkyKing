package com.sking.lib.res.adapters;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sking.lib.base.SKActivity;
import com.sking.lib.imageload.SKImageCache;
import com.sking.lib.classes.SKSize;
import com.sking.lib.imageload.SKImageTask;
import com.sking.lib.res.R;
import com.sking.lib.res.activitys.SKRShowLargePicActivity;
import com.sking.lib.res.photoviiew.SKRPhotoView;
import com.sking.lib.res.photoviiew.SKRPhotoViewAttacher;
import com.sking.lib.utils.SKBaseUtil;
import com.sking.lib.utils.SKFileUtil;
import com.sking.lib.utils.SKScreenUtil;
import com.sking.lib.utils.SKTimeUtil;
import com.sking.lib.utils.SKToastUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 */
public class SKRPhotoPagerAdapter extends PagerAdapter {
	private SKActivity context;
	private ArrayList<String> urllist;

	public SKRPhotoPagerAdapter(SKActivity context, ArrayList<String> urllist) {
		this.context = context;
		this.urllist = urllist;
	}

	@Override
	public int getCount() {
		return urllist == null ? 0 : urllist.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.skr_layout_adapter_show_large_picture, null);

		SKRPhotoView photoView = (SKRPhotoView) view.findViewById(R.id.imageview);
		ProgressBar progressBar = (ProgressBar) view
				.findViewById(R.id.progressbar);
		photoView.setOnViewTapListener(new SKRPhotoViewAttacher.OnViewTapListener() {

			@Override
			public void onViewTap(View view, float x, float y) {
				SKRShowLargePicActivity a = (SKRShowLargePicActivity) context;
				a.toogleInfo();
			}
		});
		// Now just add BaseSKRPhotoView to ViewPager and return it
		container.addView(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		String imgPath = urllist.get(position);
		try {
			URL url = new URL(imgPath);
			context.imageWorker.loadImage(new ImageTask(photoView, url,
					context, photoView, progressBar));
			photoView.setOnLongClickListener(new LongClickListener(null,
					imgPath));
		} catch (MalformedURLException e) {
			int width = SKScreenUtil.getWidth(context);
			context.imageWorker.loadImage(new ImageTask(photoView, imgPath,
					context, new SKSize(width, width), photoView, progressBar));
			photoView.setOnLongClickListener(new LongClickListener(imgPath,
					null));
		}
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	private class LongClickListener implements OnLongClickListener {
		private String localPath;
		private String urlPath;

		public LongClickListener(String localPath, String urlPath) {
			this.localPath = localPath;
			this.urlPath = urlPath;
		}

		@Override
		public boolean onLongClick(View v) {
			Builder builder = new Builder(context);
			String[] items = { "保存到手机", "取消" };
			builder.setItems(items, new DialogClickListener(localPath, urlPath));
			builder.show();
			return true;
		}

	}

	private class DialogClickListener implements
			DialogInterface.OnClickListener {
		private String localPath;
		private String urlPath;

		public DialogClickListener(String localPath, String urlPath) {
			this.localPath = localPath;
			this.urlPath = urlPath;
		}

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
			try {
				if (!SKFileUtil.isExternalMemoryAvailable()) {
					SKToastUtil.showShortToast(context, "没有SD卡,不能复制");
					return;
				}
				String imgPath;
				if (SKBaseUtil.isNull(urlPath)) {
					imgPath = localPath;
				} else {
					imgPath = SKImageCache.getInstance(context)
							.getPathAtLoacal(urlPath);
				}
				String saveDir = SKFileUtil.getExternalMemoryPath();
				String pakage = context.getPackageName();
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
					SKToastUtil.showShortToast(context, "图片已保存至" + saveDir);
					Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					Uri uri = Uri.fromFile(new File(savePath));
					intent.setData(uri);
					context.sendBroadcast(intent);//
				} else {
					SKToastUtil.showShortToast(context, "图片保存失败");
				}
			} catch (Exception e) {
				SKToastUtil.showShortToast(context, "图片保存失败");
			}
		}
	}

	private class ImageTask extends SKImageTask {
		SKRPhotoView photoView;
		ProgressBar progressBar;

		public ImageTask(ImageView imageView, URL url, Object context,
				SKRPhotoView photoView, ProgressBar progressBar) {
			super(imageView, url, context);
			this.photoView = photoView;
			this.progressBar = progressBar;
		}

		public ImageTask(ImageView imageView, String path, Object context,
                         SKSize size, SKRPhotoView photoView, ProgressBar progressBar) {
			super(imageView, path, context, size);
			this.photoView = photoView;
			this.progressBar = progressBar;
		}

		@Override
		public void success() {
			photoView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			super.success();
		}

		@Override
		public void failed() {
			photoView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			super.failed();
		}

		@Override
		public void beforeload() {
			photoView.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			super.beforeload();
		}

	}

}
