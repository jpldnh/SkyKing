package com.sking.lib.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.sking.lib.base.SKObject;
import com.sking.lib.config.SKConfig;
import com.sking.lib.exception.SKDataParseException;
import com.sking.lib.exception.SKHttpException;
import com.sking.lib.interfaces.SKNetTaskExecuteListener;
import com.sking.lib.utils.SKDeviceUtil;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * 网络请求发送器
 */
public class SKNetWorker extends SKObject {
    /**
     * 请求成功(-1)
     */
    protected static final int SUCCESS = -1;
    /**
     * 请求异常(-2)
     */
    public static final int FAILED_HTTP = -2;
    /**
     * 数据异常(-3)
     */
    public static final int FAILED_DATAPARSE = -3;
    /**
     * 无网络(-4)
     */
    public static final int FAILED_NONETWORK = -4;
    /**
     * 获取数据前显示
     */
    private static final int BEFORE = -5;

    private Context context;
    private EventHandler eventHandler;
    private NetThread netThread;
    private SKNetTaskExecuteListener onTaskExecuteListener;

    public SKNetWorker(Context mContext) {
        Looper looper;
        if ((looper = Looper.myLooper()) != null) {
            eventHandler = new EventHandler(this, looper);
        } else if ((looper = Looper.getMainLooper()) != null) {
            eventHandler = new EventHandler(this, looper);
        } else {
            eventHandler = null;
        }

        this.context = mContext.getApplicationContext();
    }

    /**
     * 发送post请求并且获取数据.该方法可发送文件数据
     *
     * @param task 网络请求任务new SKNetTask(任务ID,任务URL, 任务参数集(参数名,参数值))
     */
    public void executeTask(SKNetTask task) {
        if (hasNetWork()) {
            synchronized (this) {
                if (netThread == null) {
                    netThread = new NetThread(task);
                    netThread.start();
                    log_d("网络线程不存在或已执行完毕,开启新线程：" + netThread.getName());
                } else {
                    log_d(netThread.getName() + "执行中,添加网络任务");
                    netThread.addTask(task);
                }
            }
        } else {
            if (onTaskExecuteListener != null) {
                onTaskExecuteListener.onPostExecute(this, task);
                onTaskExecuteListener.onExecuteFailed(this, task,
                        FAILED_NONETWORK);
            }
        }
    }

    /**
     * 判断网络任务是否都已完成
     *
     * @return
     */
    public boolean isNetTasksFinished() {
        synchronized (this) {
            return netThread == null || netThread.tasks.size() <= 0;
        }
    }

    /**
     * 取消网络请求任务
     */
    public void cancelTasks() {
        synchronized (this) {
            if (netThread != null)
                netThread.cancelTasks();
        }
    }

    /**
     * 判断当前是否有可用网络
     *
     * @return 如果有true否则false
     */
    public boolean hasNetWork() {
        return SKDeviceUtil.hasNetWork(context);
    }

    private class NetThread extends Thread {
        private ArrayList<SKNetTask> tasks = new ArrayList<SKNetTask>();
        private boolean isRun = true;

        NetThread(SKNetTask task) {
            tasks.add(task);
            setName("网络线程(" + getName() + ")");
        }

        void addTask(SKNetTask task) {
            synchronized (SKNetWorker.this) {
                tasks.add(task);
            }
        }

        void cancelTasks() {
            synchronized (SKNetWorker.this) {
                tasks.clear();
                netThread = null;
                isRun = false;
            }
        }

        boolean isHaveTask() {
            return tasks.size() > 0;
        }

        @Override
        public void run() {
            log_d(getName() + "开始执行");
            while (isRun) {
                synchronized (SKNetWorker.this) {
                    if (!isHaveTask()) {
                        isRun = false;
                        netThread = null;
                        break;
                    }
                }
                SKNetTask currTask = tasks.get(0);
                TR<SKNetTask, Object> tr = new TR<SKNetTask, Object>();
                tr.setTask(currTask);
                beforeDoTask(tr);
                Message mess = eventHandler.obtainMessage();
                doTask(tr, mess);
            }
            log_d(getName() + "执行完毕");
        }

        // 给handler发消息,执行请求任务前的操作
        private void beforeDoTask(TR<SKNetTask, Object> result) {
            Message before = new Message();
            before.what = BEFORE;
            before.obj = result;
            eventHandler.sendMessage(before);
        }

