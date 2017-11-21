package com.sking.lib.interfaces;


import com.sking.lib.imageload.SKImageTask;

/**
 * Created by 谁说青春不能错 on 2016/11/29.
 */

public interface SKImageTaskExecuteListener {

    /*
    * Runs on the UI thread before the task run.
    */
    public void onPreExecute(SKImageTask task);

    /**
     * Runs on the UI thread after the task run.
     */
    public void onPostExecute(SKImageTask task);

    /**
     * Runs on the UI thread when the task run success.
     *
     * @param result
     *            the result of the server back.
     */
    public void onExecuteSuccess(SKImageTask task);

    /**
     * Runs on the UI thread when the task run failed.
     */
    public void onExecuteFailed(SKImageTask task);
}
