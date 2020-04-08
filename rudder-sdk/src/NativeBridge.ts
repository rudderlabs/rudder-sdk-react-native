import {NativeModules} from 'react-native';

export interface Configuration {
  endPointUrl?: string;
  flushQueueSize?: number;
  dbCountThreshold?: number;
  sleepTimeOut?: number;
  logLevel?: number;
  configRefreshInterval?: number;
  trackAppLifecycleEvents?: boolean;
  recordScreenViews?: boolean;
}

export interface Bridge {
  setup(configuration: Configuration): Promise<void>;
  track(
    event: string,
    properties: Object,
    userProperties: Object,
    options: Object
  ): Promise<void>;
  screen(
    name: string,
    properties: Object,
    userProperties: Object,
    options: Object
  ): Promise<void>;
  identify(user: string, traits: Object, options: Object): Promise<void>;
  reset(): Promise<void>;
  getAnonymousId(): Promise<string>;
}

const bridge: Bridge = NativeModules.RNRudderSdkModule;

if (!bridge) {
  throw new Error("Failed to load Rudderlabs native module.");
}

export default bridge;
