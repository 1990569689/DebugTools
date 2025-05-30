package com.ddonging.debugtools;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

public class App extends Application {
    protected static Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static App instance;
    public static App getInstance() {
        return instance;
    }
    public static Context getContext() {
        return mContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
        CrashReport.initCrashReport(getApplicationContext());
        CrashReport.initCrashReport(getApplicationContext(), "d6782179f5", false);
    }
}
