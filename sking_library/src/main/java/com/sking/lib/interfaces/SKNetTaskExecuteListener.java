package com.sking.lib.interfaces;

import com.sking.lib.net.SKNetTask;
import com.sking.lib.net.SKNetWorker;

/**
 * Created by 谁说青春不能错 on 2016/11/29.
 */

public interface SKNetTaskExecuteListener {
        /**
         * Runs on the UI thread before the task run.
         */
        public void onPreExecute(SKNetWorker netWorker, SKNetTask task);

        /**
         * Runs on the UI thread after the task run.
         */
        public void onPostExecute(SKNetWorker netWorker, SKNetTask task);

        /**
         * Runs on the UI thread when the task run success.
         *
         * @param result
         *            the result of the server back.
         */
        public void onExecuteSuccess(SKNetWorker netWorker, SKNetTask task,
                                     Object result);

        /**
         * Runs on the UI thread when the task run failed.
         *
         * @param failedType
         *            the type of cause the task failed.
         *            <p>
         *            See {@link SKNetWorker#FAILED_DATAPARSE
         *            XtomNetWorker.FAILED_DATAPARSE},
         *            {@link SKNetWorker#FAILED_HTTP
         *            XtomNetWorker.FAILED_HTTP},
         *            {@link SKNetWorker#FAILED_NONETWORK
         *            XtomNetWorker.FAILED_NONETWORK}
         *            </p>
         */
        public void onExecuteFailed(SKNetWorker netWorker, SKNetTask task,
                                    int failedType);
}
