package com.example;

import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.moengage.core.MoEngage;

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
            .Builder(MainActivity.this.getApplication(), "xxxxxxx")
            .build();
    MoEngage.initialise(moEngage);
  }
}
