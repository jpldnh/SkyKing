package com.clubank.tpy.activitys;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.clubank.tpy.R;
import com.clubank.tpy.adapters.MyPagerAdapter;
import com.clubank.tpy.bases.BaseActivity;
import com.clubank.tpy.bases.BaseHttpInformation;
import com.clubank.tpy.models.User;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRArrayBaseResult;
import com.sking.lib.res.results.SKRBaseResult;

import java.util.ArrayList;
import java.util.List;


/**
 * 公共Activity
 */
public class CommentActivity extends BaseActivity {
    private TextView titleText;
    private TextView titleLeft;
    private TextView titleRight;
    private ViewPager viewPager;

    private List<View> views = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comment);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void findView() {
        titleText = (TextView) findViewById(R.id.top_title_text);
        titleLeft = (TextView) findViewById(R.id.top_title_left);
        titleRight = (TextView) findViewById(R.id.top_title_right);
        viewPager = (ViewPager) findViewById(R.id.ac_view_pager);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void setListener() {
        titleText.setText("公共Activity");
        titleLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleRight.setVisibility(View.GONE);
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_advice, null);
        View view2 = LayoutInflater.from(mContext).inflate(R.layout.activity_comment, null);
        View view3 = LayoutInflater.from(mContext).inflate(R.layout.activity_login, null);
        View view4 = LayoutInflater.from(mContext).inflate(R.layout.activity_set, null);
        views.add(view);
        views.add(view2);
        views.add(view3);
        views.add(view4);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("viewpager_my","onPageScrolled");
                if(position>0)
                    setSwipEnableAnyWhere(false);
            }

            @Override
            public void onPageSelected(int position) {
                if(position==0)
                    setSwipEnableAnyWhere(true);
                Log.i("viewpager_my","onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("viewpager_my","onPageScrollStateChanged");
//                setSwipEnableAnyWhere(false);
            }
        });
        MyPagerAdapter adapter = new MyPagerAdapter(views);
        viewPager.setAdapter(adapter);

    }


    @Override
    protected void networkRequestBefore(SKRNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showProgressDialog("加载中...");
                break;
            default:
                break;
        }
    }

    @Override
    protected void networkRequestAfter(SKRNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                SKRArrayBaseResult<User> user = (SKRArrayBaseResult<User>) baseResult;
                break;
            default:
                break;
        }
    }

    @Override
    protected void networkRequestParseFailed(SKRNetTask netTask, SKRBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showToastLong(baseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {
        BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showToastLong("请求失败请重试！");
                break;
            default:
                break;
        }
    }


}
