package com.sking.lib.res.bases;

import com.sking.lib.net.SKNetTask;

import java.util.HashMap;


/**
 * 网络请求任务
 */
public abstract class SKRNetTask extends SKNetTask {
	private SKRHttpInfomation requestInformation;

	/**
	 * 实例化网络请求任务
	 * 
	 * @param information
	 *            网络请求信息
	 * @param params
	 *            任务参数集(参数名,参数值)
	 */
	public SKRNetTask(SKRHttpInfomation information,
					  HashMap<String, String> params) {
		this(information, params, null);
	}

	/**
	 * 实例化网络请求任务
	 * 
	 * @param information
	 *            网络请求信息
	 * @param params
	 *            任务参数集(参数名,参数值)
	 * @param files
	 *            任务文件集(参数名,文件的本地路径)
	 */
	public SKRNetTask(SKRHttpInfomation information, HashMap<String, String> params, HashMap<String, String> files) {
		super(information.getId(), information.getUrlPath(), params, files,
				information.getDescription());
		this.requestInformation = information;
	}

	/**
	 * @return 网络请求信息
	 */
	public SKRHttpInfomation getHttpInformation() {
		return requestInformation;
	}

}
