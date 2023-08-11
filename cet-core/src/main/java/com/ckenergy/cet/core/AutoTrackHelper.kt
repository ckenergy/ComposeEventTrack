package com.ckenergy.cet.core;

import android.text.TextUtils;
import android.util.Log;

public class AutoTrackHelper {

    private static final String TAG = "AutoTrackHelper";

    public static  void trackViewScreen(String eventName) {
        if (TextUtils.isEmpty(eventName)){
            return;
        }
        Log.d(TAG, "trackViewScreen:"+eventName);
    }

    public static  void trackClick(String eventName) {
        if (TextUtils.isEmpty(eventName)){
            return;
        }
        Log.d(TAG, "trackClick:"+eventName);
    }

}
