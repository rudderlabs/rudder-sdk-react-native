package com.reactlibrary;

import android.text.TextUtils;
import android.webkit.URLUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Config class for RudderClient
 * - endPointUri -> API endpoint for flushing events
 * - flushQueueSize -> maximum number of events to be batched
 * - dbCountThreshold -> maximum number of events to be persisted in local DB
 * - sleepTimeOut -> timeout for automatic flushing since last successful flush
 * - logLevel -> level of logging for debugging
 *
 * default values are set at Constants file
 *
 * */
public class RudderConfig {
    private String endPointUri;
    private int flushQueueSize;
    private int dbCountThreshold;
    private int sleepTimeOut;
    private int logLevel;
    private List<RudderIntegration.Factory> factories;

    // internal constructor for creating instance with default values
    RudderConfig() {
        this(Constants.BASE_URL, Constants.FLUSH_QUEUE_SIZE, Constants.DB_COUNT_THRESHOLD, Constants.SLEEP_TIMEOUT, RudderLogger.RudderLogLevel.ERROR, null);
    }

    // internal constructor to be used along with Builder
    RudderConfig(String endPointUri, int flushQueueSize, int dbCountThreshold, int sleepTimeOut, int logLevel, List<RudderIntegration.Factory> factories) {
        RudderLogger.init(logLevel);

        if (TextUtils.isEmpty(endPointUri)) {
            RudderLogger.logError("endPointUri can not be null or empty. Set to default.");
            this.endPointUri = Constants.BASE_URL;
        } else if (!URLUtil.isValidUrl(endPointUri)) {
            RudderLogger.logError("Malformed endPointUri. Set to default");
            this.endPointUri = Constants.BASE_URL;
        } else {
            // check if endpoint uri is formatted correctly
            if (!endPointUri.endsWith("/")) endPointUri += "/";
            this.endPointUri = endPointUri;
        }

        if (flushQueueSize < 1 || flushQueueSize > 100) {
            RudderLogger.logError("flushQueueSize is out of range. Min: 1, Max: 100. Set to default");
            this.flushQueueSize = Constants.FLUSH_QUEUE_SIZE;
        } else {
            this.flushQueueSize = flushQueueSize;
        }

        this.logLevel = logLevel;

        if (dbCountThreshold < 0) {
            RudderLogger.logError("invalid dbCountThreshold");
            this.dbCountThreshold = Constants.DB_COUNT_THRESHOLD;
        } else {
            this.dbCountThreshold = dbCountThreshold;
        }

        if (sleepTimeOut < 10) {
            RudderLogger.logError("invalid sleepTimeOut");
            this.sleepTimeOut = Constants.SLEEP_TIMEOUT;
        } else {
            this.sleepTimeOut = sleepTimeOut;
        }

        if (factories != null && !factories.isEmpty()) {
            this.factories = factories;
        }
    }

    /*
     * getters
     * */
    public String getEndPointUri() {
        return endPointUri;
    }

    public int getFlushQueueSize() {
        return flushQueueSize;
    }

    public int getDbCountThreshold() {
        return dbCountThreshold;
    }

    public int getSleepTimeOut() {
        return sleepTimeOut;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public List<RudderIntegration.Factory> getFactories() {
        return factories;
    }

    void setEndPointUri(String endPointUri) {
        this.endPointUri = endPointUri;
    }

    void setFlushQueueSize(int flushQueueSize) {
        this.flushQueueSize = flushQueueSize;
    }

    void setDbCountThreshold(int dbCountThreshold) {
        this.dbCountThreshold = dbCountThreshold;
    }

    void setSleepTimeOut(int sleepTimeOut) {
        this.sleepTimeOut = sleepTimeOut;
    }

    void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    void setFactories(List<RudderIntegration.Factory> factories) {
        this.factories = factories;
    }

    public static class Builder {
        private List<RudderIntegration.Factory> factories = new ArrayList<>();

        public Builder withFactory(RudderIntegration.Factory factory) {
            this.factories.add(factory);
            return this;
        }

        public Builder withFactories(List<RudderIntegration.Factory> factories) {
            this.factories.addAll(factories);
            return this;
        }

        public Builder withFactories(RudderIntegration.Factory... factories) {
            Collections.addAll(this.factories, factories);
            return this;
        }

        private String endPointUri = Constants.BASE_URL;

        public Builder withEndPointUri(String endPointUri) {
            if (TextUtils.isEmpty(endPointUri)) {
                RudderLogger.logError("endPointUri can not be null or empty. Set to default");
                return this;
            }
            if (!URLUtil.isValidUrl(endPointUri)) {
                RudderLogger.logError("Malformed endPointUri. Set to default");
                return this;
            }
            this.endPointUri = endPointUri;
            return this;
        }

        private int flushQueueSize = Constants.FLUSH_QUEUE_SIZE;

        public Builder withFlushQueueSize(int flushQueueSize) {
            if (flushQueueSize < 1 || flushQueueSize > 100) {
                RudderLogger.logError("flushQueueSize is out of range. Min: 1, Max: 100. Set to default");
                return this;
            }
            this.flushQueueSize = flushQueueSize;
            return this;
        }

        private boolean isDebug = false;

        public Builder withDebug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }

        private int logLevel = RudderLogger.RudderLogLevel.NONE;

        public Builder withLogLevel(int logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        private int dbThresholdCount = Constants.DB_COUNT_THRESHOLD;

        public Builder withDbThresholdCount(int dbThresholdCount) {
            this.dbThresholdCount = dbThresholdCount;
            return this;
        }

        private int sleepTimeout = Constants.SLEEP_TIMEOUT;

        public Builder withSleepCount(int sleepCount) {
            this.sleepTimeout = sleepCount;
            return this;
        }

        public RudderConfig build() {
            return new RudderConfig(this.endPointUri, this.flushQueueSize, this.dbThresholdCount, this.sleepTimeout, this.isDebug ? RudderLogger.RudderLogLevel.DEBUG : logLevel, this.factories);
        }
    }
}
