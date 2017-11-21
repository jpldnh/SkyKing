package com.clubank.tpy.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.clubank.tpy.R;
import com.clubank.tpy.bases.BaseFragment;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRDialogUtil;

/**
 * 消息
 */
public class ThirdPageFragment extends BaseFragment {
	private TextView titleText;
	private TextView titleLeft;
	private TextView titleRight;

	private RadioGroup typeRadioGroup;
	private ViewPager viewPager;


	private SKRDialogUtil clearDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.fragment_third_page);
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void findView() {
		titleText = (TextView) findViewById(R.id.top_title_text);
		titleLeft = (TextView) findViewById(R.id.top_title_left);
		titleRight = (TextView) findViewById(R.id.top_title_right);

	}

	@Override
	protected void setListener() {

		titleLeft.setVisibility(View.GONE);
		titleText.setText("页面三");
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
