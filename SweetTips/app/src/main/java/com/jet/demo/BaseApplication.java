package com.jet.demo;

import android.app.Application;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * 作者:幻海流心
 * GitHub:https://github.com/HuanHaiLiuXin
 * 邮箱:wall0920@163.com
 * 2017/1/3 14:20
 */

public class BaseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        CustomActivityOnCrash.setShowErrorDetails(true);
        CustomActivityOnCrash.install(this);
    }
}
