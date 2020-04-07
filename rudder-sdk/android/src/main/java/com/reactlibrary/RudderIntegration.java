package com.reactlibrary;

public abstract class RudderIntegration<T> {
    public interface Factory {
        RudderIntegration<?> create(Object settings, RudderClient client);

        String key();
    }

    public void identify(RudderMessage identify) {
    }

    public void group(RudderMessage group) {
    }

    public void track(RudderMessage track) {
    }

    public void alias(RudderMessage alias) {
    }

    public void screen(RudderMessage screen) {
    }

    public void flush() {
    }

    public void reset() {
    }

    public void dump(RudderMessage element) {

    }

    public T getUnderlyingInstance() {
        return null;
    }
}
