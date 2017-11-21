package com.sking.lib.res.views;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.sking.lib.res.R;
import com.sking.lib.res.album.SKRCommonAdapter;
import com.sking.lib.res.album.SKRImageFloder;
import com.sking.lib.res.album.SKRViewHolder;

import java.util.List;


public class SKRPopuWindowListImageDir extends
		SKRPopuWindowForListView<SKRImageFloder> {
	private ListView mListDir;

	public SKRPopuWindowListImageDir(int width, int height,
                                     List<SKRImageFloder> datas, View convertView) {
		super(convertView, width, height, true, datas);
	}

	@Override
	public void initViews() {
		mListDir = (ListView) findViewById(R.id.id_list_dir);
		mListDir.setAdapter(new SKRCommonAdapter<SKRImageFloder>(context, mDatas,
				R.layout.skr_layout_view_album_list_dir_item) {
			@Override
			public void convert(SKRViewHolder helper, SKRImageFloder item) {
				helper.setText(R.id.id_dir_item_name, item.getName());
				helper.setImageByUrl(R.id.id_dir_item_image,
						item.getFirstImagePath());
				helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
				helper.setChoose(R.id.id_dir_item_choose, item.isChoose());
			}
		});
	}

	public interface OnImageDirSelected {
		void selected(SKRImageFloder floder);
	}

	private OnImageDirSelected mImageDirSelected;

	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
		this.mImageDirSelected = mImageDirSelected;
	}

	@Override
	public void initEvents() {
		mListDir.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SKRImageFloder floder = mDatas.get(position);
				if (floder == null)
					return;
				if (mImageDirSelected != null) {
					mImageDirSelected.selected(floder);
				}
				for (SKRImageFloder imgF : mDatas) {
					imgF.setChoose(false);
				}
				floder.setChoose(true);
				((BaseAdapter) mListDir.getAdapter()).notifyDataSetChanged();
			}
		});
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void beforeInitWeNeedSomeParams(Object... params) {
		// TODO Auto-generated method stub
	}

}
