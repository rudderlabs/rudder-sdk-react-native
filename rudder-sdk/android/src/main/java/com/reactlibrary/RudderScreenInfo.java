package com.reactlibrary;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.google.gson.annotations.SerializedName;

class RudderScreenInfo {
    @SerializedName("density")
    private int density;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;

    RudderScreenInfo(Application application) {
        WindowManager manager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            Display display = manager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);

            this.density = displayMetrics.densityDpi;
            this.height = displayMetrics.heightPixels;
            this.width = displayMetrics.widthPixels;
        }
    }
}