        // 执行网络请求任务
        private void doTask(TR<SKNetTask, Object> result, Message mess) {
            SKNetTask task = result.getTask();
            log_d("Do task !!!Try " + (task.getTryTimes() + 1));
            log_d("The Task Description: " + task.getDescription());
            try {
                Object object;
                if (task.getFiles() == null) {
                    JSONObject jsonObject = SKHttpUtil.sendPOSTForJSONObject(task.getId(), task.getPath(), task.getParams(), SKConfig.ENCODING);
                    object = task.parse(jsonObject);
                } else {
                    JSONObject jsonObject = SKHttpUtil.sendPOSTWithFilesForJSONObject(task.getPath(), task.getFiles(), task.getParams(), SKConfig.ENCODING);
                    object = task.parse(jsonObject);
                }

                mess.obj = result.put(task, object);
                mess.what = SUCCESS;
                // mess.arg1 = task.getId();
                tasks.remove(task);
                eventHandler.sendMessage(mess);
            } catch (SKHttpException e) {
                tryAgain(task, FAILED_HTTP, mess, result);
            } catch (SKDataParseException e) {
                tryAgain(task, FAILED_DATAPARSE, mess, result);
            }
        }

        // 失败后再试几次
        private void tryAgain(SKNetTask task, int type, Message mess,
                              TR<SKNetTask, Object> result) {
            task.setTryTimes(task.getTryTimes() + 1);
            if (task.getTryTimes() >= SKConfig.TRYTIMES_HTTP) {
                mess.what = type;
                // mess.arg1 = task.getId();
                mess.obj = result;
                tasks.remove(task);
                eventHandler.sendMessage(mess);
            }
        }
    }

    public Context getContext() {
        return context;
    }

    public SKNetTaskExecuteListener getOnTaskExecuteListener() {
        return onTaskExecuteListener;
    }

    public void setOnTaskExecuteListener(
            SKNetTaskExecuteListener onTaskExecuteListener) {
        this.onTaskExecuteListener = onTaskExecuteListener;
    }

    private static class EventHandler extends Handler {
        private SKNetWorker netWorker;

        public EventHandler(SKNetWorker netWorker, Looper looper) {
            super(looper);
            this.netWorker = netWorker;
        }

        private SKNetTaskExecuteListener getOnTaskExecuteListener() {
            return netWorker.getOnTaskExecuteListener();
        }

        @Override
        public void handleMessage(Message msg) {
            SKNetTaskExecuteListener listener = getOnTaskExecuteListener();
            if (listener != null) {
                @SuppressWarnings("unchecked")
                TR<SKNetTask, Object> result = (TR<SKNetTask, Object>) msg.obj;
                switch (msg.what) {
                    case SUCCESS:
                        listener.onExecuteSuccess(netWorker, result.getTask(),
                                result.getResult());
                        listener.onPostExecute(netWorker, result.getTask());
                        break;
                    case FAILED_HTTP:
                        listener.onExecuteFailed(netWorker, result.getTask(),
                                FAILED_HTTP);
                        listener.onPostExecute(netWorker, result.getTask());
                        break;
                    case FAILED_DATAPARSE:
                        listener.onExecuteFailed(netWorker, result.getTask(),
                                FAILED_DATAPARSE);
                        listener.onPostExecute(netWorker, result.getTask());
                        break;
                    case BEFORE:
                        listener.onPreExecute(netWorker, result.getTask());
                        break;
                    default:
                        listener.onPostExecute(netWorker, result.getTask());
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 网络请求任务和请求返回结果的对应关系
     *
     * @param <Task>   网络请求任务
     * @param <Result> 请求返回结果
     */
    private class TR<Task, Result> {
        private Task t;
        private Result r;

        /**
         * 实例化一个 网络请求任务和请求返回结果的对应关系
         *
         * @param t 网络请求任务
         * @param r 请求返回结果
         * @return
         */
        public TR<Task, Result> put(Task t, Result r) {
            setTask(t);
            setResult(r);
            return this;
        }

        /**
         * 设置网络请求任务
         *
         * @param t 网络请求任务实例
         */
        public void setTask(Task t) {
            this.t = t;
        }

        /**
         * 设置请求返回结果
         *
         * @param r 请求返回结果实例
         */
        public void setResult(Result r) {
            this.r = r;
        }

        /**
         * 获取网络请求任务
         *
         * @return 网络请求任务实例
         */
        public Task getTask() {
            return t;
        }

        /**
         * 获取请求返回结果
         *
         * @return 请求返回结果实例
         */
        public Result getResult() {
            return r;
        }
    }
}
