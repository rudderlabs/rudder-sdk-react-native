package com.reactlibrary;

import com.google.gson.annotations.SerializedName;

class RudderLibraryInfo {
    @SerializedName("name")
    private String name = "rudder-android-library";
    @SerializedName("version")
    private String version = BuildConfig.VERSION_NAME;
}
