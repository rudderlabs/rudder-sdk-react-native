package com.example;

import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.FcmConfig;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "Example";
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // MoEngage SDK initialisation: Replace "xxxxxxx" with your APP ID.
    MoEngage moEngage = new MoEngage
            .Builder(MainActivity.this.getApplication(), "UC37PRFF3I8LGYDXK6ADJTL2")
            .setNotificationSmallIcon(R.drawable.ic_launcher_background)
            .setNotificationLargeIcon(R.drawable.ic_launcher_background)
            .enableLogs(LogLevel.VERBOSE)
//            .configureFcm(new FcmConfig(false))
            .build();
    MoEngage.initialise(moEngage);

  }
}
