package com.clubank.tpy.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clubank.tpy.R;
import com.clubank.tpy.activitys.CommentActivity;
import com.clubank.tpy.bases.BaseFragment;
import com.clubank.tpy.bases.BaseHttpInformation;
import com.clubank.tpy.models.User;
import com.clubank.tpy.umen.UmenShare;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.interfaces.SKRNetTaskCallBackListener;
import com.sking.lib.res.results.SKRArrayBaseResult;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRViewUtil;

import java.util.ArrayList;


/**
 * 帖子
 */
public class FirstPageFragment extends BaseFragment implements SKRNetTaskCallBackListener {
    private TextView titleText;
    private TextView titleLeft;
    private TextView titleRight;

    private Button textButton;
    private TextView textView;
    private Button shareButton;
    private Button testButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_first_page);
        super.onCreate(savedInstanceState);
        UmenShare.initShare(getActivity());
    }

    @Override
    protected void findView() {
        titleText = (TextView) findViewById(R.id.top_title_text);
        titleLeft = (TextView) findViewById(R.id.top_title_left);
        titleRight = (TextView) findViewById(R.id.top_title_right);
        textButton = (Button) findViewById(R.id.test);
        textView = (TextView) findViewById(R.id.textview);
        shareButton = (Button) findViewById(R.id.share);
    }

    @Override
    public void setListener() {
        titleLeft.setVisibility(View.GONE);
        titleText.setText("页面一");
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getNetWorker().clientGet(BaseApplication.getInstance().getUser().getToken(),"140");
//                Log.i("deviceInfo",SKDeviceUtil.getDeviceInfo(mContext));
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("PAY_AMOUNT","0.01");
                startActivity(intent);
            }
        });
        SKRViewUtil.setTextViewUnderLine(textView);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmenShare.show("我是标题","我是内容","www.baidu.com",R.mipmap.img_default_avatar);
            }
        });

    }

    @Override
    public void networkRequestBefore(SKRNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showProgressDialog("正在操作...");
                break;
            default:
                break;
        }
    }

    @Override
    public void networkRequestAfter(SKRNetTask netTask) {
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
    public void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
				SKRArrayBaseResult<User> user = (SKRArrayBaseResult<User>) baseResult;
				ArrayList<User> ss = ((SKRArrayBaseResult<User>) baseResult).getObjects();
                showLongToast(ss.toString());
//                JSONObject jsonObject = baseResult.getJsonObject();
//                try {
//                    long start = System.currentTimeMillis();
//                    JSONObject xml = XML.toJSONObject(baseResult.getInfo());
//                    CountryModel student = JSON.parseObject(xml.toString(), new TypeReference<CountryModel>() {
//                    });
//                    String st = student.getProcessResultOfCountry().getList().getCountry().get(0).getCountryName();
//                    long end = System.currentTimeMillis();
//
//                    SKLogger.i("usingTime", (end - start) + "---------" + st);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public void networkRequestParseFailed(SKRNetTask netTask, SKRBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showLongToast(baseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    public void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {
        BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showLongToast("请求失败！");
                break;
            default:
                break;
        }
    }
}
