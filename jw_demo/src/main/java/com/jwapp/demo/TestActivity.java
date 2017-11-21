package com.jwapp.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.jwapp.demo.adapters.TestAdapter;
import com.sking.lib.base.SKActivity;
import com.sking.lib.net.SKNetTask;

import java.util.ArrayList;

public class TestActivity extends SKActivity {

    private int curr = -1;
    private TimeThread timeThread;
    private TestAdapter adapter;
    private ListView listView;
    private ArrayList<String> list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_test2);
        super.onCreate(savedInstanceState);
        getData();
        adapter.notifyDataSetChanged();
        timeThread = new TimeThread(new TimeHandler(this));
        timeThread.start();
    }

    @Override
    protected boolean onKeyBack() {
        return false;
    }

    @Override
    protected boolean onKeyMenu() {
        return false;
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void findView() {

        listView = (ListView) findViewById(R.id.ac_listview);
    }

    @Override
    protected void setListener() {

        adapter = new TestAdapter(mContext, list);
        listView.setAdapter(adapter);
    }

    @Override
    protected void networkRequestBefore(SKNetTask netTask) {

    }

    @Override
    protected void networkRequestAfter(SKNetTask netTask) {

    }

    @Override
    protected void networkRequestSuccess(SKNetTask netTask, Object result) {

    }

    private void getData() {
        list.add("我是第1个。。。。。。。。。。。。。。");
        list.add("我是第2个。。。。。。。。。。。。。。");
        list.add("我是第3个。。。。。。。。。。。。。。");
        list.add("我是第4个。。。。。。。。。。。。。。");
        list.add("我是第5个。。。。。。。。。。。。。。");
        list.add("我是第6个。。。。。。。。。。。。。。");
    }


    private class TimeThread extends Thread {

        private TimeHandler timeHandler;

        public TimeThread(TimeHandler timeHandler) {
            this.timeHandler = timeHandler;
        }

        void cancel() {
            curr = 0;
        }

        @Override
        public void run() {
            while (curr < 0) {
                timeHandler.sendEmptyMessage(0x001);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }

    private static class TimeHandler extends Handler {
        TestActivity activity;

        public TimeHandler(TestActivity activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                   activity.changeData();
                    break;
                default:
                    break;
            }
        }
    }

    private void changeData() {
        list.add(list.get(0));
        list.remove(0);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        if (timeThread != null)
            timeThread.cancel();
        super.onDestroy();
    }
}
