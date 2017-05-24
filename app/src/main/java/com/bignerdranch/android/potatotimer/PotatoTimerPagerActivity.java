package com.bignerdranch.android.potatotimer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by garciata on 6/22/2016.
 */
public class PotatoTimerPagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ArrayList<PotatoTimer> mPotatoTimers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mPotatoTimers = SingletonPotatoTimer.get(this).getmPotatoTimers();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                PotatoTimer potatoTimer = mPotatoTimers.get(position);
                return PotatoTimerFragment.newInstance(potatoTimer.getmId());
            }

            @Override
            public int getCount() {
                return mPotatoTimers.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PotatoTimer potatoTimer = mPotatoTimers.get(position);
                if (potatoTimer.getmTitle() != null) {
                    setTitle(potatoTimer.getmTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        UUID potatoTimerId = (UUID)getIntent().getSerializableExtra(PotatoTimerFragment.EXTRA_TIMER_ID);
        for (int i = 0; i < mPotatoTimers.size(); i++) {
            if (mPotatoTimers.get(i).getmId().equals(potatoTimerId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
