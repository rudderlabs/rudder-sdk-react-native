package com.reactlibrary;

import android.app.Application;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.rudderlabs.android.sdk.core.util.Utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*
 * utility class for event processing
 * */
class EventRepository {
    private String authHeaderString;
    private RudderConfig config;
    private DBPersistentManager dbManager;

    /*
     * constructor to be called from RudderClient internally.
     * -- tasks to be performed
     * 1. persist the value of config
     * 2. initiate RudderElementCache
     * 3. initiate DBPersistentManager for SQLite operations
     * 4. start processor thread
     * */
    EventRepository(Application _application, String _writeKey, RudderConfig _config) {
        // 1. set the values of writeKey, config
        try {
            this.authHeaderString = Base64.encodeToString((_writeKey + ":").getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException ex) {
            RudderLogger.logError(ex);
        }
        this.config = _config;

        try {
            // 2. initiate RudderElementCache
            RudderElementCache.initiate(_application);

            // 3. initiate DBPersistentManager for SQLite operations
            this.dbManager = DBPersistentManager.getInstance(_application);

            // 5. start processor thread
            Thread processorThread = new Thread(getProcessorRunnable());
            processorThread.start();
        } catch (Exception ex) {
            RudderLogger.logError(ex.getCause());
        }
    }

    private Runnable getProcessorRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                // initiate sleepCount
                int sleepCount = 0;

                // initiate lists for messageId and message
                ArrayList<Integer> messageIds = new ArrayList<>();
                ArrayList<String> messages = new ArrayList<>();

                while (true) {
                    try {
                        // clear lists for reuse
                        messageIds.clear();
                        messages.clear();

                        // get current record count from db
                        int recordCount = dbManager.getDBRecordCount();
                        // if record count exceeds threshold count, remove older events
                        if (recordCount > config.getDbCountThreshold()) {
                            // fetch extra old events
                            dbManager.fetchEventsFromDB(messageIds, messages,
                                    recordCount - config.getDbCountThreshold());
                            // remove events
                            dbManager.clearEventsFromDB(messageIds);
                            // clear lists for reuse
                            messageIds.clear();
                            messages.clear();
                        }

                        // fetch enough events to form a batch
                        dbManager.fetchEventsFromDB(messageIds, messages, config.getFlushQueueSize());
                        // if there are enough events to form a batch and flush to server
                        // OR
                        // sleepTimeOut seconds has elapsed since last successful flush and
                        // we have at least one event to flush to server
                        if (messages.size() >= config.getFlushQueueSize() || (!messages.isEmpty() && sleepCount >= config.getSleepTimeOut())) {
                            // form payload JSON form the list of messages
                            String payload = getPayloadFromMessages(messages);
                            if (payload != null) {
                                // send payload to server if it is not null
                                String response = flushEventsToServer(payload);
                                RudderLogger.logInfo("ServerResponse: " + response);
                                RudderLogger.logInfo("EventCount: " + messages.size());
                                // if success received from server
                                if (response != null && response.equalsIgnoreCase("OK")) {
                                    // remove events from DB
                                    dbManager.clearEventsFromDB(messageIds);
                                    // reset sleep count to indicate successful flush
                                    sleepCount = 0;
                                }
                            }
                        }
                        // increment sleepCount to track total elapsed seconds
                        sleepCount += 1;
                        // retry entire logic in 1 second
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        RudderLogger.logError(ex);
                        ex.printStackTrace();
                    }
                }
            }
        };
    }

    /*
     * create payload string from messages list
     * - we created payload from individual message json strings to reduce the complexity
     * of deserialization and forming the payload object and creating the json string
     * again from the object
     * */
    private String getPayloadFromMessages(ArrayList<String> messages) {
        try {
            String sentAtTimestamp = Utils.getTimeStamp();
            // get string builder
            StringBuilder builder = new StringBuilder();
            // append initial json token
            builder.append("{");
            // append sent_at time stamp
            builder.append("\"sentAt\":\"").append(sentAtTimestamp).append("\",");
            // initiate batch array in the json
            builder.append("\"batch\": [");
            // loop through messages list and add in the builder
            for (int index = 0; index < messages.size(); index++) {
                String message = messages.get(index);
                // strip last ending object character
                message = message.substring(0, message.length() - 1);
                // add sentAt time stamp
                message = String.format("%s,\"sentAt\":\"%s\"}", message, sentAtTimestamp);
                // finally add message string to builder
                builder.append(message);
                // if not last item in the list, add a ","
                if (index != messages.size() - 1) builder.append(",");
            }
            // close batch array in the json
            builder.append("]");
            // append closing token in the json
            builder.append("}");
            // finally return the entire payload
            return builder.toString();
        } catch (Exception ex) {
            RudderLogger.logError(ex);
        }
        return null;
    }

    /*
     * flush events payload to server and return response as String
     * */
    private String flushEventsToServer(String payload) throws IOException {
        if (TextUtils.isEmpty(this.authHeaderString)) {
            RudderLogger.logError("WriteKey was not correct. Aborting flush to server");
            return null;
        }

        // get endPointUrl form config object
        String endPointUri = config.getEndPointUri() + "v1/batch";

        // create url object
        URL url = new URL(endPointUri);
        // get connection object
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        // set connection object to return output
        httpConnection.setDoOutput(true);
        //  set content type for network request
        httpConnection.setRequestProperty("Content-Type", "application/json");
        httpConnection.setRequestProperty("Authorization", "Basic " + this.authHeaderString);
        // set request method
        httpConnection.setRequestMethod("POST");
        // get output stream and write payload content
        OutputStream os = httpConnection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(payload);
        osw.flush();
        osw.close();
        os.close();
        // create connection
        httpConnection.connect();
        // get input stream from connection to get output from the server
        if (httpConnection.getResponseCode() == 200) {
            BufferedInputStream bis = new BufferedInputStream(httpConnection.getInputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int res = bis.read();
            // read response from the server
            while (res != -1) {
                baos.write((byte) res);
                res = bis.read();
            }
            // finally return response when reading from server is completed
            return baos.toString();
        } else {
            BufferedInputStream bis = new BufferedInputStream(httpConnection.getErrorStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int res = bis.read();
            // read response from the server
            while (res != -1) {
                baos.write((byte) res);
                res = bis.read();
            }
            // finally return response when reading from server is completed
            RudderLogger.logError("ServerError: " + baos.toString());

            return null;
        }
    }

    /*
     * generic method for dumping all the events
     * */
    void dump(RudderMessage message) {
        String eventJson = new Gson().toJson(message);
        dump(eventJson);
    }

    void dump(String eventJson) {
        dbManager.saveEvent(eventJson);
    }

    void reset() {
        dbManager.deleteAllEvents();
    }

    public void shutdown() {
    }

    public void optOut() {
        // TODO:  decide opt out functionality and restrictions
    }
}
