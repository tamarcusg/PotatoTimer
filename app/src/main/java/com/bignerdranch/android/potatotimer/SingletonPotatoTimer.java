package com.bignerdranch.android.potatotimer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by garciata on 6/20/2016.
 */
public class SingletonPotatoTimer {
    private static final String TAG = "SingletonPotatoTimer";
    private static final String FILENAME = "potatoTimers.json";

    private static SingletonPotatoTimer sSingletonPotatoTimer;
    private Context mAppContext;

    private ArrayList<PotatoTimer> mPotatoTimers;
    private PotatoTimerJSONSerializer mPotatoTimerJSONSerializer;

    private SingletonPotatoTimer(Context mAppContext) {
        this.mAppContext = mAppContext;
        mPotatoTimerJSONSerializer = new PotatoTimerJSONSerializer(mAppContext, FILENAME);
        try {
            mPotatoTimers = mPotatoTimerJSONSerializer.loadTimers();
        } catch (Exception e) {
            mPotatoTimers = new ArrayList<PotatoTimer>();
            Log.e(TAG, "error loading timers: ", e);
        }
    }

    public static SingletonPotatoTimer get(Context c) {
        if (sSingletonPotatoTimer == null) {
            sSingletonPotatoTimer = new SingletonPotatoTimer(c.getApplicationContext());
        }
        return sSingletonPotatoTimer;
    }

    public void addPotatoTimer(PotatoTimer p) {
        mPotatoTimers.add(p);
    }

    public ArrayList<PotatoTimer> getmPotatoTimers() {
        return mPotatoTimers;
    }

    public PotatoTimer getmPotatoTimer(UUID id) {
        for (PotatoTimer p : mPotatoTimers) {
            if (p.getmId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public boolean savePotatoTimers() {
        try {
            mPotatoTimerJSONSerializer.savePotatoTimers(mPotatoTimers);
            Log.d(TAG, "projects saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "error saving projects: ", e);
            return false;
        }
    }

    public void deletePotatoTimer(PotatoTimer p) {
        mPotatoTimers.remove(p);
    }
}
