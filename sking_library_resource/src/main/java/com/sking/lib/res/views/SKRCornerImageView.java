package com.sking.lib.res.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 
 */
public class SKRCornerImageView extends ImageView {

	public SKRCornerImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SKRCornerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SKRCornerImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Path clipPath = new Path();
		int w = this.getWidth();
		int h = this.getHeight();
		clipPath.addRoundRect(new RectF(0, 0, w, h), 10.0f, 10.0f,
				Path.Direction.CW);
		canvas.clipPath(clipPath);
		super.onDraw(canvas);
	}

}
