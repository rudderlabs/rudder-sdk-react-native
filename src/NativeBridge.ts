import { NativeModules } from "react-native";

export interface Configuration {
  dataPlaneUrl?: string;
  controlPlaneUrl?: string;
  flushQueueSize?: number;
  dbCountThreshold?: number;
  sleepTimeOut?: number;
  configRefreshInterval?: number;
  trackAppLifecycleEvents?: boolean;
  recordScreenViews?: boolean;
  logLevel?: number;
}

export interface Bridge {
  setup(configuration: Configuration): Promise<void>;
  track(
    event: string,
    properties: Object,
    options: Object
  ): Promise<void>;
  screen(
    name: string,
    properties: Object,
    options: Object
  ): Promise<void>;
  identify(userId: string, traits: Object, options: Object): Promise<void>;
  identify(traits: Object, options: Object): Promise<void>;
  reset(): Promise<void>;
}

const bridge: Bridge = NativeModules.RNRudderSdkModule;

if (!bridge) {
  throw new Error("Failed to load Rudderlabs native module.");
}

export default bridge;
