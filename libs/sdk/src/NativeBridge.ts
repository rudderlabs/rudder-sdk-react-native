import { NativeModules } from 'react-native';
import IDBEncryption from './IDBEncryption';
import IRudderContext from './IRudderContext';

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
  dbEncryption?: IDBEncryption;
  // eslint-disable-next-line @typescript-eslint/ban-types
  withFactories?: Array<Record<string, unknown> | Function>;
}

export interface Bridge {
  setup(configuration: Configuration, options: Record<string, unknown> | null): Promise<void>;
  track(
    event: string,
    properties: Record<string, unknown> | null,
    options: Record<string, unknown> | null,
  ): Promise<void>;
  screen(
    name: string,
    properties: Record<string, unknown> | null,
    options: Record<string, unknown> | null,
  ): Promise<void>;
  identify(
    userId: string,
    traits: Record<string, unknown> | null,
    options: Record<string, unknown> | null,
  ): Promise<void>;
  alias(newId: string, options: Record<string, unknown> | null): Promise<void>;
  group(
    groupId: string,
    traits: Record<string, unknown> | null,
    options: Record<string, unknown> | null,
  ): Promise<void>;
  reset(clearAnonymousId: boolean): Promise<void>;
  flush(): Promise<void>;
  optOut(optOut: boolean): Promise<void>;
  putDeviceToken(token: string): Promise<void>;
  putAdvertisingId(id: string): Promise<void>;
  clearAdvertisingId(): Promise<void>;
  putAnonymousId(id: string): Promise<void>;
  // eslint-disable-next-line @typescript-eslint/ban-types
  registerCallback(integrationName: string, callback: Function): Promise<void>;
  getRudderContext(): Promise<IRudderContext | null>;
  startSession(sessionId?: string): Promise<void>;
  endSession(): Promise<void>;
  getSessionId(): Promise<number | null>;
}

const bridge: Bridge = NativeModules.RNRudderSdkModule;

if (!bridge) {
  throw new Error('Failed to load Rudderlabs native module.');
}

export default bridge;
