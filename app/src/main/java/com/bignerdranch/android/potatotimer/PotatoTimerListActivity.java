package com.bignerdranch.android.potatotimer;

import android.support.v4.app.Fragment;

/**
 * Created by garciata on 6/20/2016.
 */
public class PotatoTimerListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PotatoTimerListFragment();
    }
}
