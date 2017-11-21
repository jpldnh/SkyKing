package com.sking.lib.res.activitys;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sking.lib.res.R;
import com.sking.lib.res.adapters.SKRPhotoPagerAdapter;
import com.sking.lib.res.bases.SKRActivity;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.bases.SKRNetWorker;
import com.sking.lib.res.models.SKRImageModel;
import com.sking.lib.res.photoviiew.SKRHackyViewPager;
import com.sking.lib.res.results.SKRBaseResult;

import java.util.ArrayList;

/**
 * 查看大图(本地图片请传递imagelist,网络图片传递images或者imagelist,
 * titleAndContentVisible标题和图片内容是否显示默认为true)
 */
public class SKRShowLargePicActivity extends SKRActivity {
	private RelativeLayout infoLayout;
	private TextView orderby;
	private TextView title;
	private TextView content;

	private SKRHackyViewPager mViewPager;
	private ArrayList<String> urllist;
	private int position;
	private ArrayList<SKRImageModel> images;

	private boolean titleAndContentVisible;
	
	private int bottomHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.skr_layout_activity_show_large_picture);
		super.onCreate(savedInstanceState);
		if (urllist != null && urllist.size() > 0) {
			mViewPager.setAdapter(new SKRPhotoPagerAdapter(this, urllist));
			mViewPager.setCurrentItem(position);
		}
		setImgInfo();
	}

	@Override
	protected void findView() {
		mViewPager = (SKRHackyViewPager) findViewById(R.id.gallery);

		infoLayout = (RelativeLayout) findViewById(R.id.info);

		orderby = (TextView) findViewById(R.id.orderby);
		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);

		if (bottomHeight > 0) {
			infoLayout.getLayoutParams().height = bottomHeight;
		}
		
	}

	/**
	 * 显示或隐藏图片信息
	 */
	public void toogleInfo() {
		// if (infoLayout.getVisibility() == View.VISIBLE) {
		// infoLayout.setVisibility(View.GONE);
		// } else {
		// infoLayout.setVisibility(View.VISIBLE);
		// }
		finish();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void getExras() {
		urllist = mIntent.getStringArrayListExtra("imagelist");
		position = mIntent.getIntExtra("position", 0);
		images = (ArrayList<SKRImageModel>) mIntent.getSerializableExtra("images");
		titleAndContentVisible = mIntent.getBooleanExtra(
				"titleAndContentVisible", true);
		bottomHeight = mIntent.getIntExtra("bottomHeight",0);

		if (images != null && images.size() > 0) {
			if (urllist == null || urllist.size() == 0) {
				urllist = new ArrayList<String>();
				for (int i = 0; i < images.size(); i++) {
					String url = images.get(i).getImgurlbig();
					urllist.add(url);
				}
			}
		}
	}

	@Override
	protected void setListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				SKRShowLargePicActivity.this.position = position;
				setImgInfo();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		mViewPager.setCurrentItem(position);
		orderby.setText(position + "/" + urllist.size());
	}

	protected void setImgInfo() {
		int sp = position + 1;
		orderby.setText(sp + "/" + urllist.size());
		if (images != null) {
			SKRImageModel image = images.get(position);
			if (titleAndContentVisible) {
				title.setText(image.getTitle());
				content.setText(image.getContent());
			} else {
				title.setText("");
				content.setText("");
			}
		} else {
			title.setText("");
			content.setText("");
		}
	}


	@Override
	protected SKRNetWorker initNetWorker() {
		return null;
	}

	@Override
	protected void networkRequestBefore(SKRNetTask netTask) {

	}

	@Override
	protected void networkRequestAfter(SKRNetTask netTask) {

	}

	@Override
	protected void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult SKRBaseResult) {

	}

	@Override
	protected void networkRequestParseFailed(SKRNetTask netTask, SKRBaseResult SKRBaseResult) {

	}

	@Override
	protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {

	}

	@Override
	public boolean onAutoLoginFailed(SKRNetWorker netWorker,
									 SKRNetTask netTask, int failedType, SKRBaseResult SKRBaseResult) {
		// TODO Auto-generated method stub
		return false;
	}

}
