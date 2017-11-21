package com.sking.lib.res.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class SKRFragmentAdapter extends FragmentPagerAdapter {

    private String[] DATA;//tablayout与viewpaper结合使用设置tab
    private ArrayList<Fragment> fragList = new ArrayList<Fragment>();
    public SKRFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list, String[] date) {
        super(fm);
        this.fragList = list;
        this.DATA = date;
    }

    @Override
    public int getCount() {
        return fragList==null?0:fragList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return DATA[position];
    }
}
