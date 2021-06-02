import { NativeModules } from "react-native";

export interface Configuration {
  dataPlaneUrl?: string;
  controlPlaneUrl?: string;
  flushQueueSize?: number;
  dbCountThreshold?: number;
  sleepTimeOut?: number;
  configRefreshInterval?: number;
  deviceModeCallBackThreshold? : number;
  trackAppLifecycleEvents?: boolean;
  recordScreenViews?: boolean;
  logLevel?: number;
  withFactories?: Array<Object>;
}

export interface Bridge {
  setup(
    configuration: Configuration,
    options: Object | null
    ): Promise<void>;
  track(
    event: string,
    properties: Object | null,
    options: Object | null
  ): Promise<void>;
  screen(
    name: string,
    properties: Object | null,
    options: Object | null
  ): Promise<void>;
  identify(
    userId: string,
    traits: Object | null,
    options: Object | null
  ): Promise<void>;
  reset(): Promise<void>;
  putDeviceToken(token: string): Promise<void>;
  setAdvertisingId(id: string): Promise<void>;
  setAnonymousId(id:string): Promise<void>;
  checkIntegrationReady(integrationName :string): Promise<void>;
  registerCallback(integrationName:string, callback:Object): Promise<void>;
}

const bridge: Bridge = NativeModules.RNRudderSdkModule;

if (!bridge) {
  throw new Error("Failed to load Rudderlabs native module.");
}

export default bridge;
