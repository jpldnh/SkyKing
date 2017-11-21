package com.jwapp.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sking.lib.base.SKActivity;
import com.sking.lib.net.SKNetTask;
import com.sking.lib.res.exp.rqcode.CodeCaptureActivity;
import com.sking.lib.res.exp.rqcode.CreateRqCodeActivity;
import com.sking.lib.utils.SKToastUtil;

public class QrCodeActivity extends SKActivity {

    private Button butt_read,butt_create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_qr_code);
        super.onCreate(savedInstanceState);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = null;
        if (resultCode != RESULT_OK)
            return;
        if (data != null)
            bundle = data.getExtras();
        switch (requestCode) {
            case 0x1001:
                assert bundle != null;
                SKToastUtil.showLongToast(mContext, bundle.getString("RQ_RESULT"));
                break;
        }
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
        butt_read = (Button) findViewById(R.id.qr_butt_read);
        butt_create = (Button) findViewById(R.id.qr_butt_creat);
    }

    @Override
    protected void setListener() {
        butt_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CodeCaptureActivity.class);
                startActivityForResult(intent, 0x1001);
            }
        });
        butt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CreateRqCodeActivity.class);
                startActivityForResult(intent, 0x1002);
            }
        });
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


}
