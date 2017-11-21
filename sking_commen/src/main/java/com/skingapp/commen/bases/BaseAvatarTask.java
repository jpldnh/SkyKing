package com.skingapp.commen.bases;

import android.view.View;
import android.widget.ImageView;

import com.sking.lib.classes.SKSize;
import com.sking.lib.imageload.SKImageTask;
import com.skingapp.commen.R;

import java.net.URL;

/**
 * 获取头像任务
 */
public class BaseAvatarTask extends SKImageTask {

	public BaseAvatarTask(ImageView imageView, Object context) {
		super(imageView, context);
		// TODO Auto-generated constructor stub
	}

	public BaseAvatarTask(ImageView imageView, URL url, Object context) {
		super(imageView, url, context);
		// TODO Auto-generated constructor stub
	}

	public BaseAvatarTask(ImageView imageView, String path, Object context) {
		super(imageView, path, context);
		// TODO Auto-generated constructor stub
	}

	public BaseAvatarTask(ImageView imageView, String path, Object context,
						  View fatherView) {
		super(imageView, path, context, fatherView);
		// TODO Auto-generated constructor stub
	}

	public BaseAvatarTask(ImageView imageView, URL url, Object context,
						  View fatherView) {
		super(imageView, url, context, fatherView);
		// TODO Auto-generated constructor stub
	}

	public BaseAvatarTask(ImageView imageView, String path, Object context,
						  SKSize size) {
		super(imageView, path, context, size);
		// TODO Auto-generated constructor stub
	}

	public BaseAvatarTask(ImageView imageView, URL url, Object context,
						  SKSize size) {
		super(imageView, url, context, size);
		// TODO Auto-generated constructor stub
	}

	public BaseAvatarTask(ImageView imageView, String path, Object context,
						  View fatherView, SKSize size) {
		super(imageView, path, context, fatherView, size);
		// TODO Auto-generated constructor stub
	}

	public BaseAvatarTask(ImageView imageView, URL url, Object context,
						  View fatherView, SKSize size) {
		super(imageView, url, context, fatherView, size);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void failed() {
		imageView.setImageResource(R.mipmap.img_default_avatar);
	}

	@Override
	public void beforeload() {
		imageView.setImageResource(R.mipmap.img_default_avatar);
	}

}
