package com.reactlibrary.util;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import static android.provider.Settings.Secure.ANDROID_ID;
import static android.provider.Settings.System.getString;

public class Utils {
    public static String getTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.getID();
    }

    public static String getTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(new Date());
    }

    public static String toDateString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return formatter.format(date);
    }

    public static String getDeviceId(Application application) {
        if (Build.VERSION.SDK_INT >= 17) {
            String androidId = getString(application.getContentResolver(), ANDROID_ID);
            if (!TextUtils.isEmpty(androidId)
                    && !"9774d56d682e549c".equals(androidId)
                    && !"unknown".equals(androidId)
                    && !"000000000000000".equals(androidId)
            ) {
                return androidId;
            }
        }

        // If this still fails, generate random identifier that does not persist across installations
        return UUID.randomUUID().toString();
    }

    public static Map<String, Object> convertToMap(String json) {
        return new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static String getWriteKeyFromStrings(Context context) {
        int id = context.getResources().getIdentifier(
                context.getPackageName(),
                "string",
                "rudder_write_key"
        );
        if (id != 0) {
            return context.getResources().getString(id);
        } else {
            return null;
        }
    }
}
