package com.sking.lib.res.bases;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.sking.lib.base.SKActivityManager;
import com.sking.lib.base.SKObject;
import com.sking.lib.fileload.SKFileDownLoader;
import com.sking.lib.fileload.SKFileInfo;
import com.sking.lib.interfaces.SKDownLoadListener;
import com.sking.lib.utils.SKFileUtil;
import com.sking.lib.utils.SKToastUtil;

import java.io.File;


/**
 * 软件升级
 */
public class SKRUpGrade extends SKObject {
	private Context mContext;
	private SKRUser mUser;
	private String savePath;

	public SKRUpGrade(SKRUser user) {
		this.mContext = SKActivityManager.getLastActivity();
		this.mUser = user;
	}

	// 是否强制升级
	private boolean isMust() {
		boolean must = "1".equals(mUser.getAndroid_must_update());
		return must;
	}

	public void alert(String curr, String server) {
		Builder ab = new Builder(mContext);
		ab.setTitle("软件更新");
		String message = "当前客户端版本是" + curr + ",服务器最新版本是" + server + ",确定要升级吗？";
		ab.setMessage(message);
		ab.setPositiveButton("升级", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				upGrade();
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				if (isMust())
					SKActivityManager.finishAll();
			}
		});
		ab.setCancelable(false);
		ab.show();
	}

	public void upGrade() {
		String downPath = mUser.getAndroid_update_url();
		savePath = SKFileUtil.getFileDir(mContext) + "/apps/hemaapp_"
				+ mUser.getAndroid_last_version() + ".apk";
		SKFileDownLoader downLoader = new SKFileDownLoader(mContext,
				downPath, savePath);
		downLoader.setThreadCount(3);
		downLoader.setXtomDownLoadListener(new DownLoadListener());
		downLoader.start();
	}

	private class DownLoadListener implements SKDownLoadListener {
		private ProgressDialog pBar;

		@Override
		public void onStart(final SKFileDownLoader loader) {
			pBar = new ProgressDialog(mContext) {
				@Override
				public void onBackPressed() {
					loader.stop();
				}
			};
			pBar.setTitle("正在下载");
			pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pBar.setMax(100);
			pBar.setCancelable(false);
			pBar.show();
		}

		@Override
		public void onSuccess(SKFileDownLoader loader) {
			if (pBar != null) {
				pBar.cancel();
			}
			install();
		}

		void install() {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(savePath)),
					"application/vnd.android.package-archive");
			mContext.startActivity(intent);
		}

		@Override
		public void onFailed(SKFileDownLoader loader) {
			if (pBar != null) {
				pBar.cancel();
			}
			SKToastUtil.showShortToast(mContext, "下载失败了");
		}

		@Override
		public void onLoading(SKFileDownLoader loader) {
			SKFileInfo SKFileInfo = loader.getFileInfo();
			int curr = SKFileInfo.getCurrentLength();
			int cont = SKFileInfo.getContentLength();
			int per = (int) ((float) curr / (float) cont * 100);
			if (pBar != null) {
				pBar.setProgress(per);
			}
		}

		@Override
		public void onStop(SKFileDownLoader loader) {
			if (pBar != null) {
				pBar.cancel();
			}
			SKToastUtil.showShortToast(mContext, "下载停止");
			if (isMust())
				SKActivityManager.finishAll();
		}
	}

}
