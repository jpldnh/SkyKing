package com.skingapp.commen.framents;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.interfaces.SKRNetTaskCallBackListener;
import com.sking.lib.res.results.SKRArrayBaseResult;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRViewUtil;
import com.skingapp.commen.R;
import com.skingapp.commen.bases.BaseApplication;
import com.skingapp.commen.bases.BaseFragment;
import com.skingapp.commen.bases.BaseHttpInformation;
import com.skingapp.commen.models.User;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_first_page);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {
        titleText = (TextView) findViewById(R.id.top_title_text);
        titleLeft = (TextView) findViewById(R.id.top_title_left);
        titleRight = (TextView) findViewById(R.id.top_title_right);
        textButton = (Button) findViewById(R.id.test);
        textView = (TextView) findViewById(R.id.textview);
    }

    @Override
    public void setListener() {
        titleLeft.setVisibility(View.GONE);
        titleText.setText("页面一");
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNetWorker().clientGet(BaseApplication.getInstance().getUser().getToken(), "140");
//                Intent intent = new Intent(mContext, PaymentActivity.class);
//                startActivity(intent);

            }
        });
        SKRViewUtil.setTextViewUnderLine(textView);
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
