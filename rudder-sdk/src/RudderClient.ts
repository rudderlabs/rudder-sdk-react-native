import nativeBridge, { NativeBridge } from './NativeBridge';

export interface RudderConfig {
    endPointUrl: string,
    flushQueueSize: number,
    dbCountThreshod: number,
    sleepTimeOut: number
}

export class RudderClient {
    public getInstance(writeKey: string, config: RudderConfig) {
        nativeBridge._initiateInstance(
            writeKey,
            config.endPointUrl,
            config.flushQueueSize,
            config.dbCountThreshod,
            config.sleepTimeOut
        );
    }

    public track(eventName: string) {
        nativeBridge._logEvent(
            "track",
            eventName,
            null,
            null,
            null,
            null
        );
    }
}