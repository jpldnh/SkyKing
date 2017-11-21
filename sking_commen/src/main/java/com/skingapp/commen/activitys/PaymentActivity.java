package com.skingapp.commen.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRArrayBaseResult;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.special.pay.alipay.PayResult;
import com.skingapp.commen.R;
import com.skingapp.commen.bases.BaseActivity;
import com.skingapp.commen.bases.BaseHttpInformation;
import com.skingapp.commen.config.BaseConfig;
import com.skingapp.commen.models.AlipayTrade;
import com.skingapp.commen.models.UnionTrade;
import com.skingapp.commen.models.User;
import com.skingapp.commen.models.WeixinTrade;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import java.util.Map;


/**
 * 选择支付方式页面
 */
public class PaymentActivity extends BaseActivity implements OnClickListener {
    private TextView titleText;
    private TextView titleLeft;
    private TextView titleRight;
    private TextView payMoneyText;
    private FrameLayout weichatLayout;
    private CheckBox weichatChexkBox;
    private FrameLayout alipayLayout;
    private CheckBox alipayCheckBox;
    private FrameLayout unipayLayout;
    private CheckBox unipayCheckBox;
    private Button submitButt;
    private User user;
    private String titleName = "";
    private String payMoney;
    private String keyId = "0";//关联id
    private String keyType = "0";//	2:支付开通VIP订单；
    private String PAY_TYPE = "0";//0微信,1支付宝,3银联
    private static final int REQUEST_CODE_UNIONPAY = 10;

    IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private WXPayReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_payment);
        super.onCreate(savedInstanceState);
        msgApi.registerApp(BaseConfig.APPID_WEIXIN);
        mReceiver = new WXPayReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("com.hemaapp.xiapai.wxpay");
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    protected void getExras() {
        titleName = getIntent().getStringExtra("TITLE_NAME");
        payMoney = getIntent().getStringExtra("PAY_AMOUNT");
        keyType = getIntent().getStringExtra("KEY_TYPE");
        keyId = getIntent().getStringExtra("KEY_ID");
        user = getApplicationContext().getUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_UNIONPAY:
                /**
                 * 处理银联手机支付控件返回的支付结果
                 **/
                // 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
                String str = data.getExtras().getString("pay_result");
                if (str.equalsIgnoreCase("success")) {
                    showToastLong("支付成功！");
                } else if (str.equalsIgnoreCase("fail")) {
                    showToastLong("交易失败！");
                } else if (str.equalsIgnoreCase("cancel")) {
                    showToastLong("交易取消！");
                }

                break;
        }
    }

    @Override
    protected void findView() {
        titleText = (TextView) findViewById(R.id.top_title_text);
        titleLeft = (TextView) findViewById(R.id.top_title_left);
        titleRight = (TextView) findViewById(R.id.top_title_right);
        payMoneyText = (TextView) findViewById(R.id.payment_paymoney);
        weichatLayout = (FrameLayout) findViewById(R.id.payment_weichat_layout);
        weichatChexkBox = (CheckBox) findViewById(R.id.payment_weichat_checkbox);
        alipayLayout = (FrameLayout) findViewById(R.id.payment_alipay_layout);
        alipayCheckBox = (CheckBox) findViewById(R.id.payment_alipay_checkbox);
        unipayLayout = (FrameLayout) findViewById(R.id.payment_unipay_payout);
        unipayCheckBox = (CheckBox) findViewById(R.id.payment_unipay_checkbox);
        submitButt = (Button) findViewById(R.id.payment_submit_button);

    }

    @Override
    protected void setListener() {
        if (!isNull(titleName))
            titleText.setText(titleName);
        titleLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleRight.setVisibility(View.GONE);
        payMoneyText.setText("￥" + payMoney);
        weichatChexkBox.setOnClickListener(this);
        alipayCheckBox.setOnClickListener(this);
        unipayCheckBox.setOnClickListener(this);
        submitButt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                charge();
            }
        });

    }

    @Override
    protected void networkRequestBefore(SKRNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case ALIPAY:
                showProgressDialog("请稍后...");
                break;
            case WEIXINPAY:
                showProgressDialog("请稍后...");
                break;
            case UNIONPAY:
                showProgressDialog("请稍后...");
                break;
            default:
                break;
        }
    }

    @Override
    protected void networkRequestAfter(SKRNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case ALIPAY:
                cancelProgressDialog();
                break;
            case WEIXINPAY:
                cancelProgressDialog();
                break;
            case UNIONPAY:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case ALIPAY:
                @SuppressWarnings("unchecked")
                SKRArrayBaseResult<AlipayTrade> aResult = (SKRArrayBaseResult<AlipayTrade>) baseResult;
                AlipayTrade trade = aResult.getObjects().get(0);
                String orderInfo = trade.getAlipaysign();
                new AlipayThread(orderInfo).start();
                break;
            case WEIXINPAY:
                @SuppressWarnings("unchecked")
                SKRArrayBaseResult<WeixinTrade> wResult = (SKRArrayBaseResult<WeixinTrade>) baseResult;
                WeixinTrade wTrade = wResult.getObjects().get(0);
                goWeixin(wTrade);
                break;
            case UNIONPAY:
                @SuppressWarnings("unchecked")
                SKRArrayBaseResult<UnionTrade> uResult = (SKRArrayBaseResult<UnionTrade>) baseResult;
                UnionTrade uTrade = uResult.getObjects().get(0);
                String uInfo = uTrade.getTn();
                UPPayAssistEx.startPayByJAR(this, PayActivity.class, null, null, uInfo, BaseConfig.UNIONPAY_TESTMODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void networkRequestParseFailed(SKRNetTask netTask, SKRBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case ALIPAY:
                showToastLong(baseResult.getMsg());
                break;
            case WEIXINPAY:
                showToastLong(baseResult.getMsg());
                break;
            case UNIONPAY:
                showToastLong(baseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {
        BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case ALIPAY:
                showToastLong("交易失败！");
                break;
            case WEIXINPAY:
                showToastLong("交易失败！");
                break;
            case UNIONPAY:
                showToastLong("交易失败！");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payment_weichat_checkbox:
                PAY_TYPE = "0";
                setCheckboxState(weichatChexkBox);
                break;
            case R.id.payment_alipay_checkbox:
                PAY_TYPE = "1";
                setCheckboxState(alipayCheckBox);
                break;
            case R.id.payment_unipay_checkbox:
                PAY_TYPE = "2";
                setCheckboxState(unipayCheckBox);
                break;
        }
    }


    private void charge() {
        payMoney = "0.01";
        if (isNull(payMoney)) {
            showToastLong("Error!支付金额为空!");
            return;
        }
        switch (PAY_TYPE) {
            case "0":
                weixinTrade();
                break;
            case "1":
                alipayTrade();
                break;
            case "2":
                uppayTrade();
                break;
        }
    }

    /*---------------------------------支付宝 start----------------------------------------*/
    private void alipayTrade() {
        getNetWorker().alipay(user.getToken(), keyType, keyId);
    }

    private class AlipayThread extends Thread {
        String orderInfo;
        AlipayHandler alipayHandler;

        public AlipayThread(String orderInfo) {
            this.orderInfo = orderInfo;
            alipayHandler = new AlipayHandler(PaymentActivity.this);
        }

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(PaymentActivity.this);
            // 调用支付接口，获取支付结果
//            String result = alipay.pay(orderInfo);
            Map<String, String> result = alipay.payV2(orderInfo, true);//true 显示loadingdialog，false不显示

            log_i("result = " + result);
            Message msg = new Message();
            msg.obj = result;
            alipayHandler.sendMessage(msg);
        }
    }

    private static class AlipayHandler extends Handler {
        PaymentActivity activity;

        AlipayHandler(PaymentActivity activity) {
            this.activity = activity;
        }

            public void handleMessage(Message msg) {
                if (msg == null) {
                    activity.showToastLong("支付失败");
                    return;
                }
//                PayResult result = new PayResult((String) msg.obj);
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                switch (resultStatus) {
                    case "9000":
                        activity.showToastLong("支付成功!");
                    break;
                default:
                    activity.showToastLong(resultInfo);
                    break;
            }
        };
    }
    /*---------------------------------支付宝 end----------------------------------------*/

    /*---------------------------------微信 start----------------------------------------*/
    private void weixinTrade() {
        getNetWorker().weixinpay(user.getToken(), keyType, keyId);
    }

    private void goWeixin(WeixinTrade trade) {
        // IWXAPI msgApi = WXAPIFactory.createWXAPI(this,
        // DemoConfig.APPID_WEIXIN);
        msgApi.registerApp(BaseConfig.APPID_WEIXIN);

        PayReq request = new PayReq();
        request.appId = trade.getAppid();
        request.partnerId = trade.getPartnerid();
        request.prepayId = trade.getPrepayid();
        request.packageValue = trade.getPackageValue();
        request.nonceStr = trade.getNoncestr();
        request.timeStamp = trade.getTimestamp();
        request.sign = trade.getSign();
        msgApi.sendReq(request);
    }

    private class WXPayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.skyking.xiapai.wxpay".equals(intent.getAction())) {
                int code = intent.getIntExtra("code", -1);
                switch (code) {
                    case 0:
                        showToastLong("支付成功!");
                        break;
                    case -1:
                        showToastLong("支付失败");
                        break;
                    case -2:
                        showToastLong("您取消了支付!");
                        break;
                    default:
                        break;
                }
            }
        }
    }
    /*---------------------------------微信 end----------------------------------------*/

    /*---------------------------------银联 start----------------------------------------*/
    private void uppayTrade() {
        getNetWorker().unionpay(user.getToken(), keyType, keyId);
    }
    /*---------------------------------银联 end----------------------------------------*/

    private void setCheckboxState(CheckBox checkBox) {
        weichatChexkBox.setChecked(false);
        alipayCheckBox.setChecked(false);
        unipayCheckBox.setChecked(false);
        checkBox.setChecked(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
