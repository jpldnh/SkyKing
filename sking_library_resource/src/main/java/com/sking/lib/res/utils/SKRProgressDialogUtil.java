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

public class SKRProgressDialogUtil {
	private Dialog mDialog;
	private TextView mTextView;
	private LinearLayout rootView;

	private Runnable cancelRunnable = new Runnable() {

		@Override
		public void run() {
			if (mDialog.isShowing())
				mDialog.cancel();
		}
	};

	public SKRProgressDialogUtil(Context context) {
		mDialog = new Dialog(context, R.style.SKR_Style_Dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.skr_layout_dialog_progress, null);
		mTextView = (TextView) view.findViewById(R.id.textview);
		rootView = (LinearLayout) view.findViewById(R.id.root_view);
		setCancelable(false);
		mDialog.setContentView(view);
		mDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				mTextView.removeCallbacks(cancelRunnable);
			}
		});
	}

	public void show() {
		mTextView.removeCallbacks(cancelRunnable);
		if (!mDialog.isShowing())
			mDialog.show();
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

	public void setCancelable(boolean cancelable) {
		mDialog.setCancelable(cancelable);
	}

	public void cancelImmediately() {
		mDialog.cancel();
	}

	public void cancel() {
		mTextView.postDelayed(cancelRunnable, 500);
	}

}
