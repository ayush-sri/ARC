package com.example.ayushsrivastava.arc;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by Ayush Srivastava on 1/25/2018.
 */

public class dataFragment extends android.support.v4.app.Fragment {
    View view;
    ViewPager viewPager;
    TabLayout tablayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sample,container,false);
        viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new sliderAdapter(getChildFragmentManager()));
        tablayout = (TabLayout)view.findViewById(R.id.sliding_tabs);
        tablayout.post(new Runnable() {
            @Override
            public void run() {
                tablayout.setupWithViewPager(viewPager);
                tablayout.clearOnTabSelectedListeners();
                tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition(),true);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        });

        return view;
    }
    private class sliderAdapter extends FragmentStatePagerAdapter {
        final String tabs [] = {"BMA 4001 ","BCS 4003","BCS 4016","BCS 4017","BCS 4018","BHU 4018"};
        public sliderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ContentFragment();
        }

        @Override
        public int getCount() {
            return 6;
        }
        @Override
        public CharSequence getPageTitle(int position)
        {
            return tabs[position];
        }
    }
}
