package com.jwapp.demo.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.jwapp.demo.R;

public class CycleViewPaperAdapter extends PagerAdapter{

	private Context mContext;
	public CycleViewPaperAdapter(Context mContext)
	{
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		  return arg0 == arg1;
	}
	
	public Object instantiateItem(android.view.ViewGroup container, int position) {
		ImageView view = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.adapter_cycle_view_paper, null);
		view.setBackgroundResource(R.mipmap.img_white_swan);
        container.addView(view);
        return view;
    }

    public void destroyItem(android.view.ViewGroup container, int position, Object object) {
        container.removeView( (View)object);
    }
}
