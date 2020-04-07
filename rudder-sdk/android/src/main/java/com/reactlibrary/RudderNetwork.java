package com.reactlibrary;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.google.gson.annotations.SerializedName;

import static android.content.Context.TELEPHONY_SERVICE;

class RudderNetwork {
    @SerializedName("carrier")
    private String carrier;
    @SerializedName("wifi")
    private boolean isWifiEnabled = false;
    @SerializedName("bluetooth")
    private boolean isBluetoothEnabled = false;
    @SerializedName("cellular")
    private boolean isCellularEnabled = false;

    RudderNetwork(Application application) {
        try {
            // carrier name
            TelephonyManager telephonyManager = (TelephonyManager) application.getSystemService(TELEPHONY_SERVICE);
            this.carrier = telephonyManager != null ? telephonyManager.getNetworkOperatorName() : "NA";

            // wifi enabled
            WifiManager wifi = (WifiManager) application.getSystemService(Context.WIFI_SERVICE);
            isWifiEnabled = wifi != null && wifi.isWifiEnabled();

            // bluetooth
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            isBluetoothEnabled = bluetoothAdapter != null
                    && bluetoothAdapter.isEnabled()
                    && bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON;

            // cellular status
            TelephonyManager tm = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isCellularEnabled = Settings.Global.getInt(application.getContentResolver(), "mobile_data", 1) == 1;
                } else {
                    isCellularEnabled = Settings.Secure.getInt(application.getContentResolver(), "mobile_data", 1) == 1;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
