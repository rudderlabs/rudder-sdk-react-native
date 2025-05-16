import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

type properties = { [key: string]: unknown } | null;
type options = { [key: string]: unknown } | null;
export interface Configuration {
  dataPlaneUrl?: string;
  controlPlaneUrl?: string;
  flushQueueSize?: number;
  dbCountThreshold?: number;
  sleepTimeOut?: number;
  configRefreshInterval?: number;
  autoCollectAdvertId?: boolean;
  trackAppLifecycleEvents?: boolean;
  recordScreenViews?: boolean;
  logLevel?: number;
  autoSessionTracking?: boolean;
  sessionTimeout?: number;
  enableBackgroundMode?: boolean;
  collectDeviceId?: boolean;
  enableGzip?: boolean;
  // dbEncryption and withFactories are not TurboModule compatible, so remove or adjust as needed
}

export interface Spec extends TurboModule {
  multiply(a: number, b: number): number;

  setup(config: Configuration, rudderOptionsMap: options): Promise<void>;
  track(event: string, properties: properties, options: options | null): Promise<void>;
  screen(name: string, properties: properties, options: options): Promise<void>;
  identify(userId: string, traits: properties, options: options): Promise<void>;
  alias(newId: string, previousId: string | null, options: options): Promise<void>;
  group(groupId: string, traits: properties, options: options): Promise<void>;
  reset(clearAnonymousId: boolean): Promise<void>;
  flush(): Promise<void>;
  optOut(optOut: boolean): Promise<void>;
  putDeviceToken(token: string): Promise<void>;
  putAdvertisingId(id: string): Promise<void>;
  clearAdvertisingId(): Promise<void>;
  putAnonymousId(id: string): Promise<void>;
  // registerCallback(integrationName: string, callback: Function): Promise<void>; // Not TurboModule compatible
  // getRudderContext(): Promise<IRudderContext | null>; // Not TurboModule compatible if IRudderContext is not a supported type
  startSession(sessionId?: string): Promise<void>;
  endSession(): Promise<void>;
  getSessionId(): Promise<number | null>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RudderSdkReactNative');
