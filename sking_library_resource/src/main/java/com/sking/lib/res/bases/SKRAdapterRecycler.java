package com.sking.lib.res.bases;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.sking.lib.res.R;
import com.sking.lib.utils.SKBaseUtil;

/**
 * Created by Im_jingwei on 2017/8/24.
 */

public abstract class SKRAdapterRecycler extends RecyclerView.Adapter{

    /**
     * 打印TAG，类名
     */
    private String TAG;
    protected Context mContext;
    protected Fragment mFragment;
    protected static final int VIEWTYPE_EMPTY = 0;
    protected static final int VIEWTYPE_NORMAL = 1;

    private String emptyString = "列表为空";
    private TextView emptyTextView;

    public SKRAdapterRecycler(){TAG = getLogTag();};

    public SKRAdapterRecycler(Context mContext) {
        this.mContext = mContext;
    }

    public SKRAdapterRecycler(Fragment mFragment) {
        this.mFragment = mFragment;
        this.mContext = mFragment.getActivity();
    }

    /**
     * 获取打印TAG，即类名
     *
     * @return
     */
    private String getLogTag() {
        return getClass().getSimpleName();
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    protected boolean isNull(String str) {
        return SKBaseUtil.isNull(str);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount()==0)
            return VIEWTYPE_EMPTY;
        return VIEWTYPE_NORMAL;
    }

    /**
     * 获取列表为空时的显示View(调用此方法(不重写getItemViewType时)需重写isEmpty()方法)
     *
     * @return a view 传递getView方法中的ViewGroup参数即可
     */
    public View getEmptyView(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.skr_layout_adapter_listitem_empty, null);
        emptyTextView = (TextView) view.findViewById(R.id.textview);
        emptyTextView.setText(emptyString);
        int width = parent.getWidth();
        int height = parent.getHeight();
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(width, height);
        view.setLayoutParams(params);
        return view;
    }

    /**
     * 设置空列表提示语
     *
     * @param emptyString
     */
    public void setEmptyString(String emptyString) {
        if (emptyTextView != null)
            emptyTextView.setText(emptyString);
        this.emptyString = emptyString;
    }

    /**
     * 设置空列表提示语
     *
     * @param emptyStrID
     */
    public void setEmptyString(int emptyStrID) {
        emptyString = mContext.getResources().getString(emptyStrID);
        setEmptyString(emptyString);
    }
}
