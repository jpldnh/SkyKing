package com.sking.lib.res.album;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.sking.lib.res.R;
import com.sking.lib.utils.SKToastUtil;

import java.util.ArrayList;
import java.util.List;


public class SKRAlbumAdapter extends SKRCommonAdapter<String> {

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public ArrayList<String> mSelectedImage = new ArrayList<String>();

	/**
	 * 文件夹路径
	 */
	private String mDirPath;
	/**
	 * 图片选择张数限制
	 */
	private int limitCount;

	public SKRAlbumAdapter(Context context, List<String> mDatas, int itemLayoutId,
                           String dirPath, int limitCount) {
		super(context, mDatas, itemLayoutId);
		this.mDirPath = dirPath;
		this.limitCount = limitCount;
	}

	/**
	 * @param mDirPath
	 *            the mDirPath to set
	 */
	public void setDirPath(String mDirPath) {
		this.mDirPath = mDirPath;
	}

	@Override
	public void convert(final SKRViewHolder helper, final String item) {
		// 设置no_pic
		helper.setImageResource(R.id.id_item_image,
				R.mipmap.skr_img_album_pictures_no);
		// 设置no_selected
		helper.setImageResource(R.id.id_item_select,
				R.mipmap.skr_img_album_picture_unselected);
		// 设置图片
		helper.setImageByUrl(R.id.id_item_image,item);

		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);

		mImageView.setColorFilter(null);
		// 设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener() {
			// 选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v) {
				// 已经选择过该图片
				if (mSelectedImage.contains(item)) {
					mSelectedImage.remove(item);
					mSelect.setImageResource(R.mipmap.skr_img_album_picture_unselected);
					mImageView.setColorFilter(null);
				} else
				// 未选择该图片
				{
					if (limitCount > 0) {
						int size = mSelectedImage.size();
						if (size >= limitCount) {
							SKToastUtil.showShortToast(mContext, "最多只能选择"
									+ limitCount + "张图片哦。");
							return;
						}
					}
					mSelectedImage.add(item);
					mSelect.setImageResource(R.mipmap.skr_img_album_pictures_selected);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
				}

			}
		});

		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(item)) {
			mSelect.setImageResource(R.mipmap.skr_img_album_pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}
}
