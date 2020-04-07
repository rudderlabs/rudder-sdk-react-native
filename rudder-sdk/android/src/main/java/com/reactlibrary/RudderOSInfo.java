package com.reactlibrary;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

class RudderOSInfo {
    @SerializedName("name")
    private String name = "Android";
    @SerializedName("version")
    private String version = Build.VERSION.RELEASE;
}
