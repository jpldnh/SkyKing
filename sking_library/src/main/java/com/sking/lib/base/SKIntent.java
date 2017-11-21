package com.sking.lib.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.Serializable;

public class SKIntent extends Intent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SKIntent() {
		super();
	}

	public SKIntent(Context packageContext, Class<?> cls) {
		super(packageContext, cls);
	}

	public SKIntent(Intent o) {
		super(o);
	}

	public SKIntent(String action, Uri uri, Context packageContext,
                    Class<?> cls) {
		super(action, uri, packageContext, cls);
	}

	public SKIntent(String action, Uri uri) {
		super(action, uri);
	}

	public SKIntent(String action) {
		super(action);
	}

}
