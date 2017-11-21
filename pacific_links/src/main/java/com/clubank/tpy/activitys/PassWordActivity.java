package com.clubank.tpy.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.clubank.tpy.R;
import com.clubank.tpy.bases.BaseActivity;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRBaseResult;

/**
 * 重置密码
 */
public class PassWordActivity extends BaseActivity {
    private TextView titleText;
    private TextView titleLeft;
    private TextView titleRight;
    private EditText oldPassText;
    private EditText newPassText;
    private EditText newAgainPass;
    private Button submitButt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_password);
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
        oldPassText = (EditText) findViewById(R.id.reset_pass_old_pass);
        newPassText = (EditText) findViewById(R.id.reset_pass_new_pass);
        newAgainPass = (EditText) findViewById(R.id.rest_pass_new_again);
        submitButt = (Button) findViewById(R.id.submit_butt);


    }

    @Override
    protected void setListener() {
        titleText.setText("密码设置");
        titleLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleRight.setVisibility(View.GONE);
    }


    @Override
    protected void networkRequestBefore(SKRNetTask netTask) {

    }

    @Override
    protected void networkRequestAfter(SKRNetTask netTask) {

    }

    @Override
    protected void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult baseResult) {

    }

    @Override
    protected void networkRequestParseFailed(SKRNetTask netTask, SKRBaseResult baseResult) {

    }

    @Override
    protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {

    }
}
