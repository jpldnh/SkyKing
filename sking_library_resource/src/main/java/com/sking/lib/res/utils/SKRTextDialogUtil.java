package com.sking.lib.res.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sking.lib.res.R;


public class SKRTextDialogUtil {
	private Dialog mDialog;
	private TextView mTextView;
	private LinearLayout rootView;

	private Runnable cancelRunnable = new Runnable() {

		@Override
		public void run() {
			cancel();
		}
	};

	public SKRTextDialogUtil(Context context) {
		mDialog = new Dialog(context, R.style.SKR_Style_Dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.skr_layout_dialog_text, null);
		mTextView = (TextView) view.findViewById(R.id.textview);
		rootView = (LinearLayout) view.findViewById(R.id.root_view);
		mDialog.setCancelable(true);
		mDialog.setContentView(view);
		mDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				mTextView.removeCallbacks(cancelRunnable);
			}
		});
		mDialog.show();
	}

	public void show() {
		mDialog.show();
		mTextView.postDelayed(cancelRunnable, 2000);
	}

	public void cancel() {
		mDialog.cancel();
	}

	public void setText(String text) {
		mTextView.setText(text);
	}

	public void setText(int textID) {
		mTextView.setText(textID);
	}

	public void setTextColor(int color) {
		mTextView.setTextColor(color);
	}

	public void setTextBackground(int resId) {
		mTextView.setBackgroundResource(resId);
	}

	public void setRootViewBackground(int resId) {
		rootView.setBackgroundResource(resId);
	}



}
