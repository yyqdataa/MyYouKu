package com.yyqdata.myyouku.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 严雨祺 on 2016/8/19.
 */
public class WelcomeAdapter extends FragmentStatePagerAdapter {

    private String[] names = {"头条","精选","里约奥运"};
    private List<Fragment> fragments;
    public WelcomeAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return names[position];
    }
}
