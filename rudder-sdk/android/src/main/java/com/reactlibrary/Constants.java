package com.reactlibrary;

/*
 * Default value holder class
 * */
public class Constants {
    // default base url or rudder-backend-server
    static final String BASE_URL = "https://api.rudderlabs.com";
    // default flush queue size for the events to be flushed to server
    static final int FLUSH_QUEUE_SIZE = 30;
    // default threshold of number of events to be persisted in sqlite db
    static final int DB_COUNT_THRESHOLD = 10000;
    // default timeout for event flush
    // if events are registered and flushQueueSize is not reached
    // events will be flushed to server after sleepTimeOut seconds
    static final int SLEEP_TIMEOUT = 10;
}
