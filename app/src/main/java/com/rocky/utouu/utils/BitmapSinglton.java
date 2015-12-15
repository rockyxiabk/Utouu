package com.rocky.utouu.utils;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.task.Priority;

/**
 * Created by xiabaikui on 2015/11/25.
 */
public class BitmapSinglton {
    private static BitmapSinglton singlton;
    private final BitmapUtils bitmapUtils;

    public BitmapUtils getBitmapUtils() {
        return bitmapUtils;
    }

    private BitmapSinglton(Context context) {
        bitmapUtils = new BitmapUtils(context, context.getCacheDir().getAbsolutePath());
        BitmapDisplayConfig displayConfig = new BitmapDisplayConfig();
        displayConfig.setPriority(Priority.BG_TOP);
        bitmapUtils.configDefaultDisplayConfig(displayConfig);

    }

    public static BitmapSinglton getSinglton() {
        return singlton;
    }

    public static void creatBitmapSinglton(Context context) {
        if (singlton == null) {
            singlton = new BitmapSinglton(context);
        }
    }

}
