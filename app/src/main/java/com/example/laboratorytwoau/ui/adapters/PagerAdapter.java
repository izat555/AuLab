package com.example.laboratorytwoau.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.laboratorytwoau.data.entities.TabItemModel;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<TabItemModel> mTabs;

    public PagerAdapter(FragmentManager fm, ArrayList<TabItemModel> tabs) {
        super(fm);
        mTabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return mTabs.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getTitle();
    }
}
