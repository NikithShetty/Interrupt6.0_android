package com.nikith_shetty.interrupt60;

import android.app.Application;

import com.pushbots.push.Pushbots;

/**
 * Created by Nikith_Shetty on 19/02/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Pushbots Library
        Pushbots.sharedInstance().init(this);
    }
}
