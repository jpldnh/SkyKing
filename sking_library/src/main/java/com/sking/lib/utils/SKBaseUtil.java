package com.sking.lib.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Contacts;

import com.sking.lib.config.SKConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 工具类
 */
public class SKBaseUtil {

	/*
	* 设置程序运行权限
	* */
	public static void setPerMission()
	{
		SKConfig.IS_PERMISSION = true;
	}

	/*
	* 清除程序运行权限
	* */
	public static void clearPerMission()
	{
		SKConfig.IS_PERMISSION = false;
	}

	/**
	 * 判断是否是debug模式
	 * 但是当我们没在AndroidManifest.xml中设置其debug属性时:
	 * 使用Eclipse运行这种方式打包时其debug属性为true,使用Eclipse导出这种方式打包时其debug属性为法false.
	 * 在使用ant打包时，其值就取决于ant的打包参数是release还是debug.
	 * 因此在AndroidMainifest.xml中最好不设置android:debuggable属性置，而是由打包方式来决定其值.
	 *
	 * @return
	 * @author jw
	 * @date 2017.08.29
	 */
	public static boolean isDebugable(Context context) {
		return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
	}

	/**
	 * 获取APP版本
	 *
	 * @param context
	 *            环境
	 * @return String
	 */
	public static final int getAppVersionCode(Context context)
			throws NameNotFoundException {
		PackageManager pm = context.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
		return pi.versionCode;
	}

	/**
	 * 获取APP版本
	 *
	 * @param context
	 *            环境
	 * @return String
	 */
	public static final String getAppVersionName(Context context)
			throws NameNotFoundException {
		PackageManager pm = context.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
		return pi.versionName;
	}

	/**
	 * 程序是否在前台运行
	 *
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		// Returns a list of application processes that are running on the
		// device
		ActivityManager activityManager = (ActivityManager) context
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		String packageName = context.getApplicationContext().getPackageName();
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param str
	 * @return true如果该字符串为null或者"",否则false
	 */
	public static final boolean isNull(String str) {
		return str == null || "".equals(str.trim())||"null".equals(str.trim());
	}

	/**
	 * 判断是否相等
	 *
	 * @param str
	 * @return 相等返回true,else return false
	 */
	public static boolean isEquals(Object str,Object str2) {
		if(str==null||str2==null)
			return false;
		if(str.toString().equals(str2.toString()))
			return true;
		else
			return false;
	}

	/**
	 * 判断是否是平板
	 */
	public static boolean isPhoneOrTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * 判断sd卡是否挂载
	 */
	public static boolean isSDCardMount() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取sd卡剩余空间的大小
	 */
	@SuppressWarnings("deprecation")
	public static long getSDFreeSize() {
		File path = Environment.getExternalStorageDirectory(); // 取得SD卡文件路径
		StatFs sf = new StatFs(path.getPath());
		long blockSize = sf.getBlockSize(); // 获取单个数据块的大小(Byte)
		long freeBlocks = sf.getAvailableBlocks();// 空闲的数据块的数量
		// 返回SD卡空闲大小
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 获取sd卡空间的总大小
	 */
	@SuppressWarnings("deprecation")
	public static long getSDAllSize() {
		File path = Environment.getExternalStorageDirectory(); // 取得SD卡文件路径
		StatFs sf = new StatFs(path.getPath());
		long blockSize = sf.getBlockSize(); // 获取单个数据块的大小(Byte)
		long allBlocks = sf.getBlockCount(); // 获取所有数据块数
		// 返回SD卡大小
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/*
	* 完全退出
	* */
	public static void exitApplication()
	{
		// 退出程序
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}

	/**
	 * 用当前时间给文件命名
	 *
	 * @return String yyyyMMdd_HHmmss
	 */
	public static final String getFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault());
		return dateFormat.format(date);// + ".jpg";
	}

