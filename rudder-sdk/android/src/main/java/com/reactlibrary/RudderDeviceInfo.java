package com.reactlibrary;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

class RudderDeviceInfo {
    @SerializedName("id")
    private String deviceId;
    @SerializedName("manufacturer")
    private String manufacturer = Build.MANUFACTURER;
    @SerializedName("model")
    private String model = Build.MODEL;
    @SerializedName("name")
    private String name = Build.DEVICE;

    RudderDeviceInfo(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
