package com.rocky.utouu;

import android.app.Application;

import com.rocky.utouu.utils.BitmapSinglton;

/**
 * Created by xiabaikui on 2015/12/14.
 */
public class UtouuApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BitmapSinglton.creatBitmapSinglton(getApplicationContext());
    }
}