	/**
	 * 隐藏手机号中间几位
	 */
	public static String hide(String old, String keytype) {
		try {
			if ("1".equals(keytype)) {
				return old.substring(0, 3) + "****" + old.substring(7, 11);
			} else {
				StringBuilder e = new StringBuilder();
				String[] s = old.split("@");
				int l = s[0].length();
				int z = l / 3;
				e.append(s[0].substring(0, z));
				int y = l % 3;

				for (int i = 0; i < z + y; ++i) {
					e.append("*");
				}
				e.append(s[0].substring(z * 2 + y, l));
				e.append("@");
				String var10000 = s[1];
				e.append(s[1]);
				return e.toString();
			}
		} catch (Exception var8) {
			return "";
		}
	}

	/**
	 * 拨打电话
	 *
	 * @param context
	 * @param phoneNum
	 */
	public static void call(Context context, String phoneNum) throws Exception {
		if (phoneNum != null && !phoneNum.equals("")) {
			Uri uri = Uri.parse("tel:" + phoneNum);
			Intent intent = new Intent(Intent.ACTION_DIAL, uri);
			context.startActivity(intent);
		}
	}

	/**
	 * 跳转到联系人界面
	 */
	public static void jumpToContactsList(Context context) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Contacts.People.CONTENT_URI);
		context.startActivity(intent);
	}

	/**
	 * 发送短信界面
	 */
	public static void jumpTodMessageActivity(Context context, String number) {
		try {
			Uri uri = Uri.parse("smsto:" + number);
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开网页
	 */
	public static void openWeb(Context context, String url) {
		try {
			Uri uri = Uri.parse(url);
			context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断一个apk是否安装
	 *
	 * @param context
	 * @param packageName
	 */
	public static boolean isApkInstalled(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packageName, 0);
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * 获取手机内安装的应用
	 */
	public static List<PackageInfo> getInstalledApp(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.getInstalledPackages(0);
	}


	/**
	 * 获取手机安装非系统应用
	 */
	@SuppressWarnings("static-access")
	public static List<PackageInfo> getUserInstalledApp(Context context) {
		List<PackageInfo> infos = getInstalledApp(context);
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		for (PackageInfo info : infos) {
			if ((info.applicationInfo.flags & info.applicationInfo.FLAG_SYSTEM) <= 0) {
				apps.add(info);
			}
		}
		infos.clear();
		infos = null;
		return apps;
	}

	/**
	 * 获取安装应用的信息
	 */
	public static Map<String, Object> getInstalledAppInfo(Context context, PackageInfo info) {
		Map<String, Object> appInfos = new HashMap<String, Object>();
		PackageManager pm = context.getPackageManager();
		ApplicationInfo aif = info.applicationInfo;
		appInfos.put("icon", pm.getApplicationIcon(aif));
		appInfos.put("lable", pm.getApplicationLabel(aif));
		appInfos.put("packageName", aif.packageName);
		return appInfos;
	}

	/**
	 * 打开指定包名的应用
	 */
	public static void openAppByPackage(Context context, String pkg) {
		Intent startIntent = context.getPackageManager().getLaunchIntentForPackage(pkg);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(startIntent);
	}

	/**
	 * 卸载指定包名的应用
	 */
	public static void unInstallByPackage(Context context, String packageName) {
		Uri uri = Uri.parse("package:" + packageName);
		Intent intent = new Intent(Intent.ACTION_DELETE);
		intent.setData(uri);
		context.startActivity(intent);
	}

	/*-------------------https 加密------------------------------*/
	private static X509Certificate serverCert;//本地证书
	/*
	* 初始化本地crt文件,https使用
	* */
	public static void initLocalCRT(Context mContext,String crtName,String path)
	{
		SKConfig.SERVICE_ROOT_PATH  = path;
		try {
			if(serverCert==null)
			{
				InputStream certInput = new BufferedInputStream(mContext.getAssets().open(crtName));
				CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
				serverCert = (X509Certificate) certificateFactory.generateCertificate(certInput);
			}
		} catch (IOException | CertificateException e) {
			e.printStackTrace();
		}
	}

	/*
	* 获取本地crt文件
	* */
	public static X509Certificate getLocalCRT()
	{
		return serverCert;
	}
	/*-------------------https 加密 end------------------------------*/
}
