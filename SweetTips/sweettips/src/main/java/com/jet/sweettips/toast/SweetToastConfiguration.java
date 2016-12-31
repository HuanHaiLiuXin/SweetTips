package com.jet.sweettips.toast;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SweetToast关联的属性集合类
 *
 * 作者:幻海流心
 * GitHub:https://github.com/HuanHaiLiuXin
 * 邮箱:wall0920@163.com
 * 2016/12/22 15:05
 */

public class SweetToastConfiguration {
    private WindowManager.LayoutParams mParams = null;
    private long duration = 0;

    public WindowManager.LayoutParams getParams() {
        return mParams;
    }

    public void setParams(@NonNull WindowManager.LayoutParams mParams) {
        this.mParams = mParams;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}