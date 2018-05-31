package com.atkj.ctc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;
    private String[] titles;



    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> mFragments, String[] titles) {
        super(fm);
        this.mFragments = mFragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


}
