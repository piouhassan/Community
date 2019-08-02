package com.pollen.community.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pollen.community.Tabs.TabFragment1;
import com.pollen.community.Tabs.TabFragment2;
import com.pollen.community.Tabs.TabFragment3;
import com.pollen.community.Tabs.TabFragment4;

public class CheckoutAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public CheckoutAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                TabFragment4 tab4 = new TabFragment4();
                return tab4;

            case 1:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;
            case 3:
                TabFragment1 tab1 = new TabFragment1();
                return tab1;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}