package com.skingapp.commen.bases;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.skingapp.commen.models.SysInitInfo;
import com.sking.lib.base.SKObject;
import com.sking.lib.fileload.SKFileDownLoader;
import com.sking.lib.fileload.SKFileInfo;
import com.sking.lib.interfaces.SKDownLoadListener;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.bases.SKRNetWorker;
import com.sking.lib.res.results.SKRArrayBaseResult;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRUtil;
import com.sking.lib.utils.SKFileUtil;
import com.sking.lib.utils.SKToastUtil;

import java.io.File;


/**
 * 软件升级
 */
public class BaseUpGrade extends SKObject {
	private long checkTime = 0;
	private Context mContext;
	private String savePath;
	private BaseNetWorker netWorker;
	private SysInitInfo sysInitInfo;

	public BaseUpGrade(Context mContext) {
		this.mContext = mContext;
		this.netWorker = new BaseNetWorker(mContext);
		this.netWorker.setOnTaskExecuteListener(new TaskExecuteListenerSK(mContext));
	}

	/**
	 * 检查升级
	 */
	public void check() {
		long currentTime = System.currentTimeMillis();
		boolean isCanCheck = checkTime == 0 || currentTime - checkTime > 1000 * 60 * 60 * 24;
		if (isCanCheck) {
			netWorker.init();
		}
	}

	// 是否强制升级
	private boolean isMust() {
		SysInitInfo sysInfo = BaseApplication.getInstance().getSysInitInfo();
		boolean must = "1".equals(sysInfo.getAndroid_must_update());
		return must;
	}

	private class TaskExecuteListenerSK extends BaseNetTaskExecuteListener {

		/**
		 * @param context
		 */
		public TaskExecuteListenerSK(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onPreExecute(SKRNetWorker netWorker, SKRNetTask netTask) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPostExecute(SKRNetWorker netWorker, SKRNetTask netTask) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServerSuccess(SKRNetWorker netWorker,
                                    SKRNetTask netTask, SKRBaseResult baseResult) {
			checkTime = System.currentTimeMillis();
			@SuppressWarnings("unchecked")
            SKRArrayBaseResult<SysInitInfo> sResult = (SKRArrayBaseResult<SysInitInfo>) baseResult;
			sysInitInfo = sResult.getObjects().get(0);
			String sysVersion = sysInitInfo.getAndroid_last_version();
			String version = SKRUtil.getAppVersionForTest(mContext);
			if (SKRUtil.isNeedUpDate(version, sysVersion)) {
				alert(version, sysVersion);
			}
		}

		@Override
		public void onServerFailed(SKRNetWorker netWorker,
                                   SKRNetTask netTask, SKRBaseResult baseResult) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onExecuteFailed(SKRNetWorker netWorker,
                                    SKRNetTask netTask, int failedType) {
			// TODO Auto-generated method stub

		}

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
				upGrade(sysInitInfo);
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				if (isMust())
					BaseUtil.exit(mContext);
			}
		});
		ab.setCancelable(false);
		ab.show();
	}

	public void upGrade(SysInitInfo sysInitInfo) {
		String downPath = sysInitInfo.getAndroid_update_url();
		savePath = SKFileUtil.getFileDir(mContext) + "/apps/mmzzb_"+ sysInitInfo.getAndroid_last_version() + ".apk";
		SKFileDownLoader downLoader = new SKFileDownLoader(mContext,downPath, savePath);
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
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(new File(savePath)),"application/vnd.android.package-archive");
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
			SKFileInfo fileInfo = loader.getFileInfo();
			int curr = fileInfo.getCurrentLength();
			int cont = fileInfo.getContentLength();
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
				BaseUtil.exit(mContext);
		}

	}

}
