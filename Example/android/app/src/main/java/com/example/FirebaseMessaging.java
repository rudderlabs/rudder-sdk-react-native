//package com.example;
//
//import android.os.Build;
//import android.util.Log;
//
//import androidx.annotation.RequiresApi;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.moengage.firebase.MoEFireBaseHelper;
//import com.moengage.pushbase.MoEPushHelper;
//
//public class FirebaseMessaging extends FirebaseMessagingService {
//    private static final String TAG = "Rudderstack";
//
//    @Override
//    public void onNewToken(String token) {
//        Log.d(TAG, "Refreshed token: " + token);
//
//        MoEFireBaseHelper.getInstance().passPushToken(getApplicationContext(), token);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        if (MoEPushHelper.getInstance().isFromMoEngagePlatform(remoteMessage.getData())){
//            MoEFireBaseHelper.getInstance().passPushPayload(this, remoteMessage.getData());
//        }else{
//            // your app's business logic to show notification
//        }
//    }
//}
