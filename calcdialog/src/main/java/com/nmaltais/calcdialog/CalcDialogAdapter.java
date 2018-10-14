package com.nmaltais.calcdialog;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class CalcDialogAdapter extends FragmentPagerAdapter {
    private List<CalcDialogFragment> fragments = new ArrayList<CalcDialogFragment>();

    void setFragments(List<CalcDialogFragment> fragments){
        this.fragments.clear();
        this.fragments.addAll(fragments);
        notifyDataSetChanged();
    }

    void addFragments(List<CalcDialogFragment> fragments){
        this.fragments.addAll(fragments);
        notifyDataSetChanged();
    }

    void addFragments(CalcDialogFragment... fragments){
        this.fragments.addAll(Arrays.asList(fragments));
        notifyDataSetChanged();
    }

    CalcDialogAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CalcDialogFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getTitle();
    }


}
