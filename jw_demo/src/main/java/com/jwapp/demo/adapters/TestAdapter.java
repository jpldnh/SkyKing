package com.jwapp.demo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jwapp.demo.R;
import com.sking.lib.res.bases.SKRAdapter;

import java.util.ArrayList;

public class TestAdapter extends SKRAdapter {

    private Context mContext;
    private ArrayList<String> list = new ArrayList<String>();

    public TestAdapter(Context mContext, ArrayList<String> mlist) {
        super(mContext);
        this.mContext = mContext;
        this.list = mlist;
    }


    @Override
    public int getCount() {
        return list.size() == 0 ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_test_adapter, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        setData(holder, position);
        convertView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.list_anim));
        return convertView;
    }

    private void findView(ViewHolder holder, View view) {
        holder.textView = (TextView) view.findViewById(R.id.ad_text_name);
    }

    private void setData(ViewHolder holder, int position) {
        String string = list.get(position);
        holder.textView.setText(string);
        holder.textView.setAlpha(1 - position * 0.2f);//调节透明度，0-1小数，0透明，1不透明，自己调
    }

    private class ViewHolder {
        private TextView textView;
    }

}
