package com.reactlibrary;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.reactlibrary.util.Utils;

/*
 * Primary class to be used in client
 * */
public class RudderClient {
    // singleton instance
    private static RudderClient instance;
    // repository instance
    private static EventRepository repository;
    private static Application application;

    /*
     * private constructor
     * */
    private RudderClient() {
        // message constructor initialization
    }

    /*
     * API for getting instance of RudderClient with config
     * */
    public static void _initiateInstance(
            Context _context,
            String _writeKey,
            String _endPointUrl,
            int _flushQueueSize,
            int _dbCountThreshold,
            int _sleepTimeout
    ) {
        // check if instance is already initiated
        if (instance == null) {
            // assert context is not null
            if (_context == null) {
                RudderLogger.logError("context can not be null");
            }
            // assert writeKey is not null or empty
            if (TextUtils.isEmpty(_writeKey)) {
                RudderLogger.logError("writeKey can not be null or empty");
            }
            // assert config is not null
            RudderConfig config = new RudderConfig.Builder()
                    .withEndPointUri(_endPointUrl)
                    .withFlushQueueSize(_flushQueueSize)
                    .withDbThresholdCount(_dbCountThreshold)
                    .withSleepCount(_sleepTimeout)
                    .build();

            // get application from provided context
            if (_context != null) {
                application = (Application) _context.getApplicationContext();
            }
            // initiate RudderClient instance
            instance = new RudderClient();
            // initiate EventRepository class
            if (application != null && _writeKey != null) {
                repository = new EventRepository(application, _writeKey, config);
            }
        }
    }

    public static void _logEvent(
            String _eventType,
            String _eventName,
            String _userId,
            String _eventPropsJson,
            String _userPropsJson,
            String _integrationsJson
    ) {
        RudderMessage message = new RudderMessageBuilder()
                .setEventName(_eventName)
                .setUserId(_userId)
                .setProperty(Utils.convertToMap(_eventPropsJson))
                .setUserProperty(Utils.convertToMap(_userPropsJson))
                .build();
        message.setType(_eventType);
        message.setIntegrations(Utils.convertToMap(_integrationsJson));
        if (repository != null) repository.dump(message);
    }

    static RudderClient getInstance() {
        return instance;
    }
}
