package com.clubank.tpy.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.clubank.tpy.R;
import com.clubank.tpy.bases.BaseFragmentActivity;
import com.clubank.tpy.fragments.FirstPageFragment;
import com.clubank.tpy.fragments.MineFragment;
import com.clubank.tpy.fragments.SecondPageFragment;
import com.clubank.tpy.fragments.ThirdPageFragment;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.utils.SKToastUtil;

import java.util.List;

public class MainActivity extends BaseFragmentActivity {
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        toogleFragment(FirstPageFragment.class);
        // ShareSDK分享相关
//		ShareSDK.initSDK(this);
        // ShareSDK分享相关end

        // 百度推送相关
//		startPush();
        // 百度推送相关end

        //极光推送
        registerJPushReceiver();
        //极光推送end
    }

    @Override
    protected void onDestroy() {
        // ShareSDK分享相关
//		ShareSDK.stopSDK(this);
        // ShareSDK分享相关end

        // 百度推送相关
//		stopPush();
        // 百度推送相关end
        super.onDestroy();
    }

    @Override
    protected void networkRequestBefore(SKRNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void networkRequestAfter(SKRNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void networkRequestSuccess(SKRNetTask netTask,
                                         SKRBaseResult baseResult) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void networkRequestParseFailed(SKRNetTask netTask,
                                             SKRBaseResult baseResult) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void findView() {
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void setListener() {
        radioGroup.setOnCheckedChangeListener(new OnTabListener());
    }

    private class OnTabListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radiobutton0:// 首页
                    toogleFragment(FirstPageFragment.class);
                    break;
                case R.id.radiobutton1:// 第二
                    toogleFragment(SecondPageFragment.class);
                    break;
                case R.id.radiobutton2:// 第三
                    toogleFragment(ThirdPageFragment.class);
                    break;
                case R.id.radiobutton3:// 个人中心
                    toogleFragment(MineFragment.class);
                    break;
            }
        }

    }

    /**
     * 显示或更换Fragment
     *
     * @param c
     */
    public void toogleFragment(Class<? extends Fragment> c) {
        FragmentManager manager = getSupportFragmentManager();
        String tag = c.getName();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);

        if (fragment == null) {
            try {
                fragment = c.newInstance();
                // 替换时保留Fragment,以便复用
                transaction.add(R.id.content_frame, fragment, tag);
            } catch (Exception e) {
                // ignore
            }
        } else {
            // nothing
        }
        // 遍历存在的Fragment,隐藏其他Fragment
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null)
            for (Fragment fm : fragments)
                if (!fm.equals(fragment))
                    transaction.hide(fm);

        transaction.show(fragment);
        transaction.commit();
    }

    //	public void saveDevice() {
    //		User user = getApplicationContext().getUser();
    //		getNetWorker().deviceSave(user.getToken(),
    //				PushUtils.getUserId(mContext), "2",
    //				PushUtils.getChannelId(mContext));
    //	}

    /*************   极光推送 start  *************/
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";

    public void registerJPushReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String title = intent.getStringExtra("title");
                    String messge = intent.getStringExtra("message");
                    String extras = intent.getStringExtra("extras");
                    SKToastUtil.showLongToast(mContext, messge + extras);
                }
            } catch (Exception e) {
            }
        }
    }
    /*************   极光推送 send  *************/
}
