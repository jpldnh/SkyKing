package com.sking.lib.res.exp.rqcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.sking.lib.res.exp.R;
import com.sking.lib.res.exp.base.BaseActivity;

public class CreateRqCodeActivity extends BaseActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.skr_layour_activity_create_rq);
        super.onCreate(savedInstanceState);
        Bitmap bitmap = CreateQRCodeUtil.createQRCode(mContext,"http://app.shucaiwangwang.com/index.php?g=wapsite&m=order&a=trace&code=hmscww_order_1_485", 1000,true);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void findView() {
        imageView = (ImageView) findViewById(R.id.create_rq_image);
    }

    @Override
    protected void setListener() {

    }


}
