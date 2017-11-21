package com.sking.lib.interfaces;

import com.sking.lib.fileload.SKFileDownLoader;

/**
 * 下载监听
 *
 * Created by 谁说青春不能错 on 2016/11/30.
 */

public interface SKDownLoadListener {

    public void onStart(SKFileDownLoader loader);

    public void onSuccess(SKFileDownLoader loader);

    public void onFailed(SKFileDownLoader loader);

    public void onLoading(SKFileDownLoader loader);

    public void onStop(SKFileDownLoader loader);

}
