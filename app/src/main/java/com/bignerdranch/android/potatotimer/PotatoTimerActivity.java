package com.bignerdranch.android.potatotimer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class PotatoTimerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID potatoTimerId = (UUID)getIntent().getSerializableExtra(PotatoTimerFragment.EXTRA_TIMER_ID);
        return PotatoTimerFragment.newInstance(potatoTimerId);
    }
}
