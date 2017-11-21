package com.sking.lib.res.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class SKRViewHolder {
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	private SKRViewHolder(Context context, ViewGroup parent, int layoutId,
						  int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static SKRViewHolder get(Context context, View convertView,
									ViewGroup parent, int layoutId, int position) {
		SKRViewHolder holder = null;
		if (convertView == null) {
			holder = new SKRViewHolder(context, parent, layoutId, position);
		} else {
			holder = (SKRViewHolder) convertView.getTag();
			holder.mPosition = position;
		}
		return holder;
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public SKRViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public SKRViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);

		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param bm
	 * @return
	 */
	public SKRViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param url
	 * @return
	 */
	public SKRViewHolder setImageByUrl(int viewId, String url) {
		SKRImageLoader.getInstance(3, SKRImageLoader.Type.LIFO).loadImage(url, (ImageView) getView(viewId));
		return this;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param choose
	 * @return
	 */
	public SKRViewHolder setChoose(int viewId, boolean choose) {
		View view = getView(viewId);
		view.setVisibility(choose ? View.VISIBLE : View.INVISIBLE);
		return this;
	}

	public int getPosition() {
		return mPosition;
	}

}
