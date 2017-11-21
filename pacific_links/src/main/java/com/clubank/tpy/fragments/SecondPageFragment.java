package com.clubank.tpy.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.clubank.tpy.R;
import com.clubank.tpy.bases.BaseFragment;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRBaseResult;

/**
 * 成员
 */
public class SecondPageFragment extends BaseFragment {
	private TextView titleText;
	private TextView titleLeft;
	private TextView titleRight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.fragment_second_page);
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
		titleText.setText("页面二");
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
