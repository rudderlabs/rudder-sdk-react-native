import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
import IRudderContext from './IRudderContext';

type dictionary = { [key: string]: unknown } | null;

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
  dbEncryption?: {
    key: string;
    enable: boolean;
  };
  // eslint-disable-next-line @typescript-eslint/ban-types
  withFactories?: Array<Record<string, unknown> | Function>;
}

export interface Spec extends TurboModule {
  setup(config: Configuration, rudderOptionsMap: dictionary): Promise<void>;
  track(event: string, properties: dictionary, options: dictionary | null): Promise<void>;
  screen(name: string, properties: dictionary, options: dictionary): Promise<void>;
  identify(userId: string, traits: dictionary, options: dictionary): Promise<void>;
  alias(newId: string, previousId: string | null, options: dictionary): Promise<void>;
  group(groupId: string, traits: dictionary, options: dictionary): Promise<void>;
  reset(clearAnonymousId: boolean): Promise<void>;
  flush(): Promise<void>;
  optOut(optOut: boolean): Promise<void>;
  putDeviceToken(token: string): Promise<void>;
  putAdvertisingId(id: string): Promise<void>;
  clearAdvertisingId(): Promise<void>;
  putAnonymousId(id: string): Promise<void>;
  registerCallback(name: string, callback: (data: unknown) => void): void;
  getRudderContext(): Promise<IRudderContext | null>;
  startSession(sessionId?: string): Promise<void>;
  endSession(): Promise<void>;
  getSessionId(): Promise<number | null>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RNRudderSdkModule');
