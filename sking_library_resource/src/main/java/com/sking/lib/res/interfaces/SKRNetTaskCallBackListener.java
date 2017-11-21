package com.sking.lib.res.interfaces;

import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.bases.SKRNetWorker;
import com.sking.lib.res.results.SKRBaseResult;

/**
 * Created by Im_jingwei on 2017/8/24.
 */

public interface SKRNetTaskCallBackListener {
    /**
     * 返回数据前的操作，如显示进度条
     *
     * @param netTask
     */
    public void networkRequestBefore(SKRNetTask netTask);

    /**
     * 返回数据后的操作，如关闭进度条
     *
     * @param netTask
     */
    public void networkRequestAfter(SKRNetTask netTask);

    /**
     * 服务器处理成功
     *
     * @param netTask
     * @param baseResult
     */
    public void networkRequestSuccess(SKRNetTask netTask,
                                                  SKRBaseResult baseResult);

    /**
     * 服务器处理或数据解析失败
     *
     * @param netTask
     * @param baseResult
     */
    public void networkRequestParseFailed(SKRNetTask netTask,
                                                      SKRBaseResult baseResult);

    /**
     * 获取数据失败
     *
     * @param netTask
     * @param failedType 失败原因
     *                   <p>
     *                   See {@link SkNetWorker#FAILED_DATAPARSE
     *                   XtomNetWorker.FAILED_DATAPARSE},
     *                   {@link SkNetWorker#FAILED_HTTP XtomNetWorker.FAILED_HTTP},
     *                   {@link SkNetWorker#FAILED_NONETWORK
     *                   XtomNetWorker.FAILED_NONETWORK}
     *                   </p>
     */
    public void networkRequestExecuteFailed(SKRNetTask netTask,
                                                        int failedType);

}
